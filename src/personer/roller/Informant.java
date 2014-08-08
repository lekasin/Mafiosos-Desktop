package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Informant extends Rolle {

	public Informant(){
		super("Informant");
		oppgave = "Hvem vil Informanten informere?";
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
