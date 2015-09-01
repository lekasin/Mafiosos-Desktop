package gui;

import datastruktur.Spillerliste;
import personer.Rolle;
import personer.Spiller;
import personer.roller.Quisling;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class TV extends JFrame {

	private static final long serialVersionUID = 1L;
	Spillerliste spillere;
	JTextArea tv, rolleListe;
	String vedlegg = "", roller = "";
	
	public TV(String tittel, Spillerliste sl){
		super(tittel);
		
		setLayout(new BorderLayout());

        spillere = sl;
		//TV (Tekstfelt på TV-en)
		setVisible(true);
		setMinimumSize(new Dimension(900, 700));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		tv = new JTextArea();
		tv.setEditable(false);
		tv.setLineWrap(true);
		tv.setWrapStyleWord(true);
		tv.setFont(new Font("Sans-Serif", Font.BOLD, 30));
		tv.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tv.setBackground(new Color(50, 60, 63));
        tv.setForeground(new Color(182, 78, 73));
        ((DefaultCaret)tv.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(tv);

//        JColorChooser jcc = new JColorChooser(tv.getBackground());
//        jcc.getSelectionModel().addChangeListener(e -> {
//            Color newColor = jcc.getColor();
//            tv.setBackground(newColor);
//        });
//        add(jcc, BorderLayout.SOUTH);

		rolleListe = new JTextArea();
		rolleListe.setFont(new Font("Sans-Serif", Font.BOLD, 30));
		rolleListe.setEditable(false);
		rolleListe.setText(roller);
		rolleListe.setPreferredSize(new Dimension(250, 400));
        rolleListe.setBackground(new Color(54, 64, 67));
        rolleListe.setForeground(new Color(230, 232, 222));
        add(rolleListe, BorderLayout.EAST);
	}
	
	public String vis(String t){
		tv.setText(t);
		return t;
	}
	
	public String getTvText(){
		return tv.getText();
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

	public String rex(Spiller s) {
		return leggtil(spillere.rex(s));
	}

	public void jørgen() {
		vis(spillere.jørgensListe());
	}
	
	public String drøm(Spiller s) {
		return spillere.drøm(s);
	}
	public void bedrag() {
		vis(spillere.mafiaNavn());
	}
	
	public void special(boolean lever) {
		vis((lever) ? "Special Guy er ikke drept." : "Special Guy ER drept! \n\nHvem vil Special Guy drepe?");
	}
	
	public void quisling(boolean lever, Spiller s) {
		if(!lever && s.funker() && s.drapsmann().id(Rolle.MAFIA)) {
			((Quisling)s.rolle()).konverter();
			vis("Quisling ER drept,\nog konverterer til Mafiaens side!");
		} else if(s.funker())
			vis("Quisling er ikke drept av Mafiaen.");
		else
			vis("Quisling er død.");
	}
	
	public void avstemning(){
		vis(spillere.visAvstemning());
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
	
	public void visRoller(String roller){
		rolleListe.setText(roller);
	}
	
	public void setFont(int størrelse){
		tv.setFont(new Font("Sans-Serif", Font.BOLD, størrelse));
		rolleListe.setFont(new Font("Sans-Serif", Font.BOLD, størrelse));
	}
}
