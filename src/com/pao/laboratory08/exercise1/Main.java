package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "paoj-2026/src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
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
        String input = scanner.nextLine();
        String[] tokens = input.split(" ");
        String comanda = tokens[0];
        switch (comanda){
            case "PRINT":
                for (Student s : studenti){
                    System.out.println(s);
                }
                break;
            case "SHALLOW":
                String nume = tokens[1];
                Student clona = null;
                Student original = null;
                for(Student s: studenti){
                    if (s.getNume().equals(nume)){
                        clona = s.shallowClone();
                        original = s;
                    }
                }
                clona.getAdresa().setOras("MODIFICAT");

                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);

                break;

            case "DEEP":

                String nume2 = tokens[1];
                Student clona2 = null;
                Student original2 = null;
                for(Student s: studenti){
                    if (s.getNume().equals(nume2)){
                        clona2 = s.deepClone();
                        original2 = s;
                    }
                }
                clona2.getAdresa().setOras("MODIFICAT");

                System.out.println("Original: " + original2);
                System.out.println("Clona: " + clona2);



                break;
        }
    }
}
