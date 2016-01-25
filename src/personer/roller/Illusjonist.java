package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Illusjonist extends Rolle {

	public Illusjonist(){
		super("Illusjonist");
        bilde = "illusjonist";
		oppgave = "Hvor vil Illusjonisten gjemme seg?";
		veiledning = "Illusjonist:\n" +
				"Illusjonisten velger hver natt en person å gjemme seg hos.\n" +
				"For å gjemme illusjonisten, trykker du på navnet til den han velger.\n" +
                "Illusjonisten dør da kun hvis personen han gjemmer seg hos blir drept. " +
                "Ikke hvis mafiaen peker på illusjonisten selv.";
        guide = "Illusjonisten gjør et forsvinningsnummer hver natt og dag, hvor han gjemmer seg hos en assistent. " +
                "Han velger en spiller hver natt, som han så gjemmer seg hos. " +
                "Illusjonisten kan da ikke drepes direkte (ved å pekes på), men dør hvis den personen han har valgt blir forsøkt drept. " +
                "Det samme gjelder på dagen. Han vil alltid fremstå som beskyttet ved henrettelse, men dør om den han gjemmer seg hos blir henrettet. " +
                "Gjemmer han seg hos en person med en drapsrolle, kan vedkommende drepe han på vanlig måte. Illusjonisten kan heller ikke leges.";
		side = BORGER;
		prioritet = ILLUSJONIST;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(!(!this.spiller.lever() && this.spiller.drapsmann() == spiller.rolle() && this.spiller.drapsmann().offer() == this.spiller))
			this.spiller.beskytt(this);
				
		if(spiller.drapsmann() != null) this.spiller.snipe(spiller.drapsmann());

		this.spiller.snås(this);
		return true;
	}
}
