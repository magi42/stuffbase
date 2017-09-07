package fi.magi.stuffbase.ui;

import java.util.Set;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;

import fi.magi.stuffbase.data.Stuff;
import fi.magi.stuffbase.data.StuffBase;

public class StuffView extends CustomComponent {
    private static final long serialVersionUID = -6584441341177953894L;

    class ItemPropertyId {
        Object itemId;
        Object propertyId;
        
        public ItemPropertyId(Object itemId, Object propertyId) {
            this.itemId = itemId;
            this.propertyId = propertyId;
        }
        
        public Object getItemId() {
            return itemId;
        }
        
        public Object getPropertyId() {
            return propertyId;
        }
    }
    
    StuffEditor editor;
    
    public StuffView(final StuffBase base) {
        TreeGrid<Stuff> stuffGrid = new TreeGrid<Stuff>();
        stuffGrid.setItems(base.getContained(), Stuff::getContained);
        
        // Define columns
        stuffGrid.setColumns("name", "description", "amount", "price", "source", "acquired", "booked", "comment");
        stuffGrid.getColumn("name").setExpandRatio(1);
        stuffGrid.getColumn("description").setExpandRatio(0);
        stuffGrid.getColumn("amount").setExpandRatio(0);
        stuffGrid.getColumn("price").setExpandRatio(0);
        stuffGrid.getColumn("source").setExpandRatio(0);
        stuffGrid.getColumn("acquired").setExpandRatio(0);
        stuffGrid.getColumn("booked").setExpandRatio(0);
        stuffGrid.getColumn("comment").setExpandRatio(1);
        
        stuffGrid.setSelectionMode(SelectionMode.MULTI);
        stuffGrid.addSelectionListener(event -> {
            Set<Stuff> selected = event.getAllSelectedItems();
            // System.out.println("Selected " + itemIds);
            
            if (selected.size() == 1) {
                Stuff stuff = selected.iterator().next(); 
                editor.edit(stuff);
                editor.setVisible(true);
            }
        });

        stuffGrid.setSizeFull();
        stuffGrid.focus();
                
        // Panel that handles keyboard navigation
        VerticalLayout content = new VerticalLayout();
        content.addComponent(new Label("Stuffbase"));

        content.addComponent(stuffGrid);
        content.setExpandRatio(stuffGrid, 1.0f);
        
        editor = new StuffEditor(stuff -> {
            Notification.show("Saved (not)");
        });
        content.addComponent(editor);
        content.setExpandRatio(editor, 1.0f);
        
        content.setSizeFull();
        
        setSizeFull();
        setCompositionRoot(content);
    }
}
