package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Ravn extends Rolle {

	String ut;
	Spiller gjest;
	
	public Ravn(){
		super("Ravn");
		oppgave = "Hvem vil Ravn ta med på Den Blå Fisk?";
        veiledning = "Ravn:\n" +
                "Ravn velger hver natt en person som sendes på Den blå fisk for å spise fiskesuppe neste dag.\n" +
                "Når Ravn har valgt, trykker du på vedkommendes navn for å sende dem på fiskesuppa.\n" +
                "Personen som spiser suppe, kan ikke snakke eller stemme neste dag.";
		side = BORGER;
		prioritet = RAVN;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		ut = "";
		if(gjest == null) 
			ut = "\nIngen fiskesuppe idag\n";
		if(!lever && gjest != null || !funker && gjest == null)
			leggVed(ut);
		gjest = null;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		
		if(spiller != offer)
			gjest = spiller;
		if(gjest != null)
			ut = "";
		
		if(this.spiller.lever() && spiller.lever() && !blokkert)
			ut = "\n" + spiller + " spiser Friduns fiskesuppe på Den Blå Fisk\n";
		
		leggVed(ut);
		return true;
	}
}
