package personer.roller;

import gui.Spill;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Obduksjonist extends Rolle {

	String ut = "";

	public Obduksjonist(){
		super("Obduksjonist");
		oppgave = "Hvem vil Obduksjonisten undersøke?";
		veiledning = "Obduksjonist:\n" +
				"Når minst 3 personer er døde, kan obduksjonisten velge en død person å undersøke.\n" +
				"Når obduksjonisten har valgt, trykker du på vedkommendes navn for å undersøke dem.\n" +
                "Neste dag, vises obduksjonsrapporten på skjermen, og alle får se hvilken rolle den døde hadde.";
		side = BORGER;
		prioritet = OBDUK;
		aktiver(false);
	}

	@Override
	public String oppgave() {
		if(spiller.nyligKlonet())
			return super.oppgave();

		TvUtil.vis("Hvem vil Obduksjonisten undersøke?\n");
		int i = 1;
		for(Spiller s: Spill.spillere.lik())
			TvUtil.leggTil("\n" + i++ + " " + s);

		if(informert) TvUtil.leggTil(info);
		return oppgave;
	}

	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}

	@Override
	public boolean evne(Spiller spiller) {
		ut += "\n\n";
		if(this.spiller.skjult())
			ut += "Obduksjonen viser at " + spiller + " var " + Spill.spillere.randomSpiller(this.spiller).rolle() + "!";
		else {
			if(!spiller.id(Rolle.ZOMBIE) && !spiller.id(Rolle.MAFIA) && !spiller.id(Rolle.POLITI)) 
				spiller.rolle().aktiver(false);
			ut += "Obduksjonen viser at " + spiller + " var " + spiller.rolle() + "!";
		}
		
		if(blokkert)
			return false;

		spiller.skjul(this);
		TvUtil.leggVed(ut);
		return true;
	}
}
