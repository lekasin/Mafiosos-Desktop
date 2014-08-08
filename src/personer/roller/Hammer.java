package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Hammer extends Rolle {

	int søk = 0;
	Spiller valgt;
	String ut;
	
	public Hammer() {
		super("Hammer");
		oppgave = "Hvem vil Hammer undersøke?";
		side = BORGER;
		prioritet = HAMMER;
		fortsett = false;
	}
	
	public Spiller valgt(){
		return valgt;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		if(blokkert || spiller.skjult()){
			tv.vis("Hammer ble blokkert");
			return false;
		}
		
		if(søk == 0 || valgt != spiller) {
			valgt = spiller; 
			tv.vis("Ikke nok research");
			søk = 1;
		}
		else if(valgt == spiller){
			int resultat = spiller.side();
			if(spiller.løgn()) resultat = resultat-(2*resultat);
			ut = spiller + " er ";
			if(resultat >= 0) ut += "ikke ";
			ut += "mafia!";
			tv.vis(ut);
			søk = 0;
			valgt = null;
		}

		return true;
	}
}
