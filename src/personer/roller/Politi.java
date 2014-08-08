package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Politi extends Rolle {

	int antall = 1, levende = 1;
	
	public Politi(){
		super("Politi");
		oppgave = "Hvem vil Politiet arrestere?";
		side = BORGER;
		prioritet = POLITI;
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
		if(blokkert)
			return false;
		
		if(spiller.side() < 1) {
			if(!spiller.rolle().blokkert()) spiller.blokker(this);
		}

		return true;
	}
}
