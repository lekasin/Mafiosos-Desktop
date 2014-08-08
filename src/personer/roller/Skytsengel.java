package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Skytsengel extends Rolle {

	public Skytsengel(){
		super("Skytsengel");
		oppgave = "Hvem vil Skytsengelen beskytte?";
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
