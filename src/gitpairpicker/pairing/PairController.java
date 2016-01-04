/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import com.intellij.openapi.util.text.StringUtil;
import gitpairpicker.git.GitRunner;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Controls pair logic.
 */
public class PairController {

    PairConfig pairConfig;
    String projectBasePath;
    ArrayList<TeamMember> currentPair = new ArrayList<>();

    /**
     * Logic for pairing.
     *
     * @param projectBasePath location of the .git folder.
     * @param pairConfig      configuration from .pairs.
     */
    public PairController(@NotNull PairConfig pairConfig, @NotNull String projectBasePath) {
        this.pairConfig = pairConfig;
        this.projectBasePath = projectBasePath;
    }

    public void toggleTeamMember(TeamMember teamMember) {
        if (currentPair.contains(teamMember)) {
            currentPair.remove(teamMember);
        } else {
            currentPair.add(teamMember);
        }

        String email = generatePairEmail(currentPair);
        String name = generatePairName(currentPair);

        if (StringUtil.isEmpty(email) || StringUtil.isEmpty(name)) {
            return;
        }

        GitRunner gitRunner = new GitRunner(projectBasePath);
        gitRunner.setUserName(name);
        gitRunner.setUserEmail(email);
    }

    public String getPairDisplayName() {
        return generatePairName(currentPair);
    }

    List<TeamMember> findWhoIsPaired() {
        // TODO: actually check email
        return currentPair;
    }

    /**
     * Calculate what the git user.email should be given some team members.
     *
     * @param teamMembers list of team emmbers.
     * @return emailable email for the team.
     */
    String generatePairEmail(List<TeamMember> teamMembers) {

        if (teamMembers.size() == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<>();
        for (TeamMember t : teamMembers) {
            if (t != null && t.getEmail() != null) {
                teamArray.add(t);
            }
        }
        Collections.sort(teamArray, new AlphebeticalName()); // email order should still be name order

        StringBuilder sb = new StringBuilder();

        if (StringUtil.isNotEmpty(pairConfig.getPrefix())) {
            sb.append(pairConfig.getPrefix());
            sb.append("+");
        }

        int size = teamArray.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("+");
            }
            sb.append(teamArray.get(i).getEmail());
        }

        if (StringUtil.isNotEmpty(pairConfig.getDomain())) {
            sb.append("@");
            sb.append(pairConfig.getDomain());
        }

        return sb.toString();
    }

    /**
     * Calculate what the git user.name should be given some team members.
     *
     * @param teamMembers list of team members.
     * @return human readable list of names.
     */
    String generatePairName(List<TeamMember> teamMembers) {

        if (teamMembers.size() == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<>();
        for (TeamMember t : teamMembers) {
            if (t != null && t.getName() != null) {
                teamArray.add(t);
            }
        }
        Collections.sort(teamArray, new AlphebeticalName());

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
    private static class AlphebeticalName implements Comparator<TeamMember> {
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

}
