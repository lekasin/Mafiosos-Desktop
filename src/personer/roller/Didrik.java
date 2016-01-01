package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Didrik extends Rolle {

	public Didrik(){
		super("Distré Didrik");
        bilde = "didrik";
		oppgave = "Hvem vil Didrik assistere?";
		veiledning = "Distré Didrik:\n" +
				"Didrik velger hver natt en person å assistere.\n" +
				"Didrik peker på vanlig måte, og du klikker på vedkommendes navn.\n" +
                "Hvis denne personen har en inforolle, vil infoen vedkommende får være feil.";
        guide = "Didrik jobber som assistent for diverse personer i landsbyen. " +
                "Han peker hver natt på en person han vil assistere den natten. Har denne personen en inforolle, vil Didrik rote til infoen, og gi et helt tilfeldig resultat. " +
                "Altså vil Rex få opp feil besøk, og drømmeren får tre helt tilfeldige navn, osv. " +
                "I tillegg vil Didrik behandle sakspapirene for denne personen hvis han blir henrettet, " +
                "slik at landsbyen ikke får vite om vedkommende var mafia eller ikke ved henrettelse neste dag. " +
                "Didrik er på borgernes side, til tross for at han stort sett ødelegger for landsbyen.";
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