package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Erik extends Rolle {

	public Erik(){
		super("Barnslige Erik");
		oppgave = "Hvem vil Erik ha som barnevakt?";
		veiledning = "Barnslige Erik:\n" +
				"Erik velger hver natt en barnevakt som blir forhindret fra å peke lenger enn sine nærmeste naboer.\n" +
                "Når Erik har valgt, trykker du på vedkommendes navn for gjøre de til barnvakt.\n" +
				"Når barnevakten våkner, får han opp på skjermen at han sitter barnevakt. " +
                "Forteller må da selv kontrollere at barnvakten kun peker på en av de nærmeste, eller seg selv.";
        guide = "Erik er et lite barn med opptatte foreldre, og trenger derfor barnevakt hver kveld. " +
                "Han får det som regel som han vil, og våkner derfor hver natt og velger en person som må sitte barnevakt. " +
                "Dette gjør at personen ikke kan bevege seg så langt, og dermed kun kan peke på personen til høyre eller venstre for seg selv. " +
                "Dette gjelder alle roller som velger ett offer.";
		side = BORGER;
		prioritet = ERIK;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		spiller.rolle().informer(this, "\n\n" + spiller + " sitter barnevakt, og kan kun besøke naboen.");
		return true;
	}
}