/*
 * Created on July 7, 2010
 *
 * Copyright (c) 2010, Voulimeneas Alexios
 * All rights reserved.
 *
 * This software is open-source under the BSD license
 * see "license.txt" for more information 
 */

import graphviewer.GraphCreate;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Application launch class
 * 
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class appLaunch {

	public static void main(String[]args){
		// send our gui to event dispatch thread
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		GraphCreate frame = new GraphCreate();
	    	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	    frame.pack();
	    	    frame.toFront();
	    	    frame.setVisible(true);
				// Get the size of the screen
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				// Determine the new location of the window
				int w = frame.getSize().width;
				int h = frame.getSize().height;
				int x = (dim.width-w)/2;
				int y = (dim.height-h)/2;
				// Move the window
				frame.setLocation(x, y);
	    }});
	 }
}