package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Logner extends Rolle {

	public Logner(){
		super("Løgner");
		oppgave = "Hvem vil Løgneren forfalske?";
		side = MAFIOSO;
		prioritet = LØGNER;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		spiller.lyv(this);
		return true;
	}
}
