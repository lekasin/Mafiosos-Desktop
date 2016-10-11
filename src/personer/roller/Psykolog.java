package personer.roller;

import Utils.SkjermUtil;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

import java.util.ArrayList;

public class Psykolog extends Rolle {

    ArrayList<Spiller> pasienter = new ArrayList<>();
    Spiller pasient;
    int bonusStemmer = 0;
    private boolean selvValg;

    public Psykolog() {
        super("Psykolog");
        bilde = "psykolog";
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
        if (!s.equals(spiller()) && funker)
            pasienter.add(s);
    }

    @Override
    public void autoEvne() {
        if (!funker)
            return;

        if (!pasienter.isEmpty()) {
            pasient = pasienter.get(0);
            pasienter.remove(0);
        }

        if (pasient != null) {
            SkjermUtil.logg("Psykologens pasient er " + pasient);
            tvOppgave = "Dagens pasient er " + pasient + ".\n" +
                    "Hvem vil psykologen vende " + pasient + " mot?";
        }
        else {
            SkjermUtil.logg("Psykologen har ingen pasienter");
            tvOppgave = "Psykologen har ingen pasienter i dag.";
            return;
        }

        if (blokkert())
            return;

        bonusStemmer = pasient.getStemmer();
        pasient.fjernStemmer();
        TvUtil.toFront();
    }

    public boolean harPasient(){
        return pasient != null;
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert())
            return false;

        selvValg = spiller.equals(spiller());
        if (selvValg)
            spiller.setStemmer(spiller.getStemmer() + bonusStemmer);
        return true;
    }

    private void nullstill(){
        if (pasient != null) {
            if (selvValg)
                spiller().setStemmer(spiller().getStemmer() - bonusStemmer);
            pasient.setStemmer(pasient.getStemmer() + bonusStemmer);
            pasient = null;
        }
        tvOppgave = "Psykologen har ingen pasienter i dag.";
        bonusStemmer = 0;
        offer = null;
    }

    @Override
    public void sov() {
        super.sov();
        nullstill();
    }
}
