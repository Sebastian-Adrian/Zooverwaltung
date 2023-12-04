package model.dao;

import model.Mitarbeiter.Mitarbeiter;
import model.Tier.Tier;
import model.Unterbringung.Unterbringung;

import java.sql.SQLException;
import java.util.List;

public interface ZooDAO {

    void deleteMitarbeiter(int mitarbeiterNr);

    void deleteTier(int tierNr);

    void deleteUnterbringung(int unterbringungNr);

    void removeZuordnungUnterbringungTier(int tierNr);

    void setZuordnungUnterbringungTier(int tierNr, int unterbringungNr);

    List<Unterbringung> getAllUnterbringungenMitZugewiesenerTiere() throws SQLException;

    List<Tier> getAllTiere();

    void insertMitarbeiter(Mitarbeiter mitarbeiter);

    void insertTier(Tier tier);

    void insertUnterbringung(Unterbringung unterbringung);


}
