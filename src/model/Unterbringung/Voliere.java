package model.Unterbringung;

public class Voliere extends Unterbringung {

    private final double freiflugRaumKubik;

    public Voliere(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche, double freiflugRaumKubik)
    {
        super(unterbringungNr, unterbringungTyp, fläche);
        this.freiflugRaumKubik = freiflugRaumKubik;
    }

    @Override
    public void datenAusgeben() {
        System.out.println("\t__________________________________________");
        stammdatenAusgeben();
        System.out.println("\tFreiflug Raum: " + this.freiflugRaumKubik + " m³");
        zugewieseneTiereAusgeben();
    }

    public double getFreiflugRaumKubik() {
        return freiflugRaumKubik;
    }
}
