package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Julenissen extends Rolle {

	public Julenissen(){
		super("Julenissen");
		oppgave = "Hvem vil Nissen gi gave til?";
		veiledning = "Julenissen:\n" +
				"Julenissen velger hver natt en person å gi gave til.\n" +
				"Når nissen har valgt, trykker du på vedkommendes navn for å gi dem gaven.\n" +
				"Personer som har fått gave av nissen, er garantert å få gjennomført sin rolle den natten.";
		side = BORGER;
		prioritet = NISSEN;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.rolle().jul();
		return true;
	}
}
