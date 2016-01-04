/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import gitpairpicker.yaml.Node;
import gitpairpicker.yaml.Yaml;
import junit.framework.TestCase;

import java.util.List;

/**
 * Handles loading the configuration.
 */
public class PairConfigTest extends TestCase {

    static final String YAML_SOURCE = "# This is a comment.\n" +
            "\n" +
            "pairs:\n" +
            "  gc: Grumpy Cat;grumpy.cat\n" +
            "  pp: Pinkie Pie; pinkie.pie\n" +
            "  rw: Robert A. Wallis; robert.wallis\n" +
            "\n" +
            "email:\n" +
            "  prefix:\n" +
            "  domain: smilingrob.com\n" +
            "\n" +
            "global: true\n";

    public void testFindAllTeamMembers() throws Exception {
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

    public void testGeneratePairName() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairConfig.generatePairName(grumpyCat, robert);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat & Robert A. Wallis", pairName);

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairConfig.generatePairName(robert, grumpyCat);

        // THEN it should STILL be formatted alphabetically
        assertEquals("Grumpy Cat & Robert A. Wallis", pairNameReversed);
    }

    public void testGeneratePairNameSolo() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND only one member
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairConfig.generatePairName(robert);

        // THEN it should be formatted correctly
        assertEquals("Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameTrio() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND three members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairConfig.generatePairName(grumpyCat, pinkiePie, robert);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat, Pinkie Pie, and Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameBadData() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND three members
        TeamMember grumpyCat = new TeamMember(null, "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", null, "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", null);

        // WHEN a pair name is generated
        // THEN it should't crash
        pairConfig.generatePairName(grumpyCat, pinkiePie, robert);
    }

    public void testGeneratePairEmail() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairConfig.generatePairEmail(grumpyCat, robert);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairName);

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairConfig.generatePairEmail(robert, grumpyCat);

        // THEN it should STILL be formatted alphabetically
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairNameReversed);
    }

    public void testGeneratePairEmailSolo() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND some members
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairConfig.generatePairEmail(robert);

        // THEN it should be formatted correctly
        assertEquals("prefix+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailNoPrefix() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(null, "smilingrob.com");

        // AND some members
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairConfig.generatePairEmail(robert);

        // THEN it should be formatted correctly
        assertEquals("robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailTrio() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairConfig.generatePairEmail(robert, grumpyCat, pinkiePie);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+pinkie.pie+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailBadData() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", null, "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", null);
        TeamMember robert = new TeamMember(null, "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        // THEN it should'nt crash
        pairConfig.generatePairEmail(robert, grumpyCat, pinkiePie);
    }

    public void testTeamMemberFromYamlPairChildNode() throws Exception {
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

}