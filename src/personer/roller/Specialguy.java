package personer.roller;

import Utils.TvUtil;
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
        guide = "Special Guy er en spesiell fyr, med spesielle evner. " +
                "Han våkner hver natt, og får beskjed om hvorvidt han er drept eller ikke denne natten. " +
                "Hvis han ikke er drept, legger han seg til å sove igjen, men om han skulle vært død, kan han velge en annen person til å dø i hans sted. " +
                "Dermed overlever Special Guy, mens hans offer, muligens en mafia, blir drept. " +
                "Dette kan bare gjøres én gang, og ingen andre får beskjed om at Special Guy har drept. " +
                "Etter at han har brukt evnen sin, blir Special Guy en vanlig borger." +
                " Dør han på dagen, får han ikke brukt evnen sin.";
        side = BORGER;
		prioritet = SPECIAL;
		skjerm = true;
	}

	@Override
	public String oppgave() {
		if(spiller.nyligKlonet())
			return super.oppgave();
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
