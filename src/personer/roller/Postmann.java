package personer.roller;

import Utils.SkjermUtil;
import gui.Spill;
import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by lars-erikkasin on 18.01.2016.
 */
public class Postmann extends Rolle {

    Spiller mottaker;
    List<String> pakkeSekken = new ArrayList<>();
    private final String skrin = "Førstehjelpsskrin", nåde = "Benådelse", seddel = "Stemmeseddel", brev = "Tomt brev", invite = "Suppeinvitasjon", trussel = "Trusselbrev", bombe = "Brevbombe";

    public Postmann(){
        super("Postmann");
        bilde = "postmann";
        oppgave = "Hvem vil Pat levere pakke til?";
        veiledning = "Postmann:\n" +
                "Postmannen velger hver natt en person å levere en pakke til.\n" +
                "Når Postmannen har valgt, trykker du på vedkommendes navn for å sende dem pakken.\n" +
                "Personen som har fått post, vil våkne før alle de andre neste dag, og måtte velge om han vil åpne pakken eller ikke.";
        guide = "Postmann Pat velger hver natt en person som dagen etter får en pakke på døra. Vedkommende får da våkne før resten av landsbyen, og må velge" +
                "om han eller hun ønsker å åpne pakken eller ikke. Hvis pakken ikke åpnes, skjer det ingenting, men om den åpnes, påvirkes mottakeren" +
                "av en av 7 effekter følgende dag: " +
                "1. Brevbombe: Mottakeren dør, " +
                "2. Førstehjelpsskrin: Mottakeren overlever natten, " +
                "3. Benådelse: Mottaker kan ikke stemmes ut," +
                "4. Trusselbrev: Mottaker siktes på av kløna" +
                "5. Stemmeseddel: Mottakeren får dobbelstemme, " +
                "6. Suppeinvitasjon: Mottakeren mister stemmeretten" +
                "7. Tom pakke: Ingenting skjer. " +
                "Pat har bare én av hver pakke, og må velge en ny mottaker hver natt.";
        side = BORGER;
        prioritet = POSTMANN;
    }

    @Override
    public void autoEvne() {
        if (Spill.NATT == 1)
            lastOppPakker();
        if (pakkeSekken.isEmpty())
            aktiver(false);
        sjekkAlleForbudt();
    }

    private void sjekkAlleForbudt(){
        rensForbud();
        if (Spill.spillere.levende().size() == forbudsListe.size())
            forbudsListe.clear();
    }

    private void rensForbud(){
        List<Spiller> liste = new ArrayList<>();
        liste.addAll(forbudsListe);
        for (Spiller forbud : liste)
            if (!forbud.funker())
                forbudsListe.remove(forbud);
    }

    public void lastOppPakker() {
        pakkeSekken.add(skrin);
        pakkeSekken.add(nåde);
        pakkeSekken.add(seddel);
        pakkeSekken.add(brev);
        pakkeSekken.add(invite);
        pakkeSekken.add(trussel);
        pakkeSekken.add(bombe);
    }

    @Override
    public String oppgave() {
        mottaker = null;
        tvOppgave = oppgave + hentMottakere() + hentPakkeListe();
        return super.oppgave();
    }

    private String hentMottakere(){
        if (forbudsListe.isEmpty())
            return "";

        String  ut = "\n\n";
        for (Spiller s : forbudsListe) {
            if (forbudsListe.indexOf(s) == 0)
                ut += s;
            else if (forbudsListe.indexOf(s) == forbudsListe.size()-1)
                ut += " og " + s;
            else
                ut += ", " + s;
        }
        return  ut + " har allerede fått post.";
    }

    private String hentPakkeListe(){
        String ut = "\n\nFølgende pakker gjenstår:";
        for (String s : pakkeSekken) {
            ut += "\n" + s;
        }
        return ut;
    }

    @Override
    public boolean evne(Spiller spiller) {
        if(spiller != offer)
            mottaker = spiller;

        if(this.spiller.lever() && spiller.lever() && !blokkert) {
            spiller.giPost(this);
        }

        return true;
    }

    public String åpnePakke(Spiller mottaker){
        String beskjed = mottaker + " fikk ";
        switch (velgPakke()) {
            case skrin:
                beskjed += "et førstehjelpsskrin!";
                SkjermUtil.logg(beskjed);
                beskjed += "\nDu kan dermed redde deg selv hvis du ble forsøkt drept i natt.";
                mottaker.beskytt(this);
                break;
            case nåde:
                beskjed += "en benådelse.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nDu vil dermed overleve å bli stemt ut i dag.";
                mottaker.snås(this);
                break;
            case seddel:
                beskjed += "en stemmeseddel.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nDu har dermed dobbeltstemme idag.";
                mottaker.ekstraStemme();
                break;
            case brev:
                beskjed += "et tomt brev.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nIngenting skjedde.";
                break;
            case invite:
                beskjed += "en suppeinvitasjon.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nDu må dra på den blå fisk, og får dermed ikke mulighet til å stemme.";
                mottaker.fjernStemmer();
                break;
            case trussel:
                beskjed += "et trusselbrev fra kløna.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nDu vil dø om du må holde forsvarstale i dag.";
                mottaker.kløn(this);
                break;
            case bombe:
                beskjed += "en brevbombe!";
                SkjermUtil.logg(beskjed);
                beskjed += "\nBeklager, men du er mest sannsynlig død...";
                mottaker.drep(this);
                break;
            default:
                beskjed += "et tomt brev.";
                SkjermUtil.logg(beskjed);
                beskjed += "\nIngenting skjedde.";
        }
        forbudsListe.add(mottaker);
        return beskjed;
    }

    private String velgPakke(){
        int pakkeIndeks = new Random().nextInt(pakkeSekken.size());
        String pakke = pakkeSekken.get(pakkeIndeks);
        pakkeSekken.remove(pakke);
        return pakke;
    }
}
