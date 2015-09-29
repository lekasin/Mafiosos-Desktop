package Utils;

import datastruktur.Spillerliste;
import gui.Spill;
import gui.TV;
import personer.Spiller;

import javax.swing.JFrame;
import java.util.logging.Handler;


/**
 * Created by lars-erikkasin on 15.09.15.
 */
public class TvUtil {
    public static TV tv;
    public static String roller;

    public static void init(Spillerliste spillere) {
        tv = new TV("MafiososInfo", spillere);
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

    public static void setGuideFont(int størrelse){
        tv.setGuideFont(størrelse);
    }

    public static void setTvFarge(boolean tekst) {
        tv.setTvFarge(tekst);
    }

    public static void setRollelisteFarge(boolean tekst){
        tv.setRollerFarge(tekst);
    }

    public static void setGuideFarge(boolean tekst) {
        tv.setGuideFarge(tekst);
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

    public static void visGuide(String info) {
        tv.visRolleGuide(info);
    }

    public static void lukkGuide() {
        tv.lukkRolleGuide();
    }

    public static void toggleGuide(){
        if (tv.guideErSynlig())
            lukkGuide();
        else if (Spill.hentAktiv() != null)
            visGuide(Spill.hentAktiv().getGuide());
        else
            visGuide("Ingen aktiv rolle");
    }
}
