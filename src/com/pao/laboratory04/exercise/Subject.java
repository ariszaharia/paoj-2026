package com.pao.laboratory03.exercise;

public enum Subject {
    PAOJ("PAOJ", 6),
    BD("BD", 5),
    SO("SO", 4),
    RC("RC", 4);

    private final String fullName;
    private final int credits;

    private Subject(String fullName, int credits) {
        this.fullName = fullName;
        this.credits = credits;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return name() + " (" + fullName + ", " + credits + " credite)";
    }
}
