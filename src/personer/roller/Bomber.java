package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bomber extends Rolle {
		
	public Bomber(){
		super("Bomber");
        bilde = "bomber";
		oppgave = "Hvem vil Bomberen bombe?";
		veiledning = "Bomber:\n" +
				"Bomberen kan kun bruke rollen sin én gang, og må derfor velge med omhu.\n" +
				"Om bomberen vil vente og ikke plante bomben i natt, trykker du fortsett.\n" +
				"Når bomberen vil sprenge noen, trykker du på navnet til den han peker på." +
                "Neste dag vil bomben være plantet, og sprenger om ikke bomberen blir drept.";
        guide = "Bomberen våkner hver natt, og velger om han har lyst til å bombe noen, men han har bare én bombe, og må velge med omhu. " +
                "Når han vil slå til, velger han en person han ønsker å plassere bomben hos. " +
                "Neste dag får landsbyen vite at bomba er plantet, og timeren tikker. Landsbyen må da prøve å drepe bomberen, for å desarmere bomben, innen 2 minutter! " +
                "Landsbyen vet ikke hvem bomberen er, og heller ikke hvem bomben ligger hos, men de vet de har dårlig tid! " +
                "Det er ingen forsvarstaler, men den første med flertall av stemmene dør. Dreper de bomberen, dør kun han. " +
                "Treffer de ikke bomberen, dør også personen som er bombet. " +
                "Dreper de ingen, dør personen som er bombet, i tillegg til alle som besøkte vedkommende natten før." +
                "Landsbyen får alltid vite om de som dør er mafia eller ikke. Bomberen våkner ikke lenger om natten etter at bomben er brukt.";
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
