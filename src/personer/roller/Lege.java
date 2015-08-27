package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Lege extends Rolle {
	
	public Lege(){
		super("Lege");
		oppgave = "Hvem vil Legen lege?";
		veiledning = "Legen:\n" +
				"Legen velger hver natt en person å lege.\n" +
				"Når legen har valgt, trykker du på vedkommendes navn for å lege dem.\n" +
				"Personer som er leget, kan ikke drepes den natten.\n" +
                "Men hvis legen besøker en mafia, ser han for mye, og dør selv.";
		side = BORGER;
		prioritet = LEGE;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(spiller.rolle().id(MAFIA) && !snill)
			this.spiller.drep(this);
		else
			spiller.beskytt(this);
		return true;
	}
}
