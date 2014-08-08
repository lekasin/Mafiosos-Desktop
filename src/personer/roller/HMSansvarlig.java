package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class HMSansvarlig extends Rolle {

	public HMSansvarlig(){
		super("HMS-Ansvarlig");
		oppgave = "Hvem vil HMS-Ansvarlig redde?";
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
