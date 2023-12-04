package model.Tier;

public class Vogel extends Tier {

    private final double optimalerFreiflugRaum;

    public Vogel(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr,
                 Integer unterbringungNr, double optimalerFreiflugRaum)
    {
        super(tierNr, tierTyp, tierArt, kaufpreis, kostenTag, pflegerNr, unterbringungNr);
        this.optimalerFreiflugRaum = optimalerFreiflugRaum;
    }

    @Override
    public void datenAusgeben() {
        stammdatenAusgeben();
        System.out.println("\t\t| optimaler Freiflugraum: " + this.optimalerFreiflugRaum + " m³");
        System.out.println("\t\t| PflegerNr: " + this.pflegerNr);
        System.out.println("\t\t| UnterbringungNr: " + this.unterbringungNr);
    }

    public double getOptimalerFreiflugRaum() {
        return optimalerFreiflugRaum;
    }
}
