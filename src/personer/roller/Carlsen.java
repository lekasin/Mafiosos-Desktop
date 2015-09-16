package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Carlsen extends Rolle {

    boolean angrep;

	public Carlsen(){
		super("Magnus Carlsen");
		oppgave = "Hvem vil Carlsen forsvare?";
		veiledning = "Magnus Carlsen:\n" +
				"Carlsen våkner hver natt og prøver å lese spillet. Han velger først om han vil spille angrep eller forsvar," +
                " og velger så den personen han tror får flest stemmer ved første avstemning neste dag.\n" +
				"For å bytte mellom angrep og forsvar, trykker du på Angrep/Forsvar.\n" +
                "Carlsen signalisere hvordan han vil spille ved å vise en pistolhånd for angrep, og \"stopp\"-hånd for forsvar.\n" +
                "Når Calsen har valgt, trykker du på vedkommendes navn for å angripe/forsvare dem.\n";

		side = BORGER;
		prioritet = CARLSEN;
	}

    public void skift() {
        angrep = !angrep;
        if(angrep)
            oppgave = "Hvem vil Carlsen angripe?";
        else
            oppgave = "Hvem vil Carlsen forsvare?";
    }

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;

		return true;
	}

    public Spiller sjekkResultat(boolean treff){
        if (angrep){
            if (treff)
                return offer();
            else
                return spiller;
        }
        else {
            if (treff)
                offer().forsvar(this);
            else
                spiller.inviterPåSuppe(this);
        }
        return null;
    }
}
