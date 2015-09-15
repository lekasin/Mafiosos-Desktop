package personer.roller;

import gui.Spill;
import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Sherlock extends Rolle {
	
	String ut = "";
	
	public Sherlock(){
		super("Sherlock");
		oppgave = "Hvem vil Sherlock etterforske?";
		veiledning = "Sherlock:\n" +
				"Sherlock velger hver natt en person å etterforske.\n" +
				"Når Sherlick har valgt, trykker du på vedkommendes navn for å etterforske dem.\n" +
				"Hvem denne personen har valgt i natt, kommer så opp på skjermen.\n" +
				"For å gå videre, trykker du på fortsett.";
		side = BORGER;
		prioritet = SHERLOCK;
		fortsett = false;
	}
	
	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}

	@Override
	public boolean evne(Spiller spiller) {
		TvUtil.toFront();
		
		if(this.spiller.skjult())
			ut += spiller + " har besøkt " + Spill.spillere.randomSpiller(null, spiller.offer());
		else if(spiller.offer() != null)
			ut += spiller + " har besøkt " + spiller.offer();
		else 
			ut += spiller + " har ikke besøkt noen";
		
			ut += (forsinkelse != null) ? " forrige natt.\n\n" : ".";
		
		if(blokkert){
			if(blokk != forsinkelse) ut = "Sherlock ble blokkert forrige natt!\n\n";
			TvUtil.vis("Sherlock er blokkert!");
			return false;
		}
		
		TvUtil.vis(ut);
		return true;
	}
}
