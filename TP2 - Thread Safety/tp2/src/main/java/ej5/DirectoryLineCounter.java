package ej5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class DirectoryLineCounter {
    public DirectoryLineCounter(){}

    public long countLinesInDirectory(String directoryPath) throws IOException {
        // Return 0 if the path is invalid
        Path dir;
        try {
            dir = Paths.get(directoryPath);
        } catch (InvalidPathException ignored) {
            return 0;
        }

        // If the path is not a directory, return 0
        if(!Files.isDirectory(dir))
            return 0;

        Long totalLines = 0L;
        List<Future<Long>> fileLineCounts = new ArrayList<>();

        try (
                // Executor service with default Thread creation
                ExecutorService executorService = Executors.newCachedThreadPool();

                // Open Steam of filepaths
                Stream<Path> stream = Files.list(dir);
        ) {
            // Submit a new Line Counter thread for each file in the stream
            stream.forEach((file) -> fileLineCounts.add(executorService.submit(new LineCounterCallable(file))));

            // Wait for each Thread to stop and sum the values
            Optional<Long> maybeAnswer = fileLineCounts
                    .stream()
                    .map(fileLineFuture -> {
                        try {
                            return fileLineFuture.get();
                        } catch (ExecutionException | InterruptedException e) {
                            return 0L;
                        }
                    })
                    .reduce(Long::sum)
            ;

            return maybeAnswer.orElse(0L);
        }
    }

    public long countLinesInFile(Path filePath) throws IOException {
        if(Files.isDirectory(filePath))
            return 0;

        // We do a "try with resources" to make sure that the stream is correctly closed
        try (Stream<String> lines = Files.lines(filePath)){
            return lines.count();
        }
    }

    class LineCounterCallable implements Callable<Long> {
        private final Path filepath;
        public LineCounterCallable(Path filepath){
            this.filepath = filepath;
        }
        @Override
        public Long call() throws Exception {
            return countLinesInFile(filepath);
        }
    }
}

