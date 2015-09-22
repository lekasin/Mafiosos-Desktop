package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Anarkist extends Rolle {
	
	public Anarkist(){
		super("Anarkist");
		oppgave = "Anarkisten våkner";
        guide = "Anarkisten takler ikke fred og struktur, og er dermed kun fornøyd så lenge det finnes en Mafia som lager kaos i landsbyen. " +
                "Hvis alle mafiaer dør, forgifter han drikkevannet til hele landsbyen, og dreper alle. Altså må Anarkisten drepes før mafiaen, " +
                "ellers taper landsbyen. Våkner aldri på natten.";
		side = NØYTRAL;
		prioritet = ANARKIST;
		aktiver(false);
	}

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
