package gitpairpicker.yaml;

import junit.framework.TestCase;

import java.util.Map;

/**
 * Test Simple YAML parsing.
 */
public class YamlMapTest extends TestCase {

    static final String YAML_FILE = "# This is a comment.\n" +
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

    public void testParseRoot() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Map<String, Object> root = YamlMap.parse(YAML_FILE);

        // THEN it should contain all the root sections
        assertNotNull(root);
        assertEquals("Loading all sections", 3, root.size());
        assertTrue(root.containsKey("pairs"));
        assertTrue(root.containsKey("email"));
        assertTrue(root.containsKey("global"));
    }

    public void testParseLevel1() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Map<String, Object> root = YamlMap.parse(YAML_FILE);

        // THEN it should contain pairs
        Map<String, Object> pairs = (Map<String, Object>) root.get("pairs");
        assertNotNull(pairs);

        // AND pairs should contain sublevel data
        assertEquals(3, pairs.size());
        assertTrue(pairs.containsKey("gc"));
        assertTrue(pairs.containsKey("pp"));
        assertTrue(pairs.containsKey("rw"));
    }

    public void testParseLevel1Data() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Map<String, Object> root = YamlMap.parse(YAML_FILE);

        // THEN it should contain pairs
        Map<String, Object> pairs = (Map<String, Object>) root.get("pairs");
        assertNotNull(pairs);

        // AND pairs should have the appropriate values
        assertEquals(3, pairs.size());
        assertEquals("Grumpy Cat;grumpy.cat", pairs.get("gc"));
        assertEquals("Pinkie Pie; pinkie.pie", pairs.get("pp"));
        assertEquals("Robert A. Wallis; robert.wallis", pairs.get("rw"));
    }

}