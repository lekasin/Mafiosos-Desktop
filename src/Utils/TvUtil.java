package Utils;

import datastruktur.Spillerliste;
import gui.TV;
import personer.Spiller;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by lars-erikkasin on 15.09.15.
 */
public class TvUtil {
    public static TV tv;
    public static String roller;

    public static void init(Spillerliste spillere, JMenuBar meny) {
        tv = new TV("MafiososInfo", spillere);
        tv.setJMenuBar(meny);
    }

    public static void toFront() {
        tv.toFront();
    }

    public static String getText() {
        return tv.getTvText();
    }

    public static void avstemning() {
        tv.avstemning();
    }

    public static void leggVed(String s) {
        tv.leggVed(s);
    }

    public static void rensVedlegg() {
        tv.nyttVedlegg();
    }

    public static String vis(String s) {
        return tv.vis(s);
    }

    public static String getVedlegg() {
        return tv.vedlegg();
    }

    public static void leggTil(String s) {
        tv.leggtil(s);
    }

    public static void visRoller(String roller) {
        TvUtil.roller = roller;

        tv.visRoller(roller);
    }

    public static void visOrdfører(Spiller ordfører) {
        if (ordfører.lever())
            tv.visRoller("Ordfører:\n" + ordfører + "\n\n" + roller);
        else
            visRoller(roller);
    }

    public static void setTvFont(int størrelse) {
        tv.setTvFont(størrelse);
    }

    public static void setRollelisteFont(int størrelse){
        tv.setRollerFont(størrelse);
    }

    public static void visSkjulRamme() {
        tv.dispose();
        tv.setUndecorated(!tv.isUndecorated());
        if (tv.isUndecorated())
            tv.setExtendedState(JFrame.MAXIMIZED_BOTH);
        else {
            tv.setExtendedState(JFrame.NORMAL);
            tv.setSize(TV.TVSIZE);
        }
        tv.setVisible(true);
    }

    public static void visJørgensListe() {
        tv.jørgen();
    }

    public static String hentRextLukter(Spiller s) {
        return tv.rex(s);
    }

    public static void visMafiaer() {
        tv.bedrag();
    }

    public static void visQuislingBeskjed(boolean lever, Spiller spiller) {
        tv.quisling(lever, spiller);
    }

    public static void visSpecialGuyBeskjed(boolean lever) {
        tv.special(lever);
    }

    public static String hentDrøm(Spiller spiller) {
        return tv.drøm(spiller);
    }

    public static JMenu skjermMeny(){
        JMenu skjerm = new JMenu("Display");
        skjerm.setMnemonic(KeyEvent.VK_S);
        skjerm.getAccessibleContext().setAccessibleDescription(
                "Innstillenger for TV-display");

        JMenuItem fullskjerm = new JMenuItem("Fullskjerm");
        fullskjerm.addActionListener(e -> TvUtil.visSkjulRamme());
        skjerm.add(fullskjerm);

        JMenu tekstMeny = new JMenu("Tekststørrelse");
        tekstMeny.setMnemonic(KeyEvent.VK_T);
        tekstMeny.getAccessibleContext().setAccessibleDescription(
                "Endre tekststørrelsen på displayet");
        skjerm.add(tekstMeny);

        JMenuItem tekstItem = new JMenuItem("Hoveddisplay");
        tekstItem.addActionListener(e -> {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
            if (input != null && input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setTvFont(størrelse);
            }
        });
        tekstMeny.add(tekstItem);

        tekstItem = new JMenuItem("Rolleliste");
        tekstItem.addActionListener(e -> {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
            if (input != null && input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setRollelisteFont(størrelse);
            }
        });
        tekstMeny.add(tekstItem);

        JMenu fargeMeny = new JMenu("Farger");
        fargeMeny.setMnemonic(KeyEvent.VK_T);
        fargeMeny.getAccessibleContext().setAccessibleDescription(
                "Endre fargene på displayet");
        skjerm.add(fargeMeny);

        JMenuItem fargeitem = new JMenuItem("Hoveddisplay");
        fargeitem.addActionListener(e -> tv.setTvFarge(false));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Hoveddisplaytekst");
        fargeitem.addActionListener(e -> tv.setTvFarge(true));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Rolleliste");
        fargeitem.addActionListener(e -> tv.setRollerfarge(false));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Rollelistetekst");
        fargeitem.addActionListener(e -> tv.setRollerfarge(true));
        fargeMeny.add(fargeitem);


        return skjerm;
    }
}
