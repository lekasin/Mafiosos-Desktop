package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Drommer extends Rolle {

	String ut = "";
	public Drommer(){
		super("Drømmer");
		oppgave = "Drømmeren drømmer!";
		side = BORGER;
		prioritet = DRØMMER;
		skjerm = true;
	}
	
	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}
	
	@Override
	public String oppgave() {
		if(spiller.klonet())
			return super.oppgave();
		evne(spiller);
		return oppgave; 
	}

	@Override
	public boolean evne(Spiller spiller) {
		ut += tv.drøm(spiller) + "\n";

		if(blokkert) {
			if(blokk != forsinkelse) ut = "";
			tv.vis("Drømmeren får ikke sove...");
		}
		else
			tv.vis(ut);
		
		if(informert) tv.leggtil(info);
		tv.toFront();
		return true;
	}
	
	@Override
	public String rapport() {
		String ut = tittel + "(" + spiller + ") ";
		ut += this.ut;
		return ut;
	}
}
