package io.quarkiverse.doma.runtime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtil {

    // use Files.readString(file) in Java 11 or later
    public static String readString(Path file) throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
