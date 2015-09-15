package personer.roller;

import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Hammer extends Rolle {

	int søk = 0;
	Spiller valgt;
	String ut;
	
	public Hammer() {
		super("Hammer");
		oppgave = "Hvem vil Hammer undersøke?";
		veiledning = "Hammer:\n" +
				"Hammer undersøker personer for å finne ut hvilken rolle de har, men han får ikke svar før etter to netter med undersøkelser.\n" +
				"For å undersøke en person, trykker du på navnet til personen Hammer velger. Resultatet av undersøkelsen kommer da opp på skjermen.\n" +
				"Første natt Hammer undersøker en person, kommer det opp at han ikke har gjort nok research. " +
                "For å få et resultat, må hammer undersøke samme person to netter på rad. Da vises vedkommendes rolle på skjermen.\n" +
				"Personen Hammer undersøker for øyeblikket vil ha navn i grønt, neste natt.";
		side = BORGER;
		prioritet = HAMMER;
		fortsett = false;
	}
	
	public Spiller valgt(){
		return valgt;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		TvUtil.toFront();
		if(blokkert){
			TvUtil.vis("Hammer ble blokkert");
			return false;
		}
		
		if(søk == 0 || valgt != spiller) {
			valgt = spiller; 
			TvUtil.vis("Ikke nok research");
			søk = 1;
		}
		else if(valgt == spiller){
			int resultat = spiller.side();
			if(spiller.løgn()) resultat = resultat-(2*resultat);
			ut = spiller + " er ";
			if(resultat >= 0) ut += "ikke ";
			ut += "mafia!";
			TvUtil.vis(ut);
			søk = 0;
			valgt = null;
		}

		return true;
	}
}
