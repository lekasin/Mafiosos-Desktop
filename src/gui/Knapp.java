package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import personer.Rolle;
import personer.Spiller;
import personer.roller.Zombie;

public class Knapp extends JButton{

	private static final long serialVersionUID = 1L;
	public static final Dimension KVART = new Dimension(128, 50);
	public static final Dimension HALV = new Dimension(160, 50);
	public static final Dimension HEL = new Dimension(243, 50);
	public static final Dimension SUPER = new Dimension(300, 300);

	Spiller spiller;
	Rolle rolle;
	
	boolean talt;
	
	public Knapp(String navn, Spiller spiller, Dimension dim, ActionListener al){
		super(navn);
		this.spiller = spiller;
		setPreferredSize(dim);
		addActionListener(al);
		if(!spiller.lever()) setEnabled(false);
		if(spiller.rolle() != null && spiller.id(Rolle.ZOMBIE))
			if(((Zombie)spiller.rolle()).sulten()) 
				setEnabled(true);
	}

	public Knapp(Boolean talt, String navn, Spiller spiller, Dimension dim, ActionListener al){
		super(navn);
		this.spiller = spiller;
		setPreferredSize(dim);
		addActionListener(al);
		if(!spiller.lever()) setEnabled(false);
		if(spiller.rolle() != null && spiller.id(Rolle.ZOMBIE))
			if(((Zombie)spiller.rolle()).sulten()) 
				setEnabled(true);
		if(talt) tal();
	}
	
	public Knapp(String navn, Spiller spiller, Dimension dim, ActionListener al, Boolean l){
		super(navn);
		this.spiller = spiller;
		setPreferredSize(dim);
		addActionListener(al);
		if(!l) setEnabled(false);
	}

	public Knapp(String navn, Rolle rolle, Dimension dim, ActionListener al){
		super(navn);
		this.rolle = rolle;
		setPreferredSize(dim);
		addActionListener(al);
	}

	public Knapp(String navn, Dimension dim, ActionListener al){
		super(navn);
		setPreferredSize(dim);
		addActionListener(al);
	}

	public Knapp(String navn, Spiller spiller, Dimension dim, ActionListener al, JPanel panel){
		super(navn);
		this.spiller = spiller;
		setPreferredSize(dim);
		addActionListener(al);
		if(!spiller.lever()) setEnabled(false);
		panel.add(this);
	}	
	
	public boolean tal() {
		boolean t = talt;
		talt = true;
		setForeground(Color.RED);
		return t;
	}
	
	public void ny() {
		talt = false;
		setForeground(Color.BLACK);
	}
	
	public String navn(){
		return getName();
	}

	public Spiller spiller(){
		return spiller;
	}

	public Rolle rolle(){
		return rolle;
	}
}
