package personer.roller;

import gui.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Jesus extends Rolle {

	int dag = 3;
	Spiller frelst;
	
	public Jesus(){
		super("Jesus");
		oppgave = "Hvem vil Jesus frelse?";
		veiledning = "Jesus:\n" +
				"Første natt våkner Jesus, og velger en person han vil frelse. Etter dette våkner han ikke på natten\n" +
                "Når Jesus har valgt, trykker du på vedkommendes navn for å frelse dem.\n" +
                "Hvis denne personen blir drept fra da av, dør Jesus i hans sted. Og tre dager etter han dør," +
                "står Jesus opp igjen, med kunnskap om godt og ondt!";
		side = BORGER;
		prioritet = JESUS;
	}

	public int dager(){
		return dag;
	}
	
	public boolean oppstanden(){
		return dag == 0;
	}
	
	public Spiller frelst() {
		return frelst;
	}

	@Override
	public void autoEvne() {
        if(frelst != null && lever()) {
            frelst.beskytt(this);
            frelst.forsvar(this);
            this.spiller.beskytt(this);
        }
        else if(!lever())
            dag--;

        if(oppstanden())
            TvUtil.leggVed("\nJESUS (aka " + spiller + ") ER OPPSTANDEN!!!");
    }

	@Override
	public boolean evne(Spiller spiller) {
		frelst = spiller;
		frelst.beskytt(this);
		frelst.forsvar(this);
		this.spiller.beskytt(this);
		aktiver(false);
		return true;
	}

	@Override
	public void drep() {
		if(snill) dag = 2;
		frelst = null;
		super.drep();
	}
}
