package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Didrik extends Rolle {

	public Didrik(){
		super("Distré Didrik");
		oppgave = "Hvem vil Didrik assistere?";
		veiledning = "Distré Didrik:\n" +
				"Didrik velger hver natt en person å assistere.\n" +
				"Didrik peker på vanlig måte, og du klikker på vedkommendes navn.\n" +
                "Hvis denne personen har en inforolle, vil infoen vedkommende får være feil.";
        side = BORGER;
		prioritet = DIDRIK;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		spiller.skjul(this);
		return true;
	}
}