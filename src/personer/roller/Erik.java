package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Erik extends Rolle {

	public Erik(){
		super("Barnslige Erik");
		oppgave = "Hvem vil Erik ha som barnevakt?";
		side = BORGER;
		prioritet = ERIK;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		spiller.rolle().informer(this, "\n\n" + spiller + " sitter barnevakt, og kan kun besøke naboen.");
		return true;
	}
}