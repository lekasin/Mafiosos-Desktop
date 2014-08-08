package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Havfrue extends Rolle {
	
	public Havfrue(){
		super("Havfrue");
		oppgave = "Hvem vil Havfruen forføre?";
		side = NØYTRAL;
		prioritet = HAVFRUE;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert && hvemBlokk() != spiller.rolle())
			return false;

		if(spiller.offer() == this.spiller) {
			if(snill)
				spiller.snipe(this);
			else
				spiller.drep(this);
		}
		return true;
	}
}
