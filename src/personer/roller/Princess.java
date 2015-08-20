package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Princess extends Rolle {

	String fanger;
	boolean infiltrert = false;
	
	public Princess(){
		super("Princess98");
		oppgave = "Hvem vil Jonatan kidnappe?";
		side = NØYTRAL;
		prioritet = PRINCESS;
	}
	
	public boolean befridd() {
		if(!spiller.lever() || (blokkert() && blokk.id(POLITI) && !infiltrert) || (offer.rolle() != blokk && infiltrert))
 			return true;
		return false;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			if(blokk.id(POLITI) && offer.id(POLITI))
				infiltrert = true;
			else
				return false;

		spiller.kidnapp(this);
		return true;
	}
}
