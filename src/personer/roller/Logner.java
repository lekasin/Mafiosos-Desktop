package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Logner extends Rolle {

	public Logner(){
		super("Løgner");
        veiledning = "Løgner:\n" +
                "Løgneren velger hver natt en person å forfalske identiteten til.\n" +
                "Når løgneren har valgt, trykker du på vedkommendes navn for å forfalske identiteten dems.\n" +
                "Forfalskede personer vil fremstå som om de er på motsatt side av det de egentlig er, ved henrettelse neste dag. " +
                "Mafiaer fremstår som borgere, og borgere som mafia.";
        oppgave = "Hvem vil Løgneren forfalske?";
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
