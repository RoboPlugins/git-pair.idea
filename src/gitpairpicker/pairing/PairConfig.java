/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import com.intellij.openapi.util.text.StringUtil;
import gitpairpicker.yaml.Node;
import gitpairpicker.yaml.Yaml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The team members that can pair.
 */
public class PairConfig {

    private String prefix;
    private String domain;
    private List<TeamMember> teamMembers = new ArrayList<>();

    /**
     * Initialize the pair configuration.
     *
     * @param yamlSource contents of the .pairs file.
     */
    public PairConfig(String yamlSource) {
        configureWithYamlSource(yamlSource);
    }

    /**
     * Initialize the pair configuration.
     *
     * @param prefix email prefix, useful for Gmail
     *               ex. If "team@example.com" is a team alias, then "team" should be the prefix.
     *               Because "team+robert.wallis+grumpy.cat@example.com" handled by Gmail will send mail to team@example.com.
     * @param domain everything after the '@' sign in the email address.
     */
    public PairConfig(@Nullable String prefix, @NotNull String domain) {
        this.prefix = prefix;
        this.domain = domain;
    }

    /**
     * Generate a new TeamMember for the Yaml node.
     * Members should be formatted like "initials: Full Name; email.address" in the YAML file.
     *
     * @param memberNode node under "pairs".
     * @return new TeamMember with values set, or null if it was not parsable as a complete team member.
     */
    static TeamMember teamMemberFromYamlPairChildNode(Node memberNode) {
        if (memberNode == null) {
            return null;
        }

        String initials = memberNode.getKey();
        if (initials == null) {
            return null;
        }

        String value = memberNode.getValue();
        if (value == null) {
            return null;
        }

        String[] values = value.split(";");
        if (values.length < 2) {
            return null;
        }

        String name = values[0].trim();
        String email = values[1].trim();
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(email)) {
            return null;
        }

        return new TeamMember(initials, name, email);
    }

    /**
     * Configure with a YAML source text.
     *
     * @param yamlSource contents of the .pairs file.
     */
    private void configureWithYamlSource(String yamlSource) {
        Node root = Yaml.parse(yamlSource);
        if (root != null) {
            Node emailNode = root.get("email");
            if (emailNode != null) {
                Node prefixNode = emailNode.get("prefix");
                if (prefixNode != null) {
                    prefix = prefixNode.getValue();
                }
                Node domainNode = emailNode.get("domain");
                if (domainNode != null) {
                    domain = domainNode.getValue();
                }
            }
            Node pairs = root.get("pairs");
            if (pairs != null) {
                for (Node pairNode : pairs.getChildren()) {
                    TeamMember teamMember = teamMemberFromYamlPairChildNode(pairNode);
                    if (teamMember != null) {
                        teamMembers.add(teamMember);
                    }
                }
            }
        }
    }

    /**
     * Get all the team members from the configuration.
     *
     * @return list of team members that can check in.
     */
    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    /**
     * Calculate what the git user.name should be given some team members.
     *
     * @param teamMembers list of team members.
     * @return human readable list of names.
     */
    public String generatePairName(TeamMember... teamMembers) {

        if (teamMembers.length == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<>();
        Collections.addAll(teamArray, teamMembers);
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
     * Calculate what the git user.email should be given some team members.
     *
     * @param teamMembers list of team emmbers.
     * @return emailable email for the team.
     */
    public String generatePairEmail(TeamMember... teamMembers) {

        if (teamMembers.length == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<>();
        Collections.addAll(teamArray, teamMembers);
        Collections.sort(teamArray, new AlphebeticalName()); // email order should still be name order

        StringBuilder sb = new StringBuilder();

        if (StringUtil.isNotEmpty(prefix)) {
            sb.append(prefix);
            sb.append("+");
        }

        int size = teamArray.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append("+");
            }
            sb.append(teamArray.get(i).getEmail());
        }

        if (StringUtil.isNotEmpty(domain)) {
            sb.append("@");
            sb.append(domain);
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
