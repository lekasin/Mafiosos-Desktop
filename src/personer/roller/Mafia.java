package personer.roller;

import personer.Rolle;
import personer.Spiller;


public class Mafia extends Rolle {

	int antall = 1 , levende = 1;
	boolean sniper = false, sabotage = false;
	
	public Mafia(){
		super("Mafia");
		oppgave = "Hvem vil Mafiaen drepe?";
		side = MAFIOSO;
		prioritet = MAFIA;
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
	
	public void snipe(){
		sniper = true;
	}
	
	public void sabotage(){
		sabotage = true;
	}
	
	public void saboter(Spiller spiller){
		sabotage = false;
		fortsett(false);
		spiller.blokker(this);
		oppgave();
	}
	
	
	
	@Override
	public void jul() {
		// TODO Auto-generated method stub
		if(offer != null) offer.snipe(this);
		super.jul();
	}

	@Override
	public boolean pek(Spiller spiller){
		if(spiller == null) return false;
		
		offer = spiller;
		if(sabotage) saboter(spiller);
		else if(sniper){
			spiller.snipe(this);
			sniper = false;
		}
		else if(snill) spiller.snipe(this);
		else evne(spiller);
		return true;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		fortsett(true);
		if(blokkert && !(blokk.id(POLITI) && offer.id(POLITI)))
			return false;
		else
			spiller.drep(this);
		return true;
	}
}
