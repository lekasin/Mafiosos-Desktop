package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Sofasurfer extends Rolle {
	
	Spiller valgt;
	String ut = "";
	
	public Sofasurfer(){
		super("SofaSurfer");
        oppgave = "Hvem vil SofaSurferen bo hos?";
        veiledning = "SofaSurferen våkner første natt, og velger en person han vil sove hos.\n" +
                "Når sofaSurferen har valgt, trykker du på personens navn, for la surferen flytte inn.\n" +
                "Den valgte personens rolle kommer da opp på skjermen, og sofasurferen skal fra da av våkne med denne rollen.\n" +
                "Når den valgte rollen våkner, vil vedkommende få beskjed på skjermen om at de har fått besøk på sofaen.\n" +
                "Sofasurferen kan så ikke velge en ny person før den han sover hos dør.";

        side = BORGER;
		prioritet = SOFA;
		fortsett = false;
	}
	
	public Spiller getValgt(){
		return valgt;
	}
	
	
	@Override
	public String oppgave() {
		System.out.println("Aktiv oppgave: " + aktiv);

		return super.oppgave();
	}
	@Override
	public boolean evne(Spiller spiller) {
		TvUtil.toFront();
		
		if(blokkert) {
			ut += "Du ble blokkert og kom deg ikke ut døra!";
			TvUtil.vis(ut);
			return false;
		}

		spiller.rolle().informer(this, "\n\n" + spiller +" har fått besøk av " + this.spiller.navn() + "!");
		ut += "Du bor nå hos en " + spiller.rolle() + "!";

		valgt = spiller;
		aktiver(false);
		TvUtil.vis(ut);
		
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
