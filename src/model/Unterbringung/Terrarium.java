package model.Unterbringung;

public class Terrarium extends Unterbringung {

    private final double uvIndex;

    public Terrarium(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche, double uvIndex) {
        super(unterbringungNr, unterbringungTyp, fläche);
        this.uvIndex = uvIndex;
    }

    @Override
    public void datenAusgeben() {
        System.out.println("\t__________________________________________");
        stammdatenAusgeben();
        System.out.println("\tUV-Index: " + this.uvIndex);
        zugewieseneTiereAusgeben();
    }

    public double getUvIndex() {
        return uvIndex;
    }


}

