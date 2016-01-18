package personer.roller;

import personer.FlerSpillerRolle;
import personer.Spiller;

public class Bestevenn extends FlerSpillerRolle{

	public Bestevenn(){
		super("Bestevenn");
        bilde = "bestevenn";
		oppgave = "Bestevennene blir kjent!";
		veiledning = "Bestevenner:\n" +
				"Bestevennen våkner bare første natt, og får da se hvem de andre bestevennene er.\n" +
				"De kan ikke velge noen, så for å gå videre trykker du på fortsett.";
        guide = "Bestevennene har ikke så mye å bidra med i spillet, annet enn at de kjenner hverandre. " +
                "Alle bestevennene våkner sammen første natt, og blir kjent med hverandre. " +
                "De kan fra da av jobbe sammen om å finne mafiaen, men vet ikke annet enn at de andre er uskyldige.";
		side = BORGER;
		prioritet = BESTEVENN;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		aktiver(false);
		return super.oppgave();
	}
}
