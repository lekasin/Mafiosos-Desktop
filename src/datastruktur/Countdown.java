package datastruktur;

import gui.Spill;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

public class Countdown {
	static int interval;
	static Timer timer;
	JLabel klokke;
	Spill spill;
	Start start, pause;
	String tekst;
	int tid;

	public Countdown(JLabel l, Spill s) {
		klokke = l;
		spill = s;
		this.tid = tid*60;

		timer = new Timer();
		start = new Start();
	}
	
	public int getTid() {
		return tid;
	}
	
	public void setTid(int t) {
		tid = t;
	}
	
	public void setText(String t) {
		tekst = t;
	}
	
	public void stop() {
		pause();
		klokke.setVisible(false);
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
	}
	
	public void nyStart(int t) {
		spill.informer(format(tid) + "\n" + tekst);
		spill.vindu.pause();
		pause();
		tid = t*60;
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
				spill.informer(format(tid--) + "\n" + tekst);
			} else {
				if(!spill.taler()) {
					spill.henrett(null);
					spill.godkjenn();
				}
				else spill.talt();
				this.cancel();
			}
		}
	}
}