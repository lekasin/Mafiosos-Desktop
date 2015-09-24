package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Jorgen extends Rolle {

	public Jorgen(){
		super("Jørgen");
		oppgave = "Jørgen ser på de døde";
		veiledning = "Jørgen fra begravelsesbyrået:\n" +
				"Jørgen våkner hver natt for å se undersøke de døde. Han velger ingen på natten, men får info på skjermen.\n" +
				"Når Jørgen våkner, får han opp alle de døde og hvilken rolle de hadde på skjermen.\n" +
				"For å gå videre, trykker du på fortsett.";
		guide = "Jørgen jobber i Notodden Begravelsesbyrå, og får med det en unik mulighet til å undersøke de døde før de begraves. " +
                "Han kan da finne ut de dødes identitet, og noterer dette i notanes sine. " +
                "Hver natt får Jørgen se notatene sine, med en liste over alle døde spillere og hvilke roller de hadde. " +
                "Dette kan Jørgen bruke til å tolke spillernes handlinger mens de levde. ";
        side = BORGER;
		prioritet = JØRGEN;
		skjerm = true;
	}
	
	@Override
	public String oppgave() {
		if(spiller.nyligKlonet())
			return super.oppgave();

		if(blokkert)
			TvUtil.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
		else
			TvUtil.visJørgensListe();
		if(informert) 
			TvUtil.leggTil(info);
		TvUtil.toFront();
		return oppgave;
	}
	
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			TvUtil.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
			return false;
		}
		
		TvUtil.visJørgensListe();
		return true;
	}
}
