package com.pao.laboratory05.audit;

import com.pao.laboratory05.audit.AuditEntry;
import com.pao.laboratory05.angajati.Departament;
import com.pao.laboratory05.angajati.Angajat;

import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati = new Angajat[0];

    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(
                action,
                target,
                java.time.LocalDateTime.now().toString()
        );

        auditLog = Arrays.copyOf(auditLog, auditLog.length + 1);
        auditLog[auditLog.length - 1] = entry;
    }

    public void addAngajat(Angajat a) {
        angajati = Arrays.copyOf(angajati, angajati.length + 1);
        angajati[angajati.length - 1] = a;

        logAction("ADD", a.getNume());
    }

    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        Angajat[] copy = angajati.clone();

        Arrays.sort(copy);

        for (int i = copy.length - 1; i >= 0; i--) {
            System.out.println(copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {

        logAction("FIND_BY_DEPT", numeDept);

        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        boolean gasit = false;

        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    public void printAuditLog() {
        if (auditLog.length == 0) {
            System.out.println("Nu există acțiuni în audit.");
            return;
        }

        System.out.println("--- Audit Log ---");
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}