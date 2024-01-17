package edu.cuny.ccny.ioannou_painting_application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javafx.scene.Node;

public class Assignment5_Controller {

    private enum DrawShape {
        CIRCLE("Circle"),
        RECTANGLE("Rectangle");

        private final String name;

        DrawShape(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum PenSize {

        SMALL(2),
        MEDIUM(6),
        LARGE(10),
        XLARGE(14);

        private final int radius;

        PenSize(int radius) {
            this.radius = radius;
        }
    }

    private enum DrawColor {

        BLACK(Color.BLACK),
        RED(Color.RED),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE);

        private final Color color;

        DrawColor(Color color) {
            this.color = color;
        }
    }

    @FXML
    private Button btnExit;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSaveXML;

    @FXML
    private Button btnUndo;

    @FXML
    private Pane panelDraw;

    @FXML
    private RadioButton rBtnBlack;

    @FXML
    private RadioButton rBtnBlue;

    @FXML
    private RadioButton rBtnGreen;

    @FXML
    private RadioButton rBtnLarge;

    @FXML
    private RadioButton rBtnMedium;

    @FXML
    private RadioButton rBtnRed;

    @FXML
    private RadioButton rBtnSmall;

    @FXML
    private RadioButton rBtnXLarge;

    @FXML
    private ComboBox<DrawShape> selectShape;

    @FXML
    private ToggleGroup tgColor;

    @FXML
    private ToggleGroup tgPenSize;

    private DrawColor drawColor = DrawColor.BLACK;
    private PenSize penSize = PenSize.SMALL;
    private DrawShape drawShape = DrawShape.CIRCLE;

    @FXML
    void colorChangeEventHandler() {
        if (rBtnBlack.isSelected())
            drawColor = DrawColor.BLACK;
        else if (rBtnGreen.isSelected())
            drawColor = DrawColor.GREEN;
        else if (rBtnRed.isSelected())
            drawColor = DrawColor.RED;
        else
            drawColor = DrawColor.BLUE;
    }

    @FXML
    void drawingAreaMouseDragged(MouseEvent event) {

        // ensure that the circle's perimeter doesn't overflow the drawing panels
        if (panelDraw.getLayoutBounds().contains(event.getX() - penSize.radius, event.getY() - penSize.radius) &&
                panelDraw.getLayoutBounds().contains(event.getX() + penSize.radius, event.getY() + penSize.radius)){
            if (drawShape == DrawShape.CIRCLE){
                panelDraw.getChildren().add(new Circle(event.getX(), event.getY(), penSize.radius, drawColor.color));
            }
            else if (drawShape == DrawShape.RECTANGLE){
                Rectangle rectangle = new Rectangle(event.getX(), event.getY(), penSize.radius, penSize.radius);
                rectangle.setFill(drawColor.color);
                panelDraw.getChildren().add(rectangle);
            }
        }
    }

    @FXML
    void exitButtonEventHandler() {
        System.exit(0);
    }

    @FXML
    void penSizeChangeEventHandler() {

        if (rBtnSmall.isSelected())
            penSize = PenSize.SMALL;
        else if (rBtnMedium.isSelected())
            penSize = PenSize.MEDIUM;
        else if (rBtnXLarge.isSelected())
            penSize = PenSize.XLARGE;
        else
            penSize = PenSize.LARGE;
    }

    @FXML
    void drawShapeEventHandler() {

        if (selectShape.getValue() == DrawShape.CIRCLE)
            drawShape = DrawShape.CIRCLE;
        else if (selectShape.getValue() == DrawShape.RECTANGLE)
            drawShape = DrawShape.RECTANGLE;
    }

    @FXML
    void resetButtonEventHandler() {

        panelDraw.getChildren().clear();

        rBtnBlack.setSelected(true);
        rBtnSmall.setSelected(true);

        colorChangeEventHandler();
        penSizeChangeEventHandler();

        selectShape.setValue(DrawShape.CIRCLE);
    }

    @FXML
    void saveXMLEventHandler() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as XML");
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            String fileName = String.format("%s", file.toPath().getFileName());
            if (fileName.matches("[A-Z][a-zA-Z\\d]{4,}\\.(xml|XML)")) {
                XMLEncoder xmlE = new XMLEncoder(new FileOutputStream(file.toPath().toFile()));
                for (Node node : panelDraw.getChildren()) {
                    if (node instanceof Rectangle) {
                        Rectangle rectangle = (Rectangle) node;
                        Color color = (Color) rectangle.getFill();
                        xmlE.writeObject(new A5Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight(), color.toString()));
                    }
                }
                xmlE.close();
            }
            else {
                System.err.println("Filename does not pass validation regex. You did not successfully save as XML. Please try again.");
            }
        }
        else {
            System.err.println("You did not successfully save as XML. Please try again");
        }
    }

    @FXML
    void undoButtonEventHandler() {

        if (!panelDraw.getChildren().isEmpty())
            panelDraw.getChildren().remove(panelDraw.getChildren().size() - 1);
    }

    @FXML
    void initialize() {

        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert btnSaveXML != null : "fx:id=\"btnSaveXML\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert btnUndo != null : "fx:id=\"btnUndo\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert panelDraw != null : "fx:id=\"panelDraw\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnBlack != null : "fx:id=\"rBtnBlack\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnBlue != null : "fx:id=\"rBtnBlue\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnGreen != null : "fx:id=\"rBtnGreen\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnLarge != null : "fx:id=\"rBtnLarge\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnMedium != null : "fx:id=\"rBtnMedium\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnRed != null : "fx:id=\"rBtnRed\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnSmall != null : "fx:id=\"rBtnSmall\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert rBtnXLarge != null : "fx:id=\"rBtnXLarge\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert selectShape != null : "fx:id=\"selectShape\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert tgColor != null : "fx:id=\"tgColor\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";
        assert tgPenSize != null : "fx:id=\"tgPenSize\" was not injected: check your FXML file 'PainterAssignment5.fxml'.";


        // initial state setup
        selectShape.getItems().addAll(DrawShape.values());
        selectShape.setValue(selectShape.getItems().get(0));
    }
}