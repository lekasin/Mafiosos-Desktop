package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class HMSansvarlig extends Rolle {

	public HMSansvarlig(){
		super("HMS-Ansvarlig");
        bilde = "hms";
		oppgave = "Hvem vil HMS-Ansvarlig redde?";
        veiledning = "HMS-Ansvarlig:\n" +
                "HMS-ansvarlig velger hver natt en person å redde.\n" +
                "Når HMSen har valgt, trykker du på vedkommendes navn for å redde dem.\n" +
                "Hvis denne personen dør i løpet av natten, vil vedkommende stå opp igjen neste kveld. " +
                "Etter henrettelsen.";
        guide = "HMS-ansvarlig velger en person på natten, som han velger å overvåke neste dag. " +
                "Hvis vedkommende våkner opp død, jobber han med livredning hele dagen, i et forsøk på å redde personen. " +
                "På slutten av dagen lykkes han i gjenopplivningen, og vedkommende kommer da til live igjen. " +
                "Altså er personen død en dag, men overlever likevel, og kan fortsette spillet.";
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
