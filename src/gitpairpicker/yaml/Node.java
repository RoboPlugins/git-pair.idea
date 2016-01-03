// Copyright (C) 2016 Robert A. Wallis, ALl Rights Reserved.
package gitpairpicker.yaml;

import com.intellij.openapi.util.text.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a YAML node.
 */
public class Node {

    private String key;
    private String value;
    private List<Node> children = new ArrayList<>();

    /**
     * Create a new Yaml Node.
     *
     * @param key name of the node.
     */
    public Node(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Node> getChildren() {
        return children;
    }

    /**
     * Get a child by name.
     *
     * @param key name of the child.
     * @return child or null if not present.
     */
    public Node get(String key) {
        for (Node child : children) {
            if (StringUtil.equals(child.getKey(), key)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Number of children.
     *
     * @return number of children.
     */
    public int size() {
        return children.size();
    }

    /**
     * Add a new child to the end of the children.
     *
     * @param child new child of this node.
     */
    public void addChild(Node child) {
        children.add(child);
    }

    /**
     * Adds a newline and the value if there was already a value.
     * Trims value.
     *
     * @param value new value to add.
     */
    public void appendValue(String value) {
        if (this.value == null) {
            this.value = "";
        }
        if (StringUtil.isEmpty(this.value)) {
            this.value = value.trim();
        } else {
            this.value += "\n" + value.trim();
        }
    }

    /**
     * Determines if a child of the key exists.
     *
     * @param key name of the child.
     * @return child node.
     */
    public boolean containsKey(String key) {
        for (Node child : children) {
            if (StringUtil.equals(child.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String k = key == null ? "" : key;
        String v = value == null ? "" : value;
        return "Node{" + k + ':' +
                v + ", " +
                children +
                '}';
    }
}
