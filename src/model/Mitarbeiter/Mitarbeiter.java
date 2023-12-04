package model.Mitarbeiter;

public abstract class Mitarbeiter {

    protected final int mitarbeiterNr;
    protected final String vorname;
    protected final String nachname;
    // TODO: protected final Date geburtsDatum;

    public int getMitarbeiterNr() {
        return mitarbeiterNr;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }


    public Mitarbeiter(int mitarbeiterNr, String vorname, String nachname) {
        this.mitarbeiterNr = mitarbeiterNr;
        this.vorname = vorname;
        this.nachname = nachname;
    }
}
