package gspd.ispd.fxgui.workload.dag.editor;

import gspd.ispd.commons.StringConstants;
import gspd.ispd.fxgui.commons.Icon;
import gspd.ispd.fxgui.commons.IconEditor;
import gspd.ispd.fxgui.workload.dag.icons.TaskIcon;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TaskEditor extends NodeEditor {

    private TextField labelInput;
    private TextField mflopInput;
    private ComboBox<String> lockInput;

    public TaskEditor() {
        setTitle(StringConstants.TASK_TITLE);
        // LABEL
        Label labelLabel = new Label("Label");
        labelInput = new TextField("");
        labelInput.setPrefWidth(INPUT_WIDTH);
        labelInput.textProperty().addListener((obs, n, v) -> {
            ((TaskIcon)getIcon()).setLabel(labelInput.getText());
        });
        add(labelLabel, 0, 1);
        add(labelInput, 1, 1);
        // MFLOP
        Label mflopLabel = new Label("MFLOP");
        mflopInput = new TextField();
        mflopInput.setPrefWidth(INPUT_WIDTH);
        mflopInput.textProperty().addListener((obs, o, n) -> {
            if (!n.matches("\\d*(\\.\\d*)?")) {
                mflopInput.setText(n.replaceAll("[^.\\d]", ""));
            }
        });
        mflopInput.textProperty().addListener((obs, o, n) -> {
            ((TaskIcon)getIcon()).setComputingSize(Double.parseDouble(mflopInput.getText()));
        });
        add(mflopLabel, 0, 2);
        add(mflopInput, 1, 2);
        // LOCK
        Label lockLabel = new Label("Lock");
        lockInput = new ComboBox<>();
        lockInput.setPrefWidth(INPUT_WIDTH);
        lockInput.setEditable(true);
        lockInput.valueProperty().addListener((obs, o, n) -> {
            if (n == null || n.equals("")) {
                ((TaskIcon)getIcon()).setLock(null);
            } else {
                ((TaskIcon)getIcon()).setLock(n);
            }
        });
        add(lockLabel, 0, 3);
        add(lockInput, 1, 3);
    }

    @Override
    protected void setup(Icon icon) {
        super.setup(icon);
        TaskIcon taskIcon = (TaskIcon) icon;
        labelInput.setText(taskIcon.getLabel());
        mflopInput.setText(String.valueOf(taskIcon.getComputingSize()));
        lockInput.setValue(taskIcon.getLock() == null ? "" : taskIcon.getLock());
    }

}
