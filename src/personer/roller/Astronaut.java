package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Astronaut extends Rolle {

	String ut = "";
	
	public Astronaut(){
		super("Astronaut");
		oppgave = "Er Astronauten ferdig med raketten?";
		veiledning = "Astronaut\n" +
				"Astronaut kan bruke rollen sin én gang i spillet, og signaliserer derfor kun om raketten er ferdig eller ikke, på natten.\n" +
                "Om raketten ikke er ferdig, trykker du på fortsett for å gå videre til neste rolle.\n" +
                "Når raketten er ferdig, trykker du på astronautens eget navn (eneste mulige), for å starte en rakettoppskytning neste dag.";
                side = BORGER;
		prioritet = ASTRONAUT;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert && !snill) {
			offer = null;
			return false;	
		}
		
		if(offer != null) {
			aktiver(false);
		}
		return true;
	}
	
	@Override
	public String rapport() {
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null){
			ut += " har gjort ferdig raketten!";
			if(snill) ut += ", med hjelp fra nissen";
			if(blokkert) ut += ", men ble blokkert av " + blokk + ".";
		}
		else
			ut += " har ikke fullført raketten.";		
		return ut;	
	}
}
