package datastruktur;

import gui.Spill;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
	static int interval;
	static Timer timer;
	JLabel klokke;
	Spill spill;
	Start start, pause;
	String tekst;
	boolean aktiv;
	int tid;

	public Countdown(JLabel l, Spill s) {
		klokke = l;
        setKlokkeLytter();
		spill = s;
		this.tid = tid*60;

		timer = new Timer();
		start = new Start();
	}

    private void setKlokkeLytter(){
        klokke.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                playPause();
            }
        });
    }

	public boolean getAktiv(){
		return aktiv;
	}

	public int getTid() {
		return tid;
	}
	
	public void setTid(int t) {
		tid = t;
	}

	public void setText(String t) {
		tekst = t;
        spill.informer(format(tid) + tekst);
	}
	
	public void stop() {
		pause();
		klokke.setVisible(false);
		aktiv = false;
	}
	
	public void playPause(){
		if(sjekk())
			fortsett();
		else
			pause();
	}
	
	public boolean sjekk() {
		return pause == start;
	}
	
	public void pause() {
		start.cancel();
		pause = start;
		timer = new Timer();
	}
	
	public void fortsett() {
		start = new Start();
		timer.schedule(start, 0, 1000);
		klokke.setVisible(true);
	}
	
	public void nyStartSek(int t) {
		startNyTimer(t);
	}
	
	public void nyStartMin(int t) {
		startNyTimer(t*60);
	}
	
	public void startNyTimer(int t) {
		aktiv = true;
		spill.informer(format(tid) + tekst);
		pause();
		tid = t;
		fortsett();
		klokke.setVisible(true);
	}
	
	public String format(int i) {
		int min, sek;
		
		min = i/60;
		sek = i-min*60;
		
		return min + ":" + ((sek<10) ? "0"+sek : sek);
	}

	class Start extends TimerTask {
		public void run() {
			if (tid > 0) {
				klokke.setText(format(tid));
				spill.informer(format(tid--) + tekst);
			} else {
				spill.tidenErUte();
				this.cancel();
			}
		}
	}
}