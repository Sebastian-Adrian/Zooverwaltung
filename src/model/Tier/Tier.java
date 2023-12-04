package model.Tier;

public abstract class Tier {

    protected final int tierNr;
    protected final TierTyp tierTyp;
    protected final String tierArt;
    protected final double kaufpreis;
    protected final double kostenTag;
    protected final Integer pflegerNr;
    protected final Integer unterbringungNr;

    public Tier(int tierNr, TierTyp tierTyp, String tierArt, double kaufpreis, double kostenTag, Integer pflegerNr, Integer unterbringungNr) {
        this.tierNr = tierNr;
        this.tierTyp = tierTyp;
        this.tierArt = tierArt;
        this.kaufpreis = kaufpreis;
        this.kostenTag = kostenTag;
        this.pflegerNr = pflegerNr;
        this.unterbringungNr = unterbringungNr;
    }

    public abstract void datenAusgeben();

    public double getKaufpreis() {
        return kaufpreis;
    }

    public double getKostenTag() {
        return kostenTag;
    }

    public Integer getPflegerNr() {
        return pflegerNr;
    }

    public String getTierArt() {
        return tierArt;
    }

    public int getTierNr() {
        return tierNr;
    }

    public TierTyp getTierTyp() {
        return tierTyp;
    }

    public Integer getUnterbringungNr() {
        return unterbringungNr;
    }

    void stammdatenAusgeben() {
        System.out.println("\t\t| TierNr: " + this.tierNr);
        System.out.println("\t\t| TierTyp: " + this.tierTyp);
        System.out.println("\t\t| TierArt: " + this.tierArt);
        System.out.println("\t\t| Kaufpreis: " + this.kaufpreis);
        System.out.println("\t\t| Kosten/Tag: " + this.kostenTag);
    }
}
