package model.Tier;

public class Reptil extends Tier {

    private final double uvIndex;

    public Reptil(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr,
                  Integer unterbringungNr, double uvIndex)
    {
        super(tierNr, tierTyp, tierArt, kaufpreis, kostenTag, pflegerNr, unterbringungNr);
        this.uvIndex = uvIndex;
    }

    @Override
    public void datenAusgeben() {
        stammdatenAusgeben();
        System.out.println("\t\t| UV-Index: " + this.uvIndex);
        System.out.println("\t\t| PflegerNr: " + this.pflegerNr);
        System.out.println("\t\t| UnterbringungNr: " + this.unterbringungNr);
    }

    public double getUvIndex() {
        return uvIndex;
    }
}
