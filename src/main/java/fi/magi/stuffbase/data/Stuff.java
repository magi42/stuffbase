package fi.magi.stuffbase.data;

import java.util.ArrayList;
import java.util.Date;

public class Stuff {
    private static final long serialVersionUID = 5606960066094750099L;

    int              id;
    Stuff            container;
    String           name;
    String           description;
    float            amount;
    float            weight;
    float            price;
    Date             acquired = null;
    Date             booked = null;
    String           source;
    String           comment;
    ArrayList<Stuff> contained = new ArrayList<Stuff>();

    protected Stuff(int id) {
        this.id = id;
    }

    /**
     * Creates new stuff.
     * 
     * Automatically adds the stuff to the parent container.
     */
    public Stuff(Stuff container, String name, String description, float amount, float weight, float price, Date acquired, Date booked, String source, String comment) {
        this.container   = container;
        this.name        = name;
        this.description = description;
        this.amount      = amount;
        this.weight      = weight;
        this.price       = price;
        this.acquired    = acquired;
        this.booked      = booked;
        this.source      = source;
        this.comment     = comment;
        
        // Add self to the container
        if (container != null)
            container.addStuff(this);
    }
    
    public StuffBase getRoot() {
        Stuff stuff = this;
        while (stuff.container != null)
            stuff = stuff.container;
        return (StuffBase) stuff;
    }
    
    public void addStuff(Stuff stuff) {
        contained.add(stuff);
    }
    
    public boolean removeStuff(Stuff stuff) {
        for (Stuff child: (Stuff[]) getContained().toArray())
            if (child.equals(stuff)) {
                getContained().remove(stuff);
                return true;
            } else  // Find recursively
                if (child.removeStuff(stuff))
                    return true;
        return false;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Stuff> getContained() {
        return contained;
    }
    public void setContained(ArrayList<Stuff> contained) {
        this.contained = contained;
    }

    public Stuff getContainer() {
        return container;
    }
    public void setContainer(Stuff container) {
        this.container = container;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public Date getAcquired() {
        return acquired;
    }
    public void setAcquired(Date acquired) {
        this.acquired = acquired;
    }
    public Date getBooked() {
        return booked;
    }
    public void setBooked(Date booked) {
        this.booked = booked;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
