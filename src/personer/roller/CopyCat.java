package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class CopyCat extends Rolle {
	
	Boolean kopi = false;
	Spiller negativ;
	
	public CopyCat(){
		super("CopyCat");
		oppgave = "Hvem vil Copycaten kopiere?";
		side = BORGER;
		prioritet = COPYCAT;
		fortsett = false;
	}

	@Override
	public String oppgave() {
		fortsett = false;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(spiller != offer)
			return false;
		
		if(!kopi) {
			if(blokkert){
				tv.vis("Hvem vil Copycaten velge?");
				return false;
			}
			tv.vis("Hvem vil Copycaten velge?");
			negativ = spiller;
			negativ.rolle().setSpiller(this.spiller);
			kopi = true;
			forbud2 = null;
			return true;
		}
		if(negativ.rolle().pri() < Rolle.SPECIAL && negativ.rolle().pri() > Rolle.JESUS) {
			Spiller forb = negativ.rolle().forbud();
			negativ.rolle().forby(null);
			
			if(negativ.rolle().blokkert()) {
				Rolle r = negativ.rolle().hvemBlokk();
				negativ.rolle().rens(r);
				negativ.rolle().evne(spiller);
				negativ.blokker(r);
			} else
				negativ.rolle().evne(spiller);
			
			negativ.rolle().forby(forb);
		}
		negativ.rolle().setSpiller(negativ);
		kopi = false;
		fortsett = true;
		forbud2 = this.spiller;
		return true;
	}
}
