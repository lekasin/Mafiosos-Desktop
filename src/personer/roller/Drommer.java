package personer.roller;

import Utils.SkjermUtil;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Drommer extends Rolle {

	String ut = "";
	public Drommer(){
		super("Drømmer");
        bilde = "drommer";
		oppgave = "Drømmeren drømmer!";
		veiledning = "Drømmer:\n" +
				"Drømmeren våkner opp og får info på skjermen. Han skal bare se infoen, velge noen.\n" +
				"Infoen består av tre navn, hvor to av dem er på borgernes side, og den siste er mafia.\n" +
				"For å gå videre til neste rolle, trykker du fortsett.";
        guide = "Drømmeren har profetiske drømmer, og drømmer om de andre landsbybeboerne hver natt, så lenge det lar seg gjøre. " +
                "I drømmene vil 3 tilfeldige personer være innvolvert, hvorav én er mafia, og de to andre er uskyldige borgere. " +
                "Bortsett fra det er navnene helt tilfeldige. Drømmeren vil imidlertid ikke kunne drømme hvis det ikke finnes " +
                "én mafia og to borgere (utenom drømmeren) å drømme om, eller hvis han blir blokkert. Da får ikke drømmeren sove.";
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
		ut += this.ut.substring(0, this.ut.length()-1);
		return ut;
	}
}
