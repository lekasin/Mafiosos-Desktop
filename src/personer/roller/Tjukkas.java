package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Tjukkas extends Rolle {

	public Tjukkas(){
		super("Tjukkas");
        bilde = "tjukkas";
        oppgave = "Hvem vil Tjukkasen blokkere?";
		veiledning = "Tjukkas:\n" +
				"Tjukkasen velger hver natt en person å blokkere.\n"+
				"Når Tjukkasen har valgt, trykker du på vedkommendes navn for å blokkere dem.\n" +
				"Personen som er blokkert, blir forhindret fra å utføre rollen sin den natten.";
        guide = "Tjukkasen er en person med mye makt. " +
                "Han våkner hver natt (legger seg egentlig bare veldig seint), og velger en person han vil sove hos. " +
                "Han legger seg så til å sove enten oppå personen, eller foran vedkommendes dør. " +
                "Dermed blir personen blokkert, og kan ikke utføre sin rolle denne natten. " +
                "Tjukkasen kan ikke blokkere samme person to netter på rad.";
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
