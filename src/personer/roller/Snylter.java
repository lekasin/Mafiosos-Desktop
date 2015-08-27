package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snylter extends Rolle {

	Spiller valgt;
	String ut;
	
	public Snylter() {
		super("Snylter");
		oppgave = "Hvem vil Snylteren snylte på?";
		veiledning = "Snylter:\n" +
				"Snylteren velger hver natt en person å snylte på.\n" +
				"Når snylteren har valgt, trykker du på vedkommendes navn for å snylte på dem.\n" +
				"Snylteren vil da bli påvirke av de samme effektene som den valgte.";
		side = BORGER;
		prioritet = SNYLTER;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert){
			spiller.pek(null);
			return false;
		}
		
		return true;
	}
}
