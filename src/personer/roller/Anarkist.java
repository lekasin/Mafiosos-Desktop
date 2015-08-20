package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Anarkist extends Rolle {
	
	public Anarkist(){
		super("Anarkist");
		oppgave = "Anarkisten våkner";
		side = NØYTRAL;
		prioritet = ANARKIST;
		aktiver(false);
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
