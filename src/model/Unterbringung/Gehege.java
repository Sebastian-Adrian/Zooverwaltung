package model.Unterbringung;

public class Gehege extends Unterbringung {

    private final double einfriedungHöhe;
    private final int schutzhüttenAnzahl;


    public Gehege(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche, double einfriedungHöhe, int schutzhüttenAnzahl) {
        super(unterbringungNr, unterbringungTyp, fläche);
        this.einfriedungHöhe = einfriedungHöhe;
        this.schutzhüttenAnzahl = schutzhüttenAnzahl;
    }

    @Override
    public void datenAusgeben() {
        System.out.println("\t__________________________________________");
        stammdatenAusgeben();
        System.out.println("\tEinfriedung Höhe: " + this.einfriedungHöhe + " m");
        System.out.println("\tAnzahl Schutzhütten: " + this.schutzhüttenAnzahl);
        zugewieseneTiereAusgeben();
    }

    public double getEinfriedungHöhe() {
        return einfriedungHöhe;
    }

    public int getSchutzhüttenAnzahl() {
        return schutzhüttenAnzahl;
    }

}
