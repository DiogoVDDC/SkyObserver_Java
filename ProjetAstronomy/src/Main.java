import ch.epfl.rigel.math.Polynomial;

public class Main {
    public static void main(String[] args) {
        Polynomial a = Polynomial.of(1, 2, 2);
        System.out.println(a.at(2));
        System.out.println(a);
    }
}
