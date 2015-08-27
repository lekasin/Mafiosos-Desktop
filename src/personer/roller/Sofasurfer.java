package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Sofasurfer extends Rolle {
	
	Spiller valgt;
	String ut = "";
	
	public Sofasurfer(){
		super("SofaSurfer");
		oppgave = "Hvem vil SofaSurferen bo hos?";
		veiledning = "SofaSurfer:\n" +
				"SofaSurferen velger hver natt en person å snylte på.\n" +
				"Når snylteren har valgt, trykker du på vedkommendes navn for å snylte på dem.\n" +
				"Snylteren vil da bli påvirke av de samme effektene som den valgte.";
		side = BORGER;
		prioritet = SOFA;
		fortsett = false;
	}
	
	public Spiller getValgt(){
		return valgt;
	}
	
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		System.out.println("Aktiv oppgave: " + aktiv);

		return super.oppgave();
	}
	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		
		if(blokkert) {
			ut += "Du ble blokkert og kom deg ikke ut døra!";
			tv.vis(ut);
			return false;
		}

		spiller.rolle().informer(this, "\n\n" + spiller +" har fått besøk av " + this.spiller.navn() + "!");
		ut += "Du bor nå hos en " + spiller.rolle() + "!";

		valgt = spiller;
		aktiver(false);
		tv.vis(ut);
		
		return true;
	}
	
	@Override
	public boolean lever() {
		if(offer != null && !offer.lever()) 
			spiller.drep(this);
		return super.lever();
	}
	
	@Override
	public void jul() {
		aktiver(true);
		super.jul();
	}

	public void sov(){
		ut = "";
		if((valgt == null || !valgt.lever() ) && !spiller.rolle().id(SMITH)) 
			aktiver(true);
		super.sov();

	}
	
}
