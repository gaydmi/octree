import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jcg.geometry.Point_3;

/**
 * A class for representing a node of an Octree
 *
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */
public class OctreeNode {
	public int level;
	public OctreeNode[] children = null;
	public OctreeNode father;
	public List<Point_3> currentPoints;
	public Point_3 p = null; //point stored in a leaf
	public Point_3 center;
	public double diamX;
	public double diamY;
	public double diamZ;

	/**
	 * Create the octree for storing an input point cloud
	 */
	public OctreeNode(List<Point_3> points) {
		this.children = new OctreeNode[8];
		if (points.size() == 1) this.p = points.get(0);
		this.currentPoints = points;
	}


	/**
	* 	Internal method which calculates the center of the box from its index
	 */
	private Point_3 newCenter(int index) {
		double xCenter, yCenter, zCenter;
		xCenter = this.center.getX().doubleValue();
		yCenter = this.center.getY().doubleValue();
		zCenter = this.center.getZ().doubleValue();
		double x, y, z;
		switch (index) {
			case 0: 
				x = xCenter + diamX / 4.0;
				y = yCenter + diamY / 4.0;
				z = zCenter + diamZ / 4.0;
				break;
			case 1:
				x = xCenter + diamX / 4.0;
				y = yCenter + diamY / 4.0;
				z = zCenter - diamZ / 4.0;
				break;
			case 2:	
				x = xCenter + diamX / 4.0;
				y = yCenter - diamY / 4.0;
				z = zCenter + diamZ / 4.0;
				break;
			case 3:	
				x = xCenter + diamX / 4.0;
				y = yCenter - diamY / 4.0;
				z = zCenter - diamZ / 4.0;
				break;
			case 4:	
				x = xCenter - diamX / 4.0;
				y = yCenter + diamY / 4.0;
				z = zCenter + diamZ / 4.0;
				break;
			case 5: 
				x = xCenter - diamX / 4.0;
				y = yCenter + diamY / 4.0;
				z = zCenter - diamZ / 4.0;
				break;
			case 6:	
				x = xCenter - diamX / 4.0;
				y = yCenter - diamY / 4.0;
				z = zCenter + diamZ / 4.0;
				break;
			default:
				x = xCenter - diamX / 4.0;
				y = yCenter - diamY / 4.0;
				z = zCenter - diamZ / 4.0;
				break;
		}
		return new Point_3(x, y, z);


	}

	/**
	*	Internal method which gives the index of the box for a given point
	 * @return 			index of the children for a given OctreeNode.
	 * 					Equals -1 if the point is not in the node.
	 */
	private static int subNodeSearch(OctreeNode node, Point_3 p){
		int ind = -1;
		if (p.compareCartesian(node.center, 0) >= 0) {
			if (p.compareCartesian(node.center, 1) >= 0) {
				if (p.compareCartesian(node.center, 2) >= 0) ind = 0;
				else ind = 1;
			}
			else {
				if (p.compareCartesian(node.center, 2) >= 0) ind = 2;
				else ind = 3;
			}
		}
		else {
			if (p.compareCartesian(node.center, 1) >= 0) {
				if (p.compareCartesian(node.center, 2) >= 0) ind = 4;
				else ind = 5;
			}
			else {
				if (p.compareCartesian(node.center, 2) >= 0) ind = 6;
				else ind = 7;
			}
		}
		return ind;
	}

	/**
	 * Creates the child for the given OctreeNode
	 *
	 * @param node			given OctreeNode
	 * @param l				list of the current points
	 * @param ind 			index of the child
	 */
	public static void updateChild(OctreeNode node, List<Point_3> l, int ind) {
		node.children[ind] = new OctreeNode(l);
		node.children[ind].level = node.level + 1;
		node.children[ind].father = node;
		node.children[ind].center = node.newCenter(ind);
		node.children[ind].diamX = node.diamX / 2.0;
		node.children[ind].diamY = node.diamY / 2.0;
		node.children[ind].diamZ = node.diamZ / 2.0;
	}

	/**
	 *
	 * Adds the node to the tree
	 *
	 * @param p 	the point to add to the octree
	 */
	public void add(Point_3 p) {
		OctreeNode node = this;

		while (node != null) {
			int ind = subNodeSearch(node, p);
			node.currentPoints.add(p);
			if (node.children[ind] == null) {
				int ind2;
				if (node.p == null) ind2 = -1;
				else ind2 = subNodeSearch(node,node.p);
				while (ind == ind2) {
					List<Point_3> l = new ArrayList<Point_3>();
					l.add(node.p);
					updateChild(node, l, ind);
					node = node.children[ind];
					ind = subNodeSearch(node, p);
					ind2 = subNodeSearch(node, node.p);
					node.father.p = null;
				}
				List<Point_3> l2 = new ArrayList<Point_3>();
				l2.add(p);
				updateChild(node, l2, ind);
				if (ind2 > -1) {
					l2 = new ArrayList<Point_3>();
					l2.add(node.p);
					updateChild(node, l2, ind2);
					node.p = null;
				}
				break;
			}
			node = node.children[ind];

		}
	}

}
