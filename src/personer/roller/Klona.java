package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Klona extends Rolle {

	public Klona(){
		super("Kløna");
		oppgave = "Hvem vil Kløna sikte på?";
		veiledning = "Kløna:\n" +
				"Kløna velger hver natt en person å sikte på.\n" +
				"Når kløna har valgt, trykker du på vedkommendes navn for kløne dem.\n" +
				"Den kløna sikter på, får ikke holde forsvarstale neste dag, " +
                "men dør umiddeltbart, hvis vedkommende får flest stemmer under en avstemning.";
		side = BORGER;
		prioritet = KLØNA;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(this.spiller.lever())
			spiller.kløn(this);
		return true;
	}
}
