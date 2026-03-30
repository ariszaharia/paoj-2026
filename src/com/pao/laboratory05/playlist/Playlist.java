package com.pao.laboratory05.playlist;
import com.pao.laboratory05.Song;
import com.pao.laboratory05.SongDurationComparator;
import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];


    Playlist(String name){
        this.name = name;
    }
    void addSong(Song song){
        songs = Arrays.copyOf(songs, songs.length + 1);
        songs[songs.length - 1] = song;
    }

    void printSortedByTitle(){
        Song[] copy = songs.clone();

        Arrays.sort(copy);

        for (Song song : copy){
            System.out.println(song);
        }
    }

    void printSortedByDuration(){
        Song[] copy = songs.clone();

        Arrays.sort(copy, new SongDurationComparator());

        for (Song s : copy) {
            System.out.println(s);
        }
    }
    String getName(){
        return this.name;
    }

    int getTotalDuration(){
        int total = 0;

        for (Song s : songs) {
            total += s.durationSeconds();
        }

        return total;
    }
}
