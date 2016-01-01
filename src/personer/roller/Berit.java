package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Berit extends Rolle {

	public Berit(){
		super("Gærne Berit");
		oppgave = "Hvem vil Berit forsinke?";
        veiledning = "Gærne Berit:\n" +
                "Berit velger hver natt en person å forsinke.\n" +
                "Når Berit har valgt, trykker du på vedkommendes navn for å forsinke dem.\n" +
                "Vedkommende vil da få utsatt nattens aktivitet til neste natt.";
        guide = "Gærne Berit er hver natt ute og stjeler mokkabønner fra lokale Joker-butikker. " +
                "Hun stikker så av på scooteren sin, men er paranoid nok til å tro at alle som kjører bak henne er politiet, " +
                "og kaster derfor ut mokkabønner fra den allerede treige scooteren sin, og forsinker dermed personen som kjører bak med et helt døgn. " +
                "Altså velger Berit en person hver natt, som får sin funksjon utsatt med et døgn, og dermed ikke gjennomført før neste natt.";
		side = BORGER;
		prioritet = BERIT;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);

		if(blokkert || spiller.rolle().blokkert() || !spiller.rolle().aktiv())
			return false;

		if(spiller.id(COPYCAT) || spiller.id(CUPID) || spiller.id(KIRSTEN) || spiller.id(ILLUSJONIST) || spiller.id(SOFA))
			return false;
		spiller.forsink(this);
		return true;
	}
}
