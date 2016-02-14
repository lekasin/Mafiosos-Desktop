package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;
import java.util.List;

public class Forsvarer extends Rolle {

    public Forsvarer() {
        super("Forsvarer");
        bilde = "forsvarer";
        oppgave = "Hvem vil Forsvareren forsvare?";
        veiledning = "Forsvarer:\n" +
                "Frosvareren velger hver natt en person å forsvare neste dag.\n" +
                "Når Forsvareren har valgt, trykker du på vedkommendes navn for å forsvare dem.\n" +
                "Personer som er forsvart, blir beskyttet neste dag, og overlever å bli henrettet den dagen.\n" +
                "Det vil alltid være synlig på skjermen hvem som er forsvart til enhver tid.";
        guide = "Forsvareren velger hver natt en person han ønsker å forsvare. Personen han velger kan ikke bruke sin evne på Forsvareren denne natten, " +
                "da de er opptatt med å diskutere saken når de møtes. Neste dag er det synlig for hele landsbyen hvem Forsvareren forsvarer, " +
                "og alle vet da at denne personen ikke kan henrettes. Det er fortsatt lov å mistenke og stemme på vedkommende, men henrettelsen blir aldri gjennomført.";
        side = BORGER;
        prioritet = FORSVARER;
    }

    @Override
    public void rens() {
        if (offer != null && forbudsListe.contains(offer))
            forbudsListe.remove(offer);
        super.rens();
    }

    @Override
    public void autoEvne() {
        sjekkAlleForbudt();
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert)
            return false;

        forbudsListe.add(spiller);
        spiller.forsvar(this);
        if (!spiller.id(FORSVARER))
            this.spiller().rens(spiller.rolle());
        return true;
    }

    private void sjekkAlleForbudt() {
        rensForbud();
        if (Spill.spillere.levende().size() == forbudsListe.size())
            forbudsListe.clear();
    }

    private void rensForbud() {
        List<Spiller> liste = new ArrayList<>();
        liste.addAll(forbudsListe);
        for (Spiller forbud : liste)
            if (!forbud.funker())
                forbudsListe.remove(forbud);
    }

    private String hentMottakere() {
        if (forbudsListe.isEmpty())
            return "";

        String ut = "\n\n";
        for (Spiller s : forbudsListe) {
            if (forbudsListe.indexOf(s) == 0)
                ut += s;
            else if (forbudsListe.indexOf(s) == forbudsListe.size() - 1)
                ut += " og " + s;
            else
                ut += ", " + s;
        }
        return ut + " er allerede forsvart.";
    }

    @Override
    public String oppgave() {
        tvOppgave = oppgave + hentMottakere();
        return super.oppgave();
    }
}
