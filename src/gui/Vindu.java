package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import personer.Rolle;
import personer.Spiller;
import personer.roller.Hammer;
import datastruktur.Spillerliste;

public class Vindu extends JFrame{

	private static final long serialVersionUID = 1L;
	TV tv;
	JPanel rammeverk, header, innhold, display, kontroll, rammen;
	JLabel overskrift, klokke;
	JTextArea info;
	Knapp tilbake, fortsett, pause;
	Border ramme = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

	Spillerliste spillere;

	public static Dimension rammeSize = new Dimension(500,1000), innholdSize = new Dimension(500,580), toppSize = new Dimension(1100,100), 
							totalSize = new Dimension(1180,800), kontrollSize = new Dimension(500,70);

	public Vindu(String tittel, Spillerliste spillere) {

		super(tittel);
		this.spillere = spillere;
		tv = new TV("MafiososInfo", spillere);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//Panel som inneholder alle de andre panelene og fordeler dem i vinduet
		rammeverk = new JPanel(new BorderLayout());
		add(rammeverk);
		fyllRamme(); //Fyller rammeverket

		pack();

		setVisible(true);
		setMinimumSize(totalSize);
		setLocation(250,0);
		setResizable(true);
	}

	//Oppretter og legger inn elementer til vinduet
	public void fyllRamme() {
		
		//HEADER
		header = new JPanel();
		header.setPreferredSize(toppSize);
		header.setBorder(ramme);

		klokke 		= new JLabel();
		overskrift 	= new JLabel();
		overskrift	.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
		klokke		.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
		overskrift.setText("Velkommen til Oslo Mafiosos!");
		header.add(overskrift);
		header.add(klokke);


		//DISPLAY (Venstre)
		display = new JPanel();
//		display.setPreferredSize(innholdSize);
		display.setBorder(ramme);
		
		info = new JTextArea(41, 40);
		info.setBorder(BorderFactory.createLoweredBevelBorder());
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		//info.setFont(new Font("Serif", Font.BOLD, 15));
		JScrollPane scroller = new JScrollPane(info);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		display.add(scroller);
		
		//RAMME
		rammen = new JPanel();
		rammen.setBorder(ramme);
		rammen.setPreferredSize(rammeSize);
		
		//INNHOLD (Knapper/midten)
		innhold = new JPanel();
		innhold.setPreferredSize(innholdSize);
		rammen.add(innhold);
		
//		//KONTROLL (Tilbake/Fortsett)
		kontroll = new JPanel();
		kontroll.setPreferredSize(kontrollSize);
		
		//Legger alt på plass i rammeverket
		rammeverk.add(display, BorderLayout.WEST);
		rammeverk.add(header, BorderLayout.NORTH);
		rammeverk.add(rammen, BorderLayout.CENTER);
		revalidate();
	}
	
	public void personknapper(JPanel panel, ActionListener al){
		innhold();
		for(Spiller s: spillere.spillere())
			panel.add(new Knapp(s.navn(), s, Knapp.HALV, al));
	}
	
	public void personligknapper(JPanel panel, ActionListener al, Spiller valgt, Boolean alene) {
		innhold();
		for(Spiller s: spillere.spillere()) {
			Knapp k;
			if(alene)
				k = new Knapp(s.navn(), s, Knapp.HALV, al, s == valgt);
			else
				k = new Knapp(s.navn(), s, Knapp.HALV, al);
			if(s == valgt)
				k.tal();
			panel.add(k);
		}
	}

	public void oppdaterKnapper(JPanel panel, ActionListener al, Rolle r){
		Component[] knapper = panel.getComponents();

		for(Component c: knapper){
			if(c instanceof Knapp){
				Knapp k = (Knapp) c;
				k.setForeground(Color.BLACK);
				if(k.spiller() != null) {
					if(r == null || !r.funker() || k.spiller() == r.forbud() || k.spiller() == r.forbud2() || 
							r.id(Rolle.JØRGEN) 		|| r.id(Rolle.BEDRAGER) || r.id(Rolle.QUISLING) || 
							r.id(Rolle.BESTEVENN) 	|| r.id(Rolle.DRØMMER)	|| (r.id(Rolle.ASTRONAUT) && (!r.aktiv() || k.spiller != r.spiller())) ||
							(r.id(Rolle.SPECIAL) && r.lever()) || 
							!r.funker() || (r.id(Rolle.OBDUK) && (k.spiller().funker() || k.spiller().skjult())))
						k.setEnabled(false);
					else if((k.spiller().rolle().funker() && k.spiller().funker()) || r.id(Rolle.OBDUK)){
						k.setEnabled(true);
						if(r instanceof Hammer && ((Hammer)r).valgt() == k.spiller() || r.id(Rolle.HEISENBERG) && r.offer() == k.spiller())
							k.setForeground(Color.GREEN);
					}
				}
			}
			else if(c instanceof JPanel)
				panel.remove(c);
		}
		panel.revalidate();
		panel.repaint();
	}

	public void oppdaterRamme(JPanel p){
		rammen.removeAll();
		rammen.add(p);
		rammen.add(kontroll);
		rammen.revalidate();
		rammen.repaint();
	}
	
	public JPanel innhold() {
		innhold = new JPanel();
		innhold.setPreferredSize(Vindu.innholdSize);
		oppdaterRamme(innhold);
		return innhold;
	}
	
	public void kontroll(ActionListener al, boolean time){
		//KONTROLL (Fortsett/Tilbake++-knapper)
		tilbake = new Knapp("Tilbake", (time) ? Knapp.HALV : Knapp.HEL, al);
		fortsett = new Knapp("Fortsett", (time) ? Knapp.HALV : Knapp.HEL, al);
		kontroll.removeAll();
		kontroll.add(tilbake);
		if(time) {
			pause = new Knapp("Pause", Knapp.HALV, al);
			kontroll.add(pause);
		}
		kontroll.add(fortsett);
		kontroll.revalidate();
		kontroll.repaint();
		kontroll.setVisible(true);
	}
	
	public Knapp getTilbake() {
		return tilbake;
	}
	
	public Knapp getFortsett() {
		return fortsett;
	}
	
	public void pause() {
		pause.setText("Pause");
	}
	
	public JLabel getKlokke() {
		return klokke;
	}
	
	public void nullstill(){
		rammen.removeAll();
		header = new JPanel();
		display = new JPanel();
		innhold = new JPanel();
		overskrift = new JLabel();
	}
 
	public void restart(){
		//Panel som inneholder alle de andre panelene og fordeler dem i vinduet
		remove(rammeverk);
		rammeverk = new JPanel(new BorderLayout());
		add(rammeverk);
		
		nullstill();
		fyllRamme(); //Fyller rammeverket
		revalidate();
		repaint();
	}
	
	public void startopp(){
		Oppstart o = new Oppstart(this);
		o.setAntall(spillere.length());
	}

	public void informer(String informasjon) {
		info.setText(informasjon);
	}

}