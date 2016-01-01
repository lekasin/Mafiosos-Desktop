package gui;

import Utils.MenyUtil;
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
    JTextArea tv, rolleListe, guide;
    JPanel bildepanel = new JPanel(new BorderLayout());
    JLabel bildeHolder = new JLabel();
    String vedlegg = "", roller = "";

    public static Dimension TVSIZE = new Dimension(900, 700);

    public TV(String tittel, Spillerliste sl) {
        super(tittel);

        setLayout(new BorderLayout());

        spillere = sl;
        //TV (Tekstfelt på TV-en)
        setVisible(true);
        setMinimumSize(TVSIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bildepanel.setBackground(Color.black);
        bildepanel.add(bildeHolder);

        tv = new JTextArea();
        tv.setEditable(false);
        tv.setLineWrap(true);
        tv.setWrapStyleWord(true);
        tv.setFont(new Font("Sans-Serif", Font.BOLD, 30));
        tv.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        tv.setBackground(new Color(50, 60, 63));
        tv.setBackground(Color.black);
        tv.setForeground(new Color(180, 10, 20));
        ((DefaultCaret) tv.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        add(tv);

        rolleListe = new JTextArea();
        rolleListe.setFont(new Font("Sans-Serif", Font.BOLD, 30));
        rolleListe.setEditable(false);
        rolleListe.setText(roller);
//        rolleListe.setBackground(new Color(54, 64, 67));
        rolleListe.setBackground(Color.black);
        rolleListe.setForeground(new Color(230, 232, 222));
        add(rolleListe, BorderLayout.EAST);


        guide = new JTextArea();
        guide.setFont(new Font("Sans-Serif", Font.BOLD, 25));
        guide.setEditable(false);
        guide.setLineWrap(true);
        guide.setWrapStyleWord(true);
//        guide.setBackground(new Color(54, 64, 67));
        guide.setBackground(Color.black);
        guide.setForeground(new Color(230, 232, 222));
        guide.setVisible(false);
        add(guide, BorderLayout.SOUTH);

        setJMenuBar(MenyUtil.lagMenyer());
    }

    public String vis(String t) {
        tv.setText(t);
        return t;
    }

    public String getTvText() {
        return tv.getText();
    }

    public void rens() {
        tv = new JTextArea();
    }

    public String leggtil(String t) {
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
        if (!lever && s.funker() && s.drapsmann().id(Rolle.MAFIA)) {
            ((Quisling) s.rolle()).konverter();
            vis("Quisling ER drept,\nog konverterer til Mafiaens side!");
        } else if (s.funker())
            vis("Quisling er ikke drept av Mafiaen.");
        else
            vis("Quisling er død.");
    }

    public void avstemning() {
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

    public void visRoller(String roller) {
        rolleListe.setText(roller);
    }

    public void setTvFont(int størrelse) {
        tv.setFont(new Font("Sans-Serif", Font.BOLD, størrelse));
    }

    public void setRollerFont(int størrelse) {
        rolleListe.setFont(new Font("Sans-Serif", Font.BOLD, størrelse));
    }

    public void setGuideFont(int størrelse) {
        guide.setFont(new Font("Sans-Serif", Font.BOLD, størrelse));
    }

    public void setTvFarge(boolean tekst) {
        visFargeVelger(tv, tekst);
    }

    public void setRollerFarge(boolean tekst) {
        visFargeVelger(rolleListe, tekst);
    }

    public void setGuideFarge(boolean tekst) {
        visFargeVelger(guide, tekst);
    }

    public void visFargeVelger(JTextArea display, boolean tekst) {
        JFrame fargevelger = new JFrame("Fargevelger");
        JColorChooser jcc = new JColorChooser(tv.getBackground());
        jcc.getSelectionModel().addChangeListener(e -> {
            Color newColor = jcc.getColor();
            if (tekst)
                display.setForeground(newColor);
            else
                display.setBackground(newColor);
        });
        fargevelger.setContentPane(jcc);
        fargevelger.setVisible(true);
        fargevelger.setResizable(false);
        fargevelger.pack();
    }

    public boolean guideErSynlig() {
        return guide.isVisible();
    }

    public void visRolleGuide(String guideString) {
        guide.setText(guideString);
        guide.setVisible(true);

    }

    public void toggleRolleGuide(String guideString) {
        if(guide.isVisible() && guide.getText().equals(guideString))
            lukkRolleGuide();
        else
            visRolleGuide(guideString);
    }

    public void lukkRolleGuide() {
        guide.setVisible(false);
    }

    public void visBilde(StretchIcon bilde){
        remove(tv);
        bildeHolder.setIcon(bilde);
        add(bildepanel);
        revalidate();
        repaint();
    }

    public void skjulBilde(){
        remove(bildepanel);
        add(tv);
        revalidate();
        repaint();
    }
}
