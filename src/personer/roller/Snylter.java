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
			return false;
		}
		

		if(spiller.rolle().blokkert())
			return false;
		if(!spiller.lever()) 		drep();
		if(spiller.beskyttet())		this.spiller().beskytt(spiller.beskytter()); 
		if(spiller.forsvart()) 		this.spiller().forsvar(spiller.forsvarer());
		if(spiller.reddet()) 		this.spiller().redd(spiller.redning());
		if(spiller.løgn()) 			this.spiller().lyv(spiller.løgner());
		if(spiller.skjult()) 		this.spiller().skjul(spiller.skjuler());
		if(spiller.kløna()) 		this.spiller().kløn(spiller.kløne());
		
		return true;
	}
}
