package gspd.ispd.model;

import javafx.beans.property.*;

public class VM {

    private static int ID = 1;

    private IntegerProperty id;
    private ObjectProperty<User> owner;
    private StringProperty hypervisor;
    private IntegerProperty cores;
    private DoubleProperty memory;
    private DoubleProperty storage;
    private StringProperty os;

    public VM() {
        this.id = new SimpleIntegerProperty(this, "id", ID);
        this.owner = new SimpleObjectProperty<>(this, "owner");
        this.hypervisor = new SimpleStringProperty(this, "hypervisor", "");
        this.cores = new SimpleIntegerProperty(this, "cores", 0);
        this.memory = new SimpleDoubleProperty(this, "memory", 0.0);
        this.storage = new SimpleDoubleProperty(this, "storage", 0.0);
        this.os = new SimpleStringProperty(this, "os", "");
        ID += 1;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public User getOwner() {
        return owner.get();
    }

    public Property<User> ownerProperty() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner.set(owner);
    }

    public String getHypervisor() {
        return hypervisor.get();
    }

    public StringProperty hypervisorProperty() {
        return hypervisor;
    }

    public void setHypervisor(String hypervisor) {
        this.hypervisor.set(hypervisor);
    }

    public int getCores() {
        return cores.get();
    }

    public IntegerProperty coresProperty() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores.set(cores);
    }

    public double getMemory() {
        return memory.get();
    }

    public DoubleProperty memoryProperty() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory.set(memory);
    }

    public double getStorage() {
        return storage.get();
    }

    public DoubleProperty storageProperty() {
        return storage;
    }

    public void setStorage(double storage) {
        this.storage.set(storage);
    }

    public String getOs() {
        return os.get();
    }

    public StringProperty osProperty() {
        return os;
    }

    public void setOs(String os) {
        this.os.set(os);
    }
}
