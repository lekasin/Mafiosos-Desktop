package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Heisenberg extends Rolle {
	
	Spiller offer = null;
	
	public Heisenberg(){
		super("Heisenberg");
		oppgave = "Hvem vil Heisenberg forgifte?";
		side = BORGER;
		prioritet = HEISENBERG;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(offer != null){
			if(snill)
				offer.snipe(this);
			else
				offer.drep(this);
			offer = null;
		}
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
}
