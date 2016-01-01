package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snasamann extends Rolle {

	public Snasamann(){
		super("Snåsamann");
        bilde = "snasamannen";
        oppgave = "Hvem vil Snåsamannen heale?";
		veiledning = "Snåsamann:\n" +
				"Snåsamannen velger hver natt en person å heale.\n" +
				"Når Snåsamannen har valgt, trykker du på vedkommendes navn for å heale dem neste dag.\n" +
				"Personer som er healet, blir beskyttet neste dag, og overlever å bli henrettet den dagen.";
		guide = "Snåsamann er dagens svar på skytsengelen. " +
                "Han er den siste som våkner i landsbyen, og velger en person han har lyst til å besøke denne dagen. " +
                "Han smører så inn sine hender med spenol og legger dem på hans utvalgte person. Denne kan da følgende dag ikke drepes av landsbyen! " +
                "Ingen vet hvem Snåsa har valgt, og vedkommende kan stemmes ut som alle andre. " +
                "Men idet personen henrettes, får landsbyen beskjed om at vedkommende er beskyttet, og alle legger seg igjen til å sove, uten at noen dør";
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
