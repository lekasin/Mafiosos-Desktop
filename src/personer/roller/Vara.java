package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Vara extends Rolle {

	public Vara(){
		super("VaraMafia");
        oppgave = "Varamafiaen våkner";
        guide = "VaraMafiaen er, som navnet tilsier, en person som erstatter mafiaen om noe skulle skje. " +
                "I praksis betyr det at varaen ikke våkner før vi finner ut at en mafia dør (blir drept ved avstemming). " +
                "Fra og med neste natt, blir varaen mafia, og våkner dermed når mafiaen våkner. " +
                "Før dette vet ikke mafiaen hvem varaen er, eller motsatt. " +
                "For undersøkende roller som Hammer, eller ved henrettelse på dagen, avsløres Varaen som mafia, uavhengig av om han har steppet inn eller ikke.";
		side = MAFIOSO;
		prioritet = VARA;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		return false;
	}
}
