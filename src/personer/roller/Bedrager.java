package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Bedrager extends Rolle {
	
	public Bedrager(){
		super("Bedrageren");
        bilde = "bedrager";
		oppgave = "Bedrageren gjør seg kjent med mafiaen";
		veiledning = "Bedrageren:\n" +
				"Bedrageren våkner bare første natt, og får da opp mafiaenes navn på skjermen.\n" +
                "Han kan ikke velge noen, så for å gå videre trykker du på fortsett.";
        guide = "Bedrageren er i utgangspunktet en borger, men første natten får han vite hvem som er mafia, uten at mafiaen ser hvem han er. " +
                "Hans rolle resten av spillet er rett og slett å prøve å la mafiaen vinne, til tross for at han egentlig er en borger. " +
                "Han må prøve å alliere seg med mafiaen på dagen, for ikke å bli drept, uten å gjøre dette for åpenlyst. " +
                "Bedrageren vinner med mafiaen.";
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
