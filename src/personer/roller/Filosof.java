package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Filosof extends Rolle {
	
	Spiller valgt;
	
	public Filosof(){
		super("Filosofen");
		oppgave = "Hvem vil Filosofen studere?";
		side = BORGER;
		prioritet = FILOSOF;
		fortsett = false;
	}
	
	public Spiller getValgt(){
		return valgt;
	}

	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		if(blokkert || spiller.skjult()) {
			tv.vis("Noen forhindrer Filosofen i å gjøre jobben sin"); 
			return false;
		}

		valgt = spiller;
		aktiver(false);
		if(this.spiller.skjult())
			tv.vis(spiller + " har rollen " + tv.spillere().randomSpiller(this.spiller, spiller).rolle());
		else
			tv.vis(spiller + " har rollen " + spiller.rolle());
		return true;
	}
	
	@Override
	public void jul() {
		// TODO Auto-generated method stub
		aktiver(true);
		super.jul();
	}
		
	public void sov(){
		offer = null;
		blokk = null;
		blokkert = false;
		snill = false;
		if(lever()) funker = true;
		if(valgt == null || !valgt.lever()) 
			aktiver(true);
	}
}
