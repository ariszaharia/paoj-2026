package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());

        int[] ids = new int[n];
        double[] sumas = new double[n];
        String[] dates = new String[n];
        TipTranzactie[] tips = new TipTranzactie[n];

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split(" ");
            ids[i] = Integer.parseInt(parts[0]);
            sumas[i] = Double.parseDouble(parts[1]);
            dates[i] = parts[2];
            tips[i] = TipTranzactie.valueOf(parts[3]);
        }

        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ids[i]).array());
                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(sumas[i]).array());
                byte[] dateBytes = new byte[10];
                Arrays.fill(dateBytes, (byte) ' ');
                byte[] dateRaw = dates[i].getBytes("ASCII");
                System.arraycopy(dateRaw, 0, dateBytes, 0, Math.min(dateRaw.length, 10));
                dos.write(dateBytes);
                dos.write(tips[i] == TipTranzactie.CREDIT ? 0 : 1);
                dos.write(0); // status = PENDING
                dos.write(new byte[8]); // padding
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(" ");
                String cmd = parts[0];
                if (cmd.equals("READ")) {
                    int idx = Integer.parseInt(parts[1]);
                    printRecord(raf, idx);
                } else if (cmd.equals("UPDATE")) {
                    int idx = Integer.parseInt(parts[1]);
                    String statusStr = parts[2];
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusToByte(statusStr));
                    System.out.println("Updated [" + idx + "]: " + statusStr);
                } else if (cmd.equals("PRINT_ALL")) {
                    for (int i = 0; i < n; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        }
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] bytes = new byte[RECORD_SIZE];
        raf.readFully(bytes);
        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        int id = buf.getInt(0);
        double suma = buf.getDouble(4);
        String data = new String(bytes, 12, 10, "ASCII").trim();
        String tip = (bytes[22] & 0xFF) == 0 ? "CREDIT" : "DEBIT";
        String status = statusToString(bytes[23] & 0xFF);
        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n", idx, id, data, tip, suma, status);
    }

    private static int statusToByte(String status) {
        switch (status) {
            case "PROCESSED": return 1;
            case "REJECTED": return 2;
            default: return 0;
        }
    }

    private static String statusToString(int b) {
        switch (b) {
            case 1: return "PROCESSED";
            case 2: return "REJECTED";
            default: return "PENDING";
        }
    }
}
