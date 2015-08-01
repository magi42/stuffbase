package org.vaadin.magi.stuffbase;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.vaadin.magi.stuffbase.data.Stuff;
import org.vaadin.magi.stuffbase.data.StuffBase;
import org.vaadin.magi.stuffbase.data.StuffContainer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

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
    
    public StuffView(final StuffBase base) {
        final StuffContainer container = new StuffContainer(base);

        container.insertAllTheStuff(base);

        final TreeTable stuffTable = new TreeTable(null, container);
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
        stuffTable.addListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = 7091148654878545477L;

            public void valueChange(ValueChangeEvent event) {
                System.out.println("Selected " + event.getProperty().getValue());
            }
        });
        stuffTable.setImmediate(true);

        final HashMap<Object,HashMap<Object,Field>> fields = new HashMap<Object,HashMap<Object,Field>>();
        final HashMap<Field,Object> itemIds = new HashMap<Field,Object>(); 
        
        stuffTable.setEditable(true);
        stuffTable.setTableFieldFactory(new TableFieldFactory() {
            private static final long serialVersionUID = -5741977060384915110L;

            public Field createField(Container container, final Object itemId,
                    final Object propertyId, Component uiContext) {
                final TextField tf = new TextField();
                tf.setData(new ItemPropertyId(itemId, propertyId));

                // Manage the field in the field storage
                HashMap<Object,Field> itemMap = fields.get(itemId);
                if (itemMap == null) {
                    itemMap = new HashMap<Object,Field>();
                    fields.put(itemId, itemMap);
                }
                itemMap.put(propertyId, tf);
                
                itemIds.put(tf, itemId);
                
                tf.setReadOnly(true);
                tf.addListener(new FocusListener() {
                    private static final long serialVersionUID = 1006388127259206641L;

                    public void focus(FocusEvent event) {
                        // Make the entire item editable
                        HashMap<Object,Field> itemMap = fields.get(itemId);
                        for (Field f: itemMap.values())
                            f.setReadOnly(false);
                    }
                });
                tf.addListener(new BlurListener() {
                    private static final long serialVersionUID = -4497552765206819985L;

                    public void blur(BlurEvent event) {
                        // Make the entire item read-only
                        HashMap<Object,Field> itemMap = fields.get(itemId);
                        for (Field f: itemMap.values())
                            f.setReadOnly(true);
                    }
                });
                
                if (((String)propertyId).equals("description"))
                    tf.setWidth("100%");
                if (((String)propertyId).equals("amount") ||
                    ((String)propertyId).equals("price"))
                    tf.setWidth("5em");
                return tf;
            }
        });
        
        // Keyboard navigation
        class KbdHandler implements Handler {
            private static final long serialVersionUID = -2993496725114954915L;

            Action tab_next = new ShortcutAction("Shift",
                    ShortcutAction.KeyCode.TAB, null);
            Action tab_prev = new ShortcutAction("Shift+Tab",
                    ShortcutAction.KeyCode.TAB,
                    new int[] {ShortcutAction.ModifierKey.SHIFT});
            Action cur_down = new ShortcutAction("Down",
                    ShortcutAction.KeyCode.ARROW_DOWN, null);
            Action cur_up   = new ShortcutAction("Up",
                    ShortcutAction.KeyCode.ARROW_UP,   null);
            Action enter    = new ShortcutAction("Enter",
                    ShortcutAction.KeyCode.ENTER,      null);
            Action add      = new ShortcutAction("Add Below",
                    ShortcutAction.KeyCode.A,          null);
            Action delete   = new ShortcutAction("Delete",
                    ShortcutAction.KeyCode.DELETE,     null);

            public Action[] getActions(Object target, Object sender) {
                return new Action[] {tab_next, tab_prev, cur_down,
                                     cur_up, enter, add, delete};
            }

            @SuppressWarnings("unchecked")
            public void handleAction(Action action, Object sender,
                                     Object target) {
                System.out.println("Action target = " + target.getClass().getName());

                if (target instanceof TextField) {
                    TextField tf = (TextField) target;
                    ItemPropertyId ipId = (ItemPropertyId) tf.getData();
                    
                    // On enter, close the edit mode
                    if (action == enter) {
                        // Make the entire item read-only
                        HashMap<Object,Field> itemMap = fields.get(ipId.getItemId());
                        for (Field f: itemMap.values())
                            f.setReadOnly(true);
                        stuffTable.select(ipId.getItemId());
                        stuffTable.focus();
                        return;
                    }
                    
                    Object propertyId = ipId.getPropertyId();
                    System.out.println("itemId = " + ipId.getItemId() + ", propertyId = " + ipId.getPropertyId());
                    
                    // Find the index of the property
                    Object cols[] = stuffTable.getVisibleColumns();
                    int pidIndex = 0;
                    for (int i=0; i<cols.length; i++)
                        if (cols[i].equals(propertyId))
                            pidIndex = i;
                    
                    System.out.println("Cols = " +  Arrays.deepToString(cols) + ", pidIndex = " + pidIndex);

                    Object newItemId     = null;
                    Object newPropertyId = null;
                    
                    // Move according to keypress
                    if (action == cur_down)
                        newItemId = container.nextItemId(ipId.getItemId());
                    else if (action == cur_up)
                        newItemId = container.prevItemId(ipId.getItemId());
                    else if (action == tab_next)
                        newPropertyId = cols[Math.min(pidIndex+1, cols.length-1)];
                    else if (action == tab_prev)
                        newPropertyId = cols[Math.max(pidIndex-1, 0)];

                    // If tried to go past first or last, just stay there
                    if (newItemId == null)
                        newItemId = ipId.getItemId();
                    if (newPropertyId == null)
                        newPropertyId = ipId.getPropertyId();
                    
                    // On enter, just stay where you were. If we did
                    // not catch the enter action, the focus would be
                    // moved to wrong place.

                    Field newField = fields.get(newItemId).get(newPropertyId);
                    if (newField != null)
                        newField.focus();
                } else if (target instanceof TreeTable) {
                    TreeTable table = (TreeTable) target;
                    Collection<?> selected = ((Collection<?>) table.getValue());
                    System.out.println("Selected is a " + selected.getClass().getName());

                    if (selected == null || selected.size() == 0)
                        return;
                    
                    if (action == enter) {
                        // Make the entire item editable
                        HashMap<Object,Field> itemMap = fields.get(selected.toArray()[0]);
                        for (Field f: itemMap.values())
                            f.setReadOnly(false);
                        
                        // Focus the first column
                        itemMap.get(stuffTable.getVisibleColumns()[0]).focus();
                    } else if (action == add) {
                        
                    } else if (action == delete) {
                        for (Object itemId: selected.toArray()) {
                            Item item = stuffTable.getItem(itemId);
                            if (item != null && item instanceof BeanItem<?>) {
                                // Remove the item from the container
                                BeanItem<Stuff> stuffItem = (BeanItem<Stuff>) item;
                                container.remove(itemId);
                                //stuffTable.removeItem(itemId);
                                
                                // Remove from the bean structure
                                base.removeStuff(stuffItem.getBean());
                                
                                // Remove from the map
                            }
                        }
                    }
                }
            }
        }
        
        stuffTable.focus();
        stuffTable.setSizeFull();
        // stuffTable.select(stuffTable.getItemIds().toArray()[0]);
                
        // Panel that handles keyboard navigation
        Panel navigator = new Panel();
        navigator.addStyleName(ValoTheme.PANEL_BORDERLESS);
        VerticalLayout navigatorContent = new VerticalLayout();
        navigator.setContent(navigatorContent);
        navigatorContent.addComponent(new Label("Press <b>Enter</b> to edit/accept an item, <b>Tab</b> and <b>Shift+Tab</b> to navigate fields, "+
                "<b>Up</b> and <b>Down</b> to move to previous/next item, and <b>Right</b> and <b>Left</b> to expand/collapse an item.", Label.CONTENT_XHTML));
        navigatorContent.addComponent(stuffTable);
        navigatorContent.setExpandRatio(stuffTable, 1.0f);
        navigator.addActionHandler(new KbdHandler());
        navigator.getContent().setSizeFull();
        navigator.setSizeFull();
        
        setSizeFull();
        setCompositionRoot(navigator);
    }
}
