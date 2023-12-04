package model.Unterbringung;

public class Aquaterrarium extends Unterbringung{

    private final int prozentAnteilWasser;

    public Aquaterrarium(int unterbringungNr, UnterbringungTyp unterbringungTyp, double fläche, int prozentAnteilWasser)
    {
        super(unterbringungNr, unterbringungTyp, fläche);
        this.prozentAnteilWasser = prozentAnteilWasser;
    }

    @Override
    public void datenAusgeben() {
        System.out.println("\t__________________________________________");
        stammdatenAusgeben();
        System.out.println("\tAnteil Wasser: " + this.prozentAnteilWasser + " %");
        zugewieseneTiereAusgeben();
    }

    public int getAnteilWasser() {
        return prozentAnteilWasser;
    }

}
