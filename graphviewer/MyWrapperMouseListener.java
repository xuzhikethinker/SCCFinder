/*
 * Created on July 7, 2010
 *
 * Copyright (c) 2010, Voulimeneas Alexios
 * All rights reserved.
 *
 * This software is open-source under the BSD license
 * see "license.txt" for more information 
 */
package graphviewer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A wrapper class for MouseListener
 * 
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class MyWrapperMouseListener extends MouseAdapter {
	
    private final MouseListener mouselistener;

    public MyWrapperMouseListener(MouseListener m) {
    	this.mouselistener = m;
    }
    
    private boolean isLeftMouseButton(MouseEvent e) {
    	return e.getButton() == MouseEvent.BUTTON1;
    }
    
    public void mouseClicked(MouseEvent e) {
	    if(isLeftMouseButton(e)) {
	    	this.mouselistener.mouseClicked(e);
        }
    }

    public void mousePressed(MouseEvent e) {
    	if(isLeftMouseButton(e)){
        	this.mouselistener.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isLeftMouseButton(e)) {
    		this.mouselistener.mouseReleased(e);
        }
    }
}