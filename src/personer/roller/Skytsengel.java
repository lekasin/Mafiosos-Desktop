package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Skytsengel extends Rolle {

	public Skytsengel(){
		super("Skytsengel");
		oppgave = "Hvem vil Skytsengelen beskytte?";
		veiledning = "Skytsengel:\n" +
				"Skytsengelen velger hver natt en person å beskytte.\n" +
				"Når skytsengelen har valgt, trykker du på vedkommendes navn for å beskytte dem.\n" +
				"Personer som er beskyttet, kan ikke drepes den natten.";
        guide = "Skytsengelen velger hver natt en person og beskytter vedkommende mot skade. " +
                "Denne personen kan da ikke drepes denne natten. " +
                "Skytsengelen kan også velge seg selv, men samme person kan ikke beskyttes to netter på rad.";
		side = BORGER;
		prioritet = ENGEL;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.beskytt(this);
		return true;
	}
}
