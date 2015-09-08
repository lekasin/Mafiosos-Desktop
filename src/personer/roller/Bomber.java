package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bomber extends Rolle {
		
	public Bomber(){
		super("Bomber");
		oppgave = "Hvem vil Bomberen bombe?";
		veiledning = "Bomber:\n" +
				"Bomberen kan kun bruke rollen sin én gang, og må derfor velge med omhu.\n" +
				"Om bomberen vil vente og ikke plante bomben i natt, trykker du fortsett.\n" +
				"Når bomberen vil sprenge noen, trykker du på navnet til den han peker på." +
                "Neste dag vil bomben være plantet, og sprenger om ikke bomberen blir drept.";
		side = BORGER;
		prioritet = BOMBER;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert || !spiller.lever()) {
			offer = null;
			return false;		
		}

		if(spiller != null && lever())
			aktiver(false);

		return true;
	}
}
