package Utils;

import datastruktur.Spillerliste;
import gui.Oppstart;
import gui.Spill;
import personer.Spiller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lars-erikkasin on 25.09.15.
 */
public class InnstillingsUtil {
    private static Oppstart oppstart;

    public static void setOppstart(Oppstart o) {
        oppstart = o;
    }

    public static void endreSpillerNavn(Spiller spiller){
        String input = JOptionPane.showInputDialog("Hva er " + spiller + "s nye navn?");
        spiller.setNavn(input);
    }

    public static void promptFortellerInfo(){
        String input = JOptionPane
                .showInputDialog("Fortellers telefonnummer:");
        if (input != null && input.matches("\\d{8}"))
            visFortellerInfo(Integer.parseInt(input));
        else
            JOptionPane.showMessageDialog(null,
                    "Telefonnummer må inneholde 8 siffer!");
    }

    public static void visFortellerInfo(int tlf) {
        JFrame fortellervindu = new JFrame("Forteller:");
        fortellervindu.setVisible(true);
        fortellervindu.setMinimumSize(new Dimension(300, 80));
        fortellervindu.setResizable(false);
        JPanel tlfInfo = new JPanel();
        JLabel tlfLab = new JLabel("Tlf: " + tlf);
        tlfLab.setAlignmentX(JFrame.CENTER_ALIGNMENT);
        tlfLab.setAlignmentY(JFrame.CENTER_ALIGNMENT);
        tlfLab.setFont(new Font("Sans", Font.BOLD, 30));
        tlfInfo.add(tlfLab);
        fortellervindu.add(tlfInfo);
    }


    public static void setDagtid(){
        String input = JOptionPane
                .showInputDialog("Hvor lang skal dagen være? (Minutter)");
        if (input.matches("\\d{1,2}")) {
            int min = Integer.parseInt(input);
            if (Spill.instans == null)
                oppstart.setTid(min);
            else
                Spill.instans.setTid(min);
        }
    }

    public static void setStemmetid(){
        String input = JOptionPane
                .showInputDialog("Hvor lang skal stemmetiden være? (Sekunder)");
        if (input.matches("\\d{1,2}")) {
            int sek = Integer.parseInt(input);
            if (Spill.instans == null)
                oppstart.setStemmeTid(sek);
            else
                Spill.instans.setTemmeTid(sek);
        }
    }

    public static void promptLeggTilSpiller(){
        String input = JOptionPane.showInputDialog("Navn på spiller:");
        if (input.length() == 0)
            return;
        if (oppstart.getSpillere().finnSpillerMedNavn(input) != null)
            JOptionPane.showMessageDialog(null, "Finnes allerede!");
        else
            leggTilSpiller(input);
    }

    public static void leggTilSpiller(String navn) {
        Spillerliste spillere = oppstart.getSpillere();
        if (spillere.finnSpillerMedNavn(navn) == null) {
            spillere.leggTil(new Spiller(navn));
            oppstart.antallspillere++;
            oppstart.informer(spillere.rolleString(oppstart.roller, oppstart.antallspillere));
            oppstart.oppdaterKnapper();
        }
    }
}
