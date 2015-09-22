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
        guide = "Astronauten våkner hver natt, og gir beskjed til forteller om raketten hans er ferdig eller ikke. Når Astronauten sier at raketten er ferdig, " +
                "er det klart for rakettoppskytning. Neste morgen får landsbyen vite at rakett er klar, og det skal være anonym avstemning for å avgjøre hvem som skal sendes opp i raketten (og dermed dø). " +
                "Altså: Når landsbyen våkner til rakettoppskytning, må alle umiddelbart lukke øyenene (bortsett fra astronauten). " +
                "Forteller tar da en runde med alle navnene på gjenlevende borgere, og landsbyen stemmer på den de vil sende ut. " +
                "Astronauten er derimot våken, kan se hva alle andre stemmer, og har selv en dobbeltstemme. Personen med flest stemmer, " +
                "blir henrettet, og dagen fortsetter så som vanlig. Når raketten er skutt opp, våkner ikke lenger astronauten om natten.";
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
