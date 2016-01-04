/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Test pairing logic.
 */
public class PairControllerTest extends TestCase {

    PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

    public void testGeneratePairName() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(noPrefixConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
        PairController pairController = new PairController(pairConfig, "");

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
}