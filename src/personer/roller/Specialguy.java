package personer.roller;

import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Specialguy extends Rolle {

	boolean brukt = false;

	public Specialguy(){
		super("Special Guy");
		oppgave = "Hvem vil Special Guy drepe?";
		veiledning = "Special Guy:\n" +
				"Special Guy våkner hver natt, og får vite om han er drept eller ikke.\n" +
				"Hvis Special Guy ikke er drept, legger han seg til å sove igjen, og du trykker fortsett for å gå videre.\n" +
                "Hvis han er drept, kan han velge en ny person som drepes i hans sted.\n" +
                "Når Special Guy har valgt, trykker du på vedkommendes navn for å drepe dem.\n" +
                "Når han har drept noen, våkner ikke Special Guy lenger på natten.";
        side = BORGER;
		prioritet = SPECIAL;
		skjerm = true;
	}

	@Override
	public String oppgave() {
		if(spiller.nyligKlonet())
			return super.oppgave();
		// TODO Auto-generated method stub
		if(brukt)
			TvUtil.vis("Special Guy har brukt opp kraften sin.");
		else
			TvUtil.visSpecialGuyBeskjed(lever);
		if(informert) TvUtil.leggTil(info);
		TvUtil.toFront();
		return oppgave;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(!lever() && !brukt){
			this.spiller.vekk();
			if(snill)
				spiller.snipe(this);
			else
				spiller.drep(this);
			brukt = true;
		}
		return true;
	}
}
