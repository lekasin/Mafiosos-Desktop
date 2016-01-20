package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Boddel extends Rolle {

	public Boddel(){
		super("Bøddel");
        bilde = "boddel";
		oppgave = "Hvem vil Bøddelen halshugge?";
        veiledning = "Halshugging:\n" +
                "Bøddelen har nå bestemt seg for å ta saken i egne hender, og kan nå velge en person å halshugge!\n" +
                "For å halshugge en person, trykker du på vedkommendes navn, og vedkommende blir da drept umiddelbart.\n" +
                "Bøddel signaliserer hvem han vil halshugge ved å løfte armene " +
                "(eller eventuelt bøddelvåpenet, hvis noe sånt er tilgjengelig) over hodet, og hugge mot den han vil drepe.";
        guide = "Bøddelen kan når som helst på dagtid finne fram øksen sin og drepe en annen borger. " +
                "Dette synes imidlertid landsbyen er forkastelig, og bøddelen blir umiddelbart bortvist fra landsbyen (Han er da ute av spillet). " +
                "Bøddelen må da nøye vurdere når han vil bruke evnen sin, og blir han drept på natten, er løpet kjørt. " +
                "Bøddelen kan senest gripe øksen FØR den avgjørende avstemming starter.";
        side = BORGER;
		prioritet = BØDDEL;
		aktiver(false);
	}
	
	@Override
	public String oppgave() {
        if(spiller.nyligKlonet())
            return super.oppgave();
		return TvUtil.vis(oppgave);
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		spiller.drep(this);
		drep();
		return true;
	}
	
	@Override
	public void jul() {
		super.jul();
		spiller.beskytt(this);
	}
}
