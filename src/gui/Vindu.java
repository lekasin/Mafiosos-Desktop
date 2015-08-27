package gui;

import datastruktur.ImgUtil;
import datastruktur.Spillerliste;
import personer.Rolle;
import personer.Spiller;
import personer.roller.Hammer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class Vindu extends JFrame {

    private static final long serialVersionUID = 1L;
    TV tv;
    JPanel rammeverk, header, innhold, display, kontroll, rammen;
    JLabel overskrift, klokke;
    JTextArea info, veiledning;
    JScrollPane innholdScroll;
    Knapp tilbake, fortsett;
    Border ramme = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    Spillerliste spillere;
    Spill spill;
    Oppstart oppstart;

    public static Dimension rammeSize = new Dimension(460, 800), kontrollSize = new Dimension(460, 60),
            toppSize = new Dimension(1100, 100), totalSize = new Dimension(1200, 600);

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
        setExtendedState(JFrame.MAXIMIZED_VERT);
        setMinimumSize(totalSize);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    public void setSpill(Spill s) {
        spill = s;
    }

    public void setOppstart(Oppstart o) {
        oppstart = o;
    }

    //Oppretter og legger inn elementer til vinduet
    public void fyllRamme() {

        //HEADER
        header = new JPanel(new BorderLayout());
        header.setPreferredSize(toppSize);
        header.setBorder(ramme);

        JButton innstillinger = new JButton(ImgUtil.getScaledIcon("settings.png", 80, 80));
        innstillinger.addActionListener(e -> {
            if (spill == null)
                new Innstillinger("Innstillinger", oppstart, spillere);
            else
                new Innstillinger("Innstillinger", spill, spillere);
        });

        overskrift = new JLabel();
        overskrift.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
        overskrift.setText("Velkommen til Oslo Mafiosos!");
        overskrift.setHorizontalAlignment(SwingConstants.CENTER);

        klokke = new JLabel();
        klokke.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
        klokke.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        header.add(innstillinger, BorderLayout.WEST);
        header.add(overskrift, BorderLayout.CENTER);
        header.add(klokke, BorderLayout.EAST);

        //DISPLAY (Venstre)
        display = new JPanel(new BorderLayout());
        display.setBorder(ramme);

        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5));


        veiledning = new JTextArea();
        veiledning.setColumns(40);
        veiledning.setBorder(border);
        veiledning.setEditable(false);
        veiledning.setLineWrap(true);
        veiledning.setWrapStyleWord(true);
        veiledning.setFont(new Font("Serif", Font.PLAIN, 15));

        veiledning.setText("Veiledning");
        display.add(veiledning, BorderLayout.NORTH);

        info = new JTextArea();
        info.setColumns(40);
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        //info.setFont(new Font("Serif", Font.BOLD, 15));
        JScrollPane scroller = new JScrollPane(info);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        display.add(scroller, BorderLayout.CENTER);

        //RAMME
        rammen = new JPanel(new BorderLayout());
        rammen.setPreferredSize(rammeSize);

//		//KONTROLL (Tilbake/Fortsett)
        kontroll = new JPanel();
        kontroll.setPreferredSize(kontrollSize);

        //Legger alt på plass i rammeverket
        rammeverk.add(display, BorderLayout.WEST);
        rammeverk.add(header, BorderLayout.NORTH);
        rammeverk.add(rammen, BorderLayout.CENTER);
        revalidate();
    }

    public JPanel innhold() {
        innhold = new JPanel(new WrapLayout());
        innhold.revalidate();
        innhold.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
        oppdaterRamme(innhold);
        return innhold;
    }

    public void oppdaterRamme(JPanel p) {
        innholdScroll = new JScrollPane(p, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        innholdScroll.setBorder(BorderFactory.createEmptyBorder());
        rammen.removeAll();
        rammen.add(innholdScroll, BorderLayout.CENTER);
        if (kontroll.isVisible())
            rammen.add(kontroll, BorderLayout.SOUTH);
        rammen.revalidate();
        rammen.repaint();
    }

    public void kontroll(ActionListener al, int fase) {
        //KONTROLL (Fortsett/Tilbake++-knapper)
        kontroll.removeAll();

        tilbake = new Knapp("Tilbake", Knapp.HEL, al);
        kontroll.add(tilbake);

        if (fase == Oppstart.VELGROLLER) {
            kontroll.add(new Knapp("Fjern", Knapp.HEL, e -> {
                oppstart.inverserKnapper(null);
            }));
        } else {
            fortsett = new Knapp("Fortsett", Knapp.HEL, al);
            kontroll.add(fortsett);
        }

        kontroll.revalidate();
        kontroll.repaint();
        kontroll.setVisible(true);
    }

    public void personknapper(JPanel panel, ActionListener al) {
        innhold();
        for (Spiller s : spillere.spillere()) {
            Knapp k = new Knapp(s.navn(), s, Knapp.HALV, al);
            if (s.equals(spillere.hentSistePek(Rolle.MARIUS))) k.setForeground(Color.BLUE);
            if (s.talt()) k.setForeground(Color.RED);
            panel.add(k);
        }
    }

    public void stemmeKnapper(JPanel panel, ActionListener al) {
        innhold();
        for (Spiller s : spillere.spillere()) {
            Knapp k = new Knapp(s.navn(), s, Knapp.HALV, al);
            if (s.equals(spillere.hentSistePek(Rolle.MARIUS))) k.setForeground(Color.BLUE);
            if (s.talt()) k.setForeground(Color.RED);
            if (spill.sjekkOffer(Rolle.RAVN) && s.equals(spill.finnOffer(Rolle.RAVN))) k.setEnabled(false);
            panel.add(k);
        }
    }

    public void deaktiverPersonKNapper(JPanel panel) {
        Component[] knapper = panel.getComponents();
        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
                k.setEnabled(false);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public void personligknapper(JPanel panel, ActionListener al, Spiller valgt, Boolean alene) {
        innhold();
        for (Spiller s : spillere.spillere()) {
            Knapp k;
            if (alene)
                k = new Knapp(s.navn(), s, Knapp.HALV, al, s == valgt);
            else
                k = new Knapp(s.navn(), s, Knapp.HALV, al);
            if (s == valgt)
                k.tal();
            panel.add(k);
        }
    }

    public void oppdaterKnapper(JPanel panel, ActionListener al, Rolle r) {
        Component[] knapper = panel.getComponents();

        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
                k.setForeground(Color.BLACK);
                if (k.spiller() != null) {
                    if (r == null || !r.funker() || k.spiller() == r.forbud() || k.spiller() == r.forbud2() || r.fanget() || k.spiller.fange() || !r.aktiv() ||
                            r.id(Rolle.JØRGEN) || r.id(Rolle.BEDRAGER) || r.id(Rolle.QUISLING) ||
                            r.id(Rolle.BESTEVENN) || r.id(Rolle.DRØMMER) || (r.id(Rolle.ASTRONAUT) && (!r.aktiv() || k.spiller != r.spiller())) ||
                            (r.id(Rolle.SPECIAL) && r.lever()) || (r.id(Rolle.SMITH) && k.spiller().id(Rolle.SMITH)) ||
                            !r.funker() || (r.id(Rolle.OBDUK) && (k.spiller().funker() || k.spiller().skjult())))
                        k.setEnabled(false);
                    else if ((k.spiller().rolle().funker() && k.spiller().funker()) || r.id(Rolle.OBDUK)) {
                        k.setEnabled(true);
                        if (r instanceof Hammer && ((Hammer) r).valgt() == k.spiller() || r.id(Rolle.HEISENBERG) && r.offer() == k.spiller())
                            k.setForeground(Color.GREEN);
                    }
                }
            } else if (c instanceof JPanel)
                panel.remove(c);
        }
        panel.revalidate();
        panel.repaint();
    }

    public void nullstillKnapper(JPanel panel, ActionListener al) {
        personknapper(panel, al);

        Component[] knapper = panel.getComponents();

        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public Knapp getTilbake() {
        return tilbake;
    }

    public Knapp getFortsett() {
        return fortsett;
    }

    public JLabel getKlokke() {
        return klokke;
    }

    public void visSkjulVeiledning() {
        veiledning.setVisible(!veiledning.isVisible());
    }

    public void setVeiledning(String tekst) {
        veiledning.setText(tekst);
        if (tekst.isEmpty())
            veiledning.setVisible(false);
    }

    public void nullstill() {
        rammen.removeAll();
        header = new JPanel();
        display = new JPanel();
        innhold = new JPanel();
        overskrift = new JLabel();
    }

    public void restart() {
        //Panel som inneholder alle de andre panelene og fordeler dem i vinduet
        remove(rammeverk);
        rammeverk = new JPanel(new BorderLayout());
        add(rammeverk);

        nullstill();
        fyllRamme(); //Fyller rammeverket
        revalidate();
        repaint();
    }

    public void startopp() {
        Oppstart o = new Oppstart(this);
        o.setAntall(spillere.length());
    }

    public void informer(String informasjon) {
        info.setText(informasjon);
    }
}