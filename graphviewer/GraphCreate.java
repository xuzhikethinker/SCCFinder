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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import mathgraph.GraphList;
import mathgraph.GraphNode;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import java.awt.Paint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import org.apache.commons.collections15.Transformer;

/**
 * Shows how  to create a graph editor with JUNG2.
 * Mouse modes and actions are explained in the help text.
 * 
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class GraphCreate extends JFrame {
   
	private static final long serialVersionUID = -2773758770956176724L;
	static private int numberOfVertices = 0;
    static private int numberOfEdges = 0;
    // our graph
    private Graph<Integer, Integer> graph;
    // a mathematical model of the graph
    private GraphList graphList;
    // our layout
    private AbstractLayout<Integer, Integer> layout;
    // the visual component and renderer for the graph
    private VisualizationViewer<Integer, Integer> vv;
    // a component for mouse effects
    private EditingModalGraphMouse<Integer, Integer> graphMouse;

    private JPanel controls;
    // some buttons
    private JButton plus;
    private JButton minus;
    private JRadioButton edit;
    private JRadioButton transform;
    private JRadioButton pick;
    private static JButton scc;
    private static JButton clear;
    private JButton help;
    private boolean sccButtonPressed = false;
    // instructions for the user
    private String instructions =
        "<html>"+
        "<h3>All Modes:</h3>"+
        "<ul>"+
        "<li>Right-click on a Vertex for <b>Delete Vertex</b> popup"+
        "<li>Right-click on an Edge for <b>Delete Edge</b> popup"+
        "<li>Mousewheel scales with a crossover value of 1.0.<p>"+
        "     - zoom in the graph <p>"+
        "     - zoom out the graph "+
        "</ul>"+
        "<h3>Editing Mode:</h3>"+
        "<ul>"+
        "<li>Left-click an empty area to create a new Vertex"+
        "<li>Left-click on a Vertex and drag to another Vertex to create a Directed Edge"+
        "</ul>"+
        "<h3>Picking Mode:</h3>"+
        "<ul>"+
        "<li>Left-click+drag on a Vertex moves this Vertex"+
        "<li>Left-click+CTRL on a Vertex selects the vertex and centers the display on it"+
        "</ul>"+
        "<h3>Transforming Mode:</h3>"+
        "<ul>"+
        "<li>Left-click+drag pans the graph"+
        "<li>Left-click+Shift+drag rotates the graph"+
        "<li>Left-click+CTRL+drag shears the graph"+
        "</ul>"+
        "<h3>Notes:</h3>" +
        "<ul>" +
        "<li>You can set each mode by pressing 'e','p','t' or 'E', 'P', 'T'"+
        "<li>Warning: You can't select editing mode or delete edges and vertices, after pressing the SCC button"+
        "</ul>"+
        "</html>";

    /**
     * The form of our vertices
     */
    static class VertexFactory implements Factory<Integer> { 
        public Integer create() {
        	if(!GraphCreate.clear.isEnabled()) {
        		GraphCreate.clear.setEnabled(true);
    			GraphCreate.scc.setEnabled(true);
    		}
			return GraphCreate.numberOfVertices++;
		}
    }
    
    /**
     * The form of our edges
     */
    static class EdgeFactory implements Factory<Integer> {
    	public Integer create() {
			return GraphCreate.numberOfEdges++;
		}
    }
    
    public static void setNumberOfVertices(int value) {
    	GraphCreate.numberOfVertices = value;
    }
    
    public static int getNumberOfVertices() {
    	return GraphCreate.numberOfVertices;
    }
    
    /**
     * Keylistener for JRadiobuttons
    */
    public class MyKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            char i = ke.getKeyChar();
            if(i == 'p' || i == 'P') {
            	pick.setSelected(true); 
            	graphMouse.setMode(Mode.PICKING); 
            	vv.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            else if(i == 't' || i == 'T') {
            	transform.setSelected(true); 
            	graphMouse.setMode(Mode.TRANSFORMING); 
            	vv.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            else if((i == 'e' || i == 'E') && !sccButtonPressed) {
            	edit.setSelected(true); 
            	graphMouse.setMode(Mode.EDITING); 
            	vv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        }
    }
    
    /**
     * Creates a mathematical model of our graph
     * and updates and repaints the visual component and renderer
     * for the graph
     */
    public void createMathgraph() {
    	ArrayList<Integer> vertices = new ArrayList<Integer>(this.graph.getVertices());
        this.graphList =new GraphList(new ArrayList<GraphNode>());
        for(int a: vertices){
        	GraphNode current = new GraphNode(a);
            this.graphList.getAdjacencylist().add(current);
        }
        ArrayList<Integer> edges = new ArrayList<Integer>(graph.getEdges());
        for(int a: edges) {
            GraphNode tmp = this.graphList.getNodeWithGivenName(graph.getEndpoints(a).getFirst());
            tmp.getConnections().add(graph.getEndpoints(a).getSecond());
        }
        this.graphList.StronglyConnectedComponents();
        MyTransformer vertexPaint = new  MyTransformer(this.graphList.getAdjacencylist(), this.graphList.getSCCNumber());
        this.vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        this.vv.repaint();
    }
    
    @Override 
    public void setFocusable(boolean isFocusable) {
    	minus.setFocusable(isFocusable);
        plus.setFocusable(isFocusable);
        edit.setFocusable(isFocusable);
        transform.setFocusable(isFocusable);
        pick.setFocusable(isFocusable);
        scc.setFocusable(isFocusable);
        clear.setFocusable(isFocusable);
        help.setFocusable(isFocusable);
    }
    
    /**
     * Create an instance of a directed graph
     */
    public GraphCreate() {
    	super("SCCFinder");
    	this.graph = new DirectedSparseMultigraph<Integer, Integer>();
        this.layout = new StaticLayout<Integer, Integer>(this.graph,new Dimension(600,600));
        final Transformer<Integer, Paint> redVertexPaint = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                 return Color.RED;
        }};
           
        this.vv =  new VisualizationViewer<Integer, Integer>(this.layout);
        this.vv.setBackground(Color.white);
        this.vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Integer>());
        this.vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        Container content = getContentPane();
        content.add(this.vv);
        this.vv.getRenderContext().setVertexFillPaintTransformer(redVertexPaint);
        
        // add listeners to our visual component
        Factory<Integer> vertexFactory = new VertexFactory();
        Factory<Integer> edgeFactory = new EdgeFactory();
        this.graphMouse = new EditingModalGraphMouse<Integer, Integer>(this.vv.getRenderContext(), vertexFactory, edgeFactory);
        this.vv.setGraphMouse(this.graphMouse);
        this.vv.addKeyListener(new MyKeyListener());
        final MouseListener[] mls = (MouseListener[])(this.vv.getListeners(MouseListener.class));
        final MouseListener wrapper = new MyWrapperMouseListener(mls[1]);
        
        this.graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
        this.vv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        this.controls = new JPanel();
        final ScalingControl scaler = new CrossoverScalingControl();
        // add some buttons
        this.plus = new JButton("+");
        this.plus.setToolTipText("Press this button or use the mouse wheel to zoom out");
        this.plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
                setFocusable(false);
        }});
        this.minus = new JButton("-");
        this.minus.setToolTipText("Press this button or use the mouse wheel to zoom in");
        this.minus.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
                setFocusable(false);
        }});
        this.edit=new JRadioButton("Editing");
        this.edit.setToolTipText("You can just press 'e' to select this radiobutton");
        this.edit.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e){
            	graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
                vv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                setFocusable(false);
        }});
        this.transform=new JRadioButton("Transforming");
        this.transform.setToolTipText("You can just press 't' to select this radiobutton");
        this.transform.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e){
                graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                vv.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setFocusable(false);
        }});
        this.pick = new JRadioButton("Picking");
        this.pick.setToolTipText("You can just press 'p' to select this radiobutton");
        this.pick.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
                graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
                vv.setCursor(new Cursor(Cursor.HAND_CURSOR));
                setFocusable(false);
        }});
        GraphCreate.scc = new JButton("SCC");
        GraphCreate.scc.setToolTipText("Strongly Connected Components algorithm");
        GraphCreate.scc.setEnabled(false);
        GraphCreate.scc.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		createMathgraph();
        		GraphCreate.scc.setEnabled(false);
        		edit.setEnabled(false);
        		pick.setSelected(true);
        		setFocusable(false);
        		sccButtonPressed = true;
        		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        		vv.setCursor(new Cursor(Cursor.HAND_CURSOR));
        		vv.removeMouseListener(mls[1]);
        		vv.addMouseListener(wrapper);
            }
        });
        GraphCreate.clear = new JButton("Clear");
        GraphCreate.clear.setToolTipText("Clears our graph");
        GraphCreate.clear.setEnabled(false);
        GraphCreate.clear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for (int k = 0; k <= numberOfVertices; k++) {
        			graph.removeVertex(k);
                }
        		
        		GraphCreate.scc.setEnabled(false);
        		GraphCreate.clear.setEnabled(false);
                edit.setEnabled(true);
                edit.setSelected(true);
                setFocusable(false);
                graphMouse.setMode(ModalGraphMouse.Mode.EDITING);
                vv.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                vv.getRenderContext().setVertexFillPaintTransformer(redVertexPaint);
                
                if(sccButtonPressed) { 
                	vv.removeMouseListener(wrapper);
                	vv.addMouseListener(mls[1]);
                }
                
                vv.repaint();
                // calling garbage collector here
                Runtime r = Runtime.getRuntime();
                r.gc();
                GraphCreate.numberOfEdges = 0; 
                GraphCreate.numberOfVertices = 0;
                sccButtonPressed = false;
        }});

        ButtonGroup actions = new ButtonGroup();
        actions.add(this.edit);
        actions.add(this.transform);
        actions.add(this.pick);
        this.edit.setSelected(true);
        this.controls.add(this.plus);
        this.controls.add(this.minus);
        this.controls.add(this.edit);
        this.controls.add(this.transform);
        this.controls.add(this.pick);
        this.controls.add(GraphCreate.scc);
        controls.add(GraphCreate.clear);
        this.help = new JButton("Help");
        this.help.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vv, instructions);
                setFocusable(false);
            }
        });
        this.controls.add(this.help);
        content.add(this.controls, BorderLayout.SOUTH);
    }
        
    public static void helpDisable() {
    	GraphCreate.scc.setEnabled(false);
    	GraphCreate.clear.setEnabled(false);
    }
}