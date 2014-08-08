package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import personer.Rolle;
import personer.Spiller;
import datastruktur.Spillerliste;

public class TV extends JFrame {

	private static final long serialVersionUID = 1L;
	Spillerliste spillere;
	JTextArea tv;
	String vedlegg = "";
	
	public TV(String tittel, Spillerliste sl){
		super(tittel);
		
		spillere = sl;
		//TV (Tekstfelt på TV-en)
		setVisible(true);
		setMinimumSize(new Dimension(800,800));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		tv = new JTextArea();
		tv.setBorder(BorderFactory.createLoweredBevelBorder());
		tv.setEditable(false);
		tv.setLineWrap(true);
		tv.setWrapStyleWord(true);
		tv.setFont(new Font("Sans-Serif", Font.BOLD, 40));
		add(tv);
		
	}
	
	public String vis(String t){
		tv.setText(t);
		return t;
	}
	
	public void rens(){
		tv = new JTextArea();
	}
	
	public String leggtil(String t){
		tv.append(t);
		return t;
	}

	public Spillerliste spillere() {
		return spillere;
	}

	public void rex(Spiller s) {
		leggtil(spillere.rex(s));
	}

	public void jørgen() {
		vis(spillere.jørgensListe());
	}
	
	public void drøm(Spiller s) {
		vis(spillere.drøm(s));
	}
	public void bedrag() {
		vis(spillere.mafiaNavn());
	}
	
	public void special(boolean lever) {
		vis((lever) ? "Special Guy er ikke drept." : "Special Guy ER drept! \n\nHvem vil Special Guy drepe?");
	}
	
	public void quisling(boolean lever, Spiller s) {
		vis((lever) ? "Quisling er ikke drept." : "Quisling ER drept,\nog konverterer til Mafiaens side!");
		if(!lever && s.funker()) {
			s.setRolle(spillere.finnRolle(Rolle.MAFIA));
			s.vekk();
			s.rolle().setSpiller(s);
		} else if(!s.lever())
			vis("Quisling er drept.");
	}

	public String vedlegg() {
		return vedlegg;
	}
	
	public void leggVed(String s) {
		vedlegg += s;
	}
	
	public void nyttVedlegg() {
		vedlegg = "";
	}
}
