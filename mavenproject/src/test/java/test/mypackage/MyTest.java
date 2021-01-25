package test.mypackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mypackage.Incrementor;
import org.junit.Assert;
import org.junit.Test;

public class MyTest {

    @Test
    public void MyTest() {
        Runnable myTask = new Runnable() {
            public void run() {
                Random rand = new Random();
                try {
                    for (int d = 0; d < 10; d++) {
                        int a = rand.nextInt(2);
                        Thread.sleep(a);
                        Incrementor.addClick();
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(MyTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        ExecutorService service = Executors.newCachedThreadPool();
        List<Future<?>> testLst = new ArrayList();
        for (int y = 0; y < 2; y++) {
            Future<?> result = service.submit(myTask);
            testLst.add(result);
        }
        int a = 1;
        while (a > 0) {
            a = 0;
            for (Future<?> res : testLst) {
                boolean isEnded = res.isDone();
                if (!isEnded) {
                    a++;
                }
            }
        }
        int i = Incrementor.getClick();
        Assert.assertEquals(20,i);
    }
}
