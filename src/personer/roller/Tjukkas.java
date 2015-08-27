package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Tjukkas extends Rolle {

	public Tjukkas(){
		super("Tjukkas");
		oppgave = "Hvem vil Tjukkasen blokkere?";
		veiledning = "Tjukkas:\n" +
				"Tjukkasen velger hver natt en person å blokkere.\n"+
				"Når Tjukkasen har valgt, trykker du på vedkommendes navn for å blokkere dem.\n" +
				"Personen som er blokkert, blir forhindret fra å utføre rollen sin den natten.";
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
