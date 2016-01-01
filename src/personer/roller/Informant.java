package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Informant extends Rolle {

	public Informant(){
		super("Informant");
        bilde = "informant";
		oppgave = "Hvem vil Informanten informere?";
		veiledning = "Informant:\n" +
				"Informanten velger hver natt en person å informere.\n" +
				"For å velge hvem som skal få informantens informasjon, trykker du på personens navn.\n" +
				"Når denne personen våkner på natten, får de opp på skjermen hvem mafiaen har pekt på i natt.\n" +
                "Hvis personen våknet før informanten på natten, får de ingen info.";
        guide = "Informanten våkner tidlig på natten, og er vitne til mafiaens drap, " +
                "men han er veldig glemsk, så han velger fort en person han vil informere, før han selv glemmer infoen. " +
                "Hvis den valgte personen har en rolle som våkner senere på natten, vil vedkommende få vite hvem mafiaen pekte på. " +
                "Er det ingen som dør den natten, vet den informerte hvem som egentlig ble pekt på, og kan bruke det til sin fordel. " +
                "Er informanten riktig heldig og peker på en beskyttende rolle, kan vedkommende velge å beskytte den han vet mafiaen pekte på. " +
                "Informantens info kan også ødelegges av Distré Didrik.";
		side = BORGER;
		prioritet = INFORMANT;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(this.spiller.skjult())
			spiller.rolle().informer(this, "\n\nDu har fått info om at Mafiaen har besøkt " + Spill.spillere.randomSpiller(null));
		else
			spiller.rolle().informer(this, "\n\nDu har fått info om at Mafiaen har besøkt " + Spill.spillere.finnRolle(MAFIA).offer());
		return true;
	}
}
