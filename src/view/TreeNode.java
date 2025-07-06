/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
/**
 *
 * @author m-cam
 */
import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    // The text shown inside the node (e.g., a keyword, identifier, etc.)
    public String label;

    // The token type (e.g., "IDENTIFIER", "NUMBER", "OPERATOR", etc.)
    public String type;

    // List of children nodes (used to build the tree hierarchy)
    public List<TreeNode> children = new ArrayList<>();

    // Coordinates of the parent node (used for drawing purposes)
    public double parentX, parentY;

    // Constructor for non-terminal nodes or tokens without type
    public TreeNode(String label) {
        this.label = label;
    }

    // Constructor that sets both label and token type (for styling/coloring)
    public TreeNode(String label, String type) {
        this.label = label;
        this.type = type;
    }

    // Adds a child node to this node's list of children
    public void addChild(TreeNode child) {
        children.add(child);
    }
}