package model.Tier;

public class Amphibie extends Tier {

    private final int luftFeuchtigkeit;
    private final double luftTemperatur;

    public Amphibie(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr,
                    Integer unterbringungNr, int luftFeuchtigkeit, double luftTemperatur)
    {
        super(tierNr, tierTyp, tierArt, kaufpreis, kostenTag, pflegerNr, unterbringungNr);
        this.luftFeuchtigkeit = luftFeuchtigkeit;
        this.luftTemperatur = luftTemperatur;
    }

    @Override
    public void datenAusgeben() {
        stammdatenAusgeben();
        System.out.println("\t\t| Luftfeuchtigkeit: " + this.luftFeuchtigkeit + " %");
        System.out.println("\t\t| Luft Temperatur: " + this.luftTemperatur + " °");
        System.out.println("\t\t| PflegerNr: " + this.pflegerNr);
        System.out.println("\t\t| UnterbringungNr: " + this.unterbringungNr);
    }



    public int getLuftFeuchtigkeit() {
        return luftFeuchtigkeit;
    }

    public double getLuftTemperatur() {
        return luftTemperatur;
    }
}
