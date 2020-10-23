package io.quarkiverse.doma.deployment;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;

import io.quarkus.runtime.util.ClassPathUtils;

public class DomaResourceScanner {

    private static final Logger logger = Logger.getLogger(DomaResourceScanner.class);

    private static final String META_INF = "META-INF";

    List<String> scan() {
        Set<String> files = new HashSet<>();
        try {
            ClassPathUtils.consumeAsPaths(META_INF, resource -> collect(resource, files));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return files.stream().map(it -> META_INF + "/" + it).collect(toList());
    }

    private void collect(Path resource, Set<String> files) {
        try {
            Files.walkFileTree(
                    resource,
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            String fileName = resource.relativize(file).toString();
                            if (fileName.endsWith(".sql") || fileName.endsWith(".script")) {
                                logger.debugf("resource found: %s", fileName);
                                files.add(fileName);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
