package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Zombie extends Rolle{
	
	Boolean sulten = false;
	
	public Zombie(){
		super("Zombie");
		oppgave = "Hvem vil Zombien drepe?";
        veiledning = "Zombien:\n" +
                "Når zombien er drept, holder han seg våken og velger hver natt en person å drepe.\n" +
                "Når zombien har valgt, trykker du på vedkommendes navn for å drepe dem.\n" +
                "Pass på å ikke avsløre at zombien er våken.\n";
        side = NØYTRAL;
		prioritet = ZOMBIE;
		aktiver(false);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(snill)
			spiller.snipe(this);
		else
			spiller.drep(this);
		return true;
	}
	
	@Override
	public void drep() {
		if(spiller.klonet())
			super.drep();
		else if(aktiv) {
			super.drep();
			funk(false);
			sulten = false;
		}
		else {
			aktiver(true);
			sulten = true;
		}
	}

	public boolean sulten(){
		return sulten;
	}
}
