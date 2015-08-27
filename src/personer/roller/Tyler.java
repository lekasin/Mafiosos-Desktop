package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Tyler extends Rolle {

	Spiller original;
	Rolle rolle;

	public Tyler(){
		super("Tyler Durden");
		oppgave = "Hvem vil Tyler besøke?";
		veiledning = "Tyler Durden:\n" +
				"Tyler Durden våkner hver natt, og får vite hvilken rolle han har den natten. " +
                "Han velger så en person å bruke rollen på.\n" +
				"Når Tyler har valgt, trykker du på vedkommendes navn for å velge dem.\n" +
				"Tyler får en tilfeldig valgt rolle hver natt.";
		side = BORGER;
		prioritet = TYLER;
		skjerm = true;
	}

	@Override
	public String oppgave() {
		rolle = this;
		if(funker) {
			rolle = tv.spillere().tylersRolle();
			original = rolle.spiller();
			rolle.setSpiller(this.spiller);
		}
		tv.vis("Tyler er nå " + rolle + "!\n");

		if(klonet) {
			informer(spiller.smith(), "\n\n" + spiller + " er nå en Smith!");
			klonet = false;
		}
		if(spiller.klonet()) {
			informer(spiller.smith(), "\n\n" + spiller + " er i ferd med å bli en Smith!\n(Kan bruke rollen sin en siste gang)");
			tv.toFront();
		}

		tv.leggtil(oppgave);
		tv.toFront();
		if(informert) tv.leggtil(info);

		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;

		if(rolle.pri() < Rolle.QUISLING && rolle.pri() > Rolle.UNDERCOVER) {
			Spiller forb = rolle.forbud();
			rolle.forby(null);

			if(rolle.blokkert()) {
				Rolle r = rolle.hvemBlokk();
				rolle.rens(r);
				rolle.evne(spiller);
				rolle.blokker(r);
			} else
				rolle.evne(spiller);

			rolle.forby(forb);
		}
		rolle.setSpiller(original);
		
		return true;
	}

	@Override
	public String rapport(){
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null) ut += " har valgt " + offer + "(" + offer.rolle() + ")";
		ut += ", og var " + rolle;
		if(blokkert) ut += ", men ble blokkert av " + blokk + ".";

		return ut;
	}
	
	@Override
	public void sov() {
		if(rolle != null && original != null)
				rolle.setSpiller(original);
		super.sov();
	}

}
