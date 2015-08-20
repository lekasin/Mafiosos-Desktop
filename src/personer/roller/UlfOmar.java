package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class UlfOmar extends Rolle {

	Spiller valgt;
	String ut;
	
	public UlfOmar() {
		super("Ulf Omar");
		oppgave = "Hvem vil Ulfie-O besøke?";
		side = BORGER;
		prioritet = ULF;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert){
			return false;
		}
		
		Rolle rolle = spiller.rolle();

		if(rolle.pri() > UNDERCOVER && rolle.pri() < ULF && !spiller.id(COPYCAT) && !spiller.id(BERIT)) {
			Spiller forb = rolle.forbud();
			rolle.forby(null);
		
			if(spiller.rolle().blokkert()) {
				Rolle r = spiller.rolle().hvemBlokk();
				spiller.rolle().rens(r);
				spiller.rolle().evne(this.spiller);
				spiller.blokker(r);
			}
			else
				spiller.rolle().evne(this.spiller);
		
			rolle.forby(forb);
		}

		return true;
	}
}
