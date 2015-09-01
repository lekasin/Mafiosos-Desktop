package personer.roller;

import personer.Rolle;
import personer.Spiller;


public class Mafia extends Rolle {

	public static int SNIPER = 1, SABOTØR = 2, FLUKT = 3, FORFALSKER = 4;
	
	int antall = 1 , levende = 1;
			
	boolean snipe, saboter, flukt, forfalsk;

	public Mafia(){
		super("Mafia");
		oppgave = "Hvem vil Mafiaen drepe?";
		veiledning = "Mafia:\n" +
				"Mafiaen velger hver natt en person å drepe.\n" +
				"Når mafiaen har blitt enige og valgt, trykker du på vedkommendes navn for å drepe dem.";
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
	
	public void fjern(){
		antall--;
		levende--;
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
		snipe = true;
	}
	
	public void sabotage(){
		saboter = true;
	}

	public void saboter(Spiller spiller){
		saboter = false;
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
		for(Spiller s: tv.spillere().spillere())
			if(s.id(MAFIA) && s.funker()){
				s.setOffer(spiller);
				if(!this.spiller.lever())
					setSpiller(s);
			}
		
		if(saboter) saboter(spiller);
		else if(snipe){
			spiller.snipe(this);
			snipe = false;
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
