package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Berit extends Rolle {

	Spiller forsinketSpiller, forsinketOffer;
	String forsinketRapport = "";
	public Berit(){
		super("Gærne Berit");
		oppgave = "Hvem vil Berit forsinke?";
		side = BORGER;
		prioritet = BERIT;
	}

	@Override
	public String oppgave() {
		if(forsinketSpiller != null && forsinketOffer != null && forsinketSpiller.funker() && (forsinketOffer.funker() && !forsinketSpiller.id(OBDUK))) {
			if(!(	forsinketSpiller.id(REX) || forsinketSpiller.id(SHERLOCK) || forsinketSpiller.id(FILOSOF) 
				|| 	forsinketSpiller.id(OBDUK) || forsinketSpiller.id(JØRGEN)  || forsinketSpiller.id(DRØMMER))) {
				Spiller origOffer = forsinketSpiller.offer();
				
				System.out.println("forsinkelse: " + forsinketSpiller + " pekte på " + forsinketOffer);

				
				if(forsinketSpiller.rolle().blokkert()){
					forsinketSpiller.rens(finnRolle(TJUKKAS));
					forsinketSpiller.pek(forsinketOffer);
					forsinketSpiller.pek(origOffer);
					forsinketSpiller.blokker(finnRolle(TJUKKAS));
				}
				else {
					forsinketSpiller.pek(forsinketOffer);
					forsinketSpiller.pek(origOffer);
				}
					
				
			}
		}
		
		if(forsinketSpiller != null){
			forsinketRapport = forsinketSpiller.rolle() + "(" + forsinketSpiller + ") har valgt ";
			if(forsinketOffer != null)
				forsinketRapport += forsinketOffer 	+ "(" + forsinketOffer.rolle() + ") forsinket.\n";
			else
				forsinketRapport = forsinketSpiller.rolle() + "(" + forsinketSpiller + ") ble forsinket\n";
		}

		forsinketSpiller = null;
		forsinketOffer = null;

		return super.oppgave();
	}

	@Override
	public String rapport() {
		String ut = forsinketRapport + super.rapport();
		forsinketRapport = "";
		return ut;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);

		if(blokkert || !spiller.rolle().aktiv())
			return false;

		if(spiller.id(COPYCAT) || spiller.id(CUPID) || spiller.id(KIRSTEN) || spiller.id(ILLUSJONIST) || spiller.id(SOFA))
			return false;
		spiller.forsink(this);
		forsinketSpiller = spiller;
		forsinketOffer = spiller.offer();
		System.out.println("forsikede har valgt: " + spiller.offer());
		return true;
	}

	public void setOffer(Spiller spiller) {
		forsinketOffer = spiller;
		System.out.println("forsikede valgte: " + spiller);
	}
}
