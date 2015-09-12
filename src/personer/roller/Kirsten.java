package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Kirsten extends Rolle {

	Spiller mann = null, kvinne = null;

	public Kirsten(){
		super("Kirsten Giftekniv");
		oppgave = "Hvem vil Kirsten spleise?";
		veiledning = "Kirsten Giftekniv:\n" +
				"Kirsten velger to personer hver natt. Disse to blir enten en match, eller en crash.\n" +
				"Kirsten peker altså først på en person, og du trykker på vedkommendes navn.\n" +
				"Så velger hun en til, og du trykker på denne personens navn, for å matche de to.\n"+
				"Er de to på samme side, er de en match, og begge blir beskyttet. Er de en crash, dør borgeren.";
		side = BORGER;
		prioritet = KIRSTEN;
		fortsett = false;
	}

	@Override
	public String oppgave() {
		fortsett = false;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
        if(spiller != offer)
			return false;

        if(mann == null) {
            mann = spiller;
			forbud2 = forbud;
			forby(mann);
			if(mann != kvinne) 
				forbud2 = null;
			return true;
		} else {
			offer = spiller;
			kvinne = spiller;
			fortsett = true;

            if(blokkert){
				mann = null;
				return false;
			}

			if(offer.side() < 0) {
				if(mann.side() < 0) {
					offer.beskytt(this);
					mann.beskytt(this);
				} else {
					if(snill)
						mann.snipe(this);
					else
						mann.drep(this);
				}
			}
			else
				if(mann.side() < 0) {
					if(snill)
						offer.snipe(this);
					else
						offer.drep(this);
				} else {
					offer.beskytt(this);
					mann.beskytt(this);
				}
        }

        forbud2 = null;
		return true;
	}
	
	@Override
	public String rapport(){
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null) 
			ut += " har valgt " + offer + "(" + offer.rolle() + ")";
		else
			ut += " valgte ingen.";
		if(mann != offer) {
			if(mann.side() == offer.side())
				ut += ", og de er en match!";
			else
				ut += ", men de matcher ikke!";
            //Reset for neste natt
            mann = null;
        }

        return ut;
	}
}
