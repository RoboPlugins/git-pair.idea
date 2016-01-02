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
        assertEquals("gc", teamMembers.get(0).getTag());

        // AND it should have Pinkie Pie
        assertEquals("Pinkie Pie", teamMembers.get(1).getName());
        assertEquals("pinkie.pie", teamMembers.get(1).getEmail());
        assertEquals("pp", teamMembers.get(1).getTag());

        // AND it should have Robert Wallis
        assertEquals("Robert A. Wallis", teamMembers.get(2).getName());
        assertEquals("robert.wallis", teamMembers.get(2).getEmail());
        assertEquals("rw", teamMembers.get(2).getTag());
    }

    public void testGeneratePairName() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(getTestDataPath());

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

    public void testGeneratePairEmail() throws Exception {
        // GIVEN a configuration
        PairConfig pairConfig = new PairConfig(getTestDataPath());

        // AND some members
        TeamMember grumpyCat = new TeamMember("gc", "Grumpy Cat", "grumpy.cat");
        TeamMember robert = new TeamMember("rw", "Robert A. Wallis", "robert.wallis");

        // WHEN a pair email is generated
        String pairName = pairConfig.generatePairEmail(grumpyCat, robert);

        // THEN it should be formatted correctly
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairName);

        // WHEN a pair name is generated in reverse order
        String pairNameReversed = pairConfig.generatePairName(robert, grumpyCat);

        // THEN it should STILL be formatted alphabetically
        assertEquals("prefix+grumpy.cat+robert.wallis@smilingrob.com", pairName);
    }
}