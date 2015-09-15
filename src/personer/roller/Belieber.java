package personer.roller;

import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Belieber extends Rolle {

	Spiller justin;

	public Belieber(){
		super("Belieber");
		oppgave = "Hvem er Belieberens nye Justin?";
		veiledning = "Belieber:\n" +
				"Belieberen våkner kun alene første natt, og velger seg ut en ny Justin.\n" +
				"Du trykker på navnet til den belieberen velger for å koble dem.\n" +
				"Belieberen får da rollen til vedkommende opp på skjermen (*rolle*lieber), og skal fra da av kun våkne når denne rollen våkner.\n" +
				"Når den aktuelle rollen våkner, vil vedkommende få beskjed på skjermen om at de har fått en belieber på laget.";
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
		TvUtil.vis("Belieberen er nå en " + spiller.rolle() + "liber!");
		TvUtil.toFront();
		aktiver(false);
		return true;
	}
}
