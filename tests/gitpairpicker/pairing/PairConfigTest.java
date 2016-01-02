package gitpairpicker.pairing;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

import java.util.List;

/**
 * Handles loading the configuration.
 */
public class PairConfigTest extends LightPlatformCodeInsightFixtureTestCase {

    public void testFindAllTeamMembers() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(getTestDataPath());

        // WHEN I load all the team members.
        List<TeamMember> teamMembers = pairConfig.findAllTeamMembers();

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

}