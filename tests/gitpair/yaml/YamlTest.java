/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpair.yaml;

import junit.framework.TestCase;

/**
 * Test Simple YAML parsing.
 */
public class YamlTest extends TestCase {

    static final String YAML_SOURCE = "# This is a comment.\n" +
            "\n" +
            "pairs:\n" +
            "  gc: Grumpy Cat;grumpy.cat\n" +
            "  pp: Pinkie Pie; pinkie.pie\n" +
            "  rw: Robert A. Wallis; robert.wallis\n" +
            "d0:\n" +
            "  d1: 1st value\n" +
            "    2nd value\n" +
            "    3rd value\n" +
            "    d2: first d2\n" +
            "     d3:\n" +
            "    d2: second d2\n" +
            "    4th value\n" +
            "\n" +
            "email:\n" +
            "  prefix:\n" +
            "  domain: smilingrob.com\n" +
            "\n" +
            "global: true\n";

    public void testParseRoot() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);

        // THEN it should contain all the root sections
        assertNotNull(root);
        assertEquals("Loading all sections", 4, root.size());
        assertTrue(root.containsKey("pairs"));
        assertTrue(root.containsKey("email"));
        assertTrue(root.containsKey("global"));
    }

    public void testParseLevel1() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);

        // THEN it should contain pairs
        Node pairs = root.get("pairs");
        assertNotNull(pairs);

        // AND pairs should contain sublevel data
        assertTrue(pairs.containsKey("gc"));
        assertTrue(pairs.containsKey("pp"));
        assertTrue(pairs.containsKey("rw"));
    }

    public void testParseLevel1Data() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);

        // THEN it should contain pairs
        Node pairs = root.get("pairs");
        assertNotNull(pairs);

        // AND pairs should have the appropriate values
        assertEquals("Grumpy Cat;grumpy.cat", pairs.get("gc").getValue());
        assertEquals("Pinkie Pie; pinkie.pie", pairs.get("pp").getValue());
        assertEquals("Robert A. Wallis; robert.wallis", pairs.get("rw").getValue());
    }

    public void testParseMultiDepths() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);
        Node d0 = root.get("d0");
        assertNotNull(d0);

        // THEN items below the first depth should have sub items
        assertNotNull(d0.get("d1"));
        assertNotNull(d0.get("d1").get("d2"));
        assertNotNull(d0.get("d1").get("d2").get("d3"));
    }

    public void testParseKeyCollision() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);
        Node d1 = root.get("d0").get("d1");

        // THEN pairs:rw:third: should have an overwritten value
        assertNotNull(d1.get("d2"));
        assertEquals(2, d1.size());
        assertEquals("d2", d1.getChildren().get(0).getKey());
        assertEquals("first d2", d1.getChildren().get(0).getValue());
        assertEquals("d2", d1.getChildren().get(1).getKey());
        assertEquals("second d2", d1.getChildren().get(1).getValue());
    }

    public void testParseInterspersedValues() throws Exception {

        // GIVEN a test YAML file
        // WHEN we parse it
        Node root = Yaml.parse(YAML_SOURCE);

        // THEN values added before and after adding nodes should work
        assertNotNull(root.get("d0").get("d1").get("d2"));
        assertEquals("1st value\n2nd value\n3rd value\n4th value",
                root.get("d0").get("d1").getValue());
    }


    public void testDepth() throws Exception {
        assertEquals(0, Yaml.depth("a:"));
        assertEquals(1, Yaml.depth(" a:"));
        assertEquals(2, Yaml.depth("  a:"));
        assertEquals(1, Yaml.depth("\ta:"));
        assertEquals(2, Yaml.depth("\t\ta:"));
        assertEquals(3, Yaml.depth("\t \ta:"));
        assertEquals(0, Yaml.depth("a"));
        assertEquals(1, Yaml.depth(" a"));
        assertEquals(0, Yaml.depth(""));
        assertEquals(0, Yaml.depth(" "));
        assertEquals(0, Yaml.depth("  "));
    }

}