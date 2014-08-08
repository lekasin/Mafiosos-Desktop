package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bestevenn extends Rolle {

	int antall = 1, levende = 1;

	public Bestevenn(){
		super("Bestevenn");
		oppgave = "Bestevennene blir kjent!";
		side = BORGER;
		prioritet = BESTEVENN;
	}
	
	public int antall(){
		return antall;
	}
	
	public void fler(){
		antall++;
		levende++;
	}
	
	public boolean flere(){
		return levende > 1;
	}
	
	@Override
	public void drep(){
		if(!flere())
			lever = false;
		levende--;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		aktiver(false);
		return tv.vis(oppgave);
	}
}
