package personer.roller;

import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Boddel extends Rolle {

	public Boddel(){
		super("Bøddel");
		oppgave = "Hvem vil Bøddelen halshugge?";
        veiledning = "Halshugging:\n" +
                "Bøddelen har nå bestemt seg for å ta saken i egne hender, og kan nå velge en person å halshugge!\n" +
                "For å halshugge en person, trykker du på vedkommendes navn, og vedkommende blir da drept umiddelbart.\n" +
                "Bøddel signaliserer hvem han vil halshugge ved å løfte armene " +
                "(eller eventuelt bøddelvåpenet, hvis noe sånt er tilgjengelig) over hodet, og hugge mot den han vil drepe.";

        side = BORGER;
		prioritet = BØDDEL;
		aktiver(false);
	}
	
	@Override
	public String oppgave() {
        // TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		super.jul();
		spiller.beskytt(this);
	}
}
