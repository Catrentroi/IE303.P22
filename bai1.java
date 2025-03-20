import java.util.Random;
import java.util.Scanner;

public class bai1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhap r: ");
        double r = scanner.nextDouble();
        
        double area = estimateCircleArea(r);
        
        System.out.println("Ban kinh: " + r);
        System.out.println("Dien tich xap xi: " + area);
        
        double theoreticalArea = Math.PI * r * r;
        System.out.println("Dien tich ly thuyet: " + theoreticalArea);
        System.out.println("Sai so: " + (Math.abs(area - theoreticalArea) / theoreticalArea * 100) + "%");
        
        scanner.close();
    }
    

    public static double estimateCircleArea(double r) {
        int numPoints = 1000000;
        int pointsInsideCircle = 0;
        
        Random random = new Random();
        
        for (int i = 0; i < numPoints; i++) {
            double x = random.nextDouble() * 2 * r - r;
            double y = random.nextDouble() * 2 * r - r;
            
            if (x*x + y*y <= r*r) {
                pointsInsideCircle++;
            }
        }
        

        double squareArea = (2*r) * (2*r);  
        return (double)pointsInsideCircle / numPoints * squareArea;
    }
}