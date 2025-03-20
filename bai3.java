import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class bai3 {
    static class Point {
        int x, y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public static int crossProduct(Point p1, Point p2, Point p3) {
            return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        }
        
        @Override
        public String toString() {
            return x + " " + y;
        }
    }
    
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            

            int n = scanner.nextInt();
            

            if (n <= 0) {
                System.out.println("Số lượng trạm phải lớn hơn 0");
                return;
            }
            

            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                points[i] = new Point(x, y);
            }
            

            List<Point> warningStations = findConvexHull(points);
            

            for (Point station : warningStations) {
                System.out.println(station);
            }
            
            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public static List<Point> findConvexHull(Point[] points) {
        int n = points.length;
        

        if (n <= 3) {
            if (n <= 1) {
                return Arrays.asList(points);
            }
            

            if (n == 3 && Point.crossProduct(points[0], points[1], points[2]) == 0) {

                return Arrays.asList(points[0], points[2]);
            }
            return new ArrayList<>(Arrays.asList(points));
        }
        

        int lowestPoint = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].y < points[lowestPoint].y || 
               (points[i].y == points[lowestPoint].y && points[i].x < points[lowestPoint].x)) {
                lowestPoint = i;
            }
        }
        

        Point temp = points[0];
        points[0] = points[lowestPoint];
        points[lowestPoint] = temp;
        

        final Point origin = points[0];
        Arrays.sort(points, 1, n, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                int orientation = Point.crossProduct(origin, p1, p2);
                if (orientation == 0) {

                    int distA = (p1.x - origin.x) * (p1.x - origin.x) + (p1.y - origin.y) * (p1.y - origin.y);
                    int distB = (p2.x - origin.x) * (p2.x - origin.x) + (p2.y - origin.y) * (p2.y - origin.y);
                    return distA - distB;
                }
                return -orientation;
            }
        });
        

        ArrayList<Point> uniquePoints = new ArrayList<>();
        uniquePoints.add(points[0]);
        for (int i = 1; i < n; i++) {
            if (i == n - 1 || Point.crossProduct(origin, points[i], points[i+1]) != 0) {
                uniquePoints.add(points[i]);
            }
        }
        
        if (uniquePoints.size() < 3) {
            return uniquePoints;
        }
        
        Stack<Point> hull = new Stack<>();
        hull.push(uniquePoints.get(0));
        hull.push(uniquePoints.get(1));
        
        for (int i = 2; i < uniquePoints.size(); i++) {
            while (hull.size() > 1 && Point.crossProduct(hull.get(hull.size() - 2), hull.peek(), uniquePoints.get(i)) <= 0) {
                hull.pop();
            }
            hull.push(uniquePoints.get(i));
        }
        
        return new ArrayList<>(hull);
    }
}