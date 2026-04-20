package com.pao.project.Eticketing.model.event;

public class Concert extends Eveniment {
    private final String artist;
    private final String genMuzical;

    public Concert(String denumire, String data, int durataMinute, Locatie locatie, String artist, String genMuzical) {
        super(denumire, data, durataMinute, locatie);
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist invalid");
        }
        if (genMuzical == null || genMuzical.trim().isEmpty()) {
            throw new IllegalArgumentException("Gen muzical invalid");
        }
        this.artist = artist;
        this.genMuzical = genMuzical;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenMuzical() {
        return genMuzical;
    }

    @Override
    public String toString() {
        return "Concert{" +
                "denumire='" + getDenumire() + '\'' +
                ", data='" + getData() + '\'' +
                ", artist='" + artist + '\'' +
                ", genMuzical='" + genMuzical + '\'' +
                '}';
    }
}
