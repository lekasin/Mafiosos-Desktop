package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Rex extends Rolle {

	String ut = "";
	public Rex(){
		super("Rex");
        oppgave = "Hvem vil Rex snuse på?";
        veiledning = "Rex:\n" +
                "Rex velger hver natt en person å snuse på.\n" +
                "Når Rex har valgt, trykker du på vedkommendes navn for å sjekke dem.\n" +
                "Hvem som har besøkt denne personen i natt, kommer så opp på skjermen.\n" +
                "For å gå videre, trykker du på fortsett.";
        side = BORGER;
		prioritet = REX;
		fortsett = false;
	}
	
	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		
		ut += spiller + " har hatt besøk av disse";
		if(forsinkelse != null) 
			ut += " forrige natt";
		ut += ":" + tv.rex(spiller) ;
		
		if(blokkert){
			if(blokk != forsinkelse) ut = "Rex ble blokkert forrige natt!\n\n";
			tv.vis("Rex er blokkert!");
			return false;
		}
		tv.vis(ut);
		return true;
	}
}
