package model.Tier;

public class Fisch extends Tier {

    private final double phWert;
    private final String wasserArt;
    private final double wasserTemperatur;

    public Fisch(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr,
                 Integer unterbringungNr, double phWert, String wasserArt, double wasserTemperatur)
    {
        super(tierNr, tierTyp, tierArt, kaufpreis, kostenTag, pflegerNr, unterbringungNr);
        this.phWert = phWert;
        this.wasserArt = wasserArt;
        this.wasserTemperatur = wasserTemperatur;
    }

    @Override
    public void datenAusgeben() {
        stammdatenAusgeben();
        System.out.println("\t\t| phWert: " + this.phWert);
        System.out.println("\t\t| Wasserart: " + this.wasserArt);
        System.out.println("\t\t| Wassertemperatur: " + this.wasserTemperatur + " °");
        System.out.println("\t\t| PflegerNr: " + this.pflegerNr);
        System.out.println("\t\t| UnterbringungNr: " + this.unterbringungNr);
    }

    public double getPhWert() {
        return phWert;
    }

    public String getWasserArt() {
        return wasserArt;
    }

    public double getWasserTemperatur() {
        return wasserTemperatur;
    }
}
