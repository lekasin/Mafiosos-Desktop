package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bedrager extends Rolle {
	
	public Bedrager(){
		super("Bedrageren");
		oppgave = "Bedrageren gjør seg kjent med mafiaen";
		side = BORGER;
		prioritet = BEDRAGER;
		skjerm = true;
	}

	@Override
	public String oppgave() {
		if(klonet)
			super.oppgave();
		aktiver(false);
		tv.toFront();
		if(spiller.klonet())
			informer(spiller.smith(), "\n\n" + spiller + " er nå en Smith!");
		
		tv.bedrag();
		if(informert) tv.leggtil(info);
		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
