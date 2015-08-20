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
			forby(spiller);
			kopi = true;
			tv.vis("Hvem vil Copycaten velge?");
			if(blokkert)
				return false;
			negativ = spiller;
			negativ.rolle().setSpiller(this.spiller);
			forbud2 = null;
			return true;
		}

		if(negativ != null && !blokkert) {
			Rolle rolle = negativ.rolle();

			if(rolle.pri() > UNDERCOVER && rolle.pri() < CUPID && !spiller.id(COPYCAT) && !spiller.id(BERIT) || rolle.id(SMITH)) {
				Spiller forb = rolle.forbud();
				rolle.forby(null);

				if(rolle.blokkert()) {
					Rolle r = rolle.hvemBlokk();
					rolle.rens(r);
					rolle.evne(spiller);
					negativ.blokker(r);
				} else
					rolle.evne(spiller);

				rolle.forby(forb);
			}
			negativ.rolle().setSpiller(negativ);
		}

		kopi = false;
		fortsett = true;
		forbud2 = this.spiller;
		return true;
	}
}
