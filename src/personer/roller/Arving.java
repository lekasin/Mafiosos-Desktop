package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Arving extends Rolle {

	Spiller riking;
	boolean arvet = false;
	
	public Arving(){
		super("Arving");
        bilde = "arving";
		oppgave = "Hvem skal arvingen adopteres bort til?";
		veiledning = "Arving:\n" +
                "Arvingen velger første natt hvem han vil adopteres bort til. Du trykker da på den han har valgt.\n" +
                "Arvingen vil nå ikke våkne før adoptivforelderen hans dør. " +
                "Når dette skjer, får arvingen vite hvilken rolle han har arvet, og skal fra da av kun våkne som denne rollen.\n" +
                "Du trykker da bare på fortsett for å gå til neste rollen.";
        guide = "Arvingen velger en person første runde, som han adopteres bort til. Er ellers vanlig borger helt til denne personen dør. " +
                "Arvingen arver da rollen til vedkommende, og spiller videre som den nye (for eksempel) tjukkasen (hvis tjukkasen er valgt). " +
                "Våkner kun første natt, og når den valgte personen dør.";
		side = BORGER;
		prioritet = ARVING;
	}

	public boolean arv(){
		if(lever() && riking != null && !riking.lever()){
            spiller.setRolle(riking.rolle());
            spiller.rolle().vekk();
            spiller.rolle().setSpiller(spiller);
            return true;
        } else
            return false;
	}
	

	public Spiller riking(){
		return riking;
	}

	@Override
	public void autoEvne() {
		if (arvet)
			aktiver(false);
		else if(arv() && lever() && !arvet) {
			aktiver(true);
			tvOppgave = "Arvingen har nå arvet rollen " + riking.rolle() + "!";
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
