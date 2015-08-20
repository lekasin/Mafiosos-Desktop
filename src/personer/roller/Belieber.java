package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Belieber extends Rolle {

	Spiller justin;

	public Belieber(){
		super("Belieber");
		oppgave = "Hvem er Belieberens nye Justin?";
		side = BORGER;
		prioritet = BELIEBER;
		fortsett = false;
	}

	public void beliebe(){
		if(justin != null && !justin.lever()) {
			spiller.snipe(this);
			spiller.stopp();
		}
	}

	public Spiller justin(){
		return justin;
	}

	@Override
	public boolean lever() {
		beliebe();
		return lever;
	}


	@Override
	public boolean evne(Spiller spiller) {
		justin = spiller;
		justin.rolle().informer(this, "\n\n" + spiller +", " + this.spiller.navn() + " er nå din belieber!");
		tv.vis("Belieberen er nå en " + spiller.rolle() + "liber!");
		tv.toFront();
		aktiver(false);
		return true;
	}
}
