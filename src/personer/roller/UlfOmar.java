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
//			tv.vis("Hammer ble blokkert");
			return false;
		}
		
		if((spiller.rolle().pri() > TJUKKAS && spiller.rolle().pri() < SPECIAL) || spiller.rolle().pri() == SNÅSA)
			if(spiller.rolle().blokkert()) {
				Rolle r = spiller.rolle().hvemBlokk();
				spiller.rolle().rens(r);
				spiller.rolle().evne(this.spiller);
				spiller.blokker(r);
			}
			else
				spiller.rolle().evne(this.spiller);

		return true;
	}
}
