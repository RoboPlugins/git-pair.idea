/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.pairing;

import com.intellij.openapi.util.text.StringUtil;
import gitpair.yaml.Node;
import gitpair.yaml.Yaml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The team members that can pair.
 */
public class PairConfig {

    private String prefix;
    private String domain;
    private List<TeamMember> teamMembers = new ArrayList<TeamMember>();
    private boolean shouldChangeGlobalUser;

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
     * @param prefix email prefix, useful for GMail
     *               ex. If "team@example.com" is a team alias, then "team" should be the prefix.
     *               Because "team+robert.wallis+grumpy.cat@example.com" handled by GMail will send mail to team@example.com.
     * @param domain everything after the '@' sign in the email address.
     */
    PairConfig(@Nullable String prefix, @NotNull String domain) {
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
        if (values.length == 0) {
            return null;
        }

        String name = values[0].trim();
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        String email = null;
        if (values.length > 1) {
            email = values[1].trim();
        }

        return new TeamMember(initials, name, email);
    }

    /**
     * Looks up the person in the email_addresses node by initials,
     * and assigns the value of the node to the person's email.
     *
     * @param person         a person previously parsed from the "pairs" node
     * @param emailAddresses the "email_addresses" root node
     */
    static void updateTeamMemberEmail(TeamMember person, Node emailAddresses) {
        if (person == null || emailAddresses == null) {
            return;
        }
        String initials = person.getInitials();
        if (StringUtil.isEmpty(initials)) {
            return;
        }
        Node emailNode = emailAddresses.get(person.getInitials());
        if (emailNode == null) {
            // email not found
            return;
        }
        String email = emailNode.getValue();
        if (email == null) {
            return;
        }
        email = email.trim();
        if (StringUtil.isEmpty(email)) {
            return;
        }
        person.setEmail(email);
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
                Node email_addresses = root.get("email_addresses");
                if (email_addresses != null) {
                    for (TeamMember person : teamMembers) {
                        updateTeamMemberEmail(person, email_addresses);
                    }
                }
            }
            Node global = root.get("global");
            if (global != null && global.getValue() != null) {
                if ("true".equals(global.getValue().toLowerCase())) {
                    shouldChangeGlobalUser = true;
                }
            }
        }
    }

    /**
     * Find the team member by initials and return it.
     *
     * @param initials initials to look up.
     * @return the team member or null if there was an error.
     */
    public TeamMember getTeamMemberByInitials(String initials) {
        if (initials == null) {
            return null;
        }
        for (TeamMember member : teamMembers) {
            if (initials.equals(member.getInitials())) {
                return member;
            }
        }
        return null;
    }

    /**
     * Get all the team members from the configuration.
     *
     * @return list of team members that can check in.
     */
    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * Should git use the --global flag when changing which user is logged in.
     *
     * @return true if git should change the global user.
     */
    public boolean shouldChangeGlobalUser() {
        return shouldChangeGlobalUser;
    }
}
