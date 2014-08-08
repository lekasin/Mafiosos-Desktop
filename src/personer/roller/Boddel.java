package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Boddel extends Rolle {

	public Boddel(){
		super("Bøddel");
		oppgave = "Hvem vil Bøddelen halshugge?";
		side = BORGER;
		prioritet = BØDDEL;
		aktiver(false);
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		return tv.vis(oppgave);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		spiller.drep(this);
		drep();
		return true;
	}
	
	@Override
	public void jul() {
		// TODO Auto-generated method stub
		super.jul();
		spiller.beskytt(this);
	}
}
