package gspd.ispd.fxgui.workload.dag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GenerateWorkloadEntry {

    ////////////////////////////////
    ///////// PROPERTIES ///////////
    ////////////////////////////////

    /**
     * The user
     */
    private StringProperty user = new SimpleStringProperty(this, "user", null);
    public String getUser() {
        return user.get();
    }
    public StringProperty userProperty() {
        return user;
    }
    public void setUser(String user) {
        this.user.set(user);
    }

    /**
     * The scheduler
     */
    private StringProperty scheduler = new SimpleStringProperty(this, "scheduler", null);
    public String getScheduler() {
        return scheduler.get();
    }
    public StringProperty schedulerProperty() {
        return scheduler;
    }
    public void setScheduler(String scheduler) {
        this.scheduler.set(scheduler);
    }

    /**
     * The number of tasks
     */
    private IntegerProperty quantity = new SimpleIntegerProperty(this, "quantity", 0);
    public int getQuantity() {
        return quantity.get();
    }
    public IntegerProperty quantityProperty() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /**
     * The workload type
     */
    private IntegerProperty type = new SimpleIntegerProperty(this, "type", 0);
    public int getType() {
        return type.get();
    }
    public IntegerProperty typeProperty() {
        return type;
    }
    public void setType(int type) {
        this.type.set(type);
    }

    /**
     * The workload data itself
     */
    private Object data;
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}