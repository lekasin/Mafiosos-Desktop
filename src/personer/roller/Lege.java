package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Lege extends Rolle {
	
	public Lege(){
		super("Lege");
		oppgave = "Hvem vil Legen lege?";
		side = BORGER;
		prioritet = LEGE;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(spiller.rolle().id(MAFIA) && !snill)
			this.spiller.drep(this);
		else
			spiller.beskytt(this);
		return true;
	}
}
