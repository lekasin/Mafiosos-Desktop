package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Berit extends Rolle {

	Spiller forsinketSpiller, forsinketOffer;
	String forsinketRapport = "";
	public Berit(){
		super("Gærne Berit");
		oppgave = "Hvem vil Berit forsinke?";
        veiledning = "Gærne Berit:\n" +
                "Berit velger hver natt en person å forsinke.\n" +
                "Når Berit har valgt, trykker du på vedkommendes navn for å forsinke dem.\n" +
                "Vedkommende vil da få utsatt nattens aktivitet til neste natt.";
        guide = "Gærne Berit er hver natt ute og stjeler mokkabønner fra lokale Joker-butikker. " +
                "Hun stikker så av på scooteren sin, men er paranoid nok til å tro at alle som kjører bak henne er politiet, " +
                "og kaster derfor ut mokkabønner fra den allerede treige scooteren sin, og forsinker dermed personen som kjører bak med et helt døgn. " +
                "Altså velger Berit en person hver natt, som får sin funksjon utsatt med et døgn, og dermed ikke gjennomført før neste natt.";
		side = BORGER;
		prioritet = BERIT;
	}

	@Override
	public void autoEvne() {
		if(forsinketSpiller != null && forsinketOffer != null && forsinketSpiller.funker() && (forsinketOffer.funker() && !forsinketSpiller.id(OBDUK))) {
			if(!(	forsinketSpiller.id(REX) || forsinketSpiller.id(SHERLOCK) || forsinketSpiller.id(FILOSOF)
					|| 	forsinketSpiller.id(OBDUK) || forsinketSpiller.id(JØRGEN)  || forsinketSpiller.id(DRØMMER))) {
				Spiller origOffer = forsinketSpiller.offer();

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

		if(blokkert || spiller.rolle().blokkert() || !spiller.rolle().aktiv())
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
