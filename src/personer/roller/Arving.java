package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Arving extends Rolle {

	Spiller riking;

	public Arving(){
		super("Arving");
		oppgave = "Hvem skal arvingen adopteres bort til?";
		side = BORGER;
		prioritet = ARVING;
	}

	public boolean arv(){
		if(lever() && riking != null && !riking.lever()){
				spiller.setRolle(riking.rolle());
				spiller.rolle().vekk();
				spiller.rolle().setSpiller(spiller);
				return true;
		}
		return false;
	}
	

	public Spiller riking(){
		return riking;
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(riking == null) {
			tv.vis(oppgave);
			if(informert) tv.leggtil(info);
			return oppgave;
		} else {
			aktiver(false);
			tv.vis("Arvingen har nå arvet rollen " + riking.rolle() + "!");
			if(informert) tv.leggtil(info);
			tv.toFront();
//			riking.rolle().aktiver(true);
			return "Arvingen våkner";
		}
	}

	@Override
	public boolean aktiv() {
		// TODO Auto-generated method stub
		if(arv() && lever())
			aktiver(true);
		return super.aktiv();
	}

	@Override
	public boolean evne(Spiller spiller) {
		riking = spiller;
		aktiver(false);
		return true;
	}
}
