package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snylter extends Rolle {

	Spiller valgt;
	String ut;
	
	public Snylter() {
		super("Snylter");
        bilde = "snylter";
        oppgave = "Hvem vil Snylteren snylte på?";
		veiledning = "Snylter:\n" +
				"Snylteren velger hver natt en person å snylte på.\n" +
				"Når snylteren har valgt, trykker du på vedkommendes navn for å snylte på dem.\n" +
				"Snylteren vil da bli påvirke av de samme effektene som den valgte.";
        guide = "Snylteren peker på en person hver natt, og snylter på all påvirkning denne personen har fått i løpet av natten. " +
                "Er han beskyttet, snåset, klønet eller lignende, skjer også dette med snylteren. " +
                "Er vedkommende drept, dør snylteren. Lykke til.";
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
