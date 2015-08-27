package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Obduksjonist extends Rolle {

	String ut = "";

	public Obduksjonist(){
		super("Obduksjonist");
		oppgave = "Hvem vil Obduksjonisten undersøke?\n";
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
		if(spiller.klonet())
			return super.oppgave();

		tv.vis("Hvem vil Obduksjonisten undersøke?\n");
		int i = 1;
		for(Spiller s: tv.spillere().lik())
			tv.leggtil("\n" + i++ + " " + s);

		if(informert) tv.leggtil(info);
		return oppgave;
	}

	@Override
	public void sov() {
		if(forsinkelse == null) ut = "";
		super.sov();
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(this.spiller.skjult())
			ut += "\nObduksjonen viser at " + spiller + " var " + tv.spillere().randomSpiller(this.spiller) + "!\n";
		else {
			if(!spiller.id(Rolle.ZOMBIE) && !spiller.id(Rolle.MAFIA) && !spiller.id(Rolle.POLITI)) 
				spiller.rolle().aktiver(false);
			ut += "\nObduksjonen viser at " + spiller + " var " + spiller.rolle() + "!\n";
		}
		
		if(blokkert)
			return false;

		spiller.skjul(this);
		tv.leggVed(ut);
		return true;
	}
}
