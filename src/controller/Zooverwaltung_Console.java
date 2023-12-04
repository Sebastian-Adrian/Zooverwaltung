package controller;

import model.Tier.*;
import model.Unterbringung.*;
import model.dao.SqlDAO;
import model.dao.ZooDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Zooverwaltung_Console {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ZooDAO DAO = new SqlDAO();

    private static int getAuswahl() {
        System.out.println("=============");
        System.out.print("| Auswahl: ");
        int auswahl = SCANNER.nextInt();
        System.out.println("=============\n");
        return auswahl;
    }

    public static void main(String[] args) {

        HauptMenü:
        while (true) {
            System.out.println("""
                    
                    Hauptmenü:
                    ========================================================
                    1 - Tier hinzufügen
                    2 - Tier entfernen
                    3 - Tier Zuordnung setzen
                    4 - Tier Zuordnung löschen
                    5 - Tiere anzeigen
                    6 - Unterbringung hinzufügen
                    7 - Unterbringung entfernen
                    8 - Unterbringungen und deren zugeordnete Tiere anzeigen
                    9 - Programm beenden
                    --------------------------------------------------------
                    """);
            int auswahl = getAuswahl();

            switch (auswahl) {
                case 1 -> tierHinzufügen();
                case 2 -> tierEntfernen();
                case 3 -> tierZuordnungSetzen();
                case 4 -> tierZuordnungLöschen();
                case 5 -> tiereAnzeigen(); // TODO: Optional. Nicht Teil der Aufgabenstellung !
                case 6 -> unterbringungHinzufügen();
                case 7 -> unterbringungEntfernen();
                case 8 -> unterbringungenMitTierZuordnungAnzeigen();
                case 9 -> {
                    break HauptMenü;
                }
            }
        }
    }

    private static void tierEntfernen() {

        System.out.println("""
                \t Tier entfernen
                \t=============================
                """);
        System.out.print("\tTier-Nr: ");
        int tierNr = SCANNER.nextInt();

        DAO.deleteTier(tierNr);

    }

    private static void tierHinzufügen() {

        System.out.println("""
                \tTier hinzufügen Menü:
                \t=============================
                \t1 - Amphibie anlegen
                \t2 - Fisch anlegen
                \t3 - Reptil anlegen
                \t4 - Säugetier anlegen
                \t5 - Vogel anlegen
                \t0 - zurück
                \t-----------------------------
                """);
        int auswahl = getAuswahl();

        switch (auswahl) {

            // Amphibie
            case 1 -> {
                System.out.println("\t\tAmphibie anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\tLuftfeuchtigkeit: ");
                int luftFeuchtigkeit = SCANNER.nextInt();
                System.out.print("\t\tLufttemperatur: ");
                double luftTemperatur = SCANNER.nextDouble();

                DAO.insertTier(
                        new Amphibie(tierStammdaten.tierNr, TierTyp.Amphibie, tierStammdaten.tierArt,
                        tierStammdaten.kaufpreis, tierStammdaten.kostenTag,
                        tierStammdaten.pflegerNr == 0 ? null : tierStammdaten.pflegerNr,
                        tierStammdaten.unterbringungNr == 0 ? null : tierStammdaten.unterbringungNr,
                        luftFeuchtigkeit, luftTemperatur)
                );
            }

            // Fisch
            case 2 -> {
                System.out.println("\t\tFisch anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\tphWert: ");
                double phWert = SCANNER.nextDouble();
                System.out.print("\t\tWasserart (Süßwasser / Salzwasser): ");
                String wasserArt = SCANNER.next();
                System.out.println("\t\tWasser Temperatur (#.##): ");
                double wasserTemperatur = SCANNER.nextDouble();

                DAO.insertTier(
                        new Fisch(tierStammdaten.tierNr, TierTyp.Fisch, tierStammdaten.tierArt, tierStammdaten.kaufpreis,
                                tierStammdaten.kostenTag, tierStammdaten.pflegerNr == 0 ? null : tierStammdaten.pflegerNr,
                                tierStammdaten.unterbringungNr == 0 ? null : tierStammdaten.unterbringungNr, phWert,
                                wasserArt, wasserTemperatur)
                );
            }

            // Reptil
            case 3 -> {
                System.out.println("\t\tReptil anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\toptimaler UV-Index: ");
                double uvIndex = SCANNER.nextDouble();

                DAO.insertTier(
                        new Reptil(tierStammdaten.tierNr, TierTyp.Reptil, tierStammdaten.tierArt, tierStammdaten.kaufpreis,
                                tierStammdaten.kostenTag, tierStammdaten.pflegerNr == 0 ? null : tierStammdaten.pflegerNr,
                                tierStammdaten.unterbringungNr == 0 ? null : tierStammdaten.unterbringungNr, uvIndex)
                );
            }

            // Säugetier
            case 4 -> {
                System.out.println("\t\tSäugetier anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();

                DAO.insertTier(
                        new Säugetier(tierStammdaten.tierNr, TierTyp.Säugetier, tierStammdaten.tierArt, tierStammdaten.kaufpreis,
                                tierStammdaten.kostenTag, tierStammdaten.pflegerNr == 0 ? null : tierStammdaten.pflegerNr,
                                tierStammdaten.unterbringungNr == 0 ? null : tierStammdaten.unterbringungNr)
                );
            }

            // Vogel
            case 5 -> {
                System.out.println("\t\tVogel anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\toptimaler Freiflugraum: ");
                double freiFlugRaum = SCANNER.nextDouble();

                DAO.insertTier(
                        new Vogel(tierStammdaten.tierNr, TierTyp.Vogel, tierStammdaten.tierArt, tierStammdaten.kaufpreis,
                                tierStammdaten.kostenTag, tierStammdaten.pflegerNr == 0 ? null : tierStammdaten.pflegerNr,
                                tierStammdaten.unterbringungNr == 0 ? null : tierStammdaten.unterbringungNr, freiFlugRaum)
                );
            }
            default -> {}
        }
    }

    private static void tierZuordnungLöschen() {

        System.out.println("""
                \t Tier Zuordnung entfernen
                \t=============================
                """);
        System.out.print("\tTier-Nr: ");
        int tierNr = SCANNER.nextInt();

        DAO.removeZuordnungUnterbringungTier(tierNr);
    }

    private static void tierZuordnungSetzen() {

        System.out.println("""
                \t Tier Zuordnung setzen
                \t=============================
                """);
        System.out.print("\tTier-Nr: ");
        int tierNr = SCANNER.nextInt();
        System.out.print("\tUnterbringung-Nr: ");
        int unterbringungNr = SCANNER.nextInt();

        DAO.setZuordnungUnterbringungTier(tierNr, unterbringungNr);

    }

    private static void tiereAnzeigen() {

        List<Tier> tierList = DAO.getAllTiere();

        for (Tier tier : tierList) {
            System.out.println("\t\t--------------------------------");
            tier.datenAusgeben();
        }
    }

    private static void unterbringungEntfernen() {

        System.out.println("""
                \t Unterbringung entfernen
                \t=============================
                """);
        System.out.print("\tUnterbringung-Nr: ");
        int unterbringungNr = SCANNER.nextInt();

        DAO.deleteUnterbringung(unterbringungNr);
    }

    private static void unterbringungHinzufügen() {

        System.out.println("""
                \tUnterbringung hinzufügen Menü:
                \t==============================
                \t1 - Aquarium anlegen
                \t2 - Aquaterrarium anlegen
                \t3 - Gehege anlegen
                \t4 - Terrarium anlegen
                \t5 - Voliere anlegen
                \t0 - zurück
                \t------------------------------
                """);
        int auswahl = getAuswahl();

        switch (auswahl) {

            // Aquarium
            case 1 -> {
                System.out.println("\t\tAquarium anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                unterbringungStammdaten.eingabeUnterbringungStammdaten();
                System.out.print("\t\tWassermenge in m³: ");
                double wasserMenge = SCANNER.nextDouble();
                System.out.print("\t\tWasserart (Süßwasser / Salzwasser): ");
                String wasserArt = SCANNER.next();
                System.out.print("\t\tWassertemperatur: ");
                double wasserTemperatur = SCANNER.nextDouble();

                DAO.insertUnterbringung(
                        new Aquarium(unterbringungStammdaten.unterbringungNr, UnterbringungTyp.Aquarium,
                                unterbringungStammdaten.fläche, wasserMenge, wasserArt, wasserTemperatur)
                );
            }

            // Aquaterrarium
            case 2 -> {
                System.out.println("\t\tAquaterrarium anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                unterbringungStammdaten.eingabeUnterbringungStammdaten();
                System.out.print("\t\tAnteil Wasser in %: ");
                int anteilWasser = SCANNER.nextInt();

                DAO.insertUnterbringung(
                        new Aquaterrarium(unterbringungStammdaten.unterbringungNr, UnterbringungTyp.Aquaterrarium,
                                unterbringungStammdaten.fläche, anteilWasser)
                );
            }

            // Gehege
            case 3 -> {
                System.out.println("\t\tGehege anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\tEinfriedung Höhe: ");
                double einfriedungHöhe = SCANNER.nextDouble();
                System.out.print("\t\tAnzahl Schutzhütten: ");
                int anzahlSchutzhütten = SCANNER.nextInt();

                DAO.insertUnterbringung(
                        new Gehege(unterbringungStammdaten.unterbringungNr, UnterbringungTyp.Gehege,
                                unterbringungStammdaten.fläche, einfriedungHöhe, anzahlSchutzhütten)
                );
            }

            // Terrarium
            case 4 -> {
                System.out.println("\t\tTerrarium anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\tUV-Index: ");
                double uvIndex = SCANNER.nextDouble();

                DAO.insertUnterbringung(
                        new Terrarium(unterbringungStammdaten.unterbringungNr, UnterbringungTyp.Terrarium,
                                unterbringungStammdaten.fläche, uvIndex)
                );
            }

            // Voliere
            case 5 -> {
                System.out.println("\t\tVoliere anlegen:");
                System.out.println("\t\t~~~~~~~~~~~~~~~~~~~");
                tierStammdaten.eingabeTierStammdaten();
                System.out.print("\t\tFreiflugraum in m³: ");
                double freiFlugRaum = SCANNER.nextDouble();

                DAO.insertUnterbringung(
                        new Voliere(unterbringungStammdaten.unterbringungNr, UnterbringungTyp.Voliere,
                                unterbringungStammdaten.fläche, freiFlugRaum)
                );
            }
            default -> {}
        }

    }

    private static void unterbringungenMitTierZuordnungAnzeigen() {

        List<Unterbringung> unterbringungList = new ArrayList<>();

        try {
            unterbringungList = DAO.getAllUnterbringungenMitZugewiesenerTiere();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (unterbringungList != null) {
            for (Unterbringung unterbringung : unterbringungList) {
                unterbringung.datenAusgeben();
            }
        }
    }

    public static class tierStammdaten {

        public static int tierNr;
        public static String tierArt;
        public static double kaufpreis;
        public static double kostenTag;
        public static Integer pflegerNr;
        public static Integer unterbringungNr;

        public static void eingabeTierStammdaten() {

            System.out.print("\t\tTierNr: ");
            tierNr = SCANNER.nextInt();
            System.out.print("\t\tTierArt: ");
            tierArt = SCANNER.next();
            System.out.print("\t\tKaufpreis: ");
            kaufpreis = SCANNER.nextDouble();
            System.out.print("\t\tKosten/Tag: ");
            kostenTag = SCANNER.nextDouble();

            System.out.print("\t\tpflegerNr (0 = keine Zuweisung): ");
            pflegerNr = SCANNER.nextInt();
            System.out.print("\t\tunterbringungNr (0 = keine Zuweisung): ");
            unterbringungNr = SCANNER.nextInt();
        }

    }

    public static class unterbringungStammdaten {

        public static int unterbringungNr;
        public static double fläche;

        public static void eingabeUnterbringungStammdaten() {

            System.out.print("\t\tUnterbringungNr: ");
            unterbringungNr = SCANNER.nextInt();
            System.out.print("\t\tFläche im m²: ");
            fläche = SCANNER.nextDouble();
        }
    }

}
