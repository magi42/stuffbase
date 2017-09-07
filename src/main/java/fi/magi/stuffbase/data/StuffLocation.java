package fi.magi.stuffbase.data;

import java.io.Serializable;

public class StuffLocation extends Stuff implements Serializable {
    private static final long serialVersionUID = 3106708911180828595L;

    public StuffLocation(Stuff container, String name, String description, String comment) {
        super(container, name, description, 0, 0, 0, null, null, null, comment);
    }
}
