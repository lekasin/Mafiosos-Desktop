package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Obduksjonist extends Rolle {

	String ut = "";

	public Obduksjonist(){
		super("Obduksjonist");
		oppgave = "Hvem vil Obduksjonisten undersøke?\n";
		side = BORGER;
		prioritet = OBDUK;
		aktiver(false);
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		ut = "";
		tv.vis("Hvem vil Obduksjonisten undersøke?\n");
		for(Spiller s: tv.spillere().lik())
			tv.leggtil("\n" + s);

		if(informert) tv.leggtil(info);
		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;

		spiller.skjul(this);
		if(this.spiller.skjult())
			ut = "\nObduksjonen viser at " + spiller + " var " + tv.spillere().randomSpiller(this.spiller) + "!\n";
		else {
			if(!spiller.id(Rolle.ZOMBIE) && !spiller.id(Rolle.MAFIA) && !spiller.id(Rolle.POLITI)) 
				spiller.rolle().aktiver(false);
			ut = "\nObduksjonen viser at " + spiller + " var " + spiller.rolle() + "!\n";
		}
		tv.leggVed(ut);
		return true;
	}
}
