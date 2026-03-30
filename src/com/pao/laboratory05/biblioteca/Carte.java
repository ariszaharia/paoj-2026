package com.pao.laboratory05.biblioteca;

public class Carte implements Comparable<Carte>{
    private String titlu;
    private String autor;
    private int an;
    private double rating;

    Carte(String titlu, String autor, int an, double rating){
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
        this.rating = rating;
    }

    String getTitlu(){
        return this.titlu;
    }
    String getAutor(){
        return this.autor;
    }
    int getAn(){
        return this.an;
    }
    double getRating(){
        return this.rating;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "titlu='" + titlu + '\'' +
                ", autor='" + autor + '\'' +
                ", an=" + an +
                ", rating=" + rating +
                '}';
    }
    @Override
    public int compareTo(Carte c){
         return Double.compare(this.rating, c.rating);
    }
}
