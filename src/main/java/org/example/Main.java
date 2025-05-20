package org.example;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /**
     * TODO Werte Identifizieren
     * */
    private static final double MAXBODEN = 1;
    private static final double MINBODEN = 0.5;
    private static final double MAXLICHT = 0.7;
    private static final double MINLICHT = 0.5;
    private static final double MAXTEMP = 0.7;
    private static final double MINTEMP = 0.5;
    private static final int SLEEPTIME = 5000; //5000 Millisekunden = 5 Sekunden

    public static void main(String[] args) {
        Werte werte;

        while (true){
        try {
            Thread.sleep(SLEEPTIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        werte = leseSensorWerte();
        updateDB(prepareInsertWerteInDB(werte)); //nimmt die Werte, implementiert sie in einen Insert-Statement-String und führt aus
        verarbeiteWerte(werte);

       }

    }

    private static void verarbeiteWerte(Werte werte) {
        String sqlstmt;
        int id = getWertIdFromDB(werte);
        int counter = 0;
        if (getboden() < MINBODEN){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId, aktionId) VALUES (" +id+ ", " +2+ ", " +1+ ");";
            updateDB(sqlstmt);
            actionRedLED();
            counter++;
        } else if (getboden() > MAXBODEN){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId, aktionId) VALUES (" +id+ ", " +1+ ", " +1+ ");";
            updateDB(sqlstmt);
            actionRedLED();
            counter++;
        }

        if (getLicht() < MINLICHT){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId) VALUES (" +id+ ", " +4+ ");";
            updateDB(sqlstmt);
            counter++;
        } else if (getLicht() > MAXLICHT){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId) VALUES (" +id+ ", " +3+ ");";
            updateDB(sqlstmt);
            counter++;
        }

        if (getTemp() < MINTEMP){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId) VALUES (" +id+ ", " +6+ ");";
            updateDB(sqlstmt);
            counter++;
        } else if (getTemp() > MAXTEMP){
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId) VALUES (" +id+ ", " +5+ ");";
            updateDB(sqlstmt);
            counter++;
        }

        if (counter == 3){
            actionRedLED();
            sqlstmt = "UPDATE eventAktion SET aktionId = 1 WHERE werteId = " +id+";";
            updateDB(sqlstmt);
        }else if (counter > 0){
            actionYellowLED();
            sqlstmt = "UPDATE eventAktion SET aktionId = 6 WHERE werteId = " +id+";";
            updateDB(sqlstmt);
        }else if (counter == 0){
            actionGreenLED();
            sqlstmt = "INSERT INTO eventAktion (werteId, eventId, aktionId) VALUES (" +id+ ",7 ,2 );";
            updateDB(sqlstmt);
        }

        displayAusgabe();
    }

    /**
     * TODO
     * */
    private static void displayAusgabe() {
    }


    /**
     * TODO
     * */
    private static void actionGreenLED() {
    }

    /**
     * TODO
     * */
    private static void actionYellowLED() {
    }

    /**
     * TODO
     * */
    private static void actionRedLED() {
    }


    private static Werte leseSensorWerte(){
        return new Werte (getTemp(), getLicht(), getboden());
    }
    
    /**
     * TODO
     * */
    private static float getboden() {
        return 1;
    }

    /**
     * TODO
     * */
    private static float getLicht() {
        return 1;
    }

    /**
     * TODO
     * */
    private static float getTemp() {
        return 1;
    }

    public static String prepareInsertWerteInDB(Werte werte) {
        return "INSERT INTO werte (zeitpunkt, temperatur, licht, bodenfeuchte) VALUES " +
                    "('" +werte.getZeitpunkt()+ "', " +werte.getTemperatur()+ ", " +werte.getLicht()+ ", " +werte.getBodenfeuchte()+");";
    }


    public static void updateDB(String preparedString){
        String connectionUrl = "jdbc:sqlite:src/main/resources/digiplant.db";
        try (var conn = DriverManager.getConnection(connectionUrl)) {
            System.out.println("Connection to SQLite has been established.");
            conn.prepareStatement(preparedString)
                    .executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public static int getWertIdFromDB(Werte werte) {
        String preparedString = "SELECT id FROM werte WHERE zeitpunkt = ?";
        String connectionUrl = "jdbc:sqlite:src/main/resources/digiplant.db";

        try (var conn = DriverManager.getConnection(connectionUrl);
             var stmt = conn.prepareStatement(preparedString)) {

            stmt.setString(1, werte.getZeitpunkt());
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Kein Wert gefunden!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1; // Oder eine andere sinnvolle Fehlerbehandlung
        }
    }
}