package org.vaadin.magi.stuffbase.ui;

import java.util.function.Consumer;

import org.vaadin.magi.stuffbase.data.Stuff;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class StuffEditor extends Panel {
    TextField name = new TextField("Name");
    TextField description = new TextField("Description");
    TextField amount = new TextField("Amount");
    TextField weight = new TextField("Weight");
    TextField price = new TextField("Price");
    PopupDateField acquired = new PopupDateField();
    PopupDateField booked = new PopupDateField();
    TextField source = new TextField("Source");
    TextArea comment = new TextArea("Comment");
    Image image = new Image();

    Button save = new Button("Save");
    Consumer<Stuff> consumer;
    
    public StuffEditor(Consumer<Stuff> consumer) {
        super("Edit Stuff");
        setVisible(false);
        setSizeFull();
        
        this.consumer = consumer;
        
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        GridLayout grid = new GridLayout(4,3);
        grid.setSpacing(true);
        grid.setMargin(true);
        grid.setSizeFull();
        content.addComponent(grid);
        content.setExpandRatio(grid, 1.0f);
        
        FormLayout col1 = new FormLayout();
        col1.setWidth("100%");
        name.setWidth("100%");
        description.setWidth("100%");
        comment.setWidth("100%");
        col1.addComponents(name, description, comment);
        grid.addComponent(col1, 0, 0, 0, 2);
        grid.setColumnExpandRatio(0, 1.0f);

        FormLayout col2 = new FormLayout();
        col2.setWidth("100%");
        amount.setWidth("100%");
        weight.setWidth("100%");
        price.setWidth("100%");
        col2.addComponents(amount, weight, price);
        grid.addComponent(col2, 1, 0, 1, 2);
        grid.setColumnExpandRatio(1, 0.3f);

        FormLayout col3 = new FormLayout();
        col3.setWidth("100%");
        acquired.setWidth("100%");
        booked.setWidth("100%");
        source.setWidth("100%");
        col3.addComponents(acquired, booked, source);
        grid.addComponent(col3, 2, 0, 2, 2);
        grid.setColumnExpandRatio(2, 0.3f);
        
        image.setWidth("400px");
        image.setHeight("400px");
        grid.setColumnExpandRatio(3, 1.0f);
        
        HorizontalLayout buttons = new HorizontalLayout();
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);
        
        Button cancel = new Button("Cancel");
        buttons.addComponent(cancel);

        save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        buttons.addComponent(save);
    }
    
    public void edit(Stuff stuff) {
        BeanItem<Stuff> item = new BeanItem<Stuff>(stuff);
        FieldGroup fieldgroup = new FieldGroup(item);
        fieldgroup.bindMemberFields(this);
        
        save.addClickListener(event -> {
            consumer.accept(stuff);
        });
    }
}
