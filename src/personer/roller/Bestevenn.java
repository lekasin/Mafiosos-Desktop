package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Bestevenn extends Rolle {

	int antall = 1, levende = 1;

	public Bestevenn(){
		super("Bestevenn");
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
    public int antall(){
		return antall;
	}
	
	public void fler(){
		antall++;
		levende++;
	}
	
	public void fjern(){
		antall--;
		levende--;
	}
	
	public boolean flere(){
		return levende > 1;
	}
	
	@Override
	public void drep(){
		if(!flere())
			lever = false;
		levende--;
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
