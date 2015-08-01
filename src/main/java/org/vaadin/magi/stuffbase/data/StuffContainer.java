package org.vaadin.magi.stuffbase.data;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;

/**
 * Container for Stuff beans.
 * 
 * Largely copied from the WorkRecordContainer example.
 * 
 * @author magi
 */
public class StuffContainer extends BeanItemContainer<Stuff> implements Container.Hierarchical {
    private static final long serialVersionUID = 7247295439999347995L;

    public StuffContainer(Stuff root) {
        super(Stuff.class);
        
        insertAllTheStuff(root);
    }

    public void insertAllTheStuff(Stuff stuff) {
        // Insert the stuff itself, unless it's the root stuff
        if (stuff.getContainer() != null)
            addBean(stuff);

        // Insert contained recursively
        for (Stuff child: stuff.getContained())
            insertAllTheStuff(child);
    }
    
    @Override
    public BeanItem<Stuff> addBean(Stuff bean) {
        BeanItem<Stuff> newBean = super.addBean(bean);

        for (Object it : bean.getContained().toArray())
            addBean((Stuff) it);

        return newBean;
    }
    
    /** Removes the given item and all its descendants. */
    public void remove(Object itemId) {
        for (Object childId: getChildren(itemId).toArray())
            remove(childId);
        
        removeItem(itemId);
    }
    
    public boolean areChildrenAllowed(Object itemId) {
        if (! getItem(itemId).getBean().getContained().isEmpty())
            return true;
        return false;
    }

    public Collection<?> getChildren(Object itemId) {
        return getItem(itemId).getBean().getContained();
    }

    public Object getParent(Object itemId) {
        return getItem(itemId).getBean().getContainer();
    }

    public boolean hasChildren(Object itemId) {
        return !getItem(itemId).getBean().getContained().isEmpty();
    }

    public boolean isRoot(Object itemId) {
        Stuff bean = getItem(itemId).getBean();
        if (bean.getContainer() == null)
            return true;
        if (bean.getContainer().getContainer() == null)
            return true;
        return false;
    }

    public Collection<?> rootItemIds() {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (Object itemId : getItemIds())
            if (isRoot(itemId))
                arrayList.add(itemId);
        return arrayList;
    }

    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean setParent(Object itemId, Object newParentId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
