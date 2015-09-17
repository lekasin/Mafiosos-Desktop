package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Bedrager extends Rolle {
	
	public Bedrager(){
		super("Bedrageren");
		oppgave = "Bedrageren gjør seg kjent med mafiaen";
		veiledning = "Bedrageren:\n" +
				"Bedrageren våkner bare første natt, og får da opp mafiaenes navn på skjermen.\n" +
                "Han kan ikke velge noen, så for å gå videre trykker du på fortsett.";
		side = BORGER;
		prioritet = BEDRAGER;
		skjerm = true;
	}

	@Override
	public String oppgave() {
        aktiver(false);
		TvUtil.toFront();
        if (klonet)
            return super.oppgave();

		TvUtil.visMafiaer();
		if(informert) TvUtil.leggTil(info);
		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
