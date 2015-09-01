package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Politi extends Rolle {

	int antall = 1, levende = 1;
	
	public Politi(){
		super("Politi");
		oppgave = "Hvem vil Politiet arrestere?";
		veiledning = "Politi:\n" +
				"Politiet velger hver natt en person å arrestere.\n" +
				"Når politiet har valgt, trykker du på vedkommendes navn for å arrestere dem.\n" +
                "Hvis den arrestert har en drapsrolle, får ikke vedkommende drept noen denne natten.";
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
	
	@Override
	public boolean pek(Spiller spiller) {
		for(Spiller s: tv.spillere().spillere())
			if(s.id(POLITI) && s.funker()){
				s.setOffer(spiller);
				if(!this.spiller.lever())
					setSpiller(s);
			}
		
		return super.pek(spiller);
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
