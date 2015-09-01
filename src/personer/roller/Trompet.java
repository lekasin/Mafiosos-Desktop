package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Trompet extends Rolle {

	public Trompet(){
		super("Trompet");
		oppgave = "Hvem vil Trompeten sprenge?";
		veiledning = "Trompet:\n" +
				"Trompeten er i ferd med å sprenges, og kan nå velge en person å ta med seg i eksplosjonen!\n" +
				"For å sprenge en person, trykker du på vedkommendes navn, og vedkommende blir da drept umiddelbart.\n" +
				"Trompeten velger offer helt på egenhånd, og det er ikke tillatt for andre å påvirke trompeten i denne fasen.";
		side = BORGER;
		prioritet = TROMPET;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		spiller.drep(this);
		return true;
	}
}