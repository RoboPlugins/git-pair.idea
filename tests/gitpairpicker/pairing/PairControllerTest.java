/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import gitpairpicker.git.GitRunner;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Test pairing logic.
 */
public class PairControllerTest extends TestCase {

    PairConfig pairConfig = new PairConfig(PairConfigTest.YAML_SOURCE);
    GitRunner gitRunner = new GitRunner(".");

    public void testGeneratePairName() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(grumpyCat);
        list.add(robert);

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(list);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat & Robert A. Wallis", pairName);

        // GIVEN a reversed order list
        ArrayList<TeamMember> reverseList = new ArrayList<>();
        reverseList.add(robert);
        reverseList.add(grumpyCat);

        // WHEN a pair name is generated
        String pairNameReversed = pairController.generatePairName(reverseList);

        // THEN it should STILL be formatted alphabetically
        assertEquals("Grumpy Cat & Robert A. Wallis", pairNameReversed);
    }

    public void testGeneratePairNameSolo() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND only one member
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(robert);

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(list);

        // THEN it should be formatted correctly
        assertEquals("Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameTrio() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND three members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("gc", "Grumpy Cat", "grumpy.cat"));
        list.add(new TeamMember("pp", "Pinkie Pie", "pinkie.pie"));
        list.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(list);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat, Pinkie Pie, and Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameBadData() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND broken members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(null);
        list.add(new TeamMember(null, "Grumpy Cat", "grumpy.cat"));
        list.add(new TeamMember("pp", null, "pinkie.pie"));
        list.add(new TeamMember("rw", "Robert A. Wallis", null));

        // WHEN a pair name is generated
        // THEN it should't crash
        assertEquals("Grumpy Cat & Robert A. Wallis", pairController.generatePairName(list));
    }

    public void testGeneratePairEmail() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND some members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("gc", "Grumpy Cat", "grumpy.cat"));
        list.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(list);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairName);

        // GIVEN a reversed list
        ArrayList<TeamMember> reversedList = new ArrayList<>();
        reversedList.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));
        reversedList.add(new TeamMember("gc", "Grumpy Cat", "grumpy.cat"));

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairController.generatePairEmail(reversedList);

        // THEN it should STILL be formatted alphabetically
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairNameReversed);
    }

    public void testGeneratePairEmailSolo() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND some members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(list);

        // THEN it should be formatted correctly
        assertEquals("prefix+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailNoPrefix() throws Exception {
        // GIVEN a configuration
        PairConfig noPrefixConfig = new PairConfig(null, "smilingrob.com");
        PairController pairController = new PairController(noPrefixConfig, gitRunner);

        // AND some members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(list);

        // THEN it should be formatted correctly
        assertEquals("robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailTrio() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND some members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(new TeamMember("gc", "Grumpy Cat", "grumpy.cat"));
        list.add(new TeamMember("pp", "Pinkie Pie", "pinkie.pie"));
        list.add(new TeamMember("rw", "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(list);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+pinkie.pie+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailBadData() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND some members
        ArrayList<TeamMember> list = new ArrayList<>();
        list.add(null);
        list.add(new TeamMember("gc", null, "grumpy.cat"));
        list.add(new TeamMember("pp", "Pinkie Pie", null));
        list.add(new TeamMember(null, "Robert A. Wallis", "robert.wallis"));

        // WHEN a pair email is generated
        // THEN it shouldn't crash
        assertEquals("prefix+robert.wallis+grumpy.cat@smilingrob.com", pairController.generatePairEmail(list));
    }

    public void testPairMatcher() throws Exception {
        // GIVEN a valid configuration and a configured email
        PairController pairController = new PairController(pairConfig, gitRunner);
        String email = "prefix+grumpy.cat+robert.wallis@smilingrob.com";

        // WHEN we get the members that match the email
        List<TeamMember> team = pairController.matchTeamMembersFromEmail(email);

        // THEN the list should match the members
        assertNotNull(team);
        assertEquals(2, team.size());
        assertEquals("Grumpy Cat", team.get(0).getName());
        assertEquals("Robert A. Wallis", team.get(1).getName());
    }

    public void testPairMatcherBadData() throws Exception {
        // GIVEN a valid configuration and a configured email
        PairController pairController = new PairController(pairConfig, gitRunner);

        // WHEN the email has no matching names
        // THEN it shouldn't crash
        pairController.matchTeamMembersFromEmail("");

        // WHEN the email is null
        // THEN it shouldn't crash
        pairController.matchTeamMembersFromEmail(null);

        // WHEN the email is not really an email
        // THEN it should still match
        List<TeamMember> team = pairController.matchTeamMembersFromEmail("grumpy.cat");
        assertNotNull(team);
        assertEquals("Grumpy Cat", team.get(0).getName());

        // WHEN the email is a bit malformed
        // THEN it should still match
        List<TeamMember> team1 = pairController.matchTeamMembersFromEmail("+grumpy.cat+");
        assertNotNull(team1);
        assertEquals("Grumpy Cat", team1.get(0).getName());
    }

    public void testToggleTeamMemberOff() throws Exception {
        // GIVEN a valid configuration and a configured email
        PairController pairController = new PairController(pairConfig, gitRunner);
        gitRunner.setUserEmail("grumpy.cat+robert.wallis");
        pairController.init();

        // WHEN a team member is toggled off
        pairController.toggleTeamMember(pairConfig.getTeamMemberByInitials("gc"));

        // THEN the team member should be toggled off
        assertEquals("Robert A. Wallis", pairController.getPairDisplayName());
    }

    public void testToggleTeamMemberOn() throws Exception {
        // GIVEN a valid configuration and a configured email
        PairController pairController = new PairController(pairConfig, gitRunner);
        gitRunner.setUserEmail("robert.wallis");
        pairController.init();

        // WHEN a team member is toggled on
        pairController.toggleTeamMember(pairConfig.getTeamMemberByInitials("gc"));

        // THEN the team member should be toggled on
        assertEquals("Grumpy Cat & Robert A. Wallis", pairController.getPairDisplayName());
    }

    public void testNoTeamMemberTitle() throws Exception {
        // GIVEN a valid configuration
        PairController pairController = new PairController(pairConfig, gitRunner);

        // AND an invalid email
        gitRunner.setUserEmail("not.valid");
        pairController.init();

        // WHEN the display name is fetched
        // THEN it should show git pair
        assertEquals("git pair", pairController.getPairDisplayName());
    }

    public void testIsPaired() throws Exception {
        // GIVEN a valid configuration and a configured email
        PairController pairController = new PairController(pairConfig, gitRunner);
        gitRunner.setUserEmail("robert.wallis");
        pairController.init();

        // WHEN a team member is toggled on
        boolean isPairedRw = pairController.isPaired(pairConfig.getTeamMemberByInitials("rw"));
        boolean isPairedGc = pairController.isPaired(pairConfig.getTeamMemberByInitials("gc"));

        // THEN the team member should be paired
        assertTrue(isPairedRw);
        assertFalse(isPairedGc);
    }
}