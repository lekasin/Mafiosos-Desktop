package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Marius extends Rolle {

	String ut;
	Spiller gjest;
	
	public Marius(){
		super("Marius");
		oppgave = "Hvem vil Marius trykke opp?";
		side = BORGER;
		prioritet = MARIUS;
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		ut = "\nIngen flyers idag\n";
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
			ut = "\nFlyerne påstår at " + spiller + " er mafia!\n";
					
		leggVed(ut);
		return true;
	}
}
