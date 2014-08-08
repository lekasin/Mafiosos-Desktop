package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Astronaut extends Rolle {

	String ut = "";
	
	public Astronaut(){
		super("Astronaut");
		oppgave = "Er Astronauten ferdig med raketten?";
		side = BORGER;
		prioritet = ASTRONAUT;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;			
		
		if(spiller != null) {
			aktiver(false);
		}
		return true;
	}
}
