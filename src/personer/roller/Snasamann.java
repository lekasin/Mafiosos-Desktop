package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snasamann extends Rolle {

	public Snasamann(){
		super("Snåsamann");
		oppgave = "Hvem vil Snåsamannen heale?";
		veiledning = "Snåsamann:\n" +
				"Snåsamannen velger hver natt en person å heale.\n" +
				"Når Snåsamannen har valgt, trykker du på vedkommendes navn for å heale dem neste dag.\n" +
				"Personer som er healet, blir beskyttet neste dag, og overlever å bli henrettet den dagen.";
		side = BORGER;
		prioritet = SNÅSA;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.forsvar(this);
		return true;
	}
}
