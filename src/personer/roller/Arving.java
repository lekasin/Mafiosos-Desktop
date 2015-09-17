package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Arving extends Rolle {

	Spiller riking;
	boolean arvet = false;
	
	public Arving(){
		super("Arving");
		oppgave = "Hvem skal arvingen adopteres bort til?";
		veiledning = "Arving:\n" +
                "Arvingen velger første natt hvem han vil adopteres bort til. Du trykker da på den han har valgt.\n" +
                "Arvingen vil nå ikke våkne før adoptivforelderen hans dør. " +
                "Når dette skjer, får arvingen vite hvilken rolle han har arvet, og skal fra da av kun våkne som denne rollen.\n" +
                "Du trykker da bare på fortsett for å gå til neste rollen.";
		side = BORGER;
		prioritet = ARVING;
	}

	public boolean arv(){
		if(lever() && riking != null && !riking.lever()){
				spiller.setRolle(riking.rolle());
				spiller.rolle().vekk();
				spiller.rolle().setSpiller(spiller);
				return true;
		}
		else
			return false;
	}
	

	public Spiller riking(){
		return riking;
	}

	@Override
	public void autoEvne() {
		if (arvet)
			aktiver(false);
		else if(arv() && lever() && arvet == false) {
			aktiver(true);
			oppgave = "Arvingen har nå arvet rollen " + riking.rolle() + "!";
			TvUtil.toFront();
			arvet = true;
		}
	}

	@Override
	public boolean evne(Spiller spiller) {
		riking = spiller;
		skjerm = true;
		aktiver(false);
		oppgave = "Arvingen våkner";
		return true;
	}
}
