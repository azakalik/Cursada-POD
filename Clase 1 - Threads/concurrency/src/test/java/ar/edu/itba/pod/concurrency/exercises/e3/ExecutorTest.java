package ar.edu.itba.pod.concurrency.exercises.e3;

import ar.edu.itba.pod.concurrency.exercises.e1.GenericService;
import ar.edu.itba.pod.concurrency.exercises.e1.GenericServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecutorTest {
    private GenericService service;

    @BeforeEach
    public final void before() {
        service = new GenericServiceImpl();
    }

    @Test
    public final void test() {
        class GenericServiceRunnable implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    service.addVisit();
                }
                assertEquals(5, service.getVisitCount());
            }
        }
        final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Future<?> genericServiceFuture = cachedThreadPool.submit(new GenericServiceRunnable());
    }
}
