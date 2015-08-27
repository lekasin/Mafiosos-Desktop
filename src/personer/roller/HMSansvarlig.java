package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class HMSansvarlig extends Rolle {

	public HMSansvarlig(){
		super("HMS-Ansvarlig");
		oppgave = "Hvem vil HMS-Ansvarlig redde?";
        veiledning = "HMS-Ansvarlig:\n" +
                "HMS-ansvarlig velger hver natt en person å redde.\n" +
                "Når HMSen har valgt, trykker du på vedkommendes navn for å redde dem.\n" +
                "Hvis denne personen dør i løpet av natten, vil vedkommende stå opp igjen neste kveld. " +
                "Etter henrettelsen.";
        side = BORGER;
		prioritet = HMS;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.redd(this);
		return true;
	}
}
