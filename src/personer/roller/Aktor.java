package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Aktor extends Rolle {

	public Aktor(){
		super("Aktor");
		oppgave = "Hvem vil Aktor tiltale?";
		side = BORGER;
		prioritet = AKTOR;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;			
		
		if(spiller != null) {
			spiller.beskytt(this);
			tv.leggVed("\n" + spiller + " står på TILTALEBENKEN!");
			aktiver(false);
		}
		return true;
	}
}
