package personer.roller;

import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;
import java.util.List;

public class Pyroman extends Rolle {

    boolean tenning = false;

    public Pyroman() {
        super("Pyroman");
        bilde = "pyroman";
        oppgave = "Hvem vil Pyromanen dynke med bensin?";
        veiledning = "Pyroman:\n" +
                "Pyromanen kan være natt velge en person å dynke med bensin.\n" +
                "Når Pyromanen har valgt, trykker du på vedkommendes navn for å helle bensin." +
                "Personen som dynkes, vil komme på skjermen neste dag.\n" +
                "Hvis Pyromanen viser at han ønsker å tenne på alle bensinluktende, trykker du på Tenn på.";
        guide = "Pyromanen våkner hver natt og velger en person han ønsker å dynke med bensin. " +
                "Neste dag kommer det opp på skjermen hvem pyromanen har dynket. Når Pyromanen føler tiden er inne, kan han velge å tenne på bensinen! " +
                "Når Pyromanen tenner på, dør alle som lukter bensin. Pyromanen er på eget lag, og vinner spillet hvis han brenner opp alle gjenlevende spillere. " +
                "Pyromanen kan blokkeres, både fra å dynke, og tenne på.";
        side = NØYTRAL;
        prioritet = PYROMAN;
    }

    @Override
    public void rens() {
        if (tenning) {
            for (Spiller offer : forbudsListe)
                if (offer.drapsmann().equals(this))
                    offer.vekk();
        } else if (offer != null && forbudsListe.contains(offer))
            forbudsListe.remove(offer);

        super.rens();
    }

    @Override
    public void sov() {
        if (tenning)
            forbudsListe.clear();
        super.sov();
    }

    @Override
    public void autoEvne() {
        tenning = false;
        rensForbud();
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert)
            return false;

        tenning = false;
        forbudsListe.add(spiller);
        spiller.dynk(this);

        return true;
    }

    public void tennPå() {
        if (blokkert)
            return;

        tenning = true;
        for (Spiller offer : forbudsListe)
            if (offer.lever())
                offer.drep(this);
        forbudsListe.clear();
    }

    @Override
    public String rapport() {
        if (tenning)
            return tittel + "(" + spiller.navn() + ") har tent på!";
        else
            return super.rapport();
    }

    private void rensForbud(){
        List<Spiller> liste = new ArrayList<>();
        liste.addAll(forbudsListe);
        for (Spiller forbud : liste)
            if (!forbud.funker())
                forbudsListe.remove(forbud);
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
        return  ut + " lukter bensin.";
    }

    @Override
    public String oppgave() {
        tvOppgave = oppgave + hentMottakere();
        return super.oppgave();
    }
}
