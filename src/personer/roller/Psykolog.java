package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;

public class Psykolog extends Rolle {

    Spiller pasient;
    int bonusStemmer = 0, pasientNr;
    ArrayList<Spiller> pasienter = new ArrayList<>();

    public Psykolog() {
        super("Psykolog");
        oppgave = "Hvem vil psykologen henvise til?";
        veiledning = "Psykolog:\n" +
                "Jørgen våkner hver natt for å se undersøke de døde. Han velger inge på natten, men får info på skjermen.\n" +
                "Når Jørgen våkner, får han opp alle de døde og hvilken rolle de hadde på skjermen.\n" +
                "For å gå videre, trykker du på fortsett.";
        side = BORGER;
        prioritet = PSYKOLOG;
        skjerm = true;
        forbud2 = spiller();
    }

    public void bestillTime(Spiller s) {
        if (!s.equals(spiller()))
            pasienter.add(s);
    }

    @Override
    public void autoEvne() {
        if (!funker)
            return;

        spiller().setStemmer(spiller().getStemmer() - bonusStemmer);

        if (pasient != null)
            pasient.setStemmer(pasient.getStemmer() + bonusStemmer);

        if (pasienter.size() > pasientNr)
            pasient = pasienter.get(pasientNr++);
        else
            pasient = null;

        bonusStemmer = 0;

        if (pasient != null)
            tvOppgave = "Dagens pasient er " + pasient + ".\n" +
                    "Hvem vil psykologen vende " + pasient + " mot?";
        else {
            tvOppgave = "Psykologen har ingen pasienter i dag.";
            return;
        }

        if (blokkert())
            return;

        bonusStemmer = pasient.getStemmer();
        pasient.fjernStemmer();
        TvUtil.toFront();
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert())
            return false;

        if (spiller.equals(spiller()))
            spiller.setStemmer(spiller.getStemmer() + bonusStemmer);
        return true;
    }
}
