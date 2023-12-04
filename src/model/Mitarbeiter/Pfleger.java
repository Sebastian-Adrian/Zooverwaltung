package model.Mitarbeiter;

public class Pfleger extends Mitarbeiter {

    private final double wochenStunden;
    private final double stundenLohn;

    public Pfleger(int mitarbeiterNr, String vorname, String nachname, double wochenStunden, double stundenLohn) {
        super(mitarbeiterNr, vorname, nachname);
        this.wochenStunden = wochenStunden;
        this.stundenLohn = stundenLohn;
    }

    public double getStundenLohn() {
        return stundenLohn;
    }

    public double getWochenStunden() {
        return wochenStunden;
    }
}
