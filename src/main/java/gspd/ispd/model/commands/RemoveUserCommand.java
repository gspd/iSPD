package gspd.ispd.model.commands;

import gspd.ispd.model.ISPDModel;
import gspd.ispd.model.data.User;
import gspd.ispd.util.Command;

public class RemoveUserCommand implements Command {

    private ISPDModel model;
    private User user;

    public RemoveUserCommand(ISPDModel model, User user) {
        this.model = model;
        this.user = user;
    }

    @Override
    public void execute() {
        model.getUsers().remove(user);
    }
}
