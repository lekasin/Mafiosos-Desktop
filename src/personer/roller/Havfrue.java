package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Havfrue extends Rolle {
	
	public Havfrue(){
		super("Havfrue");
		oppgave = "Hvem vil Havfruen forføre?";
		veiledning = "Havfrue:\n" +
				"Havfruen dreper personer når hun møter blikket deres.\n" +
				"For å velge havfruens offer, trykker du på vedkommendes navn når hun har valgt.\n" +
                "Hvis denne personen også peker på havfruen i løpet av natten, dør han.";
		side = NØYTRAL;
		prioritet = HAVFRUE;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert && hvemBlokk() != spiller.rolle())
			return false;

		if(spiller.offer() == this.spiller) {
			if(snill)
				spiller.snipe(this);
			else
				spiller.drep(this);
		}
		return true;
	}
}
