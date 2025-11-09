# Traveling Salesman Problem (TSP) with Java Swing

A Traveling Salesman Problem (TSP) Solver Application implemented in Java using Swing.  
It allows users to interactively add vertices and edges, auto connect edges, visualize the graph, and solve the TSP using three different approaches: Greedy, Naive, and Dynamic Programming.

---

## Abstract

The Traveling Salesman Problem (TSP) is an NP-hard optimization problem that aims to find the shortest possible route for a salesman to visit all cities and return to the starting point.  
This application:

- Implements Naive, Greedy, and Dynamic Programming algorithms in Java.  
- Provides a simple Swing GUI to visualize routes and compare algorithm performance.  
- Supports both manual and automatic graph generation.  
- Allows real-time visualization of optimal and suboptimal solutions.

---

## Objectives

- Create an interactive Java Swing interface.  
- Allow users to manually add vertices, edges, and weights.  
- Provide an auto-connect feature for quick graph generation.  
- Implement multiple algorithms for solving TSP.  
- Visualize and compare solution routes and performance.
---

## Algorithms Implemented

### Naive (Brute Force) Approach
- Tests all possible routes.  
- Guarantees the optimal solution but has factorial time complexity (O(n!)).  
- Works only for small instances.

### Dynamic Programming Approach
- Uses memoization to store subproblem results.  
- Avoids redundant calculations.  
- Time complexity: O(n² × 2ⁿ).  
- Suitable for small to medium instances.

### Greedy Approach
- Chooses the nearest unvisited city at each step.  
- Produces near-optimal results very quickly.  
- Time complexity: O(n²).  
- Best for large instances where speed matters.

---

## Application Features

| Feature | Description |
|----------|-------------|
| Add Vertex | Create vertices with a mouse click. |
| Add Edge | Connect vertices manually and enter weights. |
| Auto Connect | Automatically generate a complete graph with random weights. |
| Solve TSP | Choose among Naive, Greedy, or DP algorithms. |
| Hide Non-Optimal Edges | Toggle visibility of non-optimal edges. |
| Show Details | View route and distance details. |
| Clear All | Reset the workspace. |

---

## Performance Comparison
Performance comparison of the below graph with different approaches of the below graph:
![Image](https://github.com/user-attachments/assets/62f86cf5-7fdc-494f-80e3-0367ba82bb36)

| Algorithm | Time (ms) | Solution Cost | Remarks |
|------------|------------|----------------|----------|
| Naive | 806.95 | Optimal | Very slow |
| Dynamic Programming | 4.40 | Optimal | Efficient for medium instances |
| Greedy | 0.0243 | Suboptimal | Fastest for large instances |

---

## Example Visualizations

### Small Instance
- Naive and DP: Optimal cost = 55  
- Greedy: Suboptimal cost = 85

### Medium Instance
- DP: Cost = 41, Time = 7626.24 ms  
- Greedy: Cost = 68, Time = 0.0519 ms

### Large Instance
- Only Greedy works efficiently; others freeze due to computation limits.

---

## Limitations

- The app may freeze for large graphs when using Naive or DP algorithms.  
- In directed graphs, arrows can overlap when nodes are close together.

---

## Conclusion

This project demonstrates how different algorithms solve the TSP problem and how their performance varies with graph size.  
Dynamic Programming provides accurate results for small to medium datasets, while Greedy performs best for larger ones.

---

## Screenshots

Interface:  
![Image](https://github.com/user-attachments/assets/acf3d171-4883-4f77-8866-ba8df499dae9)

Solving with Naive and Dynamic Programming (solution = 55):  
![Image](https://github.com/user-attachments/assets/beeeefcc-8d3d-4dfc-be19-240fa335b16e)

Solving with Greedy Approach (sub-optimal solution = 85):  
![Image](https://github.com/user-attachments/assets/df3f16cd-9587-4cea-bfaa-f6fee988fdf6)

Undirected Graph Generated Using Auto Connect Button:  
![Image](https://github.com/user-attachments/assets/31582256-3032-42fb-a373-29b7615cb9b8)

Greedy Approach:  
![Image](https://github.com/user-attachments/assets/31d064eb-2547-42e8-9d61-2cdf1c9b72c7)

Hiding Non-Optimal Edges:  
![Image](https://github.com/user-attachments/assets/5d36c7f1-0ecc-4105-bffe-b6a325831db3)

Directed Graph Created Using Auto Connect Button:  
![Image](https://github.com/user-attachments/assets/3b1cbf13-6414-4a71-8d61-70ea18db2604)

Solve with Greedy:  
![Image](https://github.com/user-attachments/assets/9aded52a-06a0-4965-a23d-4d199440298a)

Non-Optimal Edges are Hidden:  
![Image](https://github.com/user-attachments/assets/827b5c3a-0058-453d-9799-24d9462b0d6e)

Large Directed Graph:  
![Image](https://github.com/user-attachments/assets/b4a6af06-1710-48b0-a290-f6b39582c685)

Solution with Greedy Approach:  
![Image](https://github.com/user-attachments/assets/c7137be2-4f8a-475f-9c06-bffed3aaabe4)
