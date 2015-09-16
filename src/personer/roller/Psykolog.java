package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Psykolog extends Rolle {

	public Psykolog(){
		super("Psykolog");
		oppgave = "Hvem vil psykologen henvise til?";
		veiledning = "Psykolog:\n" +
				"Jørgen våkner hver natt for å se undersøke de døde. Han velger inge på natten, men får info på skjermen.\n" +
				"Når Jørgen våkner, får han opp alle de døde og hvilken rolle de hadde på skjermen.\n" +
				"For å gå videre, trykker du på fortsett.";
		side = BORGER;
		prioritet = JØRGEN;
		skjerm = true;
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
