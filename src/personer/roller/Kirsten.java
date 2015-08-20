package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Kirsten extends Rolle {

	Spiller mann = null, kvinne = null;

	public Kirsten(){
		super("Kirsten Giftekniv");
		oppgave = "Hvem vil Kirsten spleise?";
		side = BORGER;
		prioritet = KIRSTEN;
		fortsett = false;
	}

	@Override
	public String oppgave() {
		mann = null;
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
			fortsett = true;
			kvinne = spiller;
			
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
		}

		return ut;
	}
}
