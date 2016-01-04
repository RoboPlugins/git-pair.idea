/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.pairing;

import junit.framework.TestCase;

/**
 * Test pairing logic.
 */
public class PairControllerTest extends TestCase {

    PairConfig pairConfig = new PairConfig("prefix", "smilingrob.com");

    public void testGeneratePairName() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(grumpyCat, robert);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat & Robert A. Wallis", pairName);

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairController.generatePairName(robert, grumpyCat);

        // THEN it should STILL be formatted alphabetically
        assertEquals("Grumpy Cat & Robert A. Wallis", pairNameReversed);
    }

    public void testGeneratePairNameSolo() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND only one member
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(robert);

        // THEN it should be formatted correctly
        assertEquals("Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameTrio() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND three members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair name is generated
        String pairName = pairController.generatePairName(grumpyCat, pinkiePie, robert);

        // THEN it should be formatted correctly
        assertEquals("Grumpy Cat, Pinkie Pie, and Robert A. Wallis", pairName);
    }

    public void testGeneratePairNameBadData() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND three members
        TeamMember grumpyCat = new TeamMember(null, "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", null, "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", null);

        // WHEN a pair name is generated
        // THEN it should't crash
        assertEquals("Grumpy Cat & Robert A. Wallis", pairController.generatePairName(null, grumpyCat, pinkiePie, robert));
    }

    public void testGeneratePairEmail() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(grumpyCat, robert);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairName);

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairController.generatePairEmail(robert, grumpyCat);

        // THEN it should STILL be formatted alphabetically
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairNameReversed);
    }

    public void testGeneratePairEmailSolo() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND some members
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(robert);

        // THEN it should be formatted correctly
        assertEquals("prefix+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailNoPrefix() throws Exception {
        // GIVEN a configuration
        PairConfig noPrefixConfig = new PairConfig(null, "smilingrob.com");
        PairController pairController = new PairController(noPrefixConfig);

        // AND some members
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(robert);

        // THEN it should be formatted correctly
        assertEquals("robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailTrio() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", "pinkie.pie");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairController.generatePairEmail(robert, grumpyCat, pinkiePie);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+pinkie.pie+robert.wallis@smilingrob.com", pairName);
    }

    public void testGeneratePairEmailBadData() throws Exception {
        // GIVEN a configuration
        PairController pairController = new PairController(pairConfig);

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", null, "grumpy.cat");
        TeamMember pinkiePie = new TeamMember("pp", "Pinkie Pie", null);
        TeamMember robert = new TeamMember(null, "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        // THEN it shouldn't crash
        assertEquals("prefix+robert.wallis+grumpy.cat@smilingrob.com", pairController.generatePairEmail(null, robert, grumpyCat, pinkiePie));
    }
}