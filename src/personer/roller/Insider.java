package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Insider extends Rolle {
	
	Politi politi;
	
	public Insider(Politi politi){
		super("Insider");
		side = FAKEBORGER;
		prioritet = INSIDER;
		aktiver(false);
		this.politi = politi;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void blokker(Rolle blokk){
		politi.blokker(blokk);
		blokkert = true;
		rens();
	}	
}
