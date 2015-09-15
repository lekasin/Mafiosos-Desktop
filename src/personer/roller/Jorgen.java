package personer.roller;

import gui.TvUtil;
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
