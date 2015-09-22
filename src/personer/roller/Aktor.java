package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Aktor extends Rolle {

	public Aktor(){
		super("Aktor");
		oppgave = "Hvem vil Aktor tiltale?";
		veiledning = "Aktor:\n" +
                "Aktor kan kun bruke rollen sin én gang, og må derfor velge med omhu.\n" +
                "Om aktor vil vente og ikke tiltale noen i natt, trykker du fortsett.\n" +
                "Når aktor vil tiltale noen, trykker du på navnet til den han peker på.\n" +
                "Denne personen vil da sitte på tiltalebenken neste dag, og dagen består kun av vedkommends forsvarstale og en avstemning.";
		guide = "Aktor våkner hver natt, og velger om han har lyst til å tiltale noen. Han kan i utgangspunktet bare gjøre dette én gang, og må derfor velge med omhu. " +
                "Når aktor vil slå til velger han en person som blir satt på tiltalebenken. Neste runde vil kun den valgte personen kunne snakke. " +
                "Det blir altså én forsvarstale på ett minutt. Ved slutten av talen må landsbyen stemme. De kan kun stemme for eller imot at den tiltalte skal drepes. " +
                "Hvis flertallet stemmer for henrettelse, blir den tiltalte drept og det blir ny natt. Stemmer flertallet imot, dør ingen, og landsbyen sovner igjen. " +
                "Hvis Aktor klarer å drepe en mafia, får han en ny sjanse til å tiltale, men ellers våkner han ikke på natten etter denne runden.";
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
			aktiver(false);
		}
		return true;
	}
}
