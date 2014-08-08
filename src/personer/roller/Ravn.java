package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Ravn extends Rolle {

	String ut;
	Spiller gjest;
	
	public Ravn(){
		super("Ravn");
		oppgave = "Hvem vil Ravn ta med på Den Blå Fisk?";
		side = BORGER;
		prioritet = RAVN;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		ut = "\nIngen fiskesuppe idag\n";
		if(!lever && gjest == null) 
			leggVed(ut);
		gjest = null;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		
		if(spiller != offer) {
			gjest = spiller;
			ut = "";
		}

		if(this.spiller.lever() && spiller.lever() && !blokkert)
			ut = "\n" + spiller + " spiser Friduns fiskesuppe på Den Blå Fisk\n";
		
		leggVed(ut);
		return true;
	}
}
