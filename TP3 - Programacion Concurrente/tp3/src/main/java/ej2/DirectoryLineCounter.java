package ej2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class DirectoryLineCounter {
    public DirectoryLineCounter(){}

    private static long getFileLineCount(Path filePath) throws IOException {
        if(Files.isDirectory(filePath))
            return 0;

        // We do a "try with resources" to make sure that the stream is correctly closed
        try (Stream<String> lines = Files.lines(filePath)){
            return lines.count();
        }
    }

    private static String getFilename(Path filepath){
        return filepath.toString();
    }

    private static long getFileSize(Path filepath) throws IOException {
        return Files.size(filepath);
    }

    private static CompletableFuture<String> getFileInformation(Path filepath) throws IOException {
        String filename = getFilename(filepath);

        CompletableFuture<Long> fileSizeCF = CompletableFuture.supplyAsync(() -> {
            try {
                return getFileSize(filepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<Long> fileLineCountCF = CompletableFuture.supplyAsync(() -> {
            try {
                return getFileLineCount(filepath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return fileSizeCF.thenCombineAsync(fileLineCountCF, (fileSize, fileLineCount) -> "File info:\nName: " + filename + "\nSize: " + fileSize + "\nLine count: " + fileLineCount + "\n\n");
    }

    public void printFilesInformation(String directoryPath) throws IOException {
        // Return 0 if the path is invalid
        Path dir;
        try {
            dir = Paths.get(directoryPath);
        } catch (InvalidPathException ignored) {
            return;
        }

        // If the path is not a directory, return 0
        if (!Files.isDirectory(dir))
            return;

        Long totalLines = 0L;
        List<CompletableFuture<String>> filesInformation = new ArrayList<>();

        try (
                // Open Steam of filepaths
                Stream<Path> stream = Files.list(dir);
        ) {
            stream.forEach((file) -> {
                try {
                    filesInformation.add(getFileInformation(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            filesInformation.forEach((fileStringCF) -> {
                try {
                    System.out.println(fileStringCF.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}

