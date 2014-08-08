package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Julenissen extends Rolle {

	public Julenissen(){
		super("Julenissen");
		oppgave = "Hvem vil Nissen gi gave til?";
		side = BORGER;
		prioritet = NISSEN;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.rolle().jul();
		return true;
	}
}
