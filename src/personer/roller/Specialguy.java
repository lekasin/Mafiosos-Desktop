package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Specialguy extends Rolle {

	boolean brukt = false;

	public Specialguy(){
		super("Special Guy");
		oppgave = "Hvem vil Special Guy drepe?";
		side = BORGER;
		prioritet = SPECIAL;
		skjerm = true;
	}

	@Override
	public String oppgave() {
		if(spiller.klonet())
			return super.oppgave();
		// TODO Auto-generated method stub
		if(brukt)
			tv.vis("Special Guy har brukt opp kraften sin.");
		else
			tv.special(lever);
		if(informert) tv.leggtil(info);
		tv.toFront();
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
