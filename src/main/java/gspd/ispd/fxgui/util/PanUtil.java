package gspd.ispd.fxgui.util;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;

public class PanUtil {

    public void makePannable(ScrollPane pane) {
        pane.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                pane.setPannable(true);
            }
        });
        pane.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                pane.setPannable(false);
            }
        });
    }
}