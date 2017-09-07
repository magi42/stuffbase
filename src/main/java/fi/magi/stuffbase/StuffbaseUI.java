package fi.magi.stuffbase;

import fi.magi.stuffbase.data.Stuff;
import fi.magi.stuffbase.data.StuffBase;
import fi.magi.stuffbase.data.StuffLocation;
import fi.magi.stuffbase.ui.StuffView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * 
 */
@Theme("stufftheme")
@Widgetset("fi.magi.stuffbase.StuffbaseWidgetset")
public class StuffbaseUI extends UI {
	private static final long serialVersionUID = 5808444354086789262L;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        StuffBase stuffbase = new StuffBase();
        
        StuffLocation home = new StuffLocation(stuffbase, "Home", null, null);
        StuffLocation livingRoom = new StuffLocation(home, "Living Room", null, null);
        new Stuff(livingRoom, "Rug", "Red, Persian style", 1, 0, 100, null, null, "IKEA", null);
        new Stuff(livingRoom, "Bed", "Single, old, broken", 1, 0, 0, null, null, null, null);
        new Stuff(livingRoom, "Lamp", "Bronze leg, 150 cm high, bending", 3, 0, 35, null, null, "Anttila", null);
        new Stuff(livingRoom, "Table Lamp", "Bronze leg, low", 1, 0, 40, null, null, "Anttila", null);
        new Stuff(livingRoom, "Chair", "Yellow", 1, 0, 0, null, null, null, null);

        
        StuffView mainView = new StuffView(stuffbase);
        setContent(mainView);
    }
}
