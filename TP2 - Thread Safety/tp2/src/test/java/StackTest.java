import ej2and3.Stack;
import ej2and3.ThreadSafeStack;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class StackTest {
    private final int THREADS = 10;

    private void testStackThreadSafety(Stack stack) {
        final ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        final List<Future<?>> futureList = new ArrayList<>();
        
        // Push 10 numbers to the stack with threads
        for (int i = 0; i < THREADS*1000; i++) {
            Future<?> f = pool.submit(() -> {
                stack.push(1);
                stack.pop();
            });
            futureList.add(f);
        }

        // Wait for the pushes to finish
        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored){}
        
        // Check if one of the futures throwed an exception
        futureList.forEach(future -> {
            try {
                future.get();
            } catch(CancellationException | ExecutionException | InterruptedException ignored){
                fail();
            }
        });

        // The 'top' value here should be 0, so an attempt to pop should throw this exception
        assertThrows(IllegalStateException.class, stack::pop);
    }

    // This test fails because Stack is not thread safe
//    @Test
//    public void testEx2Stack() {
//        final Stack stack = new Stack();
//        testStackThreadSafety(stack);
//    }

    // This test suceeds because ThreadSafeStack is thread safe
    @Test
    public void testEx3Stack() {
        final ThreadSafeStack threadSafeStack = new ThreadSafeStack();
        testStackThreadSafety(threadSafeStack);
    }
}