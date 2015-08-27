package personer.roller;

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
	public String oppgave() {
		if(riking == null) {
			aktiver(false);
			return super.oppgave();
		} else {
			aktiver(false);
			if(spiller.klonet())
				return super.oppgave();

			tv.vis("Arvingen har nå arvet rollen " + riking.rolle() + "!");
			if(informert) 
				tv.leggtil(info);
			tv.toFront();
			arvet = true;
			return "Arvingen våkner";
		}
	}

	@Override
	public boolean aktiv() {
		if(arv() && lever() && arvet == false)
			aktiver(true);
		return super.aktiv();
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
