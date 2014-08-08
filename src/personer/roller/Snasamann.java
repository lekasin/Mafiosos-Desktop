package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snasamann extends Rolle {

	public Snasamann(){
		super("Snåsamann");
		oppgave = "Hvem vil Snåsamannen heale?";
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
