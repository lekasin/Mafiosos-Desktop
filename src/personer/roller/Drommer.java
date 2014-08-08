package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Drommer extends Rolle {

	public Drommer(){
		super("Drømmer");
		oppgave = "Drømmeren drømmer!";
		side = BORGER;
		prioritet = DRØMMER;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(blokkert)
			tv.vis("Drømmeren får ikke sove...");
		else
			tv.drøm(spiller);
		if(informert) tv.leggtil(info);
		tv.toFront();
		return oppgave; 
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		return true;
	}
}
