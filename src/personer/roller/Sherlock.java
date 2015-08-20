package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Sherlock extends Rolle {
	
	String ut = "";
	
	public Sherlock(){
		super("Sherlock");
		oppgave = "Hvem vil Sherlock etterforske?";
		side = BORGER;
		prioritet = SHERLOCK;
		fortsett = false;
	}
	
	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}

	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		
		if(this.spiller.skjult())
			ut += spiller + " har besøkt " + tv.spillere().randomSpiller(null, spiller.offer());
		else if(spiller.offer() != null)
			ut += spiller + " har besøkt " + spiller.offer();
		else 
			ut += spiller + " har ikke besøkt noen";
		
			ut += (forsinkelse != null) ? " forrige natt.\n\n" : ".";
		
		if(blokkert){
			if(blokk != forsinkelse) ut = "Sherlock ble blokkert forrige natt!\n\n";
			tv.vis("Sherlock er blokkert!");
			return false;
		}
		
		tv.vis(ut);
		return true;
	}
}
