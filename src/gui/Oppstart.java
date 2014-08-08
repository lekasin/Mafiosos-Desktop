package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datastruktur.Spillerliste;
import personer.Rolle;
import personer.Spiller;
import personer.roller.*;

public class Oppstart implements ActionListener {

	JPanel innhold;
	JTextField navnefelt;
	JLabel tekst;
	Vindu vindu;
	TV tv;
	Spillerliste spillere;

	Rolle[] roller;
	Knapp fortsett, tilbake;

	int fase = 0, antallspillere = 0, indeks = 0, mafiaer = -1, politi = -1, venner = -1, backup, tid = 8;

	public static final int TITTEL = 50, VELGSPILLERE = 0, VELGROLLER = 1, HVEMERHVA = 2, STARTSPILL = 3;

	public Oppstart(Vindu vindu){
		this.vindu = vindu;
		this.tv = vindu.tv;
		this.spillere = vindu.spillere;
		this.innhold = vindu.innhold;
		
		vindu.kontroll(new Lytter(), false);

		velgSpillere();
	}

	private void velgSpillere() {
		Lytter lytt = new Lytter();
		navnefelt = new JTextField();
		navnefelt.setPreferredSize(Knapp.HEL);
		navnefelt.addActionListener(this);
		navnefelt.requestFocusInWindow();

		vindu.overskrift.setText("Hvem skal spille?");

		Knapp registrer = new Knapp("Registrer", Knapp.HEL, this);
		fortsett = vindu.getFortsett();
		fortsett.setPreferredSize(Knapp.HEL);
		fortsett.setVisible(true);
		tilbake = vindu.getTilbake();
		tilbake.setPreferredSize(Knapp.HEL);
		tilbake.setText("Fjern");

		informer(spillere.toString());
		
		innhold = vindu.innhold();
		innhold.add(navnefelt);
		innhold.add(registrer);
		innhold.add(tilbake);
		innhold.add(fortsett);
		innhold.add(new Knapp("Legg til alle", Knapp.HEL,lytt));
	}

	public void velgRoller(){
		innhold = vindu.innhold();
		tilbake.setPreferredSize(Knapp.HALV);
		innhold.setPreferredSize(new Dimension(680, 800));
		vindu.overskrift.setText("Hvilke roller skal være med?");

		Mafia mafia = new Mafia();
		Politi politi = new Politi();
		Bestevenn venn = new Bestevenn();
		innhold.add(new Knapp("Mafia", mafia, Knapp.KVART, this));
		innhold.add(new Knapp("Politi", politi, Knapp.KVART, this));
		innhold.add(new Knapp("Bestevenn", venn, Knapp.KVART, this));
		innhold.add(new Knapp("Undercover", new Undercover(mafia), Knapp.KVART, this));
		innhold.add(new Knapp("Insider", new Insider(politi), Knapp.KVART, this));

		innhold.add(new Knapp("Lege", new Lege(), Knapp.KVART, this));
		innhold.add(new Knapp("Skytsengel", new Skytsengel(), Knapp.KVART, this));
		innhold.add(new Knapp("HMS-Ansvarlig", new HMSansvarlig(), Knapp.KVART, this));
		innhold.add(new Knapp("Snåsamannen", new Snasamann(), Knapp.KVART, this));
		innhold.add(new Knapp("Jesus", new Jesus(), Knapp.KVART, this));

		innhold.add(new Knapp("Tjukkas", new Tjukkas(), Knapp.KVART, this));
		innhold.add(new Knapp("Julenissen", new Julenissen(), Knapp.KVART, this));
		innhold.add(new Knapp("Marius", new Marius(), Knapp.KVART, this));
		innhold.add(new Knapp("Ravn", new Ravn(), Knapp.KVART, this));
		innhold.add(new Knapp("Obduksjonist", new Obduksjonist(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Rex", new Rex(), Knapp.KVART, this));
		innhold.add(new Knapp("Sherlock", new Sherlock(), Knapp.KVART, this));
		innhold.add(new Knapp("Hammer", new Hammer(), Knapp.KVART, this));
		innhold.add(new Knapp("Filosof", new Filosof(), Knapp.KVART, this));
		innhold.add(new Knapp("Jørgen", new Jorgen(), Knapp.KVART, this));

		innhold.add(new Knapp("Trompet", new Trompet(), Knapp.KVART, this));
		innhold.add(new Knapp("Bøddelen", new Boddel(), Knapp.KVART, this));
		innhold.add(new Knapp("Belieber", new Belieber(), Knapp.KVART, this));
		innhold.add(new Knapp("Arving", new Arving(), Knapp.KVART, this));
		innhold.add(new Knapp("Drømmer", new Drommer(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Special Guy", new Specialguy(), Knapp.KVART, this));
		innhold.add(new Knapp("Heisenberg", new Heisenberg(), Knapp.KVART, this));
		innhold.add(new Knapp("Zombie", new Zombie(), Knapp.KVART, this));
		innhold.add(new Knapp("Morder", new Morder(), Knapp.KVART, this));
		innhold.add(new Knapp("Havfrue", new Havfrue(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Ulf Omar", new UlfOmar(), Knapp.KVART, this));
		innhold.add(new Knapp("Snylter", new Snylter(), Knapp.KVART, this));
		innhold.add(new Knapp("CopyCat", new CopyCat(), Knapp.KVART, this));
		innhold.add(new Knapp("Cupid", new Cupid(), Knapp.KVART, this));
		innhold.add(new Knapp("Kirsten Giftekniv", new Kirsten(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Løgner", new Logner(), Knapp.KVART, this));
		innhold.add(new Knapp("VaraMafia", new Vara(), Knapp.KVART, this));
		innhold.add(new Knapp("Bedrager", new Bedrager(), Knapp.KVART, this));
		innhold.add(new Knapp("Quisling", new Quisling(), Knapp.KVART, this));
		innhold.add(new Knapp("Anarkist", new Anarkist(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Distré Didrik", new Didrik(), Knapp.KVART, this));
		innhold.add(new Knapp("Kløna", new Klona(), Knapp.KVART, this));
		innhold.add(new Knapp("Aktor", new Aktor(), Knapp.KVART, this));
		innhold.add(new Knapp("Bomber", new Bomber(), Knapp.KVART, this));
		innhold.add(new Knapp("Astronaut", new Astronaut(), Knapp.KVART, this));
				
		innhold.add(new Knapp("Liten jente", new Jente(), Knapp.KVART, this));
		
		innhold.add(new Knapp("Informant", new Informant(), Knapp.KVART, this));
		innhold.add(new Knapp("Illusjonist", new Illusjonist(), Knapp.KVART, this));
		innhold.add(new Knapp("Tyler Durden", new Tyler(), Knapp.KVART, this));

//		innhold.add(new Knapp("Bestemor", new Bestemor(), Knapp.KVART, this));
//		innhold.add(new Knapp("Barnslige Erik", new Erik(), Knapp.KVART, this));
//		innhold.add(new Knapp("Bodyguard", new Bodyguard(), Knapp.KVART, this));
//		innhold.add(new Knapp("Youtuber", new Youtuber(), Knapp.KVART, this));
//		innhold.add(new Knapp("Sofasurfer", new Sofasurfer(), Knapp.KVART, this));
//		innhold.add(new Knapp("Agent Smith", new Smith(), Knapp.KVART, this));
//		innhold.add(new Knapp("Magnus Carlsen", new Carlsen(), Knapp.KVART, this));

//		innhold.add(new Knapp("Joker", new Joker(), Knapp.KVART, this));
//		innhold.add(new Knapp("Gærne Berit", new Berit(), Knapp.KVART, this));
//		innhold.add(new Knapp("Princess98", new Princess(), Knapp.KVART, this));
//		innhold.add(new Knapp("Psykolog", new Psykolog(), Knapp.KVART, this));
//		innhold.add(new Knapp("Tyster", new Tyster(), Knapp.KVART, this));
		innhold.add(tilbake);

		tilbake.setText("Tilbake");
		
		antallspillere = spillere.length();
		roller = new Rolle[60];
		roller[Rolle.MAFIA] = mafia;
		informer(spillere.rolleString(roller, --antallspillere));
	}

	public void hvemErHva(){
		innhold = vindu.innhold();
		vindu.overskrift.setText("Hvem er hva?");
		vindu.kontroll(new Lytter(), false);
		tilbake = vindu.getTilbake();
		fortsett = vindu.getFortsett();
		fortsett.setVisible(false);

		tekst = new JLabel();
		tekst.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));

		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(500, 70));
		p.setVisible(true);
		p.add(tekst);
		innhold.add(p);
		
		nesteRolle();
		
		vindu.personknapper(innhold, this);
		vindu.oppdaterRamme(innhold);
	}

	public void startSpill(){
		innhold = vindu.innhold();
		fortsett.setVisible(true);
		fortsett.setText("Start spill!");
		innhold.add(fortsett);
		vindu.overskrift.setText("Klar til å begynne spillet?");
	}
	
	public void nyfase(int f){
		switch (f){
		case VELGSPILLERE: velgSpillere(); break;
		case VELGROLLER: velgRoller(); break;
		case HVEMERHVA: hvemErHva(); 
		informer(spillere.visRoller(roller)); break;
		case STARTSPILL: startSpill(); break;
		}
	}

	public void nesteRolle(){
		while(indeks < roller.length-1 && roller[indeks] == null) indeks++;

		if(indeks == Rolle.BESTEVENN && roller[indeks] != null)
		{
			if(venner == -1)
				venner = ((Bestevenn)roller[indeks]).antall()-1;
			else if(venner > 0)
				venner--;
			else{
				venner = -1;
				indeks++;	
				while(roller[indeks] == null && indeks < roller.length-1) indeks++;
			}
		}
		if(indeks == Rolle.MAFIA && roller[indeks] != null)
		{
			if(mafiaer == -1)
				mafiaer = ((Mafia)roller[indeks]).antall()-1;
			else if(mafiaer > 0)
				mafiaer--;
			else{
				mafiaer = -1;
				indeks++;
				while(roller[indeks] == null && indeks < roller.length-1) indeks++;
			}
		}
		if(indeks == Rolle.POLITI && roller[indeks] != null)
		{
			if(politi == -1)
				politi = ((Politi)roller[indeks]).antall()-1;
			else if(politi > 0)
				politi--;
			else{
				politi = -1;
				indeks++;	
				while(roller[indeks] == null && indeks < roller.length-1) {
					if(indeks == roller.length-1 && roller[roller.length-1] == null)
						nyfase(++fase);
					indeks++;
				}
			}
		}
		
		if(indeks == roller.length-1 && roller[roller.length-1] == null && fase == HVEMERHVA)
			nyfase(++fase);
		else if(indeks < roller.length)
		{
			tekst.setText(roller[indeks].tittel());
			innhold.revalidate();
		}
		else
			nyfase(++fase);

	}

	public void setAntall(int a){
		antallspillere = a;
	}	
	
	private void informer(String informasjon){
		tv.vis(informasjon);
		vindu.informer(informasjon);
	}

	private class Lytter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//////////////////////FORTSETT//////////////////////
			if(e.getSource() == fortsett) {
				if(fase == STARTSPILL){
					Spill spill = new Spill(vindu, roller, tid);
					informer(spillere.hvemErHva());
					spill.natt();
				} 
				else if(spillere.length() < 5)
					informer("Ikke nok spillere!");
				else 
					nyfase(++fase);
			}

			//////////////////////TILBAKE//////////////////////
			else if(e.getSource() == tilbake){
				if(fase == VELGSPILLERE) {
					String n = navnefelt.getText();

					if(spillere.finnSpiller(n) != null)
						antallspillere--;

					spillere.fjern(n);
					informer(spillere.toString());

					navnefelt.setText("");
					navnefelt.requestFocusInWindow();
				} 
				else if(fase == VELGROLLER) {
					if(antallspillere < (spillere.length()-1)) {
						nyfase(fase);
					}
					else
						nyfase(--fase);
				}
				else if(fase == HVEMERHVA || fase == STARTSPILL) {
					int i = indeks; politi = -1; mafiaer = -1; venner = -1;
					for(indeks = 0; roller[indeks] == null; indeks++);
					
					if(i != indeks){
						nyfase(HVEMERHVA);
					} else
						nyfase(--fase);
				}
			}
			//////////////////////LEGG TIL ALLE//////////////////////
			else
			{
				spillere.leggTil(new Spiller("Sondre"));
				spillere.leggTil(new Spiller("Lars-Erik"));
				spillere.leggTil(new Spiller("Ørnulf"));
				spillere.leggTil(new Spiller("Anette"));
				spillere.leggTil(new Spiller("Henrik"));
//				spillere.leggTil(new Spiller("Kjetil"));
//				spillere.leggTil(new Spiller("Jens Emil"));
//				spillere.leggTil(new Spiller("Ole-Halvor"));
//				spillere.leggTil(new Spiller("Randi"));
//				spillere.leggTil(new Spiller("Bård Anders"));
//				spillere.leggTil(new Spiller("Simon"));
//				spillere.leggTil(new Spiller("Ole Martin"));
//				spillere.leggTil(new Spiller("Emil"));
//				spillere.leggTil(new Spiller("Ingrid"));
//				spillere.leggTil(new Spiller("Anders"));
//				spillere.leggTil(new Spiller("Andreas"));

				informer(spillere.toString());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(fase == VELGSPILLERE)
		{
			if(navnefelt.getText().matches("[0-9]|[0-9][0-9]")) {
				tid = Integer.parseInt(navnefelt.getText());
			}
			else if(spillere.finnSpiller(navnefelt.getText()) == null){
				spillere.leggTil(new Spiller(navnefelt.getText()));
				antallspillere++;
				informer(spillere.toString());
			}
			else{
				informer("Finnes allerede");
			}
			
			navnefelt.setText("");
			navnefelt.requestFocusInWindow();
			return;
		}

		Knapp k = (Knapp) e.getSource();

		if(fase == VELGROLLER)
		{
			int i = k.rolle().pri();

			if(roller[i] == null) {
				roller[i] = k.rolle();
				if(i != Rolle.POLITI && i != Rolle.MAFIA && i != Rolle.BESTEVENN)
					k.setEnabled(false);
			}
			else if(i == Rolle.POLITI)
				((Politi)roller[i]).fler();
			else if(i == Rolle.BESTEVENN)
				((Bestevenn) roller[i]).fler();
			else if(i == Rolle.MAFIA)
				((Mafia) roller[i]).fler();

			informer(spillere.rolleString(roller, --antallspillere));
			if(antallspillere < 1){
				nyfase(++fase);
			}
		}
		else if(fase == HVEMERHVA){

			k.spiller().setRolle(roller[indeks]);
			if(indeks != Rolle.POLITI && indeks != Rolle.MAFIA && indeks != Rolle.BESTEVENN)
				indeks++;
			nesteRolle();
			k.setEnabled(false);
		}
	}
}
