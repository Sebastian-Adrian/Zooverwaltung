package model.Unterbringung;

import model.Tier.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public abstract class Unterbringung {


    protected int unterbringungNr;
    protected UnterbringungTyp unterbringungTyp;
    protected double fläche;
    protected List<Tier> zugewieseneTiere;

    public Unterbringung(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche) {
        this.zugewieseneTiere = new ArrayList<>();
        this.unterbringungNr = unterbringungNr;
        this.unterbringungTyp = unterbringungTyp;
        this.fläche = fläche;
    }

    public void addTier(Tier tier) {
        this.zugewieseneTiere.add(tier);
    }

    public abstract void datenAusgeben();

    public double getFläche() {
        return fläche;
    }

    public int getUnterbringungNr() {
        return unterbringungNr;
    }

    public UnterbringungTyp getUnterbringungTyp()
    {
        return unterbringungTyp;
    }

    void stammdatenAusgeben() {
        System.out.println("•\tUnterbringungNr: " + this.unterbringungNr);
        System.out.println("\tTyp: " + this.unterbringungTyp.toString());
        System.out.println("\tFläche: " + this.fläche + " m²");
    }

    void zugewieseneTiereAusgeben() {
        if (! this.zugewieseneTiere.isEmpty()) {
            System.out.println("\tZugeordnete Tiere: ");
            int i = 1;
            for (Tier tier: this.zugewieseneTiere) {
                tier.datenAusgeben();
                if (i < this.zugewieseneTiere.size()) {
                    System.out.println("\t\t--------------------------------");
                }
                i++;
            }
        }
    }

}
