package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Rex extends Rolle {

	String ut = "";
	public Rex(){
		super("Rex");
		oppgave = "Hvem vil Rex snuse på?";
		side = BORGER;
		prioritet = REX;
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
		
		ut += spiller + " har hatt besøk av disse";
		if(forsinkelse != null) 
			ut += " forrige natt";
		ut += ":\n" + tv.rex(spiller) + "\n" ;
		
		if(blokkert){
			if(blokk != forsinkelse) ut = "Rex ble blokkert forrige natt!\n\n";
			tv.vis("Rex er blokkert!");
			return false;
		}
		tv.vis(ut);
		return true;
	}
}
