package org.vaadin.magi.stuffbase.ui;

import java.util.Set;

import org.vaadin.magi.stuffbase.data.Stuff;
import org.vaadin.magi.stuffbase.data.StuffBase;
import org.vaadin.magi.stuffbase.data.StuffContainer;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

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
        StuffContainer container = new StuffContainer(base);

        container.insertAllTheStuff(base);

        TreeTable stuffTable = new TreeTable(null, container);
        stuffTable.setSizeFull();
        stuffTable.setVisibleColumns(new Object[]{"name", "description", "amount", "price", "source", "acquired", "booked", "comment"});
        stuffTable.setColumnExpandRatio("name", 1.0f);
        stuffTable.setColumnExpandRatio("description", 1.0f);
        stuffTable.setColumnExpandRatio("amount", 0.0f);
        stuffTable.setColumnExpandRatio("price", 0.0f);
        stuffTable.setColumnExpandRatio("source", 0.0f);
        stuffTable.setColumnExpandRatio("acquired", 0.0f);
        stuffTable.setColumnExpandRatio("booked", 0.0f);
        stuffTable.setColumnExpandRatio("comment", 1.0f);
        
        stuffTable.setSelectable(true);
        stuffTable.setMultiSelect(true);
        stuffTable.addValueChangeListener(event -> {
            Set<Object> itemIds = (Set<Object>) event.getProperty().getValue();
            System.out.println("Selected " + itemIds);
            
            if (itemIds.size() == 1) {
                Object itemId = itemIds.stream().findFirst().get();
                Stuff stuff = container.getItem(itemId).getBean();
                editor.edit(stuff);
                editor.setVisible(true);
            }
        });
        stuffTable.setImmediate(true);

        stuffTable.focus();
        stuffTable.setSizeFull();
                
        // Panel that handles keyboard navigation
        VerticalLayout content = new VerticalLayout();
        content.addComponent(new Label("Stuffbase"));

        content.addComponent(stuffTable);
        content.setExpandRatio(stuffTable, 1.0f);
        
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
