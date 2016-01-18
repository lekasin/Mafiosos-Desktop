package gui;

import Utils.ImgUtil;
import Utils.SkjermUtil;
import Utils.TvUtil;
import Utils.VeiledningsUtil;
import Utils.MenyUtil;
import datastruktur.Spillerliste;
import personer.Rolle;
import personer.Spiller;
import personer.roller.Hammer;
import personer.roller.Joker;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class Vindu extends JFrame {

    private static final long serialVersionUID = 1L;
    JPanel innhold, kontroll;
    private JPanel rammeverk, header, display, rammen, listePanel;
    private JLabel klokke, listeTittel;
    private JScrollPane innholdScroll, loggScroller;
    private JTextArea info;
    private JList<Spiller> liste;
    private DefaultListModel<Spiller> spillerListeModel;
    private Knapp tilbake, fortsett;
    private Border ramme = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    Spillerliste spillere;
    Spill spill;
    Oppstart oppstart;

    public static Dimension rammeSize = new Dimension(600, 800), kontrollSize = new Dimension(600, 60),
            toppSize = new Dimension(1100, 100), totalSize = new Dimension(1200, 600);

    public Vindu(String tittel, Spillerliste spillere) {

        super(tittel);
        this.spillere = spillere;
        TvUtil.init(spillere);

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

        setJMenuBar(MenyUtil.lagMenyer());
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

        JLabel innstillinger = new JLabel(ImgUtil.getScaledIcon("help.png", 80, 80));
        innstillinger.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        innstillinger.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                innstillinger.setEnabled(false);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                innstillinger.setEnabled(true);
                TvUtil.toggleGuide();
            }
        });

        JLabel overskrift = new JLabel();
        overskrift.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
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


        JTextArea veiledning = new JTextArea();
        veiledning.setColumns(40);
        veiledning.setBorder(border);
        veiledning.setEditable(false);
        veiledning.setLineWrap(true);
        veiledning.setWrapStyleWord(true);
        veiledning.setFont(new Font("Serif", Font.PLAIN, 15));
        VeiledningsUtil.init(veiledning);

        veiledning.setText("Veiledning");
        display.add(veiledning, BorderLayout.NORTH);

        loggScroller = new JScrollPane();
        loggScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        display.add(loggScroller, BorderLayout.CENTER);

        Border paddingBorder = BorderFactory.createEmptyBorder(5,5,5,5);

        //Spillerliste
        spillerListeModel = new DefaultListModel<>();
        liste = new JList<>(spillerListeModel); //data has type Object[]
        liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        liste.setLayoutOrientation(JList.VERTICAL);
        liste.setCellRenderer(new CellRenderer());

        listeTittel = new JLabel("Spillere: 0");
        listeTittel.setBorder(paddingBorder);
        listeTittel.setFont(new Font("Arial", Font.BOLD, 15));

        listePanel = new JPanel(new BorderLayout());
        listePanel.setBackground(Color.white);
        listePanel.add(listeTittel, BorderLayout.NORTH);
        listePanel.add(liste);
        loggScroller.setViewportView(listePanel);

        //Logg
        info = new JTextArea();
        info.setColumns(40);
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        //info.setFont(new Font("Serif", Font.BOLD, 15));

        SkjermUtil.init(info, overskrift);

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
        if (listePanel.isVisible())
            innhold.addMouseListener(deselectListener);
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

    public List<Spiller> hentValgteSpillere(){
        return liste.getSelectedValuesList();
    }

    public void velgIngen(){
        liste.clearSelection();
    }

    public void setListetittel(String tekst){
        listeTittel.setText(tekst);
    }

    public void leggTilListe(Spiller spiller) {
        spillerListeModel.addElement(spiller);
    }

    public void fjernFraListe(Spiller spiller) {
        spillerListeModel.removeElement(spiller);
    }

    public void tømListe() {
        spillerListeModel.removeAllElements();
    }

    public void visLogg(boolean logg){
        if (logg) {
            listePanel.setVisible(false);
            loggScroller.setViewportView(info);
        } else {
            listePanel.setVisible(true);
            loggScroller.setViewportView(listePanel);
        }
    }

    public void kontroll(ActionListener al, int fase) {
        kontroll(al, fase, null);
    }

    public void kontroll(ActionListener al, int fase, Knapp knapp) {
        //KONTROLL (Fortsett/Tilbake++-knapper)
        kontroll.removeAll();

        tilbake = new Knapp("Tilbake", Knapp.HEL, al);
        kontroll.add(tilbake);

        if (knapp != null)
            kontroll.add(knapp);

        if (fase == Oppstart.VELGROLLER) {
            kontroll.add(new Knapp("Fjern", Knapp.HALV, e -> oppstart.inverserKnapper()));
        }

        if (fase == Oppstart.HVEMERHVA) {
            kontroll.add(new Knapp("Fordel roller automatisk", Knapp.HEL, e -> startRolleGjennomgang()));
        } else {
            fortsett = new Knapp("Fortsett", Knapp.HEL, al);
            kontroll.add(fortsett);
        }

        kontroll.revalidate();
        kontroll.repaint();
        kontroll.setVisible(true);
    }

    private void startRolleGjennomgang() {
        int svar = JOptionPane.showConfirmDialog(this,
                "Pass på at landsbyen sover, ellers avsløres første rolle",
                "Sover alle?", JOptionPane.DEFAULT_OPTION);

        if (svar == JOptionPane.OK_OPTION) {
            oppstart.autoFordelRoller();
            kontroll.remove(1);
            kontroll.add(fortsett);
        }
    }

    public Knapp finnKnappForRolle(Container panel, int rolle) {
        for (Component c : panel.getComponents()) {
            if (c instanceof Knapp)
                if (((Knapp) c).spiller().id(rolle))
                    return (Knapp) c;
        }
        return null;
    }

    public Knapp finnKnappForSpiller(Container panel, Spiller spiller) {
        for (Component c : panel.getComponents()) {
            if (c instanceof Knapp)
                if (((Knapp) c).spiller().equals(spiller))
                    return (Knapp) c;
        }
        return null;
    }

    public JPanel visAlleKnapper(JPanel panel, ActionListener al) {
        panel.removeAll();
        for (Spiller s : spillere.spillere()) {
            Knapp k = new Knapp(s.navn(), s, Knapp.HALV, al);
            panel.add(k);
        }
        return panel;
    }

    public JPanel personknapper(JPanel panel, ActionListener al) {
        innhold();
        for (Spiller s : spillere.spillere()) {
            Knapp k = new Knapp(s.navn(), s, Knapp.HALV, al);
            if (s.harFlyers()) k.setForeground(Color.BLUE);
            if (s.talt()) k.setForeground(Color.RED);
            panel.add(k);
        }
        return panel;
    }

    public void stemmeKnapper(JPanel panel, ActionListener al) {
        innhold();
        for (Spiller s : spillere.spillere()) {
            Knapp k = new Knapp(s.navn(), s, Knapp.HALV, al);
            if (s.spiser()) k.setEnabled(false);
            panel.add(k);
        }
    }

    public void deaktiverPersonKNapper(JPanel panel) {
        Component[] knapper = panel.getComponents();
        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
                k.setEnabled(false);
            } else if (c instanceof JPanel)
                panel.remove(c);
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

    public void jokerKnapper(Joker joker) {
        innhold();
        ActionListener jokerListener = e -> {
            joker.setOpp(((Knapp) e.getSource()).getText().equals("Opp"));
            spill.nesteRolle();
        };
        innhold.add(new Knapp("Opp", Knapp.HEL, jokerListener));
        innhold.add(new Knapp("Ned", Knapp.HEL, jokerListener));
        innhold.revalidate();
        innhold.repaint();
    }

    public void oppdaterKnapper(JPanel panel, Rolle r) {
        Component[] knapper = panel.getComponents();

        if (r == null || !r.funker() || r.fanget() || !r.aktiv() || (r.id(Rolle.SPECIAL) && r.lever()) ||
                r.id(Rolle.JØRGEN) || r.id(Rolle.BEDRAGER) || r.id(Rolle.QUISLING) || r.id(Rolle.BESTEMOR) || r.id(Rolle.BESTEVENN) || r.id(Rolle.DRØMMER))
            deaktiverPersonKNapper(panel);
        else if (r.id(Rolle.OBDUK)) {
            visDødeKnapper(panel);
            return;
        } else if (r.id(Rolle.JOKER)) {
            jokerKnapper((Joker) r);
            return;
        } else
            for (Component c : knapper) {
                if (c instanceof Knapp) {
                    Knapp k = (Knapp) c;
                    k.setForeground(Color.BLACK);
                    if (k.spiller() != null && k.spiller() == r.forbud() || k.spiller() == r.forbud2() || k.spiller.fange() ||
                            (r.id(Rolle.SMITH) && k.spiller().id(Rolle.SMITH)) ||
                            (r.id(Rolle.ASTRONAUT) && k.spiller != r.spiller()))
                        k.setEnabled(false);
                    else if ((k.spiller().rolle().funker() && k.spiller().funker())) {
                        k.setEnabled(true);
                        if (r instanceof Hammer && ((Hammer) r).valgt() == k.spiller() || r.id(Rolle.HEISENBERG) && r.offer() == k.spiller())
                            k.setForeground(Color.GREEN);
                    }
                } else if (c instanceof JPanel)
                    panel.remove(c);
            }

        panel.revalidate();
        panel.repaint();
    }

    public void visDødeKnapper(JPanel panel) {
        Component[] knapper = panel.getComponents();

        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
                Spiller s = k.spiller();
                k.setForeground(Color.BLACK);

                if (s != null && !s.funker() || s.id(Rolle.ZOMBIE) && s.rolle().aktiv())
                    k.setEnabled(true);
                else
                    k.setEnabled(false);

            } else if (c instanceof JPanel)
                panel.remove(c);
        }
        panel.revalidate();
        panel.repaint();
    }

    public void visMafiaKnapper(JPanel panel){
        Component[] knapper = panel.getComponents();

        for (Component c : knapper) {
            if (c instanceof Knapp) {
                Knapp k = (Knapp) c;
                Spiller s = k.spiller();

                if (s != null && s.funker() && s.id(Rolle.MAFIA))
                    k.setEnabled(true);
                else
                    k.setEnabled(false);

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

    public void nullstill() {
        rammen.removeAll();
        header = new JPanel();
        display = new JPanel();
        innhold = new JPanel();
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

    MouseListener deselectListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            velgIngen();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };
}