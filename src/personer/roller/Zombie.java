package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Zombie extends Rolle{
	
	Boolean sulten = false;
	
	public Zombie(){
		super("Zombie");
        bilde = "zombie";
		oppgave = "Hvem vil Zombien drepe?";
        veiledning = "Zombien:\n" +
                "Når zombien er drept, holder han seg våken og velger hver natt en person å drepe.\n" +
                "Når zombien har valgt, trykker du på vedkommendes navn for å drepe dem.\n" +
                "Pass på å ikke avsløre at zombien er våken.\n";
        guide = "Er vanlig borger frem til han blir drept. " +
                "Natten etter han dør, holder han seg våken når resten av landsbyen sovner og peker ut et offer i stillhet. " +
                "Etter å ha drept, legger han seg igjen til å sove før noen andre våkner. Men han lever nå kun på natten, og kan ikke snakke eller stemme på dagen. " +
                "Han fortsetter å drepe og sove helt til noen finner ut at han er zombie og dreper han igjen. " +
                "Zombien kan pekes på på natten, hvis noen oppdager han, og kan dermed både blokkeres og drepes. " +
                "Landsbyen kan også velge å henrette zombien på nytt på dagen.";
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
		if(spiller.nyligKlonet())
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
