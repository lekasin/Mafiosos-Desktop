package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bomber extends Rolle {
	
	public Bomber(){
		super("Bomber");
		oppgave = "Hvem vil Bomberen bombe?";
		side = BORGER;
		prioritet = BOMBER;
	}
		
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		tv.vis(oppgave);
		if(informert) tv.leggtil(info);
		return oppgave;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;			
		
		if(spiller != null && lever()) {
			tv.leggVed("\nBomben er plantet!!!");
			aktiver(false);
		}
		return true;
	}
}
