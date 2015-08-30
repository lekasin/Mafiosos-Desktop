package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Jorgen extends Rolle {

	public Jorgen(){
		super("Jørgen");
		oppgave = "Jørgen ser på de døde";
		veiledning = "Jørgen fra begravelsesbyrået:\n" +
				"Jørgen våkner hver natt for å se undersøke de døde. Han velger inge på natten, men får info på skjermen.\n" +
				"Når Jørgen våkner, får han opp alle de døde og hvilken rolle de hadde på skjermen.\n" +
				"For å gå videre, trykker du på fortsett.";
		side = BORGER;
		prioritet = JØRGEN;
		skjerm = true;
	}
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		if(spiller.nyligKlonet())
			return super.oppgave();

		if(blokkert)
			tv.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
		else
			tv.jørgen();
		if(informert) 
			tv.leggtil(info);
		tv.toFront();
		return oppgave;
	}
	
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert) {
			tv.vis("Noen blokkerte Jørgen fra å gjøre jobben sin");
			return false;
		}
		
		tv.jørgen();
		return true;
	}
}
