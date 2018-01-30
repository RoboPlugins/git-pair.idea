/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpair.pairing;

import gitpair.yaml.Node;
import gitpair.yaml.Yaml;
import junit.framework.TestCase;

import java.util.List;

/**
 * Handles loading the configuration.
 */
public class PairConfigTest extends TestCase {

    public static final String YAML_SOURCE = "# This is a comment.\n" +
            "\n" +
            "pairs:\n" +
            "  gc: Grumpy Cat;grumpy.cat\n" +
            "  pp: Pinkie Pie; pinkie.pie\n" +
            "  rw: Robert A. Wallis; robert.wallis\n" +
            "\n" +
            "email:\n" +
            "  prefix: prefix\n" +
            "  domain: example.com\n" +
            "\n" +
            "global: false\n";

    public void testFindAllTeamMembers() {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(YAML_SOURCE);

        // WHEN I load all the team members.
        List<TeamMember> teamMembers = pairConfig.getTeamMembers();

        // THEN it should contain some data
        assertNotNull(teamMembers);
        assertEquals(3, teamMembers.size());

        // AND it should have Grumpy Cat
        assertEquals("Grumpy Cat", teamMembers.get(0).getName());
        assertEquals("grumpy.cat", teamMembers.get(0).getEmail());
        assertEquals("gc", teamMembers.get(0).getInitials());

        // AND it should have Pinkie Pie
        assertEquals("Pinkie Pie", teamMembers.get(1).getName());
        assertEquals("pinkie.pie", teamMembers.get(1).getEmail());
        assertEquals("pp", teamMembers.get(1).getInitials());

        // AND it should have Robert Wallis
        assertEquals("Robert A. Wallis", teamMembers.get(2).getName());
        assertEquals("robert.wallis", teamMembers.get(2).getEmail());
        assertEquals("rw", teamMembers.get(2).getInitials());
    }

    public void testTeamMemberFromYamlPairChildNode() {
        // GIVEN a team node
        Node config = Yaml.parse(YAML_SOURCE);
        Node teamMemberNode = config.get("pairs").get("rw");

        // WHEN the team member is parsed
        TeamMember teamMember = PairConfig.teamMemberFromYamlPairChildNode(teamMemberNode);

        // THEN it should contain the correct fields
        assertEquals("rw", teamMember.getInitials());
        assertEquals("Robert A. Wallis", teamMember.getName());
        assertEquals("robert.wallis", teamMember.getEmail());

        // WHEN a bad node is parsed
        // THEN it should not crash
        assertNull(PairConfig.teamMemberFromYamlPairChildNode(null));

        Node bad1 = new Node(null);
        assertNull(PairConfig.teamMemberFromYamlPairChildNode(bad1));

        bad1.setKey("initials");
        assertNull(PairConfig.teamMemberFromYamlPairChildNode(bad1));

        // WHEN it's missing a name
        // THEN don't crash
        bad1.setValue(";email");
        assertNull(PairConfig.teamMemberFromYamlPairChildNode(bad1));

        // WHEN it's missing an email
        // THEN don't crash
        bad1.setValue("name;");
        assertNull(PairConfig.teamMemberFromYamlPairChildNode(bad1));
    }

    public void testGetTeamMemberByInitials() {
        // GIVEN a valid configuration
        PairConfig pairConfig = new PairConfig(YAML_SOURCE);

        // WHEN the team member is queried by initials
        TeamMember teamMember = pairConfig.getTeamMemberByInitials("rw");

        // THEN it should return the correct team member
        assertEquals("Robert A. Wallis", teamMember.getName());
        assertEquals("robert.wallis", teamMember.getEmail());
        assertEquals("rw", teamMember.getInitials());
    }

    public void testGlobalTrue() {
        {
            // GIVEN a valid configuration
            PairConfig globalConfig = new PairConfig("global:true");

            // THEN global should be true
            assertTrue(globalConfig.shouldChangeGlobalUser());
        }
        {
            // GIVEN a valid configuration
            PairConfig globalConfig = new PairConfig("global:True");

            // THEN global should be true
            assertTrue(globalConfig.shouldChangeGlobalUser());
        }
    }

    public void testGlobalFalse() {
        {
            // GIVEN a valid configuration
            PairConfig globalConfig = new PairConfig("global:");

            // THEN global should be false (unset)
            assertFalse(globalConfig.shouldChangeGlobalUser());
        }

        {
            // GIVEN a valid configuration
            PairConfig noGlobalConfig = new PairConfig("");

            // THEN global should be false (unset)
            assertFalse(noGlobalConfig.shouldChangeGlobalUser());
        }
    }

}