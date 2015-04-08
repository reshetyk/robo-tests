package roboscripts;

/**
 * @author alre
 */
public class Launcher {
    public static void main(String[] args) {
        PolandVisaChecker checkerDnepr = new PolandVisaChecker("Польщі Дніпропетровськ", "Національна Віза");
        PolandVisaChecker checkerHarkiv = new PolandVisaChecker("Польщі Харків", "Національна Віза");

        new Thread(checkerDnepr).start();
        new Thread(checkerHarkiv).start();
    }
}
