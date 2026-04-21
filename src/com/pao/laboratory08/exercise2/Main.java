package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

public class Main {
    private static final String FILE_PATH = "paoj-2026/src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă

        List<Student> studenti = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while ((linie = br.readLine()) != null){
            String[] date = linie.split(",");
            String nume = date[0];
            int varsta = Integer.parseInt(date[1]);
            String oras = date[2];
            String strada = date[3];
            Student student = new Student(nume, varsta, new Adresa(oras, strada));
            studenti.add(student);
        }
        br.close();
        int prag = scanner.nextInt();
        System.out.println("Filtru: varsta >= " + prag);
        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }
        System.out.println("Rezultate: " +filtrati.size() + " studenti" );
        System.out.println();

        for (Student s : studenti) {
            System.out.println(s);
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
        for(Student s : filtrati){
            bw.write(s.toString());
            bw.newLine();
        }
        bw.close();
        System.out.println();
        System.out.println("Scris in: rezultate.txt");



    }
}

