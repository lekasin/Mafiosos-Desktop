package gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import personer.Rolle;
import personer.Spiller;
import datastruktur.Spillerliste;

public class Innstillinger extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	Oppstart oppstart;
	Spill spill;
	Spillerliste spillere;
	TV tv;
	JMenuBar menuBar;
	JPanel innhold;

	public Innstillinger(String tittel, Oppstart o, Spillerliste sl) {
		super(tittel);
		oppstart = o;
		spillere = sl;
		tv = oppstart.tv;

		start();
	}

	public Innstillinger(String tittel, Spill s, Spillerliste sl) {
		super(tittel);
		spill = s;
		spillere = sl;
		tv = spill.tv;

		start();
	}

	private void start() {
		setVisible(true);
		setMinimumSize(new Dimension(400, 400));
		setLocationRelativeTo(null);

		innhold = new JPanel();
		add(innhold);

		knapper();
	}

	public void setSpill(Spill s) {
		spill = s;
	}

	public void knapper() {
		Knapp dagtid = new Knapp("Sett dagtid", Knapp.HEL, this);
		innhold.add(dagtid);
		Knapp font = new Knapp("Sett skriftstørrelse", Knapp.HEL, this);
		innhold.add(font);
		Knapp forteller = new Knapp("Fortellerinfo", Knapp.HEL, this);
		innhold.add(forteller);
		if (oppstart != null && oppstart.fase == Oppstart.VELGROLLER) {
			Knapp leggTilSpiller = new Knapp("Legg til spiller", Knapp.HEL,
					this);
			innhold.add(leggTilSpiller);
		}
		if (spill != null) {
			if (spill.dag) {
				if (spill.vindu.kontroll.isVisible()) {
					Knapp røm = new Knapp("Røm!", Knapp.HEL, this);
					innhold.add(røm);
				}
				if (spill.aktivTimer()) {
					Knapp pause = new Knapp("Start/stopp klokka", Knapp.HEL,
							this);
					innhold.add(pause);
				}
			}
		}

	}

	// Oppretter menyer
	public void lagMeny() {
		menuBar = new JMenuBar();

		JMenu meny = new JMenu("TestMeny");
		meny.setMnemonic(KeyEvent.VK_A);
		meny.getAccessibleContext().setAccessibleDescription(
				"Denne menyen utfører stuff");
		menuBar.add(meny);

		JMenuItem item = new JMenuItem("Trykk her");
		meny.add(item);

		JMenu undermeny = new JMenu("UnderMeny");
		undermeny.setMnemonic(KeyEvent.VK_T);
		undermeny.getAccessibleContext().setAccessibleDescription(
				"Denne undermenyen utfører stuff");
		meny.add(undermeny);

		item = new JMenuItem("Trykk her også");
		undermeny.add(item);
		item = new JMenuItem("Og her!");
		undermeny.add(item);

		this.add(menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

		if (((Knapp) e.getSource()).getText() == "Sett dagtid") {
			String input = JOptionPane
					.showInputDialog("Hvor lang skal dagen være? (Minutter)");
			if (input.matches("\\d{1,2}")) {
				int min = Integer.parseInt(input);
				if (spill == null)
					oppstart.setTid(min);
				else
					spill.setTid(min);
			}
		}

		if (((Knapp) e.getSource()).getText() == "Sett skriftstørrelse") {
			String input = JOptionPane
					.showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
			if (input.matches("\\d{1,2}")) {
				int størrelse = Integer.parseInt(input);
				tv.setFont(størrelse);
			}
		}

		if (((Knapp) e.getSource()).getText() == "Fortellerinfo") {
			String input = JOptionPane
					.showInputDialog("Fortellers telefonnummer:");
			if (input.matches("\\d{8}"))
				visFortellerInfo(Integer.parseInt(input));
			else
				JOptionPane.showMessageDialog(this,
						"Telefonnummer må inneholde 8 siffer!");
		}

		if (((Knapp) e.getSource()).getText() == "Legg til spiller") {
			String input = JOptionPane.showInputDialog("Navn på spiller:");
			if (input.length() == 0)
				return;
			if (spillere.finnSpiller(input) != null)
				JOptionPane.showMessageDialog(this, "Finnes allerede!");
			else
				leggTilSpiller(input);
		}

		if (((Knapp) e.getSource()).getText() == "Røm!") {
			spill.rømning();
		}

		if (((Knapp) e.getSource()).getText() == "Start/stopp klokka") {
			spill.timer.playPause();
		}
	}

	// KNAPPEFUNKSJONER
	public void leggTilSpiller(String navn) {
		if (spillere.finnSpiller(navn) == null) {
			spillere.leggTil(new Spiller(navn));
			oppstart.antallspillere++;
			oppstart.informer(spillere.rolleString(oppstart.roller,
					oppstart.antallspillere));
		}
	}

	public void visFortellerInfo(int tlf) {
		JFrame fortellervindu = new JFrame("Forteller:");
		fortellervindu.setVisible(true);
		fortellervindu.setMinimumSize(new Dimension(300, 80));
		fortellervindu.setResizable(false);
		JPanel tlfInfo = new JPanel();
		JLabel tlfLab = new JLabel("Tlf: " + tlf);
		tlfLab.setAlignmentX(CENTER_ALIGNMENT);
		tlfLab.setAlignmentY(CENTER_ALIGNMENT);
		tlfLab.setFont(new Font("Sans", Font.BOLD, 30));
		tlfInfo.add(tlfLab);
		fortellervindu.add(tlfInfo);
	}
}
