/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */
package gitpair.yaml;

import com.intellij.openapi.util.text.StringUtil;

import java.util.Stack;

/**
 * Get a simple string map for a YAML file.  Does not handle all cases, this is just simple.
 */
public class Yaml {

    /**
     * Parse a YAML file and return an object representation.
     *
     * @param fileContents contents of the YAML file.
     * @return map of the contents.
     */
    public static Node parse(String fileContents) {

        Node rootNode = new Node(null);
        Stack<Node> parentStack = new Stack<>();
        Stack<Node> nodeStack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
        parentStack.push(rootNode);
        nodeStack.push(rootNode);
        depthStack.push(-1);

        String[] lines = fileContents.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("#")) {
                // comment
                continue;
            }
            if (StringUtil.isEmpty(trimmed)) {
                continue;
            }

            int currentDepth = depth(line);

            // shallower
            if (currentDepth < depthStack.peek()) {
                while (currentDepth <= depthStack.peek()) {
                    nodeStack.pop();
                    parentStack.pop();
                    depthStack.pop();
                }
            }

            if (trimmed.contains(":")) {
                // is a new node
                String key = trimmed.substring(0, trimmed.indexOf(":"));
                Node currentNode = new Node(key);

                // deeper, only care about deeper nodes, not values
                if (currentDepth > depthStack.peek()) {
                    parentStack.push(nodeStack.peek());
                    nodeStack.push(currentNode);
                    depthStack.push(currentDepth);
                } else if (currentDepth == depthStack.peek()) {
                    // replace with new current
                    nodeStack.pop();
                    nodeStack.push(currentNode);
                }

                parentStack.peek().addChild(currentNode);

                String value = trimmed.substring(trimmed.indexOf(":") + 1);
                if (StringUtil.isNotEmpty(value)) {
                    currentNode.appendValue(value);
                }
            } else {
                // is a value
                if (currentDepth > depthStack.peek()) {
                    // add to current node if the value node is deeper
                    nodeStack.peek().appendValue(trimmed);
                } else if (currentDepth == depthStack.peek()) {
                    // add to parent node if the value is equal to the sibling node
                    parentStack.peek().appendValue(trimmed);
                }
                // if the depth was lower, the code before the ":" check should have updated the stack
            }
        }

        return rootNode;
    }

    /**
     * The amount of space in front of the content.
     *
     * @param line line of YAML.
     * @return number of spaces before content.
     */
    static int depth(String line) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return i;
            }
        }
        return 0;
    }
}
