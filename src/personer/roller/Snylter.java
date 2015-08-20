package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Snylter extends Rolle {

	Spiller valgt;
	String ut;
	
	public Snylter() {
		super("Snylter");
		oppgave = "Hvem vil Snylteren snylte på?";
		side = BORGER;
		prioritet = SNYLTER;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert){
			spiller.pek(null);
			return false;
		}
		
		return true;
	}
}
