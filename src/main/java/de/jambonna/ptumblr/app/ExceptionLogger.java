package de.jambonna.ptumblr.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Responsible of logging exceptions in specific files and generating an id for it
 */
@Component
public class ExceptionLogger {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);
    
    
    private final Path logPath;

    public ExceptionLogger(@Value("${app.varDir}") String varDir) throws IOException {
        logPath = Paths.get(varDir).toRealPath().resolve("log");
        
        // Create log dir if needed
        Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
        FileAttribute<Set<PosixFilePermission>> subDirsPermissions = PosixFilePermissions.asFileAttribute(perms);
        Files.createDirectories(logPath, subDirsPermissions);
    }

    public UUID log(Throwable exception) {
        UUID uid = UUID.randomUUID();
        logger.error("Logging exception {} :", uid, exception);
        Path p = logPath.resolve(uid + ".log");
        try {
            PrintWriter writer = new PrintWriter(p.toFile(), "UTF-8");
            exception.printStackTrace(writer);
            writer.close();
        } catch (IOException ex) {
            logger.error("Can't log exception {} to {}", uid, p);
        }
        return uid;
    }
}
