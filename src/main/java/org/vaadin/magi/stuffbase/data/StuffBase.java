package org.vaadin.magi.stuffbase.data;

public class StuffBase extends Stuff {
    private static final long serialVersionUID = -3366845223268644733L;
    
    int stuffCounter = 1;

    public StuffBase() {
        super(0);
    }
    
    public int getStuffCounter() {
        return stuffCounter;
    }
    
    public void setStuffCounter(int stuffCounter) {
        this.stuffCounter = stuffCounter;
    }
}
