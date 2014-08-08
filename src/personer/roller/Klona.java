package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Klona extends Rolle {

	public Klona(){
		super("Kløna");
		oppgave = "Hvem vil Kløna sikte på?";
		side = BORGER;
		prioritet = KLØNA;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		spiller.kløn(this);
		return true;
	}
}
