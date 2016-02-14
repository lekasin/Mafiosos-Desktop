package Utils;

import gui.Oppstart;
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
    private static JPanel innhold;

    public static void init(JTextArea info, JLabel tittel) {
        SkjermUtil.tittel = tittel;
        SkjermUtil.info = info;
    }

    public static void oppdaterInnhold(JPanel innhold) {
        if (innhold == null)
            return;

        SkjermUtil.innhold = innhold;
        if (innhold.getComponentCount() > 0 && innhold.getComponent(0) instanceof JLabel)
            innhold.remove(0);
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

    public static void beskjedPåSkjerm(String tekst) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(innhold.getWidth(), 45));
        label.setFont(new Font("Arial", Font.BOLD, Oppstart.UNDERTITTEL));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setText(tekst);
        innhold.add(label, 0);
    }
}
