import Jcg.geometry.Point_3;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class WSPD {
    Set<Pair<OctreeNode, OctreeNode>> wspd;

    /**
     * Construct Well Separated Pair Decomposition from Octree
     * @param T                     Octree wrapper for the Octree nodes
     * @param s                     precision variable
     *                             (equals to the max ratio of
     *                             radius to the distance between two sets)
     */
    WSPD(Octree T, double s) {
        wspd = new HashSet<>();
        wsPairs(T.root, T.root, s);
    }

    /**
     * Construct Well Separated Pair Decomposition from Octree and two nodes
     * Done mostly for compatibility with the requirements
     *
     * @param u                     first set defined by OctreeNode
     * @param v                     second set defined by OctreeNode
     * @param s                     precision variable
     *                              (equals to the max ratio of
     *                              radius to the distance between two sets)
     */
    WSPD(Octree T, OctreeNode u, OctreeNode v, double s) {
        wspd = new HashSet<>();
        wsPairs(u, v, s);
    }

    /**
     * The main logic method
     * Creates Well Separated Pair Decomposition from Octree's two nodes.
     * Saves all the values in the internal container called
     * @see #wspd
     *
     * @param u                     first set defined by OctreeNode
     * @param v                     second set defined by OctreeNode
     * @param s                     precision variable
     *                              (equals to the max ratio of
     *                              radius to the distance between two sets)
     */
    private void wsPairs(OctreeNode u, OctreeNode v, double s) {
        if (u == null || v == null) return;
        if (s <= 0) return;

        if (u.p != null && v.p != null && v == u) return;
        if (isWellSeparated(u, v, s)) {
            Pair<OctreeNode, OctreeNode> pair = new Pair<>(u, v);
            if (!wspd.contains(new Pair<>(u, v)) && !wspd.contains(new Pair<>(v, u)))
                wspd.add(pair);
        } else {
            if (u.level > v.level) {
                for (OctreeNode v_ch : v.children) if (v_ch != null) wsPairs(u, v_ch, s);
            } else {
                for (OctreeNode u_ch : u.children) if (u_ch != null) wsPairs(u_ch, v, s);
            }

        }
    }


    /**
     * The method verifies if two sets (defined just by OctreeNodes) are s - well separated.
     * d(u, v) >= max(R(u), R(v)) * s
     * @param u             first set defined by OctreeNode
     * @param v             second set defined by OctreeNode
     * @param s             precision variable.
     *
     * @return              <code>true</code> if two sets are completely
     *                      s-well separated;
     *                      <code>false</code> otherwise.
     */
    private boolean isWellSeparated(OctreeNode u, OctreeNode v, double s) {
        if (u == null || v == null) return false;
        double u_radius = circleRadius(u);
        double v_radius = circleRadius(v);
        double distanceBetweenCircles =
                distanceBetweenNodes(u, v) - (u_radius + v_radius);
        double maxRadius = Math.max(u_radius, v_radius);
        return distanceBetweenCircles >= s * maxRadius;
    }

    /**
     * The method returns the distance between two sets
     * @param u             first set defined by OctreeNode
     * @param v             second set defined by OctreeNode
     *
     * @return              double value of the distance between two circles' centers
     */
    private double distanceBetweenNodes(OctreeNode u, OctreeNode v) {
        return u.center.distanceFrom(v.center).doubleValue();
    }


    /**
     * The method returns the radius of the set wrapping
     * @param u             target set defined by OctreeNode
     *
     * @return              double value of the radius of the circle
     */
    private double circleRadius(OctreeNode u) {
        if (u.p != null) return 0.0;
        double x_u_radius, y_u_radius, z_u_radius;
        x_u_radius = u.diamX / 2;
        y_u_radius = u.diamY / 2;
        z_u_radius = u.diamZ / 2;
        return Math.sqrt(x_u_radius * x_u_radius +
                y_u_radius * y_u_radius + z_u_radius * z_u_radius);

    }


    /**
     * Test method. Prints the values of the WSPD
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Pair<OctreeNode, OctreeNode> pair : wspd) {
            OctreeNode fst = pair.getKey();
            if (fst.p != null) builder.append("R: "
                    + circleRadius(fst) + ", P: " + fst.p);
            else {
                builder.append("R: "
                        + circleRadius(fst) + ", C: " + fst.center);
            }
            builder.append("  ");
            OctreeNode snd = pair.getValue();
            if (snd.p != null) builder.append("R: "
                    + circleRadius(snd)+ ", P: " + snd.p);
            else {
                builder.append("R: "
                        + circleRadius(snd)+ ", C: " + snd.center);
                OctreeNode tmp = snd;

            }
            builder.append(" ,\n");
        }
        return builder.toString();
    }


    /**
     * Test method
     */
    public static void main(String[] args) {
        Point_3[] points = new Point_3[6];
        points[0] = new Point_3(0.1, 1.9, 0);
        points[1] = new Point_3(0.84, 1.75, 0);
        points[2] = new Point_3(0.76, 1.71, 0);
        points[3] = new Point_3(0.4, 0.4, 0);
        points[4] = new Point_3(1.6, 0.3, 0);
        points[5] = new Point_3(1.3, 1.75, 0);

        Octree tree = new Octree(points);
        WSPD hel = new WSPD(tree, 0.5);
        System.out.println(hel);
    }
}
