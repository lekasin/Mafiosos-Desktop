package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Jente extends Rolle {

	public Jente(){
		super("Liten Jente");
		oppgave = "Liten jente våkner";
		side = BORGER;
		prioritet = JENTE;
		aktiver(false);
	}

	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void jul() {
		// TODO Auto-generated method stub
		super.jul();
		spiller.beskytt(this);
		spiller.forsvar(this);
	}
}
