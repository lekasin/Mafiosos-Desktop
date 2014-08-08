package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Rex extends Rolle {

	public Rex(){
		super("Rex");
		oppgave = "Hvem vil Rex snuse på?";
		side = BORGER;
		prioritet = REX;
		fortsett = false;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		if(blokkert || spiller.skjult()){
			tv.vis("Rex er blokkert!");
			return false;
		}
		
		tv.vis(spiller + " har hatt besøk av disse:\n");
		tv.rex(spiller);
		return true;
	}
}
