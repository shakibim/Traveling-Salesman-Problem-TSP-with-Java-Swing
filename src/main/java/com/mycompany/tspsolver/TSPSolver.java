package com.mycompany.tspsolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TSPSolver extends JFrame {

    private JPanel drawingPanel; //JPanel for graph visualization
    private JPanel sidePanel; //JPanel for showing details 
    private JButton addVertexButton; //JButton to add a new vertex
    private JButton addEdgeButton; //JButton to add an edge between vertices
    private JButton autoConnectButton; //JButton to auto-connect vertices
    private JButton clear; //JButton to clear all vertices and edges
    private JButton solveTSP; //JButton to clear all vertices and edges
    private JButton hideorshownoedges; //JButton to solve the TSP
    private JButton showdetails; //JButton to solve the TSP 

    private ArrayList<Point> vertices; //ArrayList to store vertices
    private ArrayList<Edge> edges; //ArrayList to store edges

    public TSPSolver() {
        setTitle("Travelling Salesmen Problem Solver"); // Set JFrame title
        setSize(1000, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Specify close operation on window exit

        vertices = new ArrayList<>(); // Initialize vertices list
        edges = new ArrayList<>(); // Initialize edges list

        drawingPanel = new JPanel() {
            @Override // Override paintComponent to draw graph elements
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);

            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() { // Draw edges between vertices
            Point vertex1 = null;
            Point vertex2 = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (addVertexButton.isSelected()) { // Check if edge belongs to optimal path
                    vertices.add(e.getPoint());
                    drawingPanel.repaint();
                } else if (addEdgeButton.isSelected()) {

                    Point clickedPoint = e.getPoint();

                    if (vertex1 == null) {
                        // Check if clicked point is over any vertex
                        for (Point vertex : vertices) {
                            if (calculateDistance(clickedPoint, vertex) <= 15) { // Adjust the threshold as needed
                                vertex1 = vertex; // Set vertex1
                                drawingPanel.repaint();
                                break;
                            }
                        }
                    } else {
                        // Check if clicked point is over any vertex (excluding vertex1)
                        boolean foundVertex2 = false; // Flag to indicate if a valid vertex2 is found
                        for (Point vertex : vertices) {
                            if (calculateDistance(clickedPoint, vertex) <= 15 && !vertex.equals(vertex1)) {
                                vertex2 = vertex; // Set vertex2
                                foundVertex2 = true; // Valid vertex2 found
                                break;
                            }
                        }

                        if (foundVertex2) {
                            String weightStr = JOptionPane.showInputDialog(TSPSolver.this, // Logic for drawing optimal edges
                                    "Enter weight for the edge:");
                            int weight = 0;
                            try {
                                weight = Integer.parseInt(weightStr);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(TSPSolver.this,
                                        "Invalid weight. Please enter a valid number.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (!isDuplicateEdge(edges, vertex1, vertex2, weight)) {
                                edges.add(new Edge(vertex1, vertex2, weight));
                                String stepDetails = " Connecting vertex " + (char) ('A' + getIndexofVertices(vertex1))
                                        + " to vertex " + (char) ('A' + getIndexofVertices(vertex2))
                                        + " with weight " + weight;
                                Details.add(stepDetails); // Store the details
                            }
                            drawingPanel.repaint();
                            Distances.clear();
                            createDistancesArray();
                            createDistancesString();
                            if (isOpen) {
                                setDetailsOnSidePanel();
                            }
                        }

                        vertex1 = null; // Reset vertex1 to null
                        vertex2 = null; // Reset vertex2 to null
                    }
                }
            }
        });

        addVertexButton = new JButton("Add Vertex");
        addEdgeButton = new JButton("Add Edge");
        autoConnectButton = new JButton("Auto Connect");
        solveTSP = new JButton("Solve TSP");
        hideorshownoedges = new JButton("Hide Non-optimal Edges");
        clear = new JButton("Clear All");
        showdetails = new JButton("Show Details");
        addVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVertexButton.setSelected(true);

                addEdgeButton.setSelected(false);
            }
        });

        addEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDirected == null) {
                    String[] options = {"Directed", "Undirected"};
                    int choice = JOptionPane.showOptionDialog(
                            drawingPanel,
                            "Do you want to create Directed Graph or Undirected Graph?",
                            "Directed or Undirected",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (choice == 0) {
                        isDirected = true;
                        addVertexButton.setSelected(false);
                        addEdgeButton.setSelected(true);
                    } else if (choice == 1) {
                        isDirected = false;
                        addVertexButton.setSelected(false);
                        addEdgeButton.setSelected(true);
                    } else {
                        addVertexButton.setSelected(false);
                        addEdgeButton.setSelected(false);
                    }
                }

            }
        });

        autoConnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDirected == null) {
                    String[] options = {"Directed", "Undirected"};
                    int choice = JOptionPane.showOptionDialog(
                            drawingPanel,
                            "Do you want to create Directed Graph or Undirected Graph?",
                            "Directed or Undirected",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (choice == 0) {
                        isDirected = true;

                    } else if (choice == 1) {
                        isDirected = false;

                    }
                }

                String[] connectOptions = {"Accurate Distances", "Random Weights"};
                int connectChoice = JOptionPane.showOptionDialog(
                        drawingPanel,
                        "Do you want to connect edges with Accurate Distances or Random Weights?",
                        "Connect Edges",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        connectOptions,
                        connectOptions[0]);

                if (connectChoice == 0) {
                    autoConnectEdgesWithAccurateDistances();
                } else if (connectChoice == 1) {
                    autoConnectEdgesWithRandomWeights();
                }

                // shows the adjacency list of the graph
                Distances.clear(); // Clear all vertices and edges
                createDistancesArray();
                createDistancesString();
                if (isOpen) {
                    setDetailsOnSidePanel(); // Update side panel if open
                }
                drawingPanel.repaint();
            }
        });
        
// Actions for clear button
        clear.addActionListener(new ActionListener() {
            @Override //events perform when clear button is clicked
            public void actionPerformed(ActionEvent e) {
                vertices.clear();
                edges.clear();
                Details.clear();
                isDirected = null;
                drawingPanel.repaint();
                Distances.clear();
                backupedges.clear();
                isHidden = false;
                setDetailsOnSidePanel(); // updates the side panel

            }
        });
        
// Actions for showdetails button
        showdetails.addActionListener(new ActionListener() {
            @Override
            //events perform when show details button is clicked
            public void actionPerformed(ActionEvent e) {
                // if the panel is not opened the then open the panel and show details
                if (!isOpen) {
                    openSidePanel();
                    setDetailsOnSidePanel();
                } // if the panel the is opened then close the panel and change the text or lebel of the button
                else {
                    closeSidePanel();
                    showdetails.setText("Show Details");
                    setDetailsOnSidePanel();
                }
            }
        });

        solveTSP.addActionListener(new ActionListener() { // Actions for solveTSP button
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Greedy Approach", "Dynamic Programming", "Naive Approach"};
                int choice = JOptionPane.showOptionDialog(
                        drawingPanel,
                        "Select solving method",
                        "Select solving method",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (choice == 0) {
                    solveTSPGreedy();
                    if (isOpen) {
                        setDetailsOnSidePanel();
                    }
                    OptimalPathEdges.clear();
                    createOptimalpathEdges();
                    drawingPanel.repaint();
                } else if (choice == 1) {
                    Solve_TSP_with_DP();
                    if (isOpen) {
                        setDetailsOnSidePanel();
                    }
                    OptimalPathEdges.clear();
                    createOptimalpathEdges();
                    drawingPanel.repaint();
                } else {
                    solveTSPNaive();
                    if (isOpen) {
                        setDetailsOnSidePanel();
                    }
                    OptimalPathEdges.clear();
                    createOptimalpathEdges();
                    drawingPanel.repaint();

                }
            }
        });

        hideorshownoedges.addActionListener(new ActionListener() { // Actions for hideorshownoedges button
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!isHidden) {
                    backupedges.clear();
                    for (Edge edge : edges) {

                        backupedges.add(edge);
                    }
                    edges.clear();
                    for (Edge optimalEdge : OptimalPathEdges) {

                        edges.add(optimalEdge);
                    }
                    drawingPanel.repaint();
                    hideorshownoedges.setText("Show Non-Optimal Edges");

                } else {
                    edges.clear();
                    for (Edge edge : backupedges) {

                        edges.add(edge);
                    }
                    drawingPanel.repaint();
                    hideorshownoedges.setText("Hide Non-Optimal Edges");
                }

                isHidden = !isHidden;
            }
        });

        JPanel buttonPanel = new JPanel(); //button panel for the buttons 
        //adding each button to the button panel
        buttonPanel.add(addVertexButton);
        buttonPanel.add(addEdgeButton);
        buttonPanel.add(autoConnectButton);
        buttonPanel.add(solveTSP);
        buttonPanel.add(hideorshownoedges);
        buttonPanel.add(clear);
        buttonPanel.add(showdetails);

        add(drawingPanel, BorderLayout.CENTER);//adding drawing panel 
        add(buttonPanel, BorderLayout.SOUTH); //adding button panel  
    }

    // Initialize to false, assuming you want to show edges by default
    private double calculateDistance(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void drawGraph(Graphics g) {

        g.setColor(Color.BLUE);
        int i = 0;
        for (Point vertex : vertices) {
            g.fillOval(vertex.x - 15, vertex.y - 15, 30, 30);
            g.setColor(Color.blue);
            Font font = new Font("Arial", Font.PLAIN, 14);
            g.setFont(font);
            g.drawString(String.valueOf((char) ((i) + 'A')), vertex.x + 15, vertex.y + 15);

            if (vertices.indexOf(vertex) == vertices.size() - 1) {
                String stepDetails = " Adding vertex " + (char) ('A' + vertices.size() - 1);
                if (!Details.contains(stepDetails)) {
                    Details.add(stepDetails);

                    if (isOpen) {
                        setDetailsOnSidePanel();
                    }

                }

            }

            i++;
            g.setColor(Color.BLUE);

        }

        for (i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            // for directed
            if (isDirected) {
                if (getIndexofVertices(edge.point1) < getIndexofVertices(edge.point2)) {

                    int weightX = (edge.point1.x + edge.point2.x) / 2;
                    int weightY = (edge.point1.y + edge.point2.y) / 2;

                    int weightOffsetX = 10;
                    int weightOffsetY = 5;
                    boolean isOptimal = OptimalPathEdges.contains(edges.get(i));

                    float strokeWidth = isOptimal ? 2.0f : 1.0f;
                    ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
                    ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
                    if (isOptimal) {
                        g.setColor(Color.RED); // for optimal edges in red

                    } else {
                        g.setColor(Color.BLUE); // for non-optimal edges in blue
                    }
                    g.drawLine(edge.point1.x + 3, edge.point1.y + 3, edge.point2.x + 3, edge.point2.y + 3);

                    Font font = new Font("Arial", Font.BOLD, 16);
                    g.setFont(font);
                    g.drawString(String.valueOf(edge.weight), weightX + weightOffsetX, weightY + weightOffsetY);
                    g.setColor(Color.BLUE);
                    for (Point vertex : vertices) {
                        g.fillOval(vertex.x - 15, vertex.y - 15, 30, 30);
                    }
                    if (isOptimal) {
                        g.setColor(Color.RED); // for optimal edges in red

                    } else {
                        g.setColor(Color.BLUE); // for non-optimal edges in blue
                    }
                    drawArrowHead((Graphics2D) g, edge.point1.x + 3, edge.point1.y + 3, edge.point2.x + 3,
                            edge.point2.y + 3);

                } else {

                    int weightX = (edge.point1.x + edge.point2.x) / 2;
                    int weightY = (edge.point1.y + edge.point2.y) / 2;
                    int weightOffsetX = 10;
                    int weightOffsetY = 5;
                    boolean isOptimal = OptimalPathEdges.contains(edge);

                    int reverseWeight = edge.weight;

                    float strokeWidth = isOptimal ? 2.0f : 1.0f;
                    ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
                    if (isOptimal) {
                        g.setColor(Color.RED); // Draw optimal edges in red

                    } else {
                        g.setColor(Color.BLUE); // Draw non-optimal edges in blue
                    }

                    g.drawLine(edge.point2.x - 6, edge.point2.y - 3, edge.point1.x - 3, edge.point1.y - 3);

                    g.drawString(String.valueOf(reverseWeight), weightX - weightOffsetX, weightY - weightOffsetY);

                    g.setColor(Color.BLUE);
                    for (Point vertex : vertices) {
                        g.fillOval(vertex.x - 15, vertex.y - 15, 30, 30);
                    }
                    if (isOptimal) {
                        g.setColor(Color.RED); // for optimal edges in red

                    } else {
                        g.setColor(Color.BLUE); // for non-optimal edges in blue
                    }
                    drawArrowHead((Graphics2D) g, edge.point1.x - 3, edge.point1.y - 3, edge.point2.x - 3,
                            edge.point2.y - 3);
                    if (isHidden) {
                        for (Edge oe : OptimalPathEdges) {
                            if (getIndexofVertices(oe.point1) < getIndexofVertices(oe.point2)) {
                                drawArrowHead((Graphics2D) g, oe.point1.x + 3, oe.point1.y + 3, oe.point2.x + 3,
                                        oe.point2.y + 3);
                            } else {
                                drawArrowHead((Graphics2D) g, oe.point1.x - 3, oe.point1.y - 3, oe.point2.x - 3,
                                        oe.point2.y - 3);
                            }
                        }
                    }
                }

            } // for undirected
            else {
                boolean isOptimal = OptimalPathEdges.contains(edge);
                float strokeWidth = isOptimal ? 2.0f : 1.0f; // Adjust this value as needed
                ((Graphics2D) g).setStroke(new BasicStroke(strokeWidth));
                if (isOptimal) {
                    g.setColor(Color.RED); // Draw optimal edges in red
                } else {
                    g.setColor(Color.BLUE); // Draw non-optimal edges in blue
                }
                g.drawLine(edge.point1.x, edge.point1.y, edge.point2.x, edge.point2.y);

                // Draw the weight of the edge in the middle
                int weightX = (edge.point1.x + edge.point2.x) / 2;
                int weightY = (edge.point1.y + edge.point2.y) / 2;
                Font font = new Font("Arial", Font.BOLD, 16);
                g.setFont(font);
                g.drawString(String.valueOf(edge.weight), weightX - 5, weightY - 5);

                g.setColor(Color.BLUE);
                for (Point vertex : vertices) {
                    g.fillOval(vertex.x - 15, vertex.y - 15, 30, 30);
                }
            }

        }
    }

// method for drawing Arrowhead
    private void drawArrowHead(Graphics2D g2d, int x1, int y1, int x2, int y2) {

        int circlerad = 12; // Radius of the circles

        // Calculate the distance between the centers of the circles
        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        // Calculate the ratios for finding the intersection point
        double ratioX = (x2 - x1) / distance;
        double ratioY = (y2 - y1) / distance;

        // Calculate the coordinates of the intersection point on circle 2
        int intersectionX = (int) (x2 - ratioX * circlerad);
        int intersectionY = (int) (y2 - ratioY * circlerad);
        x2 = intersectionX;
        y2 = intersectionY;

        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 10;

        int xPoints[] = new int[3];
        int yPoints[] = new int[3];

        xPoints[0] = x2;
        yPoints[0] = y2;

        xPoints[1] = x2 - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        yPoints[1] = y2 - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

        xPoints[2] = x2 - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        yPoints[2] = y2 - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private ArrayList<String> Details = new ArrayList<>(); // Declare a list to store details

// Connect all vertices with edges with either accurate distances or random weights
    private void autoConnectEdges(boolean useAccurateDistances) {
        if (isDirected) {
            Details.add("");
            for (int i = 0; i < vertices.size(); i++) {
                for (int j = i + 1; j < vertices.size(); j++) {
                    int weight;
                    if (useAccurateDistances) {
                        weight = (int) (calculateDistance(vertices.get(i), vertices.get(j)));
                    } else {
                        weight = (int) (Math.random() * 20) + 1;
                    }

                    connectEdges(vertices.get(i), vertices.get(j), weight);
                    connectEdges(vertices.get(j), vertices.get(i), weight);
                }
            }
        } else {
            Details.add("");
            for (int i = 0; i < vertices.size(); i++) {
                for (int j = i + 1; j < vertices.size(); j++) {
                    int weight;
                    if (useAccurateDistances) {
                        weight = (int) (calculateDistance(vertices.get(i), vertices.get(j)));
                    } else {
                        weight = (int) (Math.random() * 20) + 1;
                    }

                    connectEdges(vertices.get(i), vertices.get(j), weight);
                }
            }
        }
    }

    private void connectEdges(Point point1, Point point2, int weight) {
        if (!isDuplicateEdge(edges, point1, point2, weight)) {
            String stepDetails = " Connecting vertex " + (char) ('A' + getIndexofVertices(point1))
                    + " to vertex " + (char) ('A' + getIndexofVertices(point2))
                    + " with weight " + weight;
            Details.add(stepDetails); // Store the details

            edges.add(new Edge(point1, point2, weight));
        }
    }

    private void autoConnectEdgesWithAccurateDistances() {
        autoConnectEdges(true);
    }

    private void autoConnectEdgesWithRandomWeights() {
        autoConnectEdges(false);
    }

    // function to check duplicate edges
    private boolean isDuplicateEdge(ArrayList<Edge> edges, Point point1, Point point2, Integer weight) {
        for (Edge edge : edges) {
            if (edge.getPoint1() == point1 && edge.getPoint2() == point2) {
                if (edge.getWeight() != null) {
                    return true; // Duplicate edge found
                }
            }
        }
        return false; //No duplicate edge found
    }

    private int getIndexofVertices(Point p) {
        int idx = -1;
        for (int i = 0; i < vertices.size(); i++) {
            if (p == vertices.get(i)) {
                idx = i;
            }
        }
        return idx;
    }

    private Boolean isOpen = false;
    private Boolean isDirected = null;

    private void openSidePanel() {
        isOpen = true;

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setPreferredSize(
                new Dimension((int) ((double) drawingPanel.getWidth() / 4.2), drawingPanel.getHeight()));
        sidePanel.setBackground(new Color(200, 200, 200, 200));

        JLabel titleLabel = new JLabel("Details");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        sidePanel.add(titleLabel, BorderLayout.NORTH);

        showdetails.setText("Close Details");

        int sidePanelWidth = sidePanel.getPreferredSize().width;
        int panel1Width = drawingPanel.getWidth();
        int panel1Height = drawingPanel.getHeight();

        sidePanel.setBounds(panel1Width - sidePanelWidth, 0, sidePanelWidth, panel1Height);

        // Set the layout manager of drawingPanel to BorderLayout and add sidePanel to
        // the EAST position which is the right side
        drawingPanel.setLayout(new BorderLayout());
        drawingPanel.add(sidePanel, BorderLayout.EAST);

        drawingPanel.revalidate();
        drawingPanel.repaint();
    }
    
// method to set details on the side panel
    private void setDetailsOnSidePanel() {
        JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setWrapStyleWord(true);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsTextArea.setForeground(Color.blue);
        for (String detail : Details) {
            detailsTextArea.append(detail + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(detailsTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setValue(0); // Scroll to the top

        sidePanel.removeAll();
        sidePanel.add(Box.createVerticalStrut(10)); // Add space above the label
        sidePanel.add(new JLabel("Details"), BorderLayout.NORTH);
        sidePanel.add(scrollPane);
        sidePanel.revalidate();
        sidePanel.repaint();
    }
    
// method to close the side panel
    private void closeSidePanel() {
        isOpen = false;
        drawingPanel.remove(sidePanel);
        showdetails.setText("Show Details");
        drawingPanel.revalidate();
        drawingPanel.repaint();
    }
    
   // 2D ArrayList to store the distances between vertices
    ArrayList<ArrayList<Integer>> Distances = new ArrayList<>();
    
// Method to create a 2D array of distances between vertices
    private void createDistancesArray() {
        for (int i = 0; i < vertices.size(); i++) {
            ArrayList<Integer> distanceRow = new ArrayList<>(); // Create a new row for distances
            for (int j = 0; j < vertices.size(); j++) {
                if (i == j) {
                    distanceRow.add(Integer.MAX_VALUE);
                } else {
                    int weight = findWeight(edges, vertices.get(i), vertices.get(j));
                    distanceRow.add(weight);
                }
            }
            Distances.add(distanceRow);
        }
    }
    
// Method to find the weight of an edge between two vertices p1 and p2
    private int findWeight(ArrayList<Edge> edges, Point p1, Point p2) {
        // Loop through all edges to find a matching edge between p1 and p2
        for (Edge e : edges) {
            if ((e.point1 == p1 && e.point2 == p2) || (!isDirected && e.point1 == p2 && e.point2 == p1)) {
                return e.getWeight();
            }
        }
        return Integer.MAX_VALUE; // Edge not found
    }
    
    // List to store the vertices in the optimal path
    ArrayList<Integer> PathVertex = new ArrayList<>();

    // Method to create the optimal path by finding edges between vertices in the optimal path
    private void createOptimalpathEdges() {
        for (int i = 0; i < PathVertex.size() - 1; i++) {
            Point vertex1 = vertices.get(PathVertex.get(i));
            Point vertex2 = vertices.get(PathVertex.get(i + 1)); // Adjust for wrapping

            if (!isDirected) {
                for (Edge edge : edges) {
                    if ((edge.getPoint1().equals(vertex1) && edge.getPoint2().equals(vertex2))
                            || (edge.getPoint1().equals(vertex2) && edge.getPoint2().equals(vertex1))) {
                        OptimalPathEdges.add(edge);
                    }
                }
            } else {
                for (Edge edge : edges) {
                    if (edge.getPoint1().equals(vertex1) && edge.getPoint2().equals(vertex2)) {
                        OptimalPathEdges.add(edge);
                    }
                }
            }
        }
    }

    ArrayList<Edge> OptimalPathEdges = new ArrayList<>();
    ArrayList<Edge> backupedges = new ArrayList<>();
    
    // method to solve TSP with Dynamic Programming
    private void Solve_TSP_with_DP() {
        long startTime = System.nanoTime();
        int n = vertices.size();
        int[][] dp = new int[1 << n][n];
        int[][] prevCity = new int[1 << n][n]; // Store the previous city for path reconstruction

        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE / 2);
        }

        dp[1][0] = 0;

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }

                for (int j = 0; j < n; j++) {
                    if ((mask & (1 << j)) != 0 && i != j) {
                        int newCost = dp[mask ^ (1 << i)][j] + Distances.get(j).get(i);
                        if (newCost < dp[mask][i]) {
                            dp[mask][i] = newCost;
                            prevCity[mask][i] = j; // Store the previous city for path reconstruction
                        }
                    }
                }
            }
        }

        int fullMask = (1 << n) - 1;
        int minCost = Integer.MAX_VALUE;
        int lastCity = -1;

        for (int i = 1; i < n; i++) {
            int cost = dp[fullMask][i] + Distances.get(i).get(0);
            if (cost < minCost) {
                minCost = cost;
                lastCity = i;
            }
        }

        ArrayList<Integer> path = new ArrayList<>();

        int mask = fullMask;
        int currentCity = lastCity;

        while (currentCity != 0) {
            path.add(currentCity);
            int prev = prevCity[mask][currentCity];
            mask ^= (1 << currentCity); // Remove currentCity from mask
            currentCity = prev;
        }

        Collections.reverse(path);
        path.add(0, 0);
        path.add(path.size(), 0);
        PathVertex.clear();
        PathVertex = path;
        long endTime = System.nanoTime(); // Capture end time
        long durationNano = endTime - startTime; // Calculate time difference in nanoseconds
        double durationMillis = durationNano / 1e6;

        Details.add((""));
        Details.add(" Optimal path found (Dynamic Programming): ");
        Details.add(CreateOptimalPathString(path));
        String stepDetails = " Minimum cost found (Dynamic Programming):  " + minCost;
        Details.add(stepDetails);
        String timeDetails = " Time taken (Dynamic Programming): " + durationMillis + " ms";
        Details.add(timeDetails);
    }

    // Convert Distances ArrayList to adjaccency list showing in Details panel
    private void createDistancesString() {
        Details.add("");
        Details.add(" Adjaccency List of the graph is : ");
        for (int i = 0; i < Distances.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < Distances.get(i).size(); j++) {
                if (Distances.get(i).get(j) == Integer.MAX_VALUE) {
                    sb.append("INF"); // Replace Integer.MAX_VALUE with "∞"
                } else {
                    sb.append(Distances.get(i).get(j));
                }
                if (j < Distances.get(i).size() - 1) {
                    sb.append(" , ");
                }
            }

            if (i == Distances.size() - 1) {
                Details.add("  ( " + sb.toString() + " )}");
            } else if (i == 0) {
                Details.add(" {( " + sb.toString() + " ) ");
            } else {
                Details.add("  ( " + sb.toString() + " ) ");
            }
        }
    }
    
//method to solve TSP with Greedy approach or nearest neighbour
    private void solveTSPGreedy() {
        long startTime = System.nanoTime();
        int n = vertices.size();
        ArrayList<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[n];

        int currentCity = 0; // Starting from city 0
        path.add(currentCity);
        visited[currentCity] = true;

        int totalCost = 0;

        for (int i = 0; i < n - 1; i++) {
            int nearestCity = -1;
            int minDistance = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                if (!visited[j] && Distances.get(currentCity).get(j) < minDistance
                        && Distances.get(currentCity).get(j) != Integer.MAX_VALUE) {
                    nearestCity = j;
                    minDistance = Distances.get(currentCity).get(j);
                }
            }

            if (nearestCity != -1) {
                path.add(nearestCity);
                visited[nearestCity] = true;
                totalCost += minDistance;
                currentCity = nearestCity;
            }
        }

        //Returning to the starting city
        path.add(0);
        PathVertex.clear();
        PathVertex = path;
        totalCost += Distances.get(currentCity).get(0);
        long endTime = System.nanoTime(); // Capture end time
        long durationNano = endTime - startTime; // Calculate time difference in nanoseconds
        double durationMillis = durationNano / 1e6;
        Details.add("");
        String pathDetails = " Optimal path (Greedy Approach): ";
        Details.add(pathDetails);
        Details.add(CreateOptimalPathString(path));
        String costDetails = " Minimum cost (Greedy Approach): " + totalCost;
        Details.add(costDetails);
        String timeDetails = " Time taken (Greedy Approach): " + durationMillis + " ms";
        Details.add(timeDetails);
        Details.add("");
    }

    private void generatePermutations(ArrayList<Integer> cities, int startIndex,
            ArrayList<ArrayList<Integer>> permutations) {
        if (startIndex == cities.size() - 1) {
            permutations.add(new ArrayList<>(cities));
            return;
        }

        for (int i = startIndex; i < cities.size(); i++) {
            swap(cities, startIndex, i);
            generatePermutations(cities, startIndex + 1, permutations);
            swap(cities, startIndex, i); // Backtrack
        }
    }

    private void swap(ArrayList<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    private int calculateTotalCost(ArrayList<Integer> permutation, ArrayList<ArrayList<Integer>> distances) {
        int cost = 0;
        for (int i = 0; i < permutation.size(); i++) {
            int from = permutation.get(i);
            int to = permutation.get((i + 1) % permutation.size()); // Wrap around to the starting city
            cost += distances.get(from).get(to);
        }
        return cost;
    }

// method to solve TSP in Naive apparoch or Bruteforce Apparoch
    private void solveTSPNaive() {
        long startTime = System.nanoTime();
        int n = vertices.size();

        ArrayList<Integer> cities = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cities.add(i);
        }

        ArrayList<Integer> optimalPath = null;
        int minCost = Integer.MAX_VALUE;

        ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();
        generatePermutations(cities, 0, permutations);

        for (ArrayList<Integer> permutation : permutations) {
            int cost = calculateTotalCost(permutation, Distances);
            if (cost < minCost) {
                minCost = cost;
                optimalPath = permutation;
            }
        }
        optimalPath.add(0);
        PathVertex.clear();
        PathVertex = optimalPath;
        long endTime = System.nanoTime(); // Capture end time
        long durationNano = endTime - startTime; // Calculate time difference in nanoseconds
        double durationMillis = durationNano / 1e6; // Convert to milliseconds
        Details.add("");
        String pathDetails = " Optimal path (Naive Approach): ";
        Details.add(pathDetails);
        Details.add(CreateOptimalPathString(optimalPath));
        String costDetails = " Minimum cost (Naive Approach): " + minCost;
        Details.add(costDetails);
        String timeDetails = " Time taken (Naive Approach): " + durationMillis + " ms";
        Details.add(timeDetails);
        Details.add("");
    }

// method for creating optimal path which is shown in the detail panel (eg: A>C>D)
    private static String CreateOptimalPathString(ArrayList<Integer> path) {
        StringBuilder formattedPath = new StringBuilder("");

        for (int i = 0; i < path.size(); i++) {
            int cityIndex = path.get(i);
            String cityName = getCityName(cityIndex);

            formattedPath.append(" ").append(cityName);

            if (i < path.size() - 1) {
                formattedPath.append(" >");
            }
        }

        return formattedPath.toString();
    }

    private static String getCityName(int index) {
        char prefix = (char) ('A' + index);
        return prefix + "";
    }

    private boolean isHidden = false;

    // method for hiding or showing non-optimal edges  
    private void hideOrShowNonOptimalPath() {
        if (!isHidden) {
            ArrayList<Edge> backupEdges = new ArrayList<>();
            for (Edge e : edges) {
                Point point1 = e.getPoint1();
                Point point2 = e.getPoint2();
                Integer weight = e.getWeight();
                backupEdges.add(new Edge(new Point(point1.x, point1.y), new Point(point2.x, point2.y), weight));
            }
            edges.clear();
            drawingPanel.repaint();
            for (Edge optimalEdge : OptimalPathEdges) {
                Point point1 = optimalEdge.getPoint1();
                Point point2 = optimalEdge.getPoint2();
                Integer weight = optimalEdge.getWeight();
                edges.add(new Edge(new Point(point1.x, point1.y), new Point(point2.x, point2.y), weight));
            }

            edges.addAll(backupEdges);
        } else {
            drawingPanel.repaint();
        }
    }

// The Edge class
    private class Edge {

        Point point1;
        Point point2;
        Integer weight;

        Edge(Point point1, Point point2, int weight) {
            this.point1 = point1;
            this.point2 = point2;
            this.weight = weight;
        }

        public Point getPoint1() {
            return point1;
        }

        public void setPoint1(Point point1) {
            this.point1 = point1;
        }

        public Point getPoint2() {
            return point2;
        }

        public void setPoint2(Point point2) {
            this.point2 = point2;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public boolean isWeightnull() {
            if (weight == null) {
                return true;
            } else {
                return false;
            }

        }
    }

// main method to run the code
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TSPSolver().setVisible(true);
            }
        });
    }
}
