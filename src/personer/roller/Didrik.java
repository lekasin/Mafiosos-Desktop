package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Didrik extends Rolle {

	public Didrik(){
		super("Distré Didrik");
		oppgave = "Hvem vil Didrik assistere?";
		side = BORGER;
		prioritet = DIDRIK;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		spiller.skjul(this);
		return true;
	}
}
