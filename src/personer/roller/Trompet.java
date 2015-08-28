package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Trompet extends Rolle {

	public Trompet(){
		super("Trompet");
		oppgave = "Hvem vil Trompeten sprenge?";
		side = BORGER;
		prioritet = TROMPET;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		spiller.drep(this);
		return true;
	}
}