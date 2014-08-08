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
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		tv.special(lever);
		if(informert) tv.leggtil(info);
		tv.toFront();
		return oppgave;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;

		if(!lever && !brukt){
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
