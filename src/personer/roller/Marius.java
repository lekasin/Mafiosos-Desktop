package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Marius extends Rolle {

	String ut;
	Spiller gjest;
	
	public Marius(){
		super("Marius");
		oppgave = "Hvem vil Marius trykke opp?";
		veiledning = "Marius:\n" +
				"Marius velger hver natt en person å trykke opp flyers om.\n" +
				"Når Marius har valgt, trykker du på vedkommendes navn for å trykke opp flyers.\n" +
				"Personen som trykkes opp vil vises på flyers dagen etter, " +
                "og er automatisk nominert med to stemmer.";
		side = BORGER;
		prioritet = MARIUS;
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
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
            spiller.trykkOppFlyers();
        }
					
		return true;
	}
}
