package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Heisenberg extends Rolle {
	
	Spiller offer = null;
	String giftRapport = "";

	public Heisenberg(){
		super("Heisenberg");
		oppgave = "Hvem vil Heisenberg forgifte?";
		side = BORGER;
		prioritet = HEISENBERG;
	}
	
	@Override
	public String oppgave() {
		giftRapport = "";
		if(offer != null){
			if(snill)
				offer.snipe(this);
			else
				offer.drep(this);
			giftRapport = "Heisenbergs gift svekker " + offer + "(" + offer.rolle() + ")\n";
		}
		offer = null;
		return super.oppgave();
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(!spiller.beskyttet() || snill)
			offer = spiller;
		return true;
	}
	
	@Override
	public String rapport() {
		String ut = giftRapport + super.rapport();
		return ut;
	}
	
	public String getRapport(){
		return giftRapport;
	}
}
