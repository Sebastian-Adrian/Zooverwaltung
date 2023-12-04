package model.Tier;

public class Säugetier extends Tier {

    public Säugetier(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr,
                     Integer unterbringungNr)
    {
        super(tierNr, tierTyp, tierArt, kaufpreis, kostenTag, pflegerNr, unterbringungNr);
    }

    @Override
    public void datenAusgeben() {
        stammdatenAusgeben();
        System.out.println("\t\t| PflegerNr: " + this.pflegerNr);
        System.out.println("\t\t| UnterbringungNr: " + this.unterbringungNr);
    }
}
