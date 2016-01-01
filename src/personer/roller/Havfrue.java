package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Havfrue extends Rolle {
	
	public Havfrue(){
		super("Havfrue");
        bilde = "havfrue";
		oppgave = "Hvem vil Havfruen forføre?";
		veiledning = "Havfrue:\n" +
				"Havfruen dreper personer når hun møter blikket deres.\n" +
				"For å velge havfruens offer, trykker du på vedkommendes navn når hun har valgt.\n" +
                "Hvis denne personen også peker på havfruen i løpet av natten, dør han.";
        guide = "Havfruen er en borger som liker å forføre folk. Hun våkner hver natt, og velger en person å forføre. " +
                "Hvis vedkommende også velger henne, får de blikkontakt, og personen blir forført. " +
                "Havfruen drar han da med seg til havets dyp og dreper han. Får hun blikkontakt med mafiaen, dør de begge. " +
                "Altså dreper havfruen kun hvis hun og offeret peker på hverandre. Ellers er hun på borgernes side.";
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
