package model.Unterbringung;

public class Aquarium extends Unterbringung {

    private final double wasserMengeKubik;
    private final String wasserArt;
    private final double wasserTemperatur;

    public Aquarium(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche, double wasserMengeKubik,
                    String wasserArt, double wasserTemperatur)
    {
        super(unterbringungNr, unterbringungTyp, fläche);
        this.wasserMengeKubik = wasserMengeKubik;
        this.wasserArt = wasserArt;
        this.wasserTemperatur = wasserTemperatur;

    }

    @Override
    public void datenAusgeben() {
        System.out.println("\t__________________________________________");
        stammdatenAusgeben();
        System.out.println("\tWassermenge: " + this.wasserMengeKubik + " m³");
        System.out.println("\tWasserart: " + this.wasserArt);
        System.out.println("\tWassertemperatur: " + this.wasserTemperatur + " °");
        zugewieseneTiereAusgeben();
    }



    public String getWasserArt() {
        return wasserArt;
    }

    public double getWasserMengeKubik() {
        return wasserMengeKubik;
    }

    public double getWasserTemperatur() {
        return wasserTemperatur;
    }

}
