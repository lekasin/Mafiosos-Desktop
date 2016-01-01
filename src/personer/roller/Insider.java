package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Insider extends Rolle {
	
	Politi politi;
	
	public Insider(Politi politi){
		super("Insider");
        bilde = "insider";
        oppgave = "Insideren våkner";
        guide = "En insider er mafiaens utgave av undercoveren, og er altså mafiaens inside man hos politiet. " +
                "Altså våkner han sammen med politiet og gir seg ut for å være en av dem, men spiller hele tiden på mafiaens lag. " +
                "Hans mål blir å få politiet til å arrestere feil personer, for å vinne sammen med mafiaen.";
		side = FAKEBORGER;
		prioritet = INSIDER;
		aktiver(false);
		this.politi = politi;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		return false;
	}
	
	@Override
	public void blokker(Rolle blokk){
		politi.blokker(blokk);
		blokkert = true;
		rens();
	}
}
