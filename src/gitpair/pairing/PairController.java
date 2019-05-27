/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.pairing;

import com.intellij.openapi.util.text.StringUtil;
import gitpair.git.GitRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controls pair logic.
 */
public class PairController {

    private PairConfig pairConfig;
    private GitRunner gitRunner;
    private ArrayList<TeamMember> currentPair;

    /**
     * Logic for pairing.
     *
     * @param gitRunner  git command runner.
     * @param pairConfig configuration from .pairs.
     */
    public PairController(@NotNull PairConfig pairConfig, @NotNull GitRunner gitRunner) {
        this.pairConfig = pairConfig;
        this.gitRunner = gitRunner;
        this.currentPair = new ArrayList<TeamMember>();
    }

    /**
     * Initialize controller, looks in git for who is paired.
     */
    public void init() {
        List<TeamMember> pairs = findWhoIsPaired();
        if (pairs != null) {
            currentPair = new ArrayList<TeamMember>(pairs);
        }
    }

    /**
     * Add or remove a team member.
     *
     * @param teamMember team member to turn on or off.
     */
    public void toggleTeamMember(TeamMember teamMember) {
        if (teamMember == null || StringUtil.isEmpty(teamMember.getEmail())) {
            return;
        }

        if (currentPair.contains(teamMember)) {
            currentPair.remove(teamMember);
        } else {
            currentPair.add(teamMember);
        }

        String email = generatePairEmail(currentPair);
        String name = generatePairName(currentPair);

        if (name != null) {
            gitRunner.setUserName(name, pairConfig.shouldChangeGlobalUser());
        }
        if (email != null) {
            gitRunner.setUserEmail(email, pairConfig.shouldChangeGlobalUser());
        }
        if (name == null && email == null) {
            gitRunner.unsetUserAndEmail(pairConfig.shouldChangeGlobalUser());
        }
    }

    /**
     * Generate and return the display name for the pair.
     *
     * @return formatted display name for the pair.
     */
    @Nullable
    public String getPairDisplayName() {
        if (currentPair != null && currentPair.size() > 0) {
            return generatePairName(currentPair);
        } else {
            return "git pair";
        }
    }

    /**
     * Is the current team member paired?
     *
     * @param teamMember team member to check.
     * @return true if team member is currently paired.
     */
    public boolean isPaired(@Nullable TeamMember teamMember) {
        return currentPair.contains(teamMember);
    }

    @NotNull
    public PairConfig getPairConfig() {
        return pairConfig;
    }

    /**
     * Figure out who is paired and return the list.
     *
     * @return list of currently paired members.
     */
    @Nullable
    private List<TeamMember> findWhoIsPaired() {
        // ask git who is paired, instead of relying on an internal state
        String userEmail = gitRunner.getUserEmail();
        return matchTeamMembersFromEmail(userEmail);
    }


    /**
     * Given an email address, which team members match that email address.
     * Looks in the PairConfig for matching emails.
     *
     * @param email address to parse.
     * @return list of matching members, or null if there were errors.
     */
    @Nullable
    List<TeamMember> matchTeamMembersFromEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return null;
        }
        ArrayList<TeamMember> matchingTeam = new ArrayList<TeamMember>();
        String[] emailSplit = email.split("@");
        if (emailSplit.length < 1) {
            return null;
        }
        String[] aliases = emailSplit[0].split("\\+");

        for (String alias : aliases) {
            for (TeamMember teamMember : pairConfig.getTeamMembers()) {
                String personEmail = teamMember.getEmail();
                if (alias.equals(personEmail)) {
                    matchingTeam.add(teamMember);
                    continue;
                }
                if (StringUtil.isNotEmpty(personEmail)) {
                    String[] personEmailSplit = personEmail.split("@");
                    if (personEmailSplit.length < 1) {
                        continue;
                    }
                    if (alias.equals(personEmailSplit[0])) {
                        matchingTeam.add(teamMember);
                    }
                }
            }
        }

        return matchingTeam;
    }

    /**
     * Calculate what the git user.email should be given some team members.
     *
     * @param teamMembers list of team members.
     * @return email-able email for the team.
     */
    @Nullable
    String generatePairEmail(List<TeamMember> teamMembers) {

        if (teamMembers.size() == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<TeamMember>();
        for (TeamMember t : teamMembers) {
            if (t != null && usernameFromEmail(t.getEmail()) != null) {
                teamArray.add(t);
            }
        }
        Collections.sort(teamArray, new AlphabeticalName()); // email order should still be name order

        StringBuilder sb = new StringBuilder();

        if (StringUtil.isNotEmpty(pairConfig.getPrefix())) {
            sb.append(pairConfig.getPrefix());
            sb.append("+");
        }

        int teamSize = teamArray.size();
        for (int i = 0; i < teamSize; i++) {
            if (i > 0) {
                sb.append("+");
            }
            sb.append(usernameFromEmail(teamArray.get(i).getEmail()));
        }

        if (StringUtil.isNotEmpty(pairConfig.getDomain())) {
            sb.append("@");
            sb.append(pairConfig.getDomain());
        } else {
            for (TeamMember teamMember : teamArray) {
                // get the first available domain
                String domain = domainFromEmail(teamMember.getEmail());
                if (StringUtil.isNotEmpty(domain)) {
                    sb.append("@");
                    sb.append(domain);
                    break;
                }
            }
        }

        return sb.toString();
    }

    /**
     * Calculate what the git user.name should be given some team members.
     *
     * @param teamMembers list of team members.
     * @return human readable list of names.
     */
    @Nullable
    String generatePairName(List<TeamMember> teamMembers) {

        if (teamMembers.size() == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<TeamMember>();
        for (TeamMember t : teamMembers) {
            if (t != null && t.getName() != null) {
                teamArray.add(t);
            }
        }
        Collections.sort(teamArray, new AlphabeticalName());

        StringBuilder sb = new StringBuilder();
        int size = teamArray.size();
        for (int i = 0; i < size; i++) {
            if (size == 2 && i == 1) {
                sb.append(" & ");
            } else if (i > 0 && i == size - 1) {
                // oxford comma
                sb.append(", and ");
            } else if (i > 0) {
                sb.append(", ");
            }
            sb.append(teamArray.get(i).getName());
        }

        return sb.toString();
    }

    /**
     * Compare two TeamMembers by name.
     */
    private static class AlphabeticalName implements Comparator<TeamMember> {
        @Override
        public int compare(TeamMember o1, TeamMember o2) {
            if (o1 == null || o1.getName() == null) {
                return 1; // null at end
            }
            if (o2 == null || o2.getName() == null) {
                return -1; // null at end
            }
            return o1.getName().compareTo(o2.getName());
        }
    }

    private static String usernameFromEmail(String email) {
        if (email == null)
            return null;
        String[] split = email.split("@");
        if (split.length == 0)
            return null;
        return split[0];
    }

    private static String domainFromEmail(String email) {
        if (email == null)
            return null;
        String[] split = email.split("@");
        if (split.length < 2)
            return null;
        return split[1];
    }

}
