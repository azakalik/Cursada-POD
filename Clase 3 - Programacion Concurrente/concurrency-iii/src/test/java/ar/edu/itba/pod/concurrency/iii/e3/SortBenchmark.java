package ar.edu.itba.pod.concurrency.iii.e3;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;


import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Benchmark to compare between {@link Arrays#parallelSort(int[])} and
 * {@link Arrays#sort(int[])}
 */
public class SortBenchmark {

    @Test
    public void benchmark_all() {
        final Consumer<int[]> serial = a -> Arrays.sort(a);
        final Consumer<int[]> parallel = a -> Arrays.parallelSort(a);
        final int multiplier = 1000000; // For smaller numbers (like 100 or 1000), parallel is slower
        final Random random = new Random();

        final int[] small = generateArray(10*multiplier, random);
        final int[] med = generateArray(25*multiplier, random);
        final int[] large = generateArray(50*multiplier, random);

        benchmark(small, serial, "Small serial");
        benchmark(small, parallel, "Small parallel");
        benchmark(med, serial, "Med serial");
        benchmark(med, parallel, "Med parallel");
        benchmark(large, serial, "Large serial");
        benchmark(large, parallel, "Large parallel");
    }

    private int[] generateArray(final int size, final Random random){
        final int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }

    // When we are measuring times, we should repeat the test at least a few times
    private void benchmark(final int[] array, final Consumer<int[]> orderer, final String message){
        long accumulate = 0;
        final int TIMES = 4;
        for (int i = 0; i < TIMES; i++) {
            // If we don't copy the array, we will be ordering it the first time and the next times it will be already ordered
            int[] copiedArray = Arrays.copyOf(array, array.length);
            final long startTime = System.currentTimeMillis();
            orderer.accept(copiedArray);
            final long finishtime = System.currentTimeMillis();
            accumulate += finishtime - startTime;
        }
        System.out.println(message + " took " + accumulate/TIMES + " ms ");
    }

}
