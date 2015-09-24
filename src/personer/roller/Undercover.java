package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Undercover extends Rolle {

	Mafia mafia;
	
	public Undercover(Mafia mafia){
		super("Undercover");
        oppgave = "Undercoveren våkner";
        guide = "Undercoveren er en politimann som er på Undercover-oppdrag, og har infiltrert mafiaen. " +
                "Han våkner hver natt sammen med mafiaen, men er altså egentlig en borger, og prøver derfor å få de andre mafiaene drept. " +
                "Men dette må gjøres forsiktig, for om de øvrige mafiaene finner ut hvem han er, kan de drepe ham. " +
                "Men er han riktig god, kan Undercoveren få med seg andre mafiaer til å drepe en annen mafia de tror er undercover. " +
                "Undercoveren vil for etterforskere som Hammer se ut som en mafia, men er egentlig en borger, og blir presentert som det om han dør på dagen.";
		side = FAKEMAFIA;
		prioritet = UNDERCOVER;
		aktiver(false);
		this.mafia = mafia;
	}

	@Override
	public boolean evne(Spiller spiller) {
		return false;
	}
	
	@Override
	public void blokker(Rolle blokk){
		mafia.blokker(blokk);
		blokkert = true;
		rens();
	}	
}
