package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;

import personer.Rolle;
import personer.Spiller;
import personer.roller.*;
import datastruktur.Countdown;
import datastruktur.Spillerliste;

public class Spill implements ActionListener {

	public Vindu vindu;
	TV tv;
	JPanel innhold;
	Countdown timer;

	Spillerliste spillere;
	ArrayList<Spiller> døde;
	LinkedList<Rolle> roller = new LinkedList<>();
	ListIterator<Rolle> i;
	
	Spiller sisteDød;
	Rolle aktiv;
	String annonse;
	Knapp halshugg, snipe;
	int døgn, antallDøde, tid;
	boolean dag, seier, tale, rakett;
	boolean sniper = false, dealer = false, sabotage = false;

	public Spill(Vindu v, Rolle[] r, int t) {
		vindu 		= v;
		spillere 	= v.spillere;
		innhold 	= v.innhold;
		tv 			= v.tv;
		tid = t;
		timer = new Countdown(vindu.getKlokke(), this);


		for (Rolle rolle : r)
			if (rolle != null){
				roller.add(rolle);
				rolle.setTV(tv);
				if(sjekkRolle(Rolle.BØDDEL)) 
					halshugg = new Knapp("Halshugg!", Knapp.HALV, new Special());
			}

		rapporter("Rapport:");
	}

	public void natt() {
		rapporter("\nNY NATT");

		tv.nyttVedlegg();
		dag = false;
		spillere.sov();
		refresh();
		vindu.kontroll(new Kontroll(), false);

		i = roller.listIterator();

		if (sjekkVinner()) {
			nesteRolle();
			døgn++;
		}
	}

	public void dag() {
		dag = true;
		rapporter("\nNY DAG");

		spillere.våknOpp();
		refresh();
		tittuler("Hvem vil landsbyen henrette?");
		timer.nyStart(tid);
		dødsannonse();
		sjekkVinner();
		tv.toFront();
	}
	
	public void godkjenn() {
		timer.stop();
		vindu.kontroll.setVisible(false);
		innhold = vindu.innhold();
		if(sjekkRolle(Rolle.OBDUK)) finnRolle(Rolle.OBDUK).aktiver(spillere.lik().size()>2);
		if(sjekkRolle(Rolle.HMS)) gjenoppliv();
		if(seier) {
			innhold.add(new Knapp("Nytt Spill", Knapp.SUPER, new Special()));
			tittuler("Vi har en vinner!");
		}
		else
			innhold.add(new Knapp("Landsbyen sovner", Knapp.SUPER, new Special()));
		innhold.revalidate();
		innhold.repaint();
	}

	public void refresh(Rolle r) {
		timer.stop();
		vindu.oppdaterKnapper(innhold, this, r);
		if(r instanceof Mafia){
			JPanel p = new JPanel();
			p.setPreferredSize(Vindu.kontrollSize);
			Special mk = new Special();
			if(sniper)
				p.add(new Knapp("Snipe", Knapp.HALV, mk));
			if(dealer)
				p.add(new Knapp("Beskytt", Knapp.HALV, mk));
			if(sabotage)
				p.add(new Knapp("Saboter", Knapp.HALV, mk));
			innhold.add(p);
			tittuler(r.oppgave());
		}
	}

	public void refresh() {
		innhold = vindu.innhold();
		vindu.personknapper(innhold, this);
		vindu.kontroll(new Kontroll(), true);
		vindu.oppdaterRamme(innhold);

		if(sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
			innhold.add(halshugg);
	}
	
	public void refresh(Spiller s, Boolean alene) {
		innhold = vindu.innhold();
		vindu.personligknapper(innhold, this, s, alene);
		vindu.kontroll(new Kontroll(), true);
		vindu.oppdaterRamme(innhold);

		if(sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
			innhold.add(halshugg);
	}

	public void pek(Rolle r) {
		aktiv = r;
		refresh(r);
		tittuler(r.oppgave());
		if(r.informert()) vindu.overskrift.setForeground(Color.BLUE);
		if(!r.funker()) vindu.overskrift.setForeground(Color.RED);
//		tv.leggtil(spillere.valg(r));
	}

	public void nesteRolle() {
		if (i.hasNext()) {
			Rolle r = i.next();

			while (!r.aktiv() || r == aktiv){
				if(r instanceof Jesus) r.oppgave();
				if(i.hasNext()) r = i.next();
				else break;
			}
			if(r.aktiv()) {
				pek(r);
			} else
				dag();
		}
		else
			dag();
	}

	public void forrigeRolle() {
		if(i.hasPrevious()){
			rapporter("Tilbake");

			if(dag) { 
				dag = false;
				pek(aktiv);
			}
			else {
				Rolle r = i.previous();
				while (!r.aktiv() || r == aktiv){
					if(i.hasPrevious()) r = i.previous();
					else break;
				}

				if (r.aktiv()) {
					r.rens();
					pek(r);
				}
			}
		} else {
			tv.vis("Kan ikke angre");
		}
	}

	//////////////////////////////////////// SMÅ METODER ///////////////////////////////////
	
	public Rolle finnRolle(int id) {
		return spillere.finnRolle(id);
	}
	
	public Spiller finnSpiller(int id) {
		return spillere.finnSpillerSomEr(id);
	}
	
	public Spiller finnOffer(int id) {
		return spillere.finnRolle(id).offer();
	}
	
	public boolean taler() {
		return tale;
	}

	public Boolean sjekkRolle(int id) {
		return spillere.finnRolle(id) != null;
	}
	
	
	//////////////////////////////////////  KVELDEN  ///////////////////////////////////////
	public void talt() {
		tittuler("Hvem vil landsbyen henrette?");
		timer.setText(annonse);
		timer.nyStart(2);
		tale = false;
	}

	public void gjenoppliv() {
		for(Spiller s: døde)
			if(s.reddet()) {
				s.vekk();
				tv.leggtil("\n\n" + s + " er gjenopplivet!");
				rapporter("\n" + s + " er gjenopplivet!");
				spillere.gjenoppliv(s);
			}
	}
	
	public boolean sjekkVinner() {
		if(finnRolle(Rolle.ARVING) != null)
			((Arving)finnRolle(Rolle.ARVING)).arv();
		switch (spillere.vinner()) {
		case -1:
			informer("Mafiaen har vunnet!");
			rapporter("Mafiaen har vunnet!");
			seier = true;
			break;
		case 1:
			informer("Landsbyen har vunnet!"); 
			rapporter("Landsbyen har vunnet!");
			seier = true;
			break;
		case 2:
			informer("Alle er døde! Ingen vant!"); 
			rapporter("Alle er døde! Ingen vant!");
			seier = true;
			break;
		case 3:
			informer("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!"); 
			rapporter("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkister har vunnet!");
			seier = true;
			break;

		default:
			return true;
		}
		godkjenn();
		return false;
	}

	public void henrett(Spiller s) {
		String ut;
		Spiller bombet = null;
		if(s == null)
			if(sjekkRolle(Rolle.BOMBER) && finnOffer(Rolle.BOMBER) != null) {
				s = finnOffer(Rolle.BOMBER);
				bombet = s;
			}
			else {
				rapporter("Ingen henrettet!");
				proklamer("Ingen henrettet!");
				tv.toFront();
				return;
			}
		
		ut = s.toString();
		int side = s.side();
		Spiller jesus = finnSpiller(Rolle.JESUS);
		
		if(s.løgn()) side = side-(2*side);

		if(s.forsvart() && !(aktiv.id(Rolle.TROMPET) && aktiv.snill()) && !(aktiv.id(Rolle.BØDDEL) && aktiv.snill()) && !s.id(Rolle.BOMBER)) {
			ut += " er beskyttet, og er derfor ikke død!";
		} else {
			if((aktiv.id(Rolle.TROMPET) || aktiv.id(Rolle.BØDDEL)) && aktiv.snill()) s.snipe(null);

			if (s.skjult())
				ut += " kan ha vært hva som helst.\nPapirene er rotet bort.";
			else if (side > Rolle.MAFIOSO)
				ut += " var IKKE mafia!";
			else{
				ut += " var mafia!";
				if(sjekkRolle(Rolle.VARA)){
					ut += "\n\nOg dermed trer VaraMafiaen inn i hans sted!";
					finnSpiller(Rolle.VARA).setRolle(s.rolle());
				}
			}
			
			if(sjekkRolle(Rolle.BELIEBER) && finnRolle(Rolle.BELIEBER).funker())
					((Belieber) finnRolle(Rolle.BELIEBER)).beliebe();
			
			if(sjekkRolle(Rolle.BOMBER) && finnOffer(Rolle.BOMBER) != null) {
				bombet = finnOffer(Rolle.BOMBER);
				if(s.id(Rolle.BOMBER) && !s.forsvart())
					ut = s + " VAR Bomberen, og bomben er desarmert!";
				else {
					if(s.id(Rolle.BOMBER))
						ut = s + " er beskyttet, og er derfor ikke død!";
					
					if(bombet == s) ut = "";
					else ut += "\n\n";
					
					if(!bombet.forsvart() || finnRolle(Rolle.BOMBER).snill()) {
						bombet.henrett();
						ut += bombet + " ble sprengt,\n"
							+ "og var " + (bombet.side() < 0? "" : "IKKE ") + "mafia!";
					} else {
						ut += "\n\n" + bombet + " ble forsøkt sprengt,\n"
							+ "men er beskyttet, og er derfor ikke død!";
					}
				}
			}
			
			if(!(s.id(Rolle.BOMBER) && s.forsvart()))
				s.henrett();

//			if(!s.id(Rolle.ZOMBIE)) s.stopp();
			spillere.dødsannonse();
		}
		
		if(jesus != null && (((Jesus)jesus.rolle()).frelst() == s || (bombet != null && s != finnSpiller(Rolle.BOMBER) && 
							((Jesus)jesus.rolle()).frelst() == bombet)))
			jesus.snipe(jesus.rolle());
		
		if(sjekkRolle(Rolle.ILLUSJONIST) && finnRolle(Rolle.ILLUSJONIST).offer() == s)
			finnRolle(Rolle.ILLUSJONIST).spiller().snipe(finnRolle(Rolle.ILLUSJONIST));
		
		informer(ut);
		rapporter("Landsbyen har drept " + s + "(" + s.rolle() + ")");
		rapporter(ut);
		tv.toFront();
	}

	////////////////////////////// TV-SKERM //////////////////////////////////
	
	public void informer(String tekst) {
		tv.vis(tekst);
	}

	public void proklamer(String tekst) {
		vindu.overskrift.setText(tekst);
		vindu.overskrift.setForeground(Color.BLACK);
		tv.vis(tekst);
	}

	public void tittuler(String tekst) {
		vindu.overskrift.setText(tekst);
		vindu.overskrift.setForeground(Color.BLACK);
	}

	public void rapporter(String tekst){
		if(vindu.info.getText().length() > 1) vindu.info.append("\n");
		vindu.info.append(tekst);
	}

	public void dødsannonse() {
		annonse = "Ingen døde i natt!\n";

		døde = spillere.dødsannonse();
		if (døde.size() > 0) {
			annonse = "";
			for(Spiller s: døde)
				annonse += s + " er død!\n";
		}
		
		annonse += tv.vedlegg();

		if(sjekkRolle(Rolle.JESUS) && ((Jesus)finnRolle(Rolle.JESUS)).oppstanden()){
			finnSpiller(Rolle.JESUS).vekk();
			refresh();
		}

		if (sjekkRolle(Rolle.AKTOR) && finnOffer(Rolle.AKTOR) != null) {
				refresh(finnOffer(Rolle.AKTOR), true);
				timer.nyStart(2);
		}
		
		if (sjekkRolle(Rolle.BOMBER) && finnOffer(Rolle.BOMBER) != null) {
			timer.nyStart(2);
		}
		
		rapporter(annonse);
		timer.setText(annonse);
		informer(timer.format(tid*60) + "\n" + annonse);

		if (sjekkRolle(Rolle.ASTRONAUT) && finnOffer(Rolle.ASTRONAUT) != null) {
			timer.stop();
			informer(annonse + "\nDet er tid for rakettoppskytning!!!");
			rapporter("\nDet er tid for rakettoppskytning!!!");
			rakett = true;
			tittuler("Hvem skal sendes ut i verdensrommet?");
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////
	
	//KNAPPEACTION
	public Knapp knapp(ActionEvent e){
		return (Knapp) e.getSource();
	}

	public boolean knapp(ActionEvent e, String s){
		return knapp(e).getText() == s;
	}

	
	private class Kontroll implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//TRYKKER TILBAKE
			if(knapp(e,"Tilbake")){
				forrigeRolle();
			}
			//TRYKKER FORTSETT
			else if(knapp(e,"Fortsett")){
				if(dag)
					if(rakett) {
						refresh();
						tittuler("Hvem vil landsbyen henrette?");
						timer.nyStart(5);
						rakett = false;
					}
					else if(tale)
						talt();
					else {
						henrett(null);
						godkjenn();
					}
				else
					nesteRolle();
			}
			else if(knapp(e, "Pause")) {
				knapp(e).setText("Start");
				timer.pause();
			}  else if(knapp(e, "Start")) {
				knapp(e).setText("Pause");
				if(timer.sjekk()) timer.fortsett();
			}
		}
	}

	private class Special implements ActionListener{
		public void actionPerformed(ActionEvent e) {			
			//HALSHUGG
			if(knapp(e,"Halshugg!")) {
				timer.stop();
				aktiv = finnRolle(Rolle.BØDDEL);
				henrett(finnSpiller(Rolle.BØDDEL));
				refresh();
				proklamer("Hvem vil bøddelen halshugge?");
				rapporter("Hvem vil bøddelen halshugge?");
				
				if(aktiv.spiller().forsvart() && aktiv.spiller().forsvarer().id(Rolle.JESUS)) {
					tv.leggtil("\n\nJesus har ofret seg for bøddelen, " + aktiv.spiller() + ", og " + 
							finnSpiller(Rolle.JESUS) + " er derfor død i hans sted!");
					aktiv.spiller().forsvarer().funk(false);
				}
				
				return;
			}
			//MAFIAKNAPPER
			if(knapp(e,"Snipe")) {
				((Mafia) finnRolle(Rolle.MAFIA)).snipe();
				sniper = false;
				knapp(e).setEnabled(false);
				proklamer("Hvem vil sniperen skyte?");
				return;
			}
			else if(knapp(e,"Beskytt")) {
				spillere.deal();
				dealer = false;
				knapp(e).setEnabled(false);
				return;
			}
			else if(knapp(e,"Saboter")) {
				((Mafia)aktiv).sabotage();
				sabotage = false;
				knapp(e).setEnabled(false);
				proklamer("Hvem vil sabotøren sabotere?");
				return;
			}

			//LANDSBYEN SOVNER
			else{
				if(seier){
					timer.stop();
					spillere.restart();
					vindu.restart();
					vindu.startopp();
				} else
					natt();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Spiller offer = knapp(e).spiller();

		//VELGER PÅ DAGEN
		if(dag){
			if(!(aktiv.id(Rolle.BØDDEL) || aktiv.id(Rolle.TROMPET) || (sjekkRolle(Rolle.BOMBER) && finnOffer(Rolle.BOMBER) != null) || rakett || knapp(e).tal() || offer.kløna())) {
				tale = true;
				tittuler(knapp(e).getText() + " forsvarer seg!");
				rapporter(knapp(e).getText() + " forsvarer seg!");
				timer.setText(knapp(e).getText() + " forsvarer seg!");
				timer.nyStart(1);

				return;
			}

			tittuler(offer.navn() + " ble henrettet!");
			henrett(offer);
			if(offer.rolle().id(Rolle.TROMPET) && !offer.forsvart()) {
					timer.stop();
					knapp(e).setEnabled(false);
					aktiv = offer.rolle();
					tittuler("Hvem vil Trompeten sprenge?");
					informer(offer.navn() + " var IKKE mafia, men TROMPET!");
			} 
			else if(rakett)
				vindu.oppdaterKnapper(innhold, this, finnRolle(Rolle.ASTRONAUT));
			else
				godkjenn();
		} 

		//VELGER PÅ NATTEN
		else {
			if(aktiv.funker() || (aktiv instanceof Specialguy && aktiv.aktiv())){
				aktiv.pek(offer); 
				rapporter(aktiv.rapport());
			}
			if(aktiv.fortsetter())
				nesteRolle();
			else {
				refresh(aktiv.id(Rolle.MAFIA) 	|| aktiv.id(Rolle.COPYCAT) 	|| 
						aktiv.id(Rolle.KIRSTEN) || aktiv.id(Rolle.CUPID) ? aktiv : null);
			}
		}
	}
}
