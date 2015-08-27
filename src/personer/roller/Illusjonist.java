package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Illusjonist extends Rolle {

	public Illusjonist(){
		super("Illusjonist");
		oppgave = "Hvor vil Illusjonisten gjemme seg?";
		veiledning = "Illusjonist:\n" +
				"Illusjonisten velger hver natt en person å gjemme seg hos.\n" +
				"For å gjemme illusjonisten, trykker du på navnet til den han velger.\n" +
                "Illusjonisten dør da kun hvis personen han gjemmer seg hos blir drept. " +
                "Ikke hvis mafiaen peker på illusjonisten selv.";
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

		this.spiller.forsvar(this);
		return true;
	}
}
