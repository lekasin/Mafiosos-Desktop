package personer.roller;

import java.util.ArrayList;

import personer.Rolle;
import personer.Spiller;

public class Bestemor extends Rolle {
	
	boolean flereBesøk = false;
	public Bestemor(){
		super("Bestemor");
		side = BORGER;
		oppgave = "Bestemor våkner";
		prioritet = BESTEMOR;
		aktiver(false);
	}

	public boolean flereBesøk(){
		return flereBesøk;
	}

    @Override
    public void autoEvne() {
        if(spiller.død() || spiller.rolle().id(SMITH))
            return;

        spiller.rensAlle();

        ArrayList<Spiller> besøk = tv.spillere().besøk(spiller, null);
        for(Spiller s: besøk) {
            if(s.rolle().blokkert())
                break;
            if(lever) {
                s.beskytt(this);
            }
            else if(spiller.drapsmann() != s.rolle()) {
                s.drep(this);
                flereBesøk = true;
            }
            if(spiller.kidnappet() && besøk.size() > 1 && !s.id(PRINCESS)) {
                s.kidnapp(null);
                flereBesøk = true;
            }
        }

        if(!lever && flereBesøk) spiller.vekk();
        if(spiller.kidnappet() && flereBesøk) tv.spillere().befriSpiller(spiller);
    }

    @Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
