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

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Random;

import mathgraph.GraphNode;

import org.apache.commons.collections15.Transformer;


/**
 * A transformer for our vertices (JUNG2 related staff)
 *
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class MyTransformer implements Transformer<Integer, Paint> {
	
	private ArrayList<GraphNode> list;
	private ArrayList<Color> colors = new ArrayList<Color>();
	
	public MyTransformer(ArrayList<GraphNode> list, int numberOfScc) {
		this.list = list;
		
		/** Color Palette, Excel   (#chart) */
		
		this.colors.add(new Color(0, 0, 0));
		this.colors.add(new Color(255,	255, 255));
		this.colors.add(new Color(255, 0, 0));
		this.colors.add(new Color(0, 255, 0));
		this.colors.add(new Color(0, 0, 255));
		this.colors.add(new Color(255, 255, 0));
		this.colors.add(new Color(255, 0, 255));
		this.colors.add(new Color(0, 255, 255));
		this.colors.add(new Color(128, 0, 0));
		this.colors.add(new Color(0, 128, 0));
		
		this.colors.add(new Color(0, 0, 128));
		this.colors.add(new Color(128, 128, 0));
		this.colors.add(new Color(128, 0, 128));
		this.colors.add(new Color(0, 128, 128));
		this.colors.add(new Color(192, 192, 192));
		this.colors.add(new Color(128, 128, 128));
		this.colors.add(new Color(153, 153, 255));
		this.colors.add(new Color(153, 51, 102));
		this.colors.add(new Color(255, 255, 204));
		this.colors.add(new Color(204, 255, 255));
		
		this.colors.add(new Color(102, 0, 102));
		this.colors.add(new Color(255, 128, 128));
		this.colors.add(new Color(0, 102, 204));
		this.colors.add(new Color(204, 204, 255));
		this.colors.add(new Color(0, 0, 128));
		this.colors.add(new Color(255, 0, 255));
		this.colors.add(new Color(255, 255, 0));
		this.colors.add(new Color(0, 255, 255));
		this.colors.add(new Color(128, 0, 128));
		this.colors.add(new Color(128, 0, 0));
		
		this.colors.add(new Color(0, 128, 128));
		this.colors.add(new Color(0, 0, 255));
		this.colors.add(new Color(0, 204, 255));
		this.colors.add(new Color(204, 255, 255));
		this.colors.add(new Color(204, 255, 204));
		this.colors.add(new Color(255, 255, 153));
		this.colors.add(new Color(153, 204, 255));
		this.colors.add(new Color(255, 153, 204));
		this.colors.add(new Color(204, 153, 255));
		this.colors.add(new Color(255, 204, 153));
		
		this.colors.add(new Color(51, 102, 255));
		this.colors.add(new Color(51, 204, 204));
		this.colors.add(new Color(153, 204, 0));
		this.colors.add(new Color(255, 204, 0));
		this.colors.add(new Color(255, 153, 0));
		this.colors.add(new Color(255, 102, 0));
		this.colors.add(new Color(102, 102, 153));
		this.colors.add(new Color(150, 150, 150));
		this.colors.add(new Color(0, 51, 102));
		this.colors.add(new Color(51, 153, 102));
		
		this.colors.add(new Color(0, 51, 0));
		this.colors.add(new Color(51, 51, 0));
		this.colors.add(new Color(153, 51, 0));
		this.colors.add(new Color(153,	51, 102));
		this.colors.add(new Color(51, 51, 153));
		this.colors.add(new Color(51, 51, 51));
		
		/** if number of scc is bigger than 56 choose a random color */
		if(numberOfScc > 56) {
			Random a = new Random();
			for(int i = 56; i < numberOfScc; i++) {
				this.colors.add(new Color(a.nextInt(255), a.nextInt(255), a.nextInt(255)));
			}
		}
	}
	
	public Paint transform(Integer arg0) {
		int index = 0;
		for(GraphNode tmp : this.list) {
			if(tmp.getName() == arg0) {
				index = tmp.getSCC();
				break;
			}
		}
		return this.colors.get(index-1);
	}
}