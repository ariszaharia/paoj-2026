package com.pao.project.Eticketing.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuditService {

    private static volatile AuditService instance;
    private static final Object lock = new Object();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // cached absolute path used for writing
    private final Path auditPath;

    private AuditService() {
        // Build absolute path to the audit folder using Path API for proper cross-platform handling
        Path userHome = Paths.get(System.getProperty("user.home"));
        Path projectPath = userHome.resolve("IdeaProjects").resolve("lab3").resolve("paoj-2026");
        
        // Construct the audit folder path
        Path auditDir = projectPath.resolve("src").resolve("com").resolve("pao").resolve("project").resolve("Eticketing").resolve("audit");
        Path auditFile = auditDir.resolve("audit.csv");
        
        // Ensure directory exists
        try {
            Files.createDirectories(auditDir);
        } catch (IOException e) {
            System.err.println("Eroare la crearea directorului audit: " + e.getMessage());
        }
        
        this.auditPath = auditFile;
        System.out.println("Audit file will be written to: " + auditPath.toString());
    }

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        try {
            try (PrintWriter out = new PrintWriter(new FileWriter(auditPath.toFile(), true))) {
                String timestamp = LocalDateTime.now().format(formatter);
                out.println(actionName + "," + timestamp);
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}