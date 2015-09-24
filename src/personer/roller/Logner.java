package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Logner extends Rolle {

	public Logner(){
		super("Løgner");
        oppgave = "Hvem vil Løgneren forfalske?";
        veiledning = "Løgner:\n" +
                "Løgneren velger hver natt en person å forfalske identiteten til.\n" +
                "Når løgneren har valgt, trykker du på vedkommendes navn for å forfalske identiteten dems.\n" +
                "Forfalskede personer vil fremstå som om de er på motsatt side av det de egentlig er, ved henrettelse neste dag. " +
                "Mafiaer fremstår som borgere, og borgere som mafia.";
        guide = "Løgneren er på mafiaen sitt lag, og fremstår som mafia ved henrettelse, " +
                "men han er god på å forfalske folks identiteter, og velger en person hver natt som han vil forfalske. " +
                "Vedkommende kommer til å fremstå som det motsatte av det han er ved henrettelse dagen etter. " +
                "Altså vil en mafia fremstå som borger, og borgere som mafia. " +
                "Løgneren kan også peke på seg selv. Hammer vil også få feil resultat.";
		side = MAFIOSO;
		prioritet = LØGNER;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		spiller.lyv(this);
		return true;
	}
}
