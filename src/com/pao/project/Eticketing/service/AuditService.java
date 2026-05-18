package com.pao.project.Eticketing.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {
    private static final String AUDIT_FILE = "audit.csv";
    private static AuditService instance;
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String numeActiune) {
        lock.lock();
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.println(numeActiune + "," + LocalDateTime.now());
        } catch (IOException e) {
            System.err.println("Eroare la scriere audit: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
