/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import compiler.compiler;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author m-cam
 */
public class DerivationTreeApp extends Application {

    private Pane treePane;
    private TextArea codeInput;

    @Override
    public void start(Stage primaryStage) {
        // Create the pane where the derivation tree will be drawn
        treePane = new Pane();

        // Create the text area for user code input
        codeInput = new TextArea();

        // Set size and style for the tree pane
        treePane.setPrefSize(1000, 700);
        treePane.setStyle("-fx-background-color: white; -fx-border-color: black;");

        // Set preferred size for the input area
        codeInput.setPrefSize(300, 500);

        // Create the compile button and set its action
        Button compileButton = new Button("Compile");
        compileButton.setOnAction(e -> compileAndDisplay());

        // Create the main layout with the tree pane and code input side by side
        HBox mainLayout = new HBox(10, treePane, codeInput);

        // Use a BorderPane to position elements in the window
        BorderPane root = new BorderPane();
        root.setCenter(mainLayout);           // Place the main layout at the center
        root.setBottom(compileButton);        // Place the compile button at the bottom
        BorderPane.setMargin(compileButton, new Insets(10)); // Add spacing around the button

        // Set up the scene and show the window
        Scene scene = new Scene(root, 950, 550);
        primaryStage.setTitle("Derivation Tree Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void compileAndDisplay() {
        // Clear any existing content in the tree panel
        treePane.getChildren().clear();

        try {
            // Instantiate the compiler and compile the user's code
            compiler compiler = new compiler();
            TreeNode root = compiler.compile(codeInput.getText());

            // Get the horizontal center of the pane
            double centerX = treePane.getPrefWidth() / 2;

            // Start drawing the derivation tree from the root
            drawTree(root, centerX, 40, 1);

        } catch (Exception ex) {
            // Display error if compilation fails
            showError(ex.getMessage());
        }
    }

    private void drawTree(TreeNode node, double x, double y, double scale) {
        if (node == null) {
            return;
        }

        // Create the circle representing the node
        Circle circle = new Circle(x, y, 20);

        // Choose the color based on the token type (null means non-terminal)
        Color fill = (node.type == null) ? Color.LIGHTBLUE : getColorForType(node.type);
        circle.setFill(fill);
        circle.setStroke(getColorForType(node.type));

        // Create and center the label for the node
        Text label = new Text(node.label);
        double textWidth = label.getLayoutBounds().getWidth();
        double textHeight = label.getLayoutBounds().getHeight();
        label.setX(x - textWidth / 2);
        label.setY(y + textHeight / 4);

        // Add circle and label to the pane
        treePane.getChildren().addAll(circle, label);

        // If the node has no children, stop here
        if (node.children.isEmpty()) {
            return;
        }

        // Count total leaf nodes in all children to calculate space needed
        double totalLeaves = 0;
        for (TreeNode child : node.children) {
            totalLeaves += countLeaves(child);
        }

        double spacing = scale * 50; // Horizontal spacing between leaves
        double startX = x - (totalLeaves * spacing) / 2; // Starting X for first child

        // Recursively draw all child nodes
        for (TreeNode child : node.children) {
            int leaves = countLeaves(child);
            double childWidth = leaves * spacing;
            double childX = startX + childWidth / 2;
            double childY = y + 80; // Vertical offset for next level

            // Draw line connecting parent to child
            Line line = new Line(x, y + 20, childX, childY - 20);
            treePane.getChildren().add(line);

            // Recursive call for child node
            drawTree(child, childX, childY, scale * 1);
            startX += childWidth;
        }
    }

    // Counts the number of leaf nodes in a subtree
    private int countLeaves(TreeNode node) {
        if (node.children.isEmpty()) {
            return 1;
        }

        int total = 0;
        for (TreeNode child : node.children) {
            total += countLeaves(child);
        }
        return total;
    }

    // Displays an error alert with the given message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Compilation Error");
        alert.setHeaderText("An error occurred");

        // Create a non-editable text area for the error message
        TextArea textArea = new TextArea(message);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(textArea, 0, 0);

        alert.getDialogPane().setContent(content);
        alert.showAndWait(); // Show the dialog
    }

// Returns a specific color depending on the token type
    private Color getColorForType(String type) {
        if (type == null) {
            return Color.LIGHTBLUE; // Default color for non-terminal symbols
        }
        if (type.equals("KEYWORD")) {
            return Color.LIGHTCORAL;
        }
        if (type.equals("IDENTIFIER")) {
            return Color.LIGHTGREEN;
        }
        if (type.equals("NUMBER")) {
            return Color.LIGHTYELLOW;
        }
        if (type.equals("OPERATOR")) {
            return Color.LIGHTPINK;
        }
        if (type.equals("LBRACE") || type.equals("RBRACE")
                || type.equals("LPAREN") || type.equals("RPAREN")) {
            return Color.LIGHTGRAY;
        }
        return Color.LIGHTBLUE; // Fallback color
    }
}
