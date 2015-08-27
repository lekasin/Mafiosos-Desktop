package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Cupid extends Rolle {

	Spiller mann = null, kvinne = null;
	Spiller utMann = null, utKvinne = null;

	public Cupid(){
		super("Cupid");
		oppgave = "Hvem vil Cupid spleise?";
		veiledning = "Cupid:\n" +
				"Cupid velger to personer hver natt. Disse blir koblet sammen, og blir påvirket av de samme effektene.\n" +
				"Cupid peker altså først på en person, og du trykker på vedkommendes navn.\n" +
				"Så velger han en til, og du trykker på denne personens navn, for å koble de to.\n"+
				"Trykker du på fortsett, går du rett til neste rolle. Du trenger ikke gjøre det to ganger";
		side = BORGER;
		prioritet = CUPID;
		fortsett = false;
	}

	@Override
	public String oppgave() {
		fortsett = false;
		return super.oppgave();
	}
	
	public Spiller getMann() {
		return utMann;
	}
	
	public Spiller getKvinne() {
		return utKvinne;
	}
	
	public void nullstill() {
		utMann = null;
		utKvinne = null;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(spiller != offer)
			return false;
		
		if(spiller.id(Rolle.BELIEBER)) ((Belieber)spiller.rolle()).beliebe();

		if(mann == null) {
			mann = spiller;
			forbud2 = forbud;
			forby(mann);
			if(mann != kvinne) 
				forbud2 = null;
			return true;
		} else {
			fortsett = true;
			kvinne = spiller;
			
			if(blokkert){
				mann = null;
				return false;
			}
			
			utMann = mann;
			utKvinne = spiller;
			
			forbud2 = null;
			mann = null;
		}
		return true;
	}
}
