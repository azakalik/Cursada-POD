package ar.edu.itba.pod.concurrency.exercises.e3;

import ar.edu.itba.pod.concurrency.exercises.e1.GenericService;
import ar.edu.itba.pod.concurrency.exercises.e1.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadTest {
    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    @Test
    public final void threadTest() {
        class GenericServiceThread extends Thread {
            @Override
            public void run(){
                for (int i = 0; i < 5; i++) {
                    service.addVisit();
                }
                assertEquals(5, service.getVisitCount());
            }
        }
        Thread thread = new GenericServiceThread();
        thread.start();
    }

    @Test
    public final void runnableTest() {
        class GenericServiceRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    service.addVisit();
                }
                assertEquals(5, service.getVisitCount());
            }
        }

        Thread thread = new Thread(new GenericServiceRunnable());
        thread.start();
    }

    @Test
    public final void lambdaTest() {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                service.addVisit();
            }
            assertEquals(5, service.getVisitCount());
        });
        thread.start();
    }
}
