package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Youtuber extends Rolle {

	String ut = "";

	public Youtuber(){
		super("Youtuber");
		oppgave = "Hvem vil Youtuberen filme?";
		side = BORGER;
		prioritet = YOUTUBER;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			pek(null);
			return false;
		}
		
		return true;
	}
}