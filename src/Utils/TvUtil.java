package Utils;

import datastruktur.Spillerliste;
import gui.Oppstart;
import gui.Spill;
import gui.TV;
import personer.Rolle;
import personer.Spiller;
import personer.roller.Mafia;

import javax.swing.*;


/**
 * Created by lars-erikkasin on 15.09.15.
 */
public class TvUtil {
    public static TV tv;
    public static String roller;
    public static Spiller ordfører;

    public static void init(Spillerliste spillere) {
        tv = new TV("MafiososInfo", spillere);
    }

    public static void toFront() {
        tv.toFront();
    }

    public static String getText() {
        return tv.getTvText();
    }

    public static void avstemming() {
        tv.avstemming();
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

    public static void setRoller(String roller) {
        TvUtil.roller = roller;
    }

    public static void visSideInfo(String sideInfo) {
        tv.visRoller(sideInfo);
    }

    public static void visOrdfører(Spiller ordfører) {
        if (ordfører != null && ordfører.lever()) {
            TvUtil.ordfører = ordfører;
            oppdaterSideInfo(true);
        } else
            TvUtil.ordfører = null;

    }

    public static void oppdaterSideInfo(boolean dag){
        if (ordfører != null)
            visSideInfo((dag ? "Dag " : "Natt ") + Spill.NATT + "\n\nOrdfører:\n" + ordfører + "\n\n" + roller);
        else
            visSideInfo((dag ? "Dag " : "Natt ") + Spill.NATT + "\n\n" + roller);
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
        if (Oppstart.hentRolle() != null)
            tv.toggleRolleGuide(Oppstart.hentRolle().getGuide());
        else if (Spill.hentAktiv() != null)
            tv.toggleRolleGuide(Spill.hentAktiv().getGuide());
        else if (tv.guideErSynlig())
            lukkGuide();
        else
            visGuide("Ingen aktiv rolle");
    }

    public static void visRolleBilde(Rolle rolle) {
        tv.visBilde(rolle.getBilde());
    }

    public static void visMafiaRolleBilde(Mafia mafia, String spesialist) {
        tv.visBilde(mafia.getBilde(spesialist));
    }

    public static void skjulBilde() {
        tv.skjulBilde();
    }

    public static void visResultatBilde(int resultat) {
        switch (resultat) {
            case Spill.HENRETTETMAFIA:
                tv.visBilde(ImgUtil.getStretchIcon("skyldig.jpg"));
                break;
            case Spill.HENRETTETBORGER:
                tv.visBilde(ImgUtil.getStretchIcon("uskyldig.jpg"));
                break;
            case Spill.HENRETTETBESKYTTET:
                tv.visBilde(ImgUtil.getStretchIcon("beskyttet.jpg"));
                break;
            case Spill.HENRETTETTROMPET:
                tv.visBilde(ImgUtil.getStretchIcon("trompet.jpg"));
                break;
            case Spill.HENRETTETBOMBER:
                tv.visBilde(ImgUtil.getStretchIcon("desarmert.jpg"));
                break;
            default:
                tv.visBilde(ImgUtil.getStretchIcon("uskyldig.jpg"));
        }
    }

    public static void visVinnerBilde(int vinner){
        switch (vinner) {
            case ResultatUtil.MAFIASEIER:
                tv.visBilde(ImgUtil.getStretchIcon("mafiaseier.jpg"));
                break;
            case ResultatUtil.LANDSBYSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("landsbyseier.jpg"));
                break;
            case ResultatUtil.UAVGJORTSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("uavgjortseier.jpg"));
                break;
            case ResultatUtil.ANARKISTSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("anarkistseier.jpg"));
                break;
            case ResultatUtil.PRINCESSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("princesseier.jpg"));
                break;
            case ResultatUtil.JOKERSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("jokerseier.jpg"));
                break;
            case ResultatUtil.SMITHSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("smithseier.jpg"));
                break;
            case ResultatUtil.PYROMANSEIER:
                tv.visBilde(ImgUtil.getStretchIcon("pyromanseier.jpg"));
                break;
            default:
                tv.visBilde(ImgUtil.getStretchIcon("landsbyseier.jpg"));
        }
    }

}
