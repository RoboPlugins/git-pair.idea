/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
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
     * Find the team member by initials and return it.
     *
     * @param initials initals to look up.
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
}
