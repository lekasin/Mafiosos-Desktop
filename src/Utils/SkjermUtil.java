package Utils;

import personer.Rolle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lars-erikkasin on 17.09.15.
 */
public class SkjermUtil {
    private static JTextArea info;
    private static JLabel tittel;
    private static String logg;

    public static void init(JTextArea info, JLabel tittel) {
        SkjermUtil.tittel = tittel;
        SkjermUtil.info = info;
    }

    public static void logg(String tekst){
        if (info.getText().length() > 1) {
            info.append("\n");
            logg += "\n";
        }
        logg += tekst;
        info.append(tekst);
    }

    public static void fullLogg(String tekst){
        info.setText("");
        logg(tekst);
    }

    public static void lastLogg(){
        fullLogg(logg);
    }

    public static void tittuler(String tekst){
        tittel.setText(tekst);
    }

    public static String hentTittel(){
        return tittel.getText();
    }

    public static void fargTittel(Color farge){
        tittel.setForeground(farge);
    }

    public static void setTittelFarge(Rolle r) {
        if (r == null) {
            fargTittel(Color.BLACK);
            return;
        }

        if (!r.funker() && !r.nyligKlonet())
            fargTittel(Color.RED);
        else if (r.skjerm() || r.informert() || r.nyligKlonet())
            fargTittel(Color.BLUE);
        else
            fargTittel(Color.BLACK);
    }

}
