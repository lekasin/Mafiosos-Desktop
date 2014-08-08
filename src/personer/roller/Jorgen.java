package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Jorgen extends Rolle {

	public Jorgen(){
		super("Jørgen");
		oppgave = "Jørgen ser på de døde";
		side = BORGER;
		prioritet = JØRGEN;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(blokkert)
			tv.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
		else
			tv.jørgen();
		if(informert) 
			tv.leggtil(info);
		tv.toFront();
		return oppgave;
	}
	
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			tv.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
			return false;
		}
		
		tv.jørgen();
		return true;
	}
}
