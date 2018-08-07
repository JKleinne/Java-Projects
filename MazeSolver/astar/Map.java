/*
 * Possible optimizations:
 * - calculate f as soon as g or h are set, so it will not have to be
 *      calculated each time it is retrieved
 * - store nodes in openList sorted by their f value.
 */

package game.astar;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a simple map to be used for A* pathfinding.
 */
public class Map
{

	/**
	 * The width of the map, in columns.
	 */
	private int width;

	/**
	 * The height of the map, in rows.
	 */
	private int height;

	/**
	 * Array full of nodes to be used for the pathfinding.
	 */
	private Node[][] nodes;

	/**
	 * Creates a map based on a two dimensional array, where each zero is a
	 * walkable node and any other number is not.
	 * 
	 * @param map
	 *            The map array used to creating the map.
	 */
	public Map(int[][] map)
	{
		this.width = map[0].length;
		this.height = map.length;
		nodes = new Node[width][height];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				nodes[x][y] = new Node(x, y, map[y][x] == 0);
			}
		}
	}

	/**
	 * Draws the map, where each walkable node is drawn white, each non-walkable
	 * node drawn black and each node that is in the path in yellow.
	 * 
	 * @param g
	 *            A <code>Graphics</code> object in order to be able to draw
	 *            things.
	 * @param path
	 *            Optional parameter. List containing the nodes to be drawn as
	 *            path nodes.
	 */
	public void drawMap(Graphics g, List<Node> path)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (!nodes[x][y].isWalkable())
				{
					g.setColor(Color.WHITE);
				}
				else if (path != null && path.contains(new Node(x, y, true)))
				{
					g.setColor(Color.YELLOW);
				}
				else
				{
					g.setColor(Color.BLACK);
				}
				g.fillRect(x * 32, y * 32, 32, 32);
			}
		}
	}

	/**
	 * Prints the map to the standard out, where each walkable node is simply
	 * not printed, each non-walkable node is printed as a '#' (pound sign) and
	 * each node that is in the path as a '@' (at sign).
	 * 
	 * @param path
	 *            Optional parameter. List containing the nodes to be drawn as
	 *            path nodes.
	 */
	public void printMap(List<Node> path)
	{
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				if (!nodes[i][j].isWalkable())
				{
					System.out.print(" #");
				}
				else if (path.contains(new Node(i, j, true)))
				{
					System.out.print(" @");
				}
				else
				{
					System.out.print("  ");
				}
			}
			System.out.print("\n");
		}
	}

	/**
	 * If the X and Y parameters are within the map boundaries, return the node
	 * in the specific coordinates, null otherwise.
	 * 
	 * @param x
	 *            Desired node's X coordinate.
	 * @param y
	 *            Desired node's Y coordinate.
	 * @return The desired node if the parameters are valid, null otherwise.
	 */
	public Node getNode(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height)
		{
			return nodes[x][y];
		}
		else
		{
			return null;
		}
	}

	/**
	 * Tries to calculate a path from the start and end positions.
	 * 
	 * @param startX
	 *            The X coordinate of the start position.
	 * @param startY
	 *            The Y coordinate of the start position.
	 * @param goalX
	 *            The X coordinate of the goal position.
	 * @param goalY
	 *            The Y coordinate of the goal position.
	 * @return A list containing all of the visited nodes if there is a
	 *         solution, an empty list otherwise.
	 */
	public final List<Node> findPath(int startX, int startY, int goalX, int goalY)
	{
		// If our start position is the same as our goal position ...
		if (startX == goalX && startY == goalY)
		{
			// Return an empty path, because we don't need to move at all.
			return new LinkedList<Node>();
		}

		// The set of nodes already visited.
		List<Node> openList = new LinkedList<Node>();
		// The set of currently discovered nodes still to be visited.
		List<Node> closedList = new LinkedList<Node>();

		// Add starting node to open list.
		openList.add(nodes[startX][startY]);

		// This loop will be broken as soon as the current node position is
		// equal to the goal position.
		while (true)
		{
			// Gets node with the lowest F score from open list.
			Node current = lowestFInList(openList);
			// Remove current node from open list.
			openList.remove(current);
			// Add current node to closed list.
			closedList.add(current);

			// If the current node position is equal to the goal position ...
			if ((current.getX() == goalX) && (current.getY() == goalY))
			{
				// Return a LinkedList containing all of the visited nodes.
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(current, closedList);
			for (Node adjacent : adjacentNodes)
			{
				// If node is not in the open list ...
				if (!openList.contains(adjacent))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set H costs of this node (estimated costs to goal).
					adjacent.setH(nodes[goalX][goalY]);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
					// Add node to openList.
					openList.add(adjacent);
				}
				// Else if the node is in the open list and the G score from
				// current node is cheaper than previous costs ...
				else if (adjacent.getG() > adjacent.calculateG(current))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
				}
			}

			// If no path exists ...
			if (openList.isEmpty())
			{
				// Return an empty list.
				return new LinkedList<Node>();
			}
			// But if it does, continue the loop.
		}
	}

	/**
	 * @param start
	 *            The first node on the path.
	 * @param goal
	 *            The last node on the path.
	 * @return a list containing all of the visited nodes, from the goal to the
	 *         start.
	 */
	private List<Node> calcPath(Node start, Node goal)
	{
		LinkedList<Node> path = new LinkedList<Node>();

		Node node = goal;
		boolean done = false;
		while (!done)
		{
			path.addFirst(node);
			node = node.getParent();
			if (node.equals(start))
			{
				done = true;
			}
		}
		return path;
	}

	/**
	 * @param list
	 *            The list to be checked.
	 * @return The node with the lowest F score in the list.
	 */
	private Node lowestFInList(List<Node> list)
	{
		Node cheapest = list.get(0);
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getF() < cheapest.getF())
			{
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}

	/**
	 * @param node
	 *            The node to be checked for adjacent nodes.
	 * @param closedList
	 *            A list containing all of the nodes already visited.
	 * @return A LinkedList with nodes adjacent to the given node if those
	 *         exist, are walkable and are not already in the closed list.
	 */
	private List<Node> getAdjacent(Node node, List<Node> closedList)
	{
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// Check left node
		if (x > 0)
		{
			adjacent = getNode(x - 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check right node
		if (x < width)
		{
			adjacent = getNode(x + 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check top node
		if (y > 0)
		{
			adjacent = this.getNode(x, y - 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check bottom node
		if (y < height)
		{
			adjacent = this.getNode(x, y + 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}
		return adjacentNodes;
	}

}
