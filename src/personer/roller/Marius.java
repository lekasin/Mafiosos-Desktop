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
        guide = "Marius velger en person på natta, og skynder seg så til Nilz&Otto Grafisk AS. " +
                "Der trykker han opp flyers, som påstår at personen han valgte er mafia. Når landsbyen våkner, får de se flyerne og hvem de handler om. " +
                "Dette gjør automatisk vedkommende til en mistenkt neste dag, og personen har allerede 2 anonymer stemmer fra Marius ved avstemning. " +
                "Hvis ikke landsbyen vil bestemme noe, dør den mistenkte. " +
                "Landsbyen får så klart ikke vite hvem Marius er, og han kan si og stemme som han vil på dagen.";
		side = BORGER;
		prioritet = MARIUS;
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
            spiller.trykkOppFlyers(this);
        }
					
		return true;
	}
}
