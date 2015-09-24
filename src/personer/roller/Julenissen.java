package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Julenissen extends Rolle {

	public Julenissen(){
		super("Julenissen");
		oppgave = "Hvem vil Nissen gi gave til?";
		veiledning = "Julenissen:\n" +
				"Julenissen velger hver natt en person å gi gave til.\n" +
				"Når nissen har valgt, trykker du på vedkommendes navn for å gi dem gaven.\n" +
				"Personer som har fått gave av nissen, er garantert å få gjennomført sin rolle den natten.";
        guide = "Julenissen delere ut gaver og oppfyller ønskene til andre borgere. " +
                "Han våkner hver natt og velger en person som skal få sitt ønske oppfylt. " +
                "Det vil si at den personen nissen velger, ikke kan blokkeres på noen måte den natten, men er garantert å få gjennomført oppgaven sin. " +
                "Drapsroller får garantert drepe, uavhengig av om offeret er leget, inforoller får garantert info, osv. " +
                "I tillegg opphever nissen unntak, som at legen dør av å peke på mafiaen, og at morderen dør ved å velge samme offer som mafiaen. " +
                "Alle liker Julenissen.";
		side = BORGER;
		prioritet = NISSEN;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		spiller.rolle().jul();
		return true;
	}
}
