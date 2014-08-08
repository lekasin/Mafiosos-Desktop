package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Tjukkas extends Rolle {

	public Tjukkas(){
		super("Tjukkas");
		oppgave = "Hvem vil Tjukkasen blokkere?";
		side = BORGER;
		prioritet = TJUKKAS;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.blokker(this);
		if(spiller.rolle() instanceof Zombie && spiller.offer() != null) 
			spiller.offer().vekk();
		return true;
	}
}
