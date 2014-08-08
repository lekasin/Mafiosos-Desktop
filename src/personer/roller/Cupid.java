package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Cupid extends Rolle {

	Spiller mann = null;

	public Cupid(){
		super("Cupid");
		oppgave = "Hvem vil Cupid spleise?";
		side = BORGER;
		prioritet = CUPID;
		fortsett = false;
	}

	@Override
	public String oppgave() {
		fortsett = false;
		return super.oppgave();
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(spiller != offer)
			return false;
		
		if(spiller.id(Rolle.BELIEBER)) ((Belieber)spiller.rolle()).beliebe();

		if(mann == null) {
			mann = spiller;
			return true;
		} else {
			fortsett = true;

			if(blokkert){
				mann = null;
				return false;
			}
			
			if(spiller.rolle().blokkert()); //HMMMMMMM
			if(!spiller.lever()) 	mann.drep(spiller.drapsmann());
			if(spiller.beskyttet())	mann.beskytt(spiller.beskytter()); 
			if(spiller.forsvart()) 	mann.forsvar(spiller.forsvarer());
			if(spiller.reddet()) 	mann.redd(spiller.redning());
			if(spiller.løgn()) 		mann.lyv(spiller.løgner());
			if(spiller.skjult()) 	mann.skjul(spiller.skjuler());
			if(spiller.kløna()) 	mann.kløn(spiller.kløne());
			
			if(mann.rolle().blokkert());//HMMMMMMM
			if(!mann.lever()) 	 	spiller.drep(spiller.drapsmann());
			if(mann.beskyttet())	spiller.beskytt(spiller.beskytter()); 
			if(mann.forsvart()) 	spiller.forsvar(spiller.forsvarer());
			if(mann.reddet()) 		spiller.redd(spiller.redning());
			if(mann.løgn()) 		spiller.lyv(spiller.løgner());
			if(mann.skjult()) 		spiller.skjul(spiller.skjuler());
			if(mann.kløna()) 		spiller.kløn(spiller.kløne());
			
			mann = null;
		}
		return true;
	}
}
