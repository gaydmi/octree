import Jcg.geometry.*;
import javafx.util.Pair;

/**
 * Implementation of a fast algorithm for computing the closest pair,
 * based on WSP.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 */
public class FastClosestPair_3 implements ClosestPair_3 {

    /**
     * Compute the closest pair of a set of points in 3D space
     *
     * @param points the set of points
     * @return a pair of closest points
     */
    public Point_3[] findClosestPair(Point_3[] points) {
        if (points.length < 2) throw new Error("Error: too few points");

        System.out.print("Computing closest pair: fast computation...");
        long startTime = System.currentTimeMillis();
        Point_3[] result = new Point_3[]{points[0], points[1]};

        double distance = result[0].distanceFrom(result[1]).doubleValue();
        Octree octree = new Octree(points);
        double s = 2;
        WSPD wspd = new WSPD(octree, s);

        for (Pair<OctreeNode, OctreeNode> pair : wspd.wspd) {
            OctreeNode fstNode = pair.getKey();
            OctreeNode sndNode = pair.getValue();
            if (fstNode.p != null && sndNode.p != null) {
                double pairDistance = fstNode.p.distanceFrom(
                        sndNode.p).doubleValue();
                if (pairDistance <= distance) {
                    distance = pairDistance;
                    result = new Point_3[]{fstNode.p, sndNode.p};
                }
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