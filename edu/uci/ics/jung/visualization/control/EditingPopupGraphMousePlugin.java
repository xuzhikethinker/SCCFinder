/*
 * Copyright (c) 2005, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.visualization.control;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graphviewer.GraphCreate;

/**
 * a plugin that uses popup menus to create vertices, undirected edges,
 * and directed edges.
 * 
 * @author Tom Nelson
 *
 */
public class EditingPopupGraphMousePlugin<V,E> extends AbstractPopupGraphMousePlugin {
    
    protected Factory<V> vertexFactory;
    protected Factory<E> edgeFactory;
    protected JPopupMenu popup;

    public EditingPopupGraphMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
    }
    
	@SuppressWarnings({ "unchecked", "serial", "serial" })
	protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<V,E> vv =
            (VisualizationViewer<V,E>)e.getSource();
        final Layout<V,E> layout = vv.getGraphLayout();
        final Graph<V,E> graph = layout.getGraph();
        final Point2D p = e.getPoint();
        final Point2D ivp = p;
        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
        if(pickSupport != null) {
            
            final V vertex = pickSupport.getVertex(layout, ivp.getX(), ivp.getY());
            final E edge = pickSupport.getEdge(layout, ivp.getX(), ivp.getY());
            final PickedState<V> pickedVertexState = vv.getPickedVertexState();
            final PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
            
            if(vertex != null) {
            	/** change here - Voulimeneas Alexios */
            	/*Set<V> picked = pickedVertexState.getPicked();
            	if(picked.size() > 0) {
            		if(graph instanceof UndirectedGraph == false) {
            			JMenu directedMenu = new JMenu("Create Directed Edge");
            			popup.add(directedMenu);
            			for(final V other : picked) {
            				directedMenu.add(new AbstractAction("["+other+","+vertex+"]") {
            					public void actionPerformed(ActionEvent e) {
            						graph.addEdge(edgeFactory.create(),
            								other, vertex, EdgeType.DIRECTED);
            						vv.repaint();
            					}
            				});
            			}
            		}
            		if(graph instanceof DirectedGraph == false) {
            			JMenu undirectedMenu = new JMenu("Create Undirected Edge");
            			popup.add(undirectedMenu);
            			for(final V other : picked) {
            				undirectedMenu.add(new AbstractAction("[" + other+","+vertex+"]") {
            					public void actionPerformed(ActionEvent e) {
            						graph.addEdge(edgeFactory.create(),
            								other, vertex);
            						vv.repaint();
            					}
            				});
            			}
            		}
                }*/
            	popup = new JPopupMenu();
                popup.add(new AbstractAction("Delete Vertex") {
                    public void actionPerformed(ActionEvent e) {
                        pickedVertexState.pick(vertex, false);
                        /** change here - Voulimeneas Alexios */
                        String name = ((Object) vertex).toString();
                        //System.out.println(name+"@");
                        String max = new Integer(GraphCreate.getNumberOfVertices() - 1).toString();
                        //System.out.println(max+"@");
                        if(name.equals(max)) GraphCreate.setNumberOfVertices(GraphCreate.getNumberOfVertices() - 1);
                        graph.removeVertex(vertex);
                        if(graph.getVertexCount() == 0) {GraphCreate.setNumberOfVertices(0); GraphCreate.helpDisable();}
                        vv.repaint();
                    }});
                if(popup.getComponentCount() > 0) {
                    popup.show(vv, e.getX(), e.getY());
                }
            } else if(edge != null) {
            	popup = new JPopupMenu();
                popup.add(new AbstractAction("Delete Edge") {
                    public void actionPerformed(ActionEvent e) {
                        pickedEdgeState.pick(edge, false);
                        graph.removeEdge(edge);
                        vv.repaint();
                    }});
                if(popup.getComponentCount() > 0) {
                    popup.show(vv, e.getX(), e.getY());
                }
                
            } 
            /** change here - Voulimeneas Alexios */
            /*else {
            	popup = new JPopupMenu();
                popup.add(new AbstractAction("Create Vertex") {
                    public void actionPerformed(ActionEvent e) {
                        V newVertex = vertexFactory.create();
                        graph.addVertex(newVertex);
                        layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p));
                        vv.repaint();
                    }
                });
            }*/
           
        }
    }
}

