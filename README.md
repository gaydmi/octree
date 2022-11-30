# Fast approximation algorithms for 3D geometric problems

The goal of this project is to provide fast tools for the approximation of geometric distances between 3d points, leading to fast solutions for several problems. More precisely, it does:

    * constructs a (compressed) octree (a hierarchical decomposition of the 3d space for partitioning point clouds);
    * uses the octree to recursively compute a well separated pair decomposition of the input 3d point cloud;
    * application 1: implements a O(nlog n) algorithm for the closest pair problem for a 3d point cloud;
    * application 2: implement a O(nlog n) algorithm for approximating the diameter of a 3d point cloud;
    * improves the runtime performances of the standard force-directed method by using a well separated pair decomposition to approximate repulsive forces;

To test the algorithm, you could run the class PointCloudViewer which provides a full graphic user interface for visualizing 3D point clouds. To test your fast implementation of the force-directed method, you can run the class NetworkLayout which provides a full graphic user interface for visualizing network layouts.
