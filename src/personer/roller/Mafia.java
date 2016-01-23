package personer.roller;

import Utils.ImgUtil;
import Utils.TvUtil;
import gui.Spill;
import gui.StretchIcon;
import personer.FlerSpillerRolle;
import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Mafia extends FlerSpillerRolle {

    public static final String SNIPER = "Sniper", SABOTØR = "Sabotør", SJÅFØR = "Sjåfør", FORFALSKER = "Forfalsker", LOMMETYV = "Lommetyv";

    boolean mine, snipe, saboter, undersøk;
    HashMap<String, Spiller> spesialister = new HashMap<>();

    public Mafia() {
        super("Mafia");
        bilde = "mafia";
        oppgave = "Hvem vil Mafiaen drepe?";
        veiledning = "Mafia:\n" +
                "Mafiaen velger hver natt en person å drepe.\n" +
                "Når mafiaen har blitt enige og valgt, trykker du på vedkommendes navn for å drepe dem.";
        guide = "Mafiaen er åpenbart spillets viktigste rolle. Det finnes ikke en runde uten minst én mafia. " +
                "Mafiaen våkner hver natt, og blir enige om et offer de vil drepe den natten. " +
                "Hvis de ikke blir forhindret, dør da dette offeret i løpet av natten, og er ute av spillet. " +
                "Målet deres er så klart å drepe alle borgerne, og når alle gjenlevende personer er mafiaer, har mafiaen vunnet. " +
                "Om alle mafiaene dør, har landsbyen vunnet.";
        side = MAFIOSO;
        prioritet = MAFIA;
    }

    public StretchIcon getBilde(String spesialist) {
        String bildePath = "roller/" + bilde;
        switch (spesialist) {
            case SNIPER :
                bildePath += "sniper";
                break;
            case SJÅFØR :
                bildePath += "sjafor";
                break;
            case SABOTØR :
                bildePath += "sabotor";
                break;
            case FORFALSKER :
                bildePath += "forfalsker";
                break;
            case LOMMETYV :
                bildePath += "lommetyv";
                break;
            default:
        }
        bildePath += ".jpg";
        return ImgUtil.getStretchIcon(bildePath);
    }

    @Override
    public void autoEvne() {
        if (Spill.NATT == 1 && !Spill.spillere.hentTommeRoller().isEmpty()) {
            String info = "\n\nDisse rollene står tomme:";
            for (Rolle rolle : Spill.spillere.hentTommeRoller()) {
                info += "\n" + rolle;
            }
            informer(null, info);
        }
    }

    public void snipe() {
        snipe = true;
    }

    public void sabotage() {
        saboter = true;
    }

    public void undersøkelse() {
        undersøk = true;
    }

    public void saboter(Spiller spiller) {
        saboter = false;
        fortsett(false);
        flerTrykk(true);
        spiller.blokker(this);
        oppgave();
        spesialister.remove(SABOTØR);
    }

    public void undersøk(Spiller spiller){
        undersøk = false;
        fortsett(false);
        TvUtil.toFront();
        TvUtil.vis(spiller + " er " + spiller.rolle() + "!");
        spesialister.remove(LOMMETYV);
    }

    @Override
    public void jul() {
        if (offer != null) offer.snipe(this);
        super.jul();
    }

    @Override
    public boolean pek(Spiller spiller) {
        flerTrykk(false);
        if (spiller == null) return false;

        for (Spiller s : Spill.spillere.spillere())
            if (s.id(MAFIA) && s.funker()) {
                s.setOffer(spiller);
                if (this.spiller == null || !this.spiller.lever())
                    setSpiller(s);
            }

        if (saboter)
            saboter(spiller);
        else if (undersøk) {
            undersøk(spiller);
        } else {
            if (snipe) {
                snill = true;
                snipe = false;
                spesialister.remove(SNIPER);
            }
            super.pek(spiller);
        }
        return true;
    }

    @Override
    public boolean evne(Spiller spiller) {
        fortsett(true);
        if (mine) {
            mine();
            spiller.minelegg();
        }
        else if (blokkert && !(blokk.id(POLITI) && offer.id(POLITI)))
            return false;
        else if (snill)
            spiller.snipe(this);
        else if (blokkert && !(blokk.id(POLITI) && offer.id(POLITI)))
            return false;
        else
            spiller.drep(this);
        return true;
    }

    public boolean mine() {
        mine = !mine;
        if (mine) {
            oppgave = "Hvem vil Mafiaen legge minen hos?";
        } else {
            oppgave = "Hvem vil Mafiaen drepe?";
        }
        return mine;
    }

    @Override
    public String rapport(){
        String ut = tittel + "(" + spiller + ")";
        if(offer != null && mine)
            ut += " har lagt minen hos " + offer + "(" + offer.rolle() + ")";
        else
            ut = super.rapport();
        return ut;
    }

    public boolean leggTilSpesialist(String spesialist){
        if (spesialister.size() < antall) {
            spesialister.put(spesialist, null);
            return spesialister.size() == antall;
        }
        return true;
    }

    public void fjernSpesialist(String spesialist){
        if (spesialister.containsKey(spesialist))
            spesialister.remove(spesialist);
    }

    public String hentLedigSpesialist(){
        for (String s : spesialister.keySet()) {
            if (spesialister.get(s) == null)
                return s;
        }
        return "";
    }

    public String hentRandomLedigSpesialist(){
        List<String> spesNavn = new ArrayList<>();

        for (String s : spesialister.keySet()) {
            if (spesialister.get(s) == null)
                spesNavn.add(s);
        }

        if (spesNavn.isEmpty())
            return "";
        else {
            Collections.shuffle(spesNavn);
            return spesNavn.get(0);
        }
    }

    public void setSpesialist(String spesialist, Spiller spiller) {
        spesialister.put(spesialist, spiller);
    }

    public boolean spesialistLever(String spesialist) {
        return spesialister.containsKey(spesialist) && spesialister.get(spesialist) != null && spesialister.get(spesialist).funker();
    }

    public boolean fjernAlleSpesialister(){
        if (spesialister.isEmpty())
            return false;
        spesialister.clear();
        return true;
    }

    public boolean nullstillSpesialister(){
        boolean tom = true;
        for (String spesialist : spesialister.keySet()) {
            if (spesialister.put(spesialist, null) != null)
                tom = false;
        }
        return !tom;
    }
}
