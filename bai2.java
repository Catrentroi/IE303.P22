import java.util.Random;

public class bai2 {
    public static void main(String[] args) {
        int totalPoints = 10000000; //cang lon cang chinh xac
        int pointsInsideCircle = 0;

        Random random = new Random();

        System.out.println("Dang tinh gia tri cua xap xi cua pi " + totalPoints + " diem...");

        for (int i = 0; i < totalPoints; i++) {
            double x = 2.0 * random.nextDouble() - 1.0;
            double y = 2.0 * random.nextDouble() - 1.0;

            if (x * x + y * y <= 1.0) {
                pointsInsideCircle++;
            }
        }

        double piApproximation = 4.0 * pointsInsideCircle / totalPoints;

        System.out.println("Gia tri xap xi cua pi " + piApproximation);
        System.out.println("Gia tri thuc cua pi:    " + Math.PI);
        System.out.println("Sai so: " + Math.abs(piApproximation - Math.PI));
    }
}