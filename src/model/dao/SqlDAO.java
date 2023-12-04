package model.dao;

import model.Mitarbeiter.Mitarbeiter;
import model.Mitarbeiter.Pfleger;
import model.Tier.*;
import model.Unterbringung.*;

import java.sql.*;
import java.util.*;

public class SqlDAO implements ZooDAO {

    private Connection sqlConnection = null;

    public SqlDAO() {
        try {
            String SQL_DRIVER = "com.mysql.cj.jdbc.Driver";
            Class.forName(SQL_DRIVER);

            openConnection();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            if (sqlConnection != null) {
                sqlConnection.close();
            }
        } catch (SQLException e) {
            System.err.println("Verbindung konnte nicht geschlossen werden.");
            System.err.println(e.getMessage());
        }
        sqlConnection = null;
    }

    private void commitStatement(Statement statement) throws SQLException {
        statement.close();
        sqlConnection.commit();
    }

    private void commitStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
        sqlConnection.commit();
    }

    @Override
    public void deleteMitarbeiter(int mitarbeiterNr) {
        //TODO: Optional
    }

    @Override
    public void deleteTier(int tierNr) {

        Tier tier = getTier(tierNr);

        if (tier != null) {
            TierTyp tierTyp = tier.getTierTyp();

            String sql = "DELETE T.*, A.* FROM ? AS A " +
                         "LEFT JOIN " +
                         "Tier AS T ON T.TierNr = A.TierNr " +
                         "WHERE " +
                         "T.TierNr = ?";
            try {
                // SQL-Injection nicht möglich, da Typ aus Enum. Die TierNummer ist maskiert.
                sql = sql.replaceFirst("\\?", tierTyp.name());
                PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
                preparedStatement.setInt(1, tierNr);
                preparedStatement.executeUpdate();
                commitStatement(preparedStatement);

                System.out.println("\tTier entfernt!\n");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("\tKein Tier mit dieser Nummer gefunden!\n");
        }
    }

    @Override
    public void deleteUnterbringung(int unterbringungNr) {

        String sql = "SELECT * FROM Tier " +
                     "WHERE UnterbringungNr = ? " +
                     "LIMIT 1;";

        UnterbringungTyp unterbringungTyp = null;

        try {
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, unterbringungNr);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                sql = "SELECT UnterbringungTyp FROM Unterbringung AS U " +
                      "WHERE U.UnterbringungNr = ?;";
                preparedStatement = sqlConnection.prepareStatement(sql);
                preparedStatement.setInt(1, unterbringungNr);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.isBeforeFirst()) {
                    resultSet.next();
                    String uTyp = resultSet.getString("UnterbringungTyp");

                    switch (uTyp) {
                        case "Aquarium" -> unterbringungTyp = UnterbringungTyp.Aquarium;
                        case "Aquaterrarium" -> unterbringungTyp = UnterbringungTyp.Aquaterrarium;
                        case "Gehege" -> unterbringungTyp = UnterbringungTyp.Gehege;
                        case "Terrarium" -> unterbringungTyp = UnterbringungTyp.Terrarium;
                        case "Voliere" -> unterbringungTyp = UnterbringungTyp.Voliere;
                    }

                    sql = "DELETE U.*, A.* FROM ? AS A " +
                          "LEFT JOIN " +
                          "Unterbringung AS U ON U.UnterbringungNr = A.UnterbringungNr " +
                          "WHERE " +
                          "U.UnterbringungNr = ?";
                    try {
                        if (unterbringungTyp != null) {
                            // SQL-Injection nicht möglich, da Typ aus Enum. Die UnterbringungNr ist maskiert.
                            sql = sql.replaceFirst("\\?", unterbringungTyp.name());
                            preparedStatement = sqlConnection.prepareStatement(sql);
                            preparedStatement.setInt(1, unterbringungNr);
                            preparedStatement.executeUpdate();
                            commitStatement(preparedStatement);
                        }
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } else {
                // Wenn noch Tiere vorhanden sind, hat Anwender höchstwahrscheinlich einen Fehler begangen.
                System.err.println("Unterbringung kann nicht gelöscht werden. Es gibt noch zugewiesene Tiere!");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

    private void executeQuerys(List<String> insertQuery, Statement statement) throws SQLException {

        for (String cmd : insertQuery) {
            statement.executeUpdate(cmd);
        }
        commitStatement(statement);
    }

    @Override
    public List<Unterbringung> getAllUnterbringungenMitZugewiesenerTiere() throws SQLException {

        List<Unterbringung> unterbringungList = new ArrayList<>();

        Statement statement = sqlConnection.createStatement();
        String sql = "SELECT " +
                     "    *, GROUP_CONCAT(TierNr) AS 'zugewiesene Tiere' " +
                     "FROM " +
                     "    Unterbringung AS U " +
                     "        LEFT JOIN " +
                     "    Tier ON U.UnterbringungNr = Tier.UnterbringungNr " +
                     "        LEFT JOIN " +
                     "    Gehege AS G ON U.UnterbringungNr = G.UnterbringungNr " +
                     "        LEFT JOIN " +
                     "    Voliere AS V ON U.UnterbringungNr = V.UnterbringungNr " +
                     "        LEFT JOIN " +
                     "    Aquarium AS A ON U.UnterbringungNr = A.UnterbringungNr " +
                     "        LEFT JOIN " +
                     "    Terrarium AS T ON U.UnterbringungNr = T.UnterbringungNr " +
                     "        LEFT JOIN " +
                     "    Aquaterrarium AS AQ ON U.UnterbringungNr = AQ.UnterbringungNr " +
                     "GROUP BY U.UnterbringungNr";

        ResultSet resultSet = statement.executeQuery(sql);
        HashMap<Integer, List<Tier>> tierList = getTiereMitZugewiesenerUnterbringung();

        while (resultSet.next()) {

            String unterbringungTyp = resultSet.getString("UnterbringungTyp");
            Integer unterbringungNr = resultSet.getInt("UnterbringungNr");
            double fläche = resultSet.getDouble("Fläche");

            switch (unterbringungTyp) {
                case "Gehege" -> {
                    double einfriedungHöhe = resultSet.getDouble("EinfriedungHöhe");
                    int anzahlSchutzhütten = resultSet.getInt("SchutzhüttenAnzahl");
                    Gehege gehege = new Gehege(unterbringungNr, UnterbringungTyp.Gehege, fläche, einfriedungHöhe,
                            anzahlSchutzhütten);
                    if (tierList.containsKey(unterbringungNr)) {
                        for (Tier tier : tierList.get(unterbringungNr)) {
                            gehege.addTier(tier);
                        }
                    }
                    unterbringungList.add(gehege);
                }
                case "Voliere" -> {
                    double freiflugRaum = resultSet.getDouble("FreiflugRaum");
                    Voliere voliere = new Voliere(unterbringungNr, UnterbringungTyp.Voliere, fläche, freiflugRaum);
                    if (tierList.containsKey(unterbringungNr)) {
                        for (Tier tier : tierList.get(unterbringungNr)) {
                            voliere.addTier(tier);
                        }
                    }
                    unterbringungList.add(voliere);
                }
                case "Aquarium" -> {
                    double wasserTemperatur = resultSet.getDouble("WasserTemperatur");
                    double wasserMenge = resultSet.getDouble("WasserMenge");
                    String wasserArt = resultSet.getString("WasserArt");
                    Aquarium aquarium = new Aquarium(unterbringungNr, UnterbringungTyp.Aquarium, fläche, wasserMenge,
                            wasserArt, wasserTemperatur);
                    if (tierList.containsKey(unterbringungNr)) {
                        for (Tier tier : tierList.get(unterbringungNr)) {
                            aquarium.addTier(tier);
                        }
                    }
                    unterbringungList.add(aquarium);
                }
                case "Aquaterrarium" -> {
                    int anteilWasser = resultSet.getInt("AnteilWasser");
                    Aquaterrarium aquaterrarium = new Aquaterrarium(unterbringungNr, UnterbringungTyp.Aquaterrarium,
                            fläche, anteilWasser);
                    if (tierList.containsKey(unterbringungNr)) {
                        for (Tier tier : tierList.get(unterbringungNr)) {
                            aquaterrarium.addTier(tier);
                        }
                    }
                    unterbringungList.add(aquaterrarium);
                }
                case "Terrarium" -> {
                    double uvIndex = resultSet.getDouble("UVIndex");
                    Terrarium terrarium = new Terrarium(unterbringungNr, UnterbringungTyp.Terrarium, fläche, uvIndex);
                    if (tierList.containsKey(unterbringungNr)) {
                        for (Tier tier : tierList.get(unterbringungNr)) {
                            terrarium.addTier(tier);
                        }
                    }
                    unterbringungList.add(terrarium);
                }
            }
        }
        return unterbringungList;
    }

    @Override
    public List<Tier> getAllTiere() {

        List<Tier> tierList = new ArrayList<>();
        String sql = "SELECT " +
                        "* " +
                     "FROM " +
                        "Tier AS T " +
                            "LEFT JOIN " +
                        "Amphibie AS A ON T.TierNr = A.TierNr " +
                            "LEFT JOIN " +
                        "Fisch AS F ON T.TierNr = F.TierNr " +
                            "LEFT JOIN " +
                        "Reptil AS R ON T.TierNr = R.TierNr " +
                            "LEFT JOIN " +
                        "Säugetier AS S ON T.TierNr = S.TierNr " +
                            "LEFT JOIN " +
                        "Vogel AS V ON T.TierNr = V.TierNr;";

        try {
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Tier tier = loadTier(resultSet);
                if (tier != null) {
                    tierList.add(tier);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tierList;

    }

    private Tier getTier(int tierNr) {

        TierTyp tierTyp = getTierTyp(tierNr);

        if (tierTyp != null) {
            try {
                String sql = "SELECT * FROM Tier AS T " +
                             "LEFT JOIN " +
                             "? AS type ON T.TierNr = type.TierNr " +
                             "WHERE " +
                             "T.TierNr = ?";

                // SQL-Injection nicht möglich, da Typ aus Enum. Die TierNr ist maskiert.
                sql = sql.replaceFirst("\\?", tierTyp.name());
                PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
                preparedStatement.setInt(1, tierNr);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.isBeforeFirst()) {
                    resultSet.next();
                    return loadTier(resultSet);
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return null;

    }

    private TierTyp getTierTyp(int tierNr) {

        String sql = "SELECT TierTyp FROM Tier AS T WHERE T.TierNr = ?";

        try {

            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, tierNr);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                resultSet.next();

                String type = resultSet.getString("TierTyp");

                switch (type) {
                    case "Amphibie":
                        return TierTyp.Amphibie;
                    case "Fisch":
                        return TierTyp.Fisch;
                    case "Reptil":
                        return TierTyp.Reptil;
                    case "Säugetier":
                        return TierTyp.Säugetier;
                    case "Vogel":
                        return TierTyp.Vogel;
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private HashMap<Integer, List<Tier>> getTiereMitZugewiesenerUnterbringung() {

        // Pro UnterbringungNr eine Liste der Tiere
        HashMap<Integer, List<Tier>> tierList = new HashMap<>();

        String sql = "SELECT " +
                     "    * " +
                     "FROM " +
                        "Tier AS T " +
                            "LEFT JOIN " +
                        "Amphibie AS A ON T.TierNr = A.TierNr " +
                            "LEFT JOIN " +
                        "Fisch AS F ON T.TierNr = F.TierNr " +
                            "LEFT JOIN " +
                        "Reptil AS R ON T.TierNr = R.TierNr " +
                            "LEFT JOIN " +
                        "Säugetier AS S ON T.TierNr = S.TierNr " +
                            "LEFT JOIN " +
                        "Vogel AS V ON T.TierNr = V.TierNr;";

        try {
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Tier tier = loadTier(resultSet);

                // Tier hat zugeordnete Unterbringung
                if (tier != null && tier.getUnterbringungNr() != null) {
                    List<Tier> list = tierList.computeIfAbsent(tier.getUnterbringungNr(), k -> new ArrayList<>());
                    list.add(tier);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return tierList;
    }

    private void insertIntoAmphibie(Amphibie amphibie, Statement statement, List<String> tierQuery) throws SQLException {

            tierQuery.add(String.format(Locale.ROOT,
                    "INSERT INTO Amphibie (TierNr, LuftFeuchtigkeit, LuftTemperatur) VALUES (%d, %d, %f)",
                    amphibie.getTierNr(), amphibie.getLuftFeuchtigkeit(), amphibie.getLuftTemperatur()
            ));
            executeQuerys(tierQuery, statement);
    }

    private void insertIntoAquarium(Aquarium aquarium, Statement statement, List<String> insertQuery) throws SQLException {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Aquarium (UnterbringungNr, WasserTemperatur, WasserArt, WasserMenge) " +
                "VALUES (%d, %f, '%s', %f)",
                aquarium.getUnterbringungNr(), aquarium.getWasserTemperatur(),
                aquarium.getWasserArt(), aquarium.getWasserMengeKubik()));
        executeQuerys(insertQuery, statement);
    }

    private void insertIntoAquaterrarium(Aquaterrarium aquaterrarium, Statement statement, List<String> insertQuery) throws SQLException {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Aquaterrarium (UnterbringungNr, AnteilWasser) VALUES (%d, %d)",
                aquaterrarium.getUnterbringungNr(), aquaterrarium.getAnteilWasser()
        ));
        executeQuerys(insertQuery, statement);
    }

    private void insertIntoFisch(Fisch fisch, Statement statement, List<String> tierQuery) throws SQLException {

        tierQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Fisch (TierNr, phWert, WasserArt, WasserTemperatur) VALUES (%d, %.1f, '%s', %.1f)",
                fisch.getTierNr(), fisch.getPhWert(), fisch.getWasserArt(), fisch.getWasserTemperatur()
        ));
        executeQuerys(tierQuery, statement);
    }

    private void insertIntoGehege(Gehege gehege, Statement statement, List<String> insertQuery) throws SQLException {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Gehege (UnterbringungNr, EinfriedungHöhe, SchutzhüttenAnzahl) VALUES (%d, %f, %d)",
                gehege.getUnterbringungNr(), gehege.getEinfriedungHöhe(), gehege.getSchutzhüttenAnzahl()
        ));
        executeQuerys(insertQuery, statement);
    }

    private void insertIntoMitarbeiter(Mitarbeiter mitarbeiter, List<String> mitarbeiterQuery) {

        mitarbeiterQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Mitarbeiter (MitarbeiterNr, vorname, name) VALUES (%d, '%s', '%s')",
                mitarbeiter.getMitarbeiterNr(), mitarbeiter.getVorname(), mitarbeiter.getNachname()
        ));
    }

    @Override
    public void insertMitarbeiter(Mitarbeiter mitarbeiter) {

        List<String> mitarbeiterQuery = new ArrayList<>();
        Savepoint savepointMitarbeiter = null;

        try {
            savepointMitarbeiter = sqlConnection.setSavepoint();
            Statement statement = sqlConnection.createStatement();

            insertIntoMitarbeiter(mitarbeiter, mitarbeiterQuery);

            if (mitarbeiter instanceof Pfleger pfleger) {
                insertIntoPfleger(pfleger, statement, mitarbeiterQuery);
            }
        } catch (SQLException e) {
            if (savepointMitarbeiter != null) {
                System.err.println(e.getMessage());
                rollbackQuery(savepointMitarbeiter);
            }
        }
    }

    private void insertIntoPfleger(Pfleger pfleger, Statement statement, List<String> mitarbeiterQuery) throws SQLException {

        mitarbeiterQuery.add(String.format(Locale.ROOT,
                "INSERT INTO pfleger (MitarbeiterNr, wochenStunden, stundenLohn) VALUES (%d, %.2f, %.2f)",
                pfleger.getMitarbeiterNr(), pfleger.getWochenStunden(), pfleger.getStundenLohn()
        ));
        executeQuerys(mitarbeiterQuery, statement);
    }

    private void insertIntoReptil(Reptil reptil, Statement statement, List<String> tierQuery) throws SQLException {

        tierQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Reptil (TierNr, UVIndex) VALUES (%d, %.1f)",
                reptil.getTierNr(), reptil.getUvIndex()
        ));
        executeQuerys(tierQuery, statement);
    }

    private void insertIntoSäugetier(Säugetier säugetier, Statement statement, List<String> tierQuery) throws SQLException {

        tierQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Säugetier (TierNr) VALUES (%d)",
                säugetier.getTierNr()));
        executeQuerys(tierQuery, statement);
    }

    private void insertIntoTerrarium(Terrarium terrarium, Statement statement, List<String> insertQuery) throws SQLException {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Terrarium (UnterbringungNr, UVIndex) VALUES (%d, %f)",
                terrarium.getUnterbringungNr(), terrarium.getUvIndex()
        ));
        executeQuerys(insertQuery, statement);
    }

    private void insertIntoTier(Tier tier, List<String> tierQuery) {

        tierQuery.add(String.format(Locale.ROOT,
                "INSERT INTO tier (TierNr, TierTyp, TierArt, Kaufpreis, KostenTag, PflegerNr, UnterbringungNr) VALUES " +
                "(%d, '%s', '%s', %.2f, %.2f, %d, %d)", tier.getTierNr(), tier.getTierTyp().name(), tier.getTierArt(),
                tier.getKaufpreis(), tier.getKostenTag(), tier.getPflegerNr(), tier.getUnterbringungNr()
        ));
    }

    @Override
    public void insertTier(Tier tier) {

        Savepoint savepointTier = null;
        List<String> tierQuery = new ArrayList<>();

        try {
            savepointTier = sqlConnection.setSavepoint();
            Statement statement = sqlConnection.createStatement();

            insertIntoTier(tier, tierQuery);

            if (tier instanceof Amphibie amphibie) {
                insertIntoAmphibie(amphibie, statement, tierQuery);
            } else if (tier instanceof Fisch fisch) {
                insertIntoFisch(fisch, statement, tierQuery);
            } else if (tier instanceof Reptil reptil) {
                insertIntoReptil(reptil, statement, tierQuery);
            } else if (tier instanceof Säugetier säugetier) {
                insertIntoSäugetier(säugetier, statement, tierQuery);
            } else if (tier instanceof Vogel vogel) {
                insertIntoVogel(vogel, statement, tierQuery);
            }

        } catch (SQLException e) {
            if (savepointTier != null) {
                System.err.println(e.getMessage());
                rollbackQuery(savepointTier);
            }
        }
    }

    private void insertIntoUnterbringung(Unterbringung unterbringung, List<String> insertQuery) {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Unterbringung (UnterbringungNr, UnterbringungTyp, Fläche) VALUES (%d, '%s', %.2f)",
                unterbringung.getUnterbringungNr(), unterbringung.getUnterbringungTyp().name(), unterbringung.getFläche()
        ));
    }

    @Override
    public void insertUnterbringung(Unterbringung unterbringung) {

        Savepoint savepointUnterbringung = null;
        List<String> insertQuery = new ArrayList<>();

        try {
            savepointUnterbringung = sqlConnection.setSavepoint();
            Statement statement = sqlConnection.createStatement();

            insertIntoUnterbringung(unterbringung, insertQuery);

            if (unterbringung instanceof Gehege gehege) {
                insertIntoGehege(gehege, statement, insertQuery);
            } else if (unterbringung instanceof Aquarium aquarium) {
                insertIntoAquarium(aquarium, statement, insertQuery);
            } else if (unterbringung instanceof Aquaterrarium aquaterrarium) {
                insertIntoAquaterrarium(aquaterrarium, statement, insertQuery);
            } else if (unterbringung instanceof Terrarium terrarium) {
                insertIntoTerrarium(terrarium, statement, insertQuery);
            } else if (unterbringung instanceof Voliere voliere) {
                insertIntoVoliere(voliere, statement, insertQuery);
            }

        } catch (SQLException e) {
            if (savepointUnterbringung != null) {
                System.err.println(e.getMessage());
                rollbackQuery(savepointUnterbringung);
            }
        }
    }

    private void insertIntoVogel(Vogel vogel, Statement statement, List<String> tierQuery) throws SQLException {

        tierQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Vogel (TierNr, FreiflugRaum) VALUES (%d, %.2f)",
                vogel.getTierNr(), vogel.getOptimalerFreiflugRaum()));
        executeQuerys(tierQuery, statement);
    }

    private void insertIntoVoliere(Voliere voliere, Statement statement, List<String> insertQuery) throws SQLException {

        insertQuery.add(String.format(Locale.ROOT,
                "INSERT INTO Voliere (UnterbringungNr, FreiflugRaum) VALUES (%d, %.2f)",
                voliere.getUnterbringungNr(), voliere.getFreiflugRaumKubik()
                ));
        executeQuerys(insertQuery, statement);
    }

    private Tier loadTier(ResultSet resultSet) throws SQLException {

        Tier tier = null;

        int tierNr = resultSet.getInt("TierNr");
        String tierTyp = resultSet.getString("TierTyp");
        String tierArt = resultSet.getString("TierArt");
        double kaufPreis = resultSet.getDouble("Kaufpreis");
        double kostenTag = resultSet.getDouble("KostenTag");
        Integer pflegerNr = resultSet.getInt("PflegerNr");
        Integer unterbringungNr = resultSet.getInt("UnterbringungNr");

        switch (tierTyp) {
            case "Amphibie" -> {
                int luftFeuchtigkeit = resultSet.getInt("LuftFeuchtigkeit");
                double luftTemperatur = resultSet.getDouble("LuftTemperatur");
                tier = new Amphibie(tierNr, TierTyp.Amphibie, tierArt, kaufPreis, kostenTag,
                        pflegerNr, unterbringungNr, luftFeuchtigkeit, luftTemperatur);
            }
            case "Fisch" -> {
                double phWert = resultSet.getDouble("phWert");
                String wassertArt = resultSet.getString("WasserArt");
                double wasserTemperatur = resultSet.getDouble("WasserTemperatur");
                tier = new Fisch(tierNr, TierTyp.Fisch, tierArt, kaufPreis, kostenTag, pflegerNr,
                        unterbringungNr, phWert, wassertArt, wasserTemperatur);
            }
            case "Reptil" -> {
                double uvIndex = resultSet.getDouble("UVIndex");
                tier = new Reptil(tierNr, TierTyp.Reptil, tierArt, kaufPreis, kostenTag, pflegerNr,
                        unterbringungNr, uvIndex);
            }
            case "Säugetier" -> tier = new Säugetier(tierNr, TierTyp.Säugetier, tierArt, kaufPreis, kostenTag, pflegerNr,
                    unterbringungNr);
            case "Vogel" -> {
                double freiFlugRaum = resultSet.getDouble("FreiflugRaum");
                tier = new Vogel(tierNr, TierTyp.Vogel, tierArt, kaufPreis, kostenTag, pflegerNr,
                        unterbringungNr, freiFlugRaum);
            }
        }
        return tier;
    }

    private void openConnection() {

        try {
            closeConnection();
            String SQL_SERVER = "127.0.0.1";
            String SQL_SERVER_PORT = "3306";
            String SQL_USER = "java";
            String SQL_PASSWORD = "21252002WI";
            String SQL_DATABASE = "db_zooverwaltung";

            sqlConnection = DriverManager.getConnection(
                    "jdbc:mysql://" + SQL_SERVER + ":" + SQL_SERVER_PORT + "/" + SQL_DATABASE,
                    SQL_USER, SQL_PASSWORD);
            sqlConnection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Problem beim Aufbau der Datenbankverbindung:");
        }
    }

    @Override
    public void removeZuordnungUnterbringungTier(int tierNr) {

        String sql = "UPDATE Tier " +
                     "SET " +
                        "UnterbringungNr = NULL " +
                     "WHERE " +
                        "TierNr = ?";
        try {
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, tierNr);
            preparedStatement.executeUpdate();
            commitStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private void rollbackQuery(Savepoint savepoint) {

        if (savepoint != null) {
            try {
                sqlConnection.rollback(savepoint);
                System.err.println("Rollback durchgeführt");
            } catch (SQLException e) {
                System.err.println("Fehler beim Rollback:");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setZuordnungUnterbringungTier(int tierNr, int unterbringungNr) {

        String sql = "UPDATE Tier " +
                     "SET UnterbringungNr = ? " +
                     "WHERE TierNr = ?;";

        try {
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            preparedStatement.setInt(1, unterbringungNr);
            preparedStatement.setInt(2, tierNr);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
