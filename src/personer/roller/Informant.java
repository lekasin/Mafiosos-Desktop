package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Informant extends Rolle {

	public Informant(){
		super("Informant");
		oppgave = "Hvem vil Informanten informere?";
		veiledning = "Informant:\n" +
				"Informanten velger hver natt en person å informere.\n" +
				"For å velge hvem som skal få informantens informasjon, trykker du på personens navn.\n" +
				"Når denne personen våkner på natten, får de opp på skjermen hvem mafiaen har pekt på i natt.\n" +
                "Hvis personen våknet før informanten på natten, får de ingen info.";
		side = BORGER;
		prioritet = INFORMANT;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(this.spiller.skjult())
			spiller.rolle().informer(this, "\n\nDu har fått info om at Mafiaen har besøkt " + tv.spillere().randomSpiller(null));
		else
			spiller.rolle().informer(this, "\n\nDu har fått info om at Mafiaen har besøkt " + ((Mafia)tv.spillere().finnRolle(MAFIA)).offer());
		return true;
	}
}
