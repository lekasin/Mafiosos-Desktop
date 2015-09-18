package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class CopyCat extends Rolle {

	Boolean kopi = false;
	Spiller negativ;

	public CopyCat(){
		super("CopyCat");
		oppgave = "Hvem vil Copycaten kopiere?";
		veiledning = "CopyCat:\n" +
				"Copycat velger to personer. En å kopiere, og en å bruke den kopierte rollen på.\n" +
				"Først velger han en å kopiere, og du trykker på vedkommendes navn for å kopiere rollen.\n" +
				"Så velger copycaten en gang til, og du trykker på den personens navn for å utføre den kopierte rollen.\n" +
				"Trykker du på fortsett, går du rett til neste rolle. Du trenger ikke gjøre det to ganger";
		side = BORGER;
		prioritet = COPYCAT;
		fortsett = false;
	}

	@Override
	public void autoEvne() {
		fortsett = false;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(spiller != offer)
			return false;

		if(!kopi) {
			forby(spiller);
			kopi = true;
			TvUtil.vis("Hvem vil Copycaten velge?");
			if(blokkert)
				return false;
			negativ = spiller;
			negativ.rolle().setSpiller(this.spiller);
			forbud2 = null;
			return true;
		}

		if(negativ != null && !blokkert) {
			Rolle rolle = negativ.rolle();

			if(rolle.pri() > UNDERCOVER && rolle.pri() < CUPID && !spiller.id(COPYCAT) && !spiller.id(BERIT) || rolle.id(SMITH) || rolle.id(RAVN) || rolle.id(MARIUS)) {
				Spiller forb = rolle.forbud();
				rolle.forby(null);

				if(rolle.blokkert()) {
					Rolle r = rolle.hvemBlokk();
					rolle.rens(r);
					rolle.evne(spiller);
					negativ.blokker(r);
				} else
					rolle.evne(spiller);

				rolle.forby(forb);
			}
			negativ.rolle().setSpiller(negativ);
		}

		kopi = false;
		fortsett = true;
		forbud2 = this.spiller;
		return true;
	}
}
