package com.brz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Map;

class PhotoSegregatorIdea {
    public static void main(String[] args) throws IOException {
        new PhotoSegregatorIdea().run();
    }

    private void run() throws IOException {
        String workingDir = Paths.get("").toAbsolutePath().toString();
        Arrays.stream(getFiles(workingDir))
                .map(this::toSourceTargetPaths)
                .forEach(entry -> move(entry.getKey(), entry.getValue()));
    }

    private File[] getFiles(String dir) {
        return new File(dir).listFiles(f -> f.isFile() && f.getParent().equals(dir));
    }

    private Path createNewPath(File file) throws IOException {
        return Paths.get(file.getParent(), getCreatedDate(file).toString(), getExtension(file), file.getName());
    }

    private LocalDate getCreatedDate(File file) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        Instant creationTime = attrs.creationTime().toInstant();
        return LocalDate.ofInstant(creationTime, ZoneId.systemDefault());
    }

    private Map.Entry<Path, Path> toSourceTargetPaths(File file) {
        try {
            return Map.entry(file.toPath(), createNewPath(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void move(Path source, Path target) {
//        Files.move(source, target);
        System.out.printf("Moved %s -> %s\n", source, target);
    }

    private String getExtension(File file) {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(absolutePath.lastIndexOf(".") + 1);
    }
}
