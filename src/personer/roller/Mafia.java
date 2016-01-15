package personer.roller;

import Utils.ImgUtil;
import gui.Spill;
import gui.StretchIcon;
import personer.Rolle;
import personer.Spiller;

import java.util.HashMap;


public class Mafia extends Rolle {

    public static final String SNIPER = "Sniper", SABOTØR = "Sabotør", SJÅFØR = "Sjåfør", FORFALSKER = "Forfalsker";

    int antall = 1, levende = 1;
    boolean mine, snipe, saboter;
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
            default:
        }
        bildePath += ".jpg";
        return ImgUtil.getStretchIcon(bildePath);
    }

    @Override
    public int antall() {
        return antall;
    }

    public void fler() {
        antall++;
        levende++;
    }

    public void fjern() {
        antall--;
        levende--;
    }

    public boolean flere() {
        return levende > 1;
    }

    @Override
    public void drep() {
        if (!flere())
            lever = false;
        levende--;
    }

    public void snipe() {
        snipe = true;
    }

    public void sabotage() {
        saboter = true;
    }

    public void saboter(Spiller spiller) {
        saboter = false;
        fortsett(false);
        spiller.blokker(this);
        oppgave();
        spesialister.remove(SABOTØR);
    }

    @Override
    public void jul() {
        if (offer != null) offer.snipe(this);
        super.jul();
    }

    @Override
    public boolean pek(Spiller spiller) {
        if (spiller == null) return false;

        for (Spiller s : Spill.spillere.spillere())
            if (s.id(MAFIA) && s.funker()) {
                s.setOffer(spiller);
                if (!this.spiller.lever())
                    setSpiller(s);
            }

        if (saboter)
            saboter(spiller);
        else {
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
