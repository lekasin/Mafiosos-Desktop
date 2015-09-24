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
                "Psykologen våkner hver natt, og får vite hvem som er pasienten hennes denne natten.\n" +
                "Hun velger så en person hun vil vende pasienten imot, og når hun har valgt, trykker du på den hun velger.\n" +
                "Pasientens stemme neste dag, vil da havne på den hun velger, uavhengig av hvem pasienten stemmer på.\n" +
                "Psykologen kan også velge å peke på seg selv, for å la personen stemme det samme som henne neste dag.";
        guide = "Psykologen får pasienter hver natt, i form av at folk besøker henne (peker på henne). " +
                "Disse legges så i en kø, og hver natt blir førstemann i køen nattens pasient. " +
                "Når psykologen har fått en pasient, får hun vite navnet på vedkommende på skjermen. " +
                "Psykologen har da evnen til å påvirke pasienten psykisk, og få dem til å stemme på den hun ønsker neste dag." +
                "Hun kan velge der og da (på natten) hvem pasienten skal stemme på, eller hun kan peke på seg selv, " +
                "som betyr at pasienten skal stemme likt som henne neste dag.";
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
