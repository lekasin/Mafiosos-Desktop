package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Trompet extends Rolle {

	public Trompet(){
		super("Trompet");
        bilde = "trompet";
        oppgave = "Hvem vil Trompeten sprenge?";
		veiledning = "Trompet:\n" +
				"Trompeten er i ferd med å sprenges, og kan nå velge en person å ta med seg i eksplosjonen!\n" +
				"For å sprenge en person, trykker du på vedkommendes navn, og vedkommende blir da drept umiddelbart.\n" +
				"Trompeten velger offer helt på egenhånd, og det er ikke tillatt for andre å påvirke trompeten i denne fasen.";
        guide = "Trompeten er egentlig en selvmordsbomber, som ikke slår til før han må. " +
                "Han fungerer på den måten at hvis trompeten blir drept ved avstemming, eksploderer han, og kan velge å ta med seg en annen person i smellet. " +
                "Dette fører til at både han og hans offer dør. Trompeten prøver så klart å ta med seg en mafia i døden. " +
                "Resten av landsbyen skal ikke ha noen innvirkning på Trompetens valg etter at han er stemt ut. " +
                "Dør Trompeten på natten, får han ikke brukt evnen sin.";
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