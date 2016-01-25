package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Jente extends Rolle {

	public Jente(){
		super("Liten Jente");
        bilde = "litenjente";
		oppgave = "Liten jente våkner";
        guide = "Den lille jenta er nysgjerrig, og klarer ikke å holde øynene lukket på natten. " +
                "Hun kan dermed smugtitte når mafiaen peker ut sitt offer, og kan med det se hvem som er mafia! Men hun må passe seg! " +
                "Oppdager mafiaen henne, kan hun lett drepes før hun rekker å overbevise landsbyen om å stoppe dem.";
		side = BORGER;
		prioritet = JENTE;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		return false;
	}

	@Override
	public void jul() {
		super.jul();
		spiller.beskytt(this);
		spiller.snås(this);
	}
}
