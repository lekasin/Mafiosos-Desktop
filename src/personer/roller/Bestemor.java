package personer.roller;

import java.util.ArrayList;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Bestemor extends Rolle {
	
	boolean flereBesøk = false;
	public Bestemor(){
		super("Bestemor");
		side = BORGER;
		oppgave = "Bestemor våkner";
        guide = "Bestemor våkner aldri på natten, men personer som besøker Bestemor, blir holdt igjen for dessert. " +
                "De er da opptatt(blokkert) og beskyttet(de sitter hos bestemor og er altså ikke hjemme). " +
                "Bestemor er altså imun mot alle typer påvirkning(beskyttelse, blokkering, snåsing osv.). " +
                "Så lenge Bestemor har flere besøkende, kan hun heller ikke drepes, kidnappes eller smithes. " +
                "Hvis mafiaen besøker bestemor sammen med noen andre, dreper mafiaen alle de andre besøkende, " +
                "men ikke bestemor selv, da hun blir stående på kjøkkenet for å lage mat til alle gjestene. Det samme gjelder Princess98 og Smith." +
                "Besøker disse eller andre drapsroller bestemor alene, blir hun påvirket på vanlig måte, siden hun selv må stå for sosialiseringen.";
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
        flereBesøk = false;

        ArrayList<Spiller> besøk = Spill.spillere.besøk(spiller, null);
        for(Spiller s: besøk) {
            if(s.rolle().blokkert())
                break;
            if(lever && !s.beskyttet()) {
                s.beskytt(this);
            }
            else if(spiller.drapsmann() != s.rolle()) {
                s.drep(this);
                flereBesøk = true;
            }

            //Trenger ikke sjekke for Smith eller Princess hvis alle er døde
            if (lever) {
                if (nyligKlonet() && besøk.size() > 1) {
                    if (!s.id(SMITH)) {
                        s.rens(this);
                        s.klon(finnRolle(SMITH));
                        flereBesøk = true;
                    }
                } else if (spiller.kidnappet() && besøk.size() > 1 && !s.id(PRINCESS)) {
                    s.kidnapp(null);
                    flereBesøk = true;
                }
            }
        }

        if(!lever && flereBesøk) spiller.vekk();
        if(spiller.kidnappet() && flereBesøk)
            Spill.spillere.befriSpiller(spiller);
        if(nyligKlonet() && flereBesøk) {
            spiller.avbrytKloning();
            aktiver(false);
        }
    }

    @Override
	public boolean evne(Spiller spiller) {
		return true;
	}
}
