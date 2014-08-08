package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Undercover extends Rolle {

	Mafia mafia;
	
	public Undercover(Mafia mafia){
		super("Undercover");
		side = FAKEMAFIA;
		prioritet = UNDERCOVER;
		aktiver(false);
		this.mafia = mafia;
	}

	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void blokker(Rolle blokk){
		mafia.blokker(blokk);
		blokkert = true;
		rens();
	}	
}
