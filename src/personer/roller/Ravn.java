package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Ravn extends Rolle {

	String ut;
	Spiller gjest;
	
	public Ravn(){
		super("Ravn");
		oppgave = "Hvem vil Ravn ta med på Den Blå Fisk?";
        veiledning = "Ravn:\n" +
                "Ravn velger hver natt en person som sendes på Den blå fisk for å spise fiskesuppe neste dag.\n" +
                "Når Ravn har valgt, trykker du på vedkommendes navn for å sende dem på fiskesuppa.\n" +
                "Personen som spiser suppe, kan ikke snakke eller stemme neste dag.";
        guide = "Hver natt velger Ravn en person som dagen etter skal være med ham på Den Blå Fisk og spise kona Friduns fiskesuppe. " +
                "Koselig er det, men når den utvalgte da er på restaurant hele dagen, " +
                "får ikke vedkommende deltatt i hverken diskusjon eller avstemminger som de holder på med hjemme i landsbyen. " +
                "Altså vil personer som spiser fiskesuppe ikke kunne prate eller stemme neste dag.";
		side = BORGER;
		prioritet = RAVN;
	}
	
	@Override
	public String oppgave() {
		gjest = null;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		
		if(spiller != offer)
			gjest = spiller;
		if(gjest != null)
			ut = "";
		
		if(this.spiller.lever() && spiller.lever() && !blokkert) {
            spiller.inviterPåSuppe(this);
        }

		return true;
	}
}
