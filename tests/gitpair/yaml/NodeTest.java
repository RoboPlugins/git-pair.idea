/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpair.yaml;

import junit.framework.TestCase;

/**
 * Make sure YAML node logic works.
 */
public class NodeTest extends TestCase {

    public void testGet() throws Exception {
        // GIVEN a node graph
        Node node = new Node(null);

        // WHEN a child is added
        Node child = new Node("test");
        node.addChild(child);

        // THEN we can retrieve it by the key name
        assertEquals(child, node.get("test"));
    }

    public void testSize() throws Exception {
        // GIVEN a node graph
        Node node = new Node(null);
        node.addChild(new Node("test"));
        node.addChild(new Node("test"));
        node.addChild(new Node("test"));

        // WHEN we ask it how big it is
        // THEN it returns the number of children
        assertEquals(3, node.size());
    }

    public void testAppendValue() throws Exception {
        // GIVEN a node
        Node node = new Node(null);

        // WHEN we append more than one value
        node.appendValue("value 1");
        node.appendValue("value 2");

        // THEN then both values have been added
        assertEquals("value 1\nvalue 2", node.getValue());

        // WHEN we overwrite the value
        node.setValue("value 3");

        // THEN the value is overwritten
        assertEquals("value 3", node.getValue());
    }

    public void testContainsKey() throws Exception {
        // GIVEN a node graph
        Node node = new Node(null);

        // WHEN a child is added
        Node child = new Node("test");
        node.addChild(child);

        // THEN the child exists
        assertTrue(node.containsKey("test"));

        // AND a non added child does not exist
        assertFalse(node.containsKey("not inserted"));

        // WHEN we rename a child

        // THEN we can get it by it's new name
    }

    public void testRenameChild() throws Exception {
        // GIVEN a node graph
        Node node = new Node(null);

        // WHEN a child is added
        Node child = new Node("test");
        node.addChild(child);

        // THEN the child exists
        assertTrue(node.containsKey("test"));

        // AND a non added child does not exist
        assertFalse(node.containsKey("renamed"));

        // WHEN we rename a child
        child.setKey("renamed");

        // THEN the child exists by it's new name
        assertTrue(node.containsKey("renamed"));

        // AND the old name does not exist
        assertFalse(node.containsKey("test"));
    }
}