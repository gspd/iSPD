package gspd.ispd.fxgui.workload.dag.icons;

import gspd.ispd.fxgui.commons.Icon;
import gspd.ispd.commons.ISPDType;
import gspd.ispd.fxgui.commons.IconEditor;
import gspd.ispd.fxgui.commons.NodeIcon;
import gspd.ispd.fxgui.workload.dag.editor.RecursionEditor;
import gspd.ispd.fxgui.workload.dag.shapes.RecursionShape;
import javafx.scene.paint.Color;
import javafx.util.Builder;

public class RecursionIcon extends NodeIcon {

    public static final ISPDType RECURSION_TYPE = ISPDType.type(NODE_TYPE, "RECURSION_TYPE");

    //////////////////////////////////////////
    //////////// CONSTRUCTOR /////////////////
    //////////////////////////////////////////

    public RecursionIcon(boolean selected, double centerX, double centerY) {
        super(RecursionShape::new, selected, centerX, centerY);

        setType(RECURSION_TYPE);
    }

    public RecursionIcon(double centerX, double centerY) {
        this(false, centerX, centerY);
    }

    public RecursionIcon(boolean selected) {
        this(selected, 0.0, 0.0);
    }

    public RecursionIcon() {
        this(false, 0.0, 0.0);
    }

    //////////////////////////////////////////
    ////////////// OVERRIDE //////////////////
    //////////////////////////////////////////

    private static final Builder<RecursionIcon> RECURSION_BUILDER = RecursionIcon::new;
    @Override
    public Builder<? extends Icon> iconBuilder() {
        return RECURSION_BUILDER;
    }

    private static final RecursionEditor RECURSION_EDITOR = new RecursionEditor();
    @Override
    protected IconEditor editor() {
        RECURSION_EDITOR.setIcon(this);
        return RECURSION_EDITOR;
    }

    @Override
    protected void updateIcon() {
        RecursionShape shape = (RecursionShape) getContent();
        if (isSelected()) {
            shape.setFill(Color.LIGHTSKYBLUE);
            shape.setStroke(Color.DODGERBLUE);
        } else {
            shape.setFill(Color.WHITE);
            shape.setStroke(Color.BLACK);
        }
    }
}
