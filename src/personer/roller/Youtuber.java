package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Youtuber extends Rolle {

	String ut = "";

	public Youtuber(){
		super("Youtuber");
		oppgave = "Hvem vil Youtuberen filme?";
		veiledning = "Youtuber:\n" +
				"Youtuberen velger hver natt en person å filme.\n" +
				"Når youtuberen har valgt, trykker du på vedkommendes navn for å filme dem.\n" +
				"Neste dag vises det på skjermen hvor mange som har besøkt den valgte denne natten.\n" +
				"Hvis personen dør samme natt, vises også hvilken rolle personen hadde.";
        guide = "Youtuberen velger en person hver natt som han filmer fra avstand. Hvis noen besøker vedkommende får han det på film. " +
                "Men filmen viser kun hvor mange som besøker personen, ikke hvem. " +
                "Hvis den valgte personen blir drept, går Youtuberen nærmere og avslører identiten til den drepte. " +
                "Neste dag får hele landsbyen se Youtuberens film, og får infoen han har funnet. " +
                "Didrik kan så klart rote til alt av Youtuberens info.";
		side = BORGER;
		prioritet = YOUTUBER;
	}

	@Override
	public boolean evne(Spiller spiller) {
        return !blokkert;
    }
}