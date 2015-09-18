package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Drommer extends Rolle {

	String ut = "";
	public Drommer(){
		super("Drømmer");
		oppgave = "Drømmeren drømmer!";
		veiledning = "Drømmer:\n" +
				"Drømmeren våkner opp og får info på skjermen. Han skal bare se infoen, velge noen.\n" +
				"Infoen består av tre navn, hvor to av dem er på borgernes side, og den siste er mafia.\n" +
				"For å gå videre til neste rolle, trykker du fortsett.";
		side = BORGER;
		prioritet = DRØMMER;
		skjerm = true;
	}
	
	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}
	
	@Override
	public String oppgave() {
		if(spiller.nyligKlonet() || klonet)
			return super.oppgave();
		if (funker)
			evne(spiller);
		return oppgave; 
	}

	@Override
	public boolean evne(Spiller spiller) {
		ut += TvUtil.hentDrøm(spiller) + "\n";

		if(blokkert) {
			if(blokk != forsinkelse) ut = "";
			TvUtil.vis("Drømmeren får ikke sove...");
		}
		else
			TvUtil.vis(ut);
		
		if(informert) TvUtil.leggTil(info);
		TvUtil.toFront();
		return true;
	}
	
	@Override
	public String rapport() {
		String ut = tittel + "(" + spiller + ") ";
		ut += this.ut;
		return ut;
	}
}
