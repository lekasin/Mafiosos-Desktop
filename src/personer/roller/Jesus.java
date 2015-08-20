package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Jesus extends Rolle {

	int dag = 3;
	Spiller frelst;
	
	public Jesus(){
		super("Jesus");
		oppgave = "Hvem vil Jesus frelse?";
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
	public String oppgave() {
		// TODO Auto-generated method stub
		if(frelst != null && lever()) {
			frelst.beskytt(this);
			frelst.forsvar(this);
			this.spiller.beskytt(this);
		}
		else if(!lever()) 
			dag--;
			
		if(oppstanden()) 
			tv.leggVed("\nJESUS (aka " + spiller + ") ER OPPSTANDEN!!!");
		return super.oppgave();
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
		// TODO Auto-generated method stub
		if(snill) dag = 2;
		frelst = null;
		super.drep();
	}
}
