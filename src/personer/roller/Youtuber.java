package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Youtuber extends Rolle {

	String ut = "";

	public Youtuber(){
		super("Youtuber");
		oppgave = "Hvem vil Youtuberen filme?";
		veiledning = "Youtuber:\n" +
				"Youtuberen velger hver natt en person å filme.\n" +
				"Når youtuberen har valgt, trykker du på vedkommendes navn for å filme dem.\n" +
				"Neste dag vises det på skjermen hvor mange som har besøkt den valgte denne natten.\n" +
				"Hvis personen dør samme natt, vises også hvilken rolle personen hadde.";
		side = BORGER;
		prioritet = YOUTUBER;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			pek(null);
			return false;
		}
		
		return true;
	}
}