
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

/**
 * A class for representing an Octree
 *
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */
public class Octree {
    public OctreeNode root;

    /**
     * Constructor for  wrapper class of OctreeNode. Contains the root of the tree
     *
     * @param points        The array of points for constructing the Octree
     *
     * @return              Saves the root node (@see OctreeNode) for the tree
     */
    public Octree(Point_3[] points){

        // Searching max values for all dimensions
        double maxX = Double.MIN_VALUE, maxY =  Double.MIN_VALUE, maxZ =  Double.MIN_VALUE;
        double minX =  Double.MAX_VALUE, minY =  Double.MAX_VALUE, minZ =  Double.MAX_VALUE;
        for(int i = 0; i < points.length; i++) {
            double x = (double) points[i].getX();
            double y = (double) points[i].getY();
            double z = (double) points[i].getZ();
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
        }

        // Select the min and max values so that the boxes would be squares
        minX = Math.min(minX, Math.min(minY, minZ));
        minY = minX;
        minZ = minX;
        maxX = Math.max(maxX, Math.max(maxY, maxZ));
        maxY = maxX;
        maxZ = maxX;

        // Magic is the ratio for the gap
        // between the edge of the box and the point made so that
        // all points would be strictly inside the box
        double magic = 5.0;
        minX -= (maxX-minX) / magic;
        minY -= (maxY-minY) / magic;
        minZ -= (maxZ-minZ) / magic;
        maxX += (maxX-minX) / magic;
        maxY += (maxY-minY) / magic;
        maxZ += (maxZ-minZ) / magic;

        List<Point_3> pointList = new ArrayList<Point_3>();
        for(int i = 0; i < points.length; i++) pointList.add(points[i]);
        Iterator<Point_3> it = pointList.iterator();
        List<Point_3> l = new ArrayList<Point_3>();
        l.add(it.next());
        root = new OctreeNode(l);
        root.level = 1;
        root.center = new Point_3((maxX+minX) / 2.0, (maxY+minY) / 2.0, (maxZ+minZ) / 2.0);
        root.diamX = maxX - minX;
        root.diamY = maxY - minY;
        root.diamZ = maxZ - minZ;
        while (it.hasNext()) {
            Point_3 point = it.next();
            root.add(point);
        }
    }

    /**
    * Test method
     */
    public static void main(String[] args) {
        Point_3[] points = new Point_3[13];
        points[0] = new Point_3(1,1,1);
        points[1] = new Point_3(1,1,2);

        points[2] = new Point_3(1,2,1);
        points[3] = new Point_3(1,2,2);
        points[4] = new Point_3(2,1,1);
        points[5] = new Point_3(2,1,2);
        points[6] = new Point_3(2,2,1);
        points[7] = new Point_3(2,2,2);
        points[8] = new Point_3(2,2,1.6);
        points[9] = new Point_3(1.1,1.3,1.2);
        points[10] = new Point_3(1.2,1.2,1.3);
        points[11] = new Point_3(1.25,1.25,1.35);
        points[12] = new Point_3(1.22,1.22,1.31);
        Octree tree = new Octree(points);
        System.out.println(tree.root.center);
        for(int i=0; i<8;i++) {
            System.out.println(tree.root.children[i]);
            if (tree.root.children[i]!=null) {
                System.out.println(i+ " point " + tree.root.children[i].p);
                for(int j=0; j<8;j++) {
                    System.out.println(i+ " "+ j+ " "+ tree.root.children[i].children[j]);
                    if (tree.root.children[i].children[j]!=null) {
                        System.out.println(tree.root.children[i].children[j].p);
                    }
                }

            }
        }
        System.out.println("NEW");

    }

}
