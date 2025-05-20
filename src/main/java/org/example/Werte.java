package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Werte {
    private String zeitpunkt;
    private float temperatur;
    private float licht;
    private float bodenfeuchte;

    public Werte(float temperatur, float licht, float bodenfeuchte) {
        this.zeitpunkt = getTimeStamp();
        this.temperatur = temperatur;
        this.licht = licht;
        this.bodenfeuchte = bodenfeuchte;
    }

    public String getZeitpunkt() {
        return zeitpunkt;
    }

    public float getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(float temperatur) {
        this.temperatur = temperatur;
    }

    public float getLicht() {
        return licht;
    }

    public void setLicht(float licht) {
        this.licht = licht;
    }

    public float getBodenfeuchte() {
        return bodenfeuchte;
    }

    public void setBodenfeuchte(float bodenfeuchte) {
        this.bodenfeuchte = bodenfeuchte;
    }

    /**
     *TODO prüfen
     * */
    private String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY-h-mm-ss");
        return sdf.format(date);
    }
}
