package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Rex extends Rolle {

	String ut = "";
	public Rex(){
		super("Rex");
        bilde = "rex";
        oppgave = "Hvem vil Rex snuse på?";
        veiledning = "Rex:\n" +
                "Rex velger hver natt en person å snuse på.\n" +
                "Når Rex har valgt, trykker du på vedkommendes navn for å sjekke dem.\n" +
                "Hvem som har besøkt denne personen i natt, kommer så opp på skjermen.\n" +
                "For å gå videre, trykker du på fortsett.";
        guide = "Politihunden Rex snuser rundt i landsbyen, og hver natt velger han en person han vil snuse på. " +
                "Rex får så beskjed ved hjelp av luktesansen (Får opp på skjermen), hvilke andre personer som har besøkt denne personen samme natt. " +
                "På den måten kan Rex finne ut noe om hva som har skjedd med denne personen i natt, og muligens også hvem som er hva?";
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
		TvUtil.toFront();
		
		ut += spiller + " har hatt besøk av disse";
		if(forsinkelse != null) 
			ut += " forrige natt";
		ut += ":" + TvUtil.hentRextLukter(spiller);
		
		if(blokkert){
			if(blokk != forsinkelse) ut = "Rex ble blokkert forrige natt!\n\n";
			ut = "Rex er blokkert!";
		}
		TvUtil.vis(ut);
		return true;
	}

    @Override
    public String rapport() {
        if (ut.isEmpty())
            return super.rapport();
        return super.rapport() + "\nInfo: " + ut;
    }
}
