package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Quisling extends Rolle {

	public Quisling(){
		super("Quisling");
		oppgave = "Hvordan går det med Quisling?";
		side = BORGER;
		prioritet = QUISLING;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(spiller.rolle() == this)
			tv.quisling(lever, spiller);
		else {
			if(funker) funk(false);
			tv.vis("Quisling har allerede konvertert.");
		}
		if(informert) tv.leggtil(info);
		tv.toFront();
		return oppgave;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
