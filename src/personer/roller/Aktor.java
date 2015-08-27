package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Aktor extends Rolle {

	public Aktor(){
		super("Aktor");
		oppgave = "Hvem vil Aktor tiltale?";
		veiledning = "Aktor:\n" +
                "Aktor kan kun bruke rollen sin én gang, og må derfor velge med omhu.\n" +
                "Om aktor vil vente og ikke anklage noen i natt, trykker du fortsett.\n" +
                "Når aktor vil anklage noen, trykker du på navnet til den han peker på.\n" +
                "Denne personen vil da sitte på tiltalebenken neste dag, og dagen består kun av vedkommends forsvarstale og en avstemning.";
		side = BORGER;
		prioritet = AKTOR;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			offer = null;
			return false;			
		}
		
		if(spiller != null) {
			spiller.beskytt(this);
			tv.leggVed("\n" + spiller + " står på TILTALEBENKEN!");
			aktiver(false);
		}
		return true;
	}
}
