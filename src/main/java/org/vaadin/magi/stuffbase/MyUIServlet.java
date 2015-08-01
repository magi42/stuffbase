package org.vaadin.magi.stuffbase;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = StuffbaseUI.class, productionMode = false)
public class MyUIServlet extends VaadinServlet {
}