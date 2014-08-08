package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Vara extends Rolle {

	public Vara(){
		super("VaraMafia");
		side = MAFIOSO;
		prioritet = VARA;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}
}
