package Utils;

import datastruktur.Spillerliste;
import gui.Spill;
import personer.Spiller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lars-erikkasin on 25.09.15.
 */
public class InnstillingsUtil {

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


//    public static void setDagtid(){
//        String input = JOptionPane
//                .showInputDialog("Hvor lang skal dagen være? (Minutter)");
//        if (input.matches("\\d{1,2}")) {
//            int min = Integer.parseInt(input);
//            if (spill == null)
//                oppstart.setTid(min);
//            else
//                spill.setTid(min);
//        }
//    }
//
//    public static void promptLeggTilSpillers(){
//        String input = JOptionPane.showInputDialog("Navn på spiller:");
//        if (input.length() == 0)
//            return;
//        if (spillere.finnSpillerMedNavn(input) != null)
//            JOptionPane.showMessageDialog(null, "Finnes allerede!");
//        else
//            leggTilSpiller(input);
//    }
//
//    // KNAPPEFUNKSJONER
//    public static void leggTilSpiller(String navn) {
//        Spillerliste spillere = Spill.spillere;
//        if (spillere.finnSpillerMedNavn(navn) == null) {
//            spillere.leggTil(new Spiller(navn));
//            oppstart.antallspillere++;
//            oppstart.informer(spillere.rolleString(oppstart.roller,
//                    oppstart.antallspillere));
//            oppstart.oppdaterKnapper();
//        }
//    }
}
