package Utils;

import datastruktur.Spillerliste;
import gui.Spill;
import personer.Rolle;
import personer.Spiller;

/**
 * Created by lars-erikkasin on 18.01.2016.
 */
public class ResultatUtil {
    public static final int MAFIASEIER = 0, LANDSBYSEIER = 1, UAVGJORTSEIER = 2, JOKERSEIER = 3, PRINCESSEIER = 4, SMITHSEIER = 5,
            ANARKISTSEIER = 6, PYROMANSEIER = 7;

    public static String hentVinner(){
        switch (vinner()) {
            case MAFIASEIER:
                return "Mafiaen har vunnet!";
            case LANDSBYSEIER:
                return "Landsbyen har vunnet!";
            case UAVGJORTSEIER:
                return "Alle er døde! Ingen vant!";
            case ANARKISTSEIER:
                return "Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!";
            case SMITHSEIER:
                return "Agent Smith har tatt over hele landsbyen, og har vunnet!";
            case PRINCESSEIER:
                return "Princess98 har kidnappet hele landsbyen, og har vunnet!";
            case JOKERSEIER:
                return "Jokeren, " + Spill.spillere.finnSpiller(Rolle.JOKER) + " seiret, og vi har en vinner!";
            case PYROMANSEIER:
                return "Pyromanen har vunnet!";
            default:
                return "";
        }
    }

    public static int vinner() {
        Spillerliste spillere = Spill.spillere;

        int slemme = 0;
        int snille = 0;
        int anarki = 0;
        int smiths = 0;
        int fanger = 0;
        int pyroman = 0;

        for (Spiller s : spillere.spillere()) {
            if (s.lever()) {
                if (s.id(Rolle.ANARKIST)) anarki++;
                if (s.id(Rolle.SMITH)) smiths++;
                if (s.id(Rolle.PYROMAN)) pyroman++;
                if (spillere.hentFanger().contains(s)) fanger++;
                if (s.side() < Rolle.NØYTRAL) slemme++;
                else snille++;
            }
        }
        if (snille + slemme < 1)
            return UAVGJORTSEIER;
        else if (snille < 1)
            return MAFIASEIER;
        else if (slemme < 1) {
            if (smiths == snille)
                return SMITHSEIER;
            else if (pyroman == snille)
                return PYROMANSEIER;
            else if (anarki > 0)
                return ANARKISTSEIER;
            else
                return LANDSBYSEIER;
        } else if (fanger == snille + slemme - 1 && spillere.finnSpiller(Rolle.PRINCESS).lever())
            return PRINCESSEIER;
        else
            return -1;
    }

}
