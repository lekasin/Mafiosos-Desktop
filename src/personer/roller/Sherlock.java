package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Sherlock extends Rolle {
	
	public Sherlock(){
		super("Sherlock");
		oppgave = "Hvem vil Sherlock etterforske?";
		side = BORGER;
		prioritet = SHERLOCK;
		fortsett = false;
	}

	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		if(blokkert || spiller.skjult()){
			tv.vis("Sherlock er blokkert!");
			return false;
		}
		
		if(this.spiller.skjult())
			tv.vis(spiller + " har besøkt " + tv.spillere().randomSpiller(null, spiller.offer()) + ".");
		else if(spiller.offer() != null)
			tv.vis(spiller + " har besøkt " + spiller.offer() + ".");
		else 
			tv.vis(spiller + " har ikke besøkt noen.");
		return true;
	}
}
