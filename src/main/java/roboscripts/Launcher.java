package roboscripts;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author alre
 */
public class Launcher {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        PolandVisaChecker checkerDnepr = new PolandVisaChecker("Польщі Дніпропетровськ", "Національна Віза");
//        PolandVisaChecker checkerHarkiv = new PolandVisaChecker("Польщі Харків", "Національна Віза");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> submit = executorService.submit(checkerDnepr);
        while (!submit.get().booleanValue()) {
            submit = executorService.submit(checkerDnepr);
        }

//        new Thread(checkerDnepr).start();
//        new Thread(checkerHarkiv).start();
    }
}
