/*
 * Copyright (C) 2019 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.pairing;

import gitpair.yaml.Node;
import gitpair.yaml.Yaml;
import junit.framework.TestCase;

import java.util.List;

/**
 * https://github.com/pivotal-legacy/git_scripts
 * Supports a `git pair-commit` style that differs from the other style.
 * Each pair does not have an email address inline, but has a record in email_addresses.
 * https://github.com/RoboPlugins/git-pair.idea/issues/12
 */
public class PairCommitStyleConfigTest extends TestCase {
    static final String COMMIT_STYLE_YAML = "# This is a comment.\n" +
            "\n" +
            "pairs:\n" +
            "  gc: Grumpy Cat\n" +
            "  rw: Robert A. Wallis\n" +
            "\n" +
            "email_addresses:\n" +
            "  gc: grumpy.cat@example.com\n" +
            "  rw: smilingrob@gmail.com\n" +
            "\n" +
            "global: false\n";

    public void testFindAllTeamMembers() {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(COMMIT_STYLE_YAML);

        // WHEN I load all the team members.
        List<TeamMember> teamMembers = pairConfig.getTeamMembers();

        // THEN it should contain some data
        assertNotNull(teamMembers);
        assertEquals(2, teamMembers.size());

        // AND it should have Grumpy Cat
        assertEquals("Grumpy Cat", teamMembers.get(0).getName());
        assertEquals("grumpy.cat@example.com", teamMembers.get(0).getEmail());
        assertEquals("gc", teamMembers.get(0).getInitials());

        // AND it should have Robert Wallis
        assertEquals("Robert A. Wallis", teamMembers.get(1).getName());
        assertEquals("smilingrob@gmail.com", teamMembers.get(1).getEmail());
        assertEquals("rw", teamMembers.get(1).getInitials());
    }

    public void testUpdateTeamMemberEmail() {
        // GIVEN a team member
        Node config = Yaml.parse(COMMIT_STYLE_YAML);
        Node teamMemberNode = config.get("pairs").get("rw");
        TeamMember teamMember = PairConfig.teamMemberFromYamlPairChildNode(teamMemberNode);

        // AND an email_addresses section
        Node emailAddressesNode = config.get("email_addresses");

        // WHEN the team member is updated
        PairConfig.updateTeamMemberEmail(teamMember, emailAddressesNode);

        // THEN it should contain the correct fields
        assertNotNull(teamMember);
        assertEquals("rw", teamMember.getInitials());
        assertEquals("Robert A. Wallis", teamMember.getName());
        assertEquals("smilingrob@gmail.com", teamMember.getEmail());

        // WHEN a bad node is parsed
        // THEN it should not crash
        PairConfig.updateTeamMemberEmail(null, null);

        Node bad1 = new Node(null);
        PairConfig.updateTeamMemberEmail(teamMember, bad1);

        bad1.setKey("gc");
        PairConfig.updateTeamMemberEmail(teamMember, bad1);

        // WHEN it's not a full address
        // THEN don't crash
        bad1.setValue("@");
        PairConfig.updateTeamMemberEmail(teamMember, bad1);

        // WHEN it's not a full address
        // THEN don't crash
        bad1.setValue(".");
        PairConfig.updateTeamMemberEmail(teamMember, bad1);

        // WHEN it's empty
        // THEN don't crash
        bad1.setValue("");
        PairConfig.updateTeamMemberEmail(teamMember, bad1);
    }

    public void testGetTeamMemberByInitials() {
        // GIVEN a valid configuration
        PairConfig pairConfig = new PairConfig(COMMIT_STYLE_YAML);

        // WHEN the team member is queried by initials
        TeamMember teamMember = pairConfig.getTeamMemberByInitials("rw");

        // THEN it should return the correct team member
        assertNotNull(teamMember);
        assertEquals("Robert A. Wallis", teamMember.getName());
        assertEquals("smilingrob@gmail.com", teamMember.getEmail());
        assertEquals("rw", teamMember.getInitials());
    }
}
