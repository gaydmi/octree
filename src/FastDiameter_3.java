import Jcg.geometry.*;
import javafx.util.Pair;

/**
 * Implementation of a fast algorithm for computing an approximation of the diameter of a 3D point cloud,
 * based on WSP.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 */
public class FastDiameter_3 implements Diameter_3 {

    /**
     * approximation factor (for approximating the diameter)
     **/
    public double epsilon;

    public FastDiameter_3(double epsilon) {
        this.epsilon = epsilon;
    }



    /**
     * Compute a farthest pair of points realizing an (1-epsilon)-approximation of the diameter of a set of points in 3D space
     *
     * @param points the set of points
     * @return a pair of farthest points
     */
    public Point_3[] findFarthestPair(Point_3[] points) {
        if (points.length < 2) throw new Error("Error: too few points");

        System.out.print("Computing farthest pair: fast computation...");
        long startTime = System.currentTimeMillis();
        Point_3[] result = new Point_3[]{points[0], points[1]};
        double distance = 0.;
        double s = 4.0 / this.epsilon;
        Octree octree = new Octree(points);
        WSPD wspd = new WSPD(octree, s);


        for (Pair<OctreeNode, OctreeNode> pair : wspd.wspd) {
            OctreeNode fstNode = pair.getKey();
            OctreeNode sndNode = pair.getValue();
            Point_3 p1 = fstNode.currentPoints.get(0);
            Point_3 p2 = sndNode.currentPoints.get(0);

            double pairDistance = p1.distanceFrom(p2).doubleValue();
            if (pairDistance > distance) {
                result = new Point_3[]{p1, p2};
                distance = pairDistance;

            }
        }

        System.out.println(distance);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Duration: " + duration);
        System.out.println("done");

        return result;
    }

}