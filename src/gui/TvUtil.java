package gui;

import datastruktur.Spillerliste;
import personer.Spiller;

import javax.swing.*;

/**
 * Created by lars-erikkasin on 15.09.15.
 */
public class TvUtil {
    private static TV tv;

    public static void setTv(Spillerliste spillere){
        tv = new TV("MafiososInfo", spillere);
    }

    public static void toFront(){
        tv.toFront();
    }

    public static String getText(){
        return tv.getTvText();
    }

    public static void avstemning(){
        tv.avstemning();
    }

    public static void leggVed(String s){
        tv.leggVed(s);
    }

    public static void rensVedlegg(){
        tv.nyttVedlegg();
    }

    public static String vis(String s){
        return tv.vis(s);
    }

    public static String getVedlegg(){
        return tv.vedlegg();
    }

    public static void leggTil(String s){
        tv.leggtil(s);
    }

    public static void visRoller(String roller){
        tv.visRoller(roller);
    }

    public static void setFont(int størrelse){
        tv.setFont(størrelse);
    }

    public static void visSkjulRamme(){
        tv.dispose();
        tv.setUndecorated(!tv.isUndecorated());
        tv.setExtendedState(JFrame.MAXIMIZED_BOTH);
        tv.setVisible(true);
    }

    public static void visJørgensListe(){
        tv.jørgen();
    }

    public static String hentRextLukter(Spiller s){
        return tv.rex(s);
    }

    public static void visMafiaer(){
        tv.bedrag();
    }

    public static void visQuislingBeskjed(boolean lever, Spiller spiller){
        tv.quisling(lever, spiller);
    }

    public static void visSpecialGuyBeskjed(boolean lever){
        tv.special(lever);
    }

    public static String hentDrøm(Spiller spiller){
        return tv.drøm(spiller);
    }

}
