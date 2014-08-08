package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bedrager extends Rolle {
	
	public Bedrager(){
		super("Bedrageren");
		oppgave = "Bedrageren gjør seg kjent med mafiaen";
		side = BORGER;
		prioritet = BEDRAGER;
	}

	@Override
	public String oppgave() {
		aktiver(false);
		tv.bedrag();
		tv.toFront();
		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
