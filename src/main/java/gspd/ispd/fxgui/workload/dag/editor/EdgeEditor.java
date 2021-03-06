package gspd.ispd.fxgui.workload.dag.editor;

import gspd.ispd.fxgui.commons.EdgeIcon;
import gspd.ispd.fxgui.commons.Icon;
import gspd.ispd.fxgui.commons.IconEditor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class EdgeEditor extends IconEditor {

    private TextField messageInput;
    public EdgeEditor() {
        Label messageLabel = new Label("Message");
        messageInput = new TextField();
        messageInput.setPrefWidth(INPUT_WIDTH);
        messageInput.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("\\d*(\\.\\d*)?")) {
                messageInput.setText(n.replaceAll("[^.\\d]", ""));
            }
        });
        messageInput.textProperty().addListener((obs, o, n) -> {
            ((EdgeIcon) getIcon()).setMessageSize(Double.parseDouble(n));
        });
        add(messageLabel, 0, 1);
        add(messageInput, 1, 1);
    }


    @Override
    protected void setup(Icon icon) {
        EdgeIcon edgeIcon = (EdgeIcon) icon;
        messageInput.setText(String.valueOf(edgeIcon.getMessageSize()));
    }
}
