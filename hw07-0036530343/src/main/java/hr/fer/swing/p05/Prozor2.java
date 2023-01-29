package hr.fer.swing.p05;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Prozor2 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Prozor2() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prozor1");
		setLocation(20, 20);
		setSize(500, 200);
		initGUI();
		
		pack();
		
	}
	
	private void initGUI() {
		getContentPane().setLayout(new MojLayout());

		JLabel labela = new JLabel("Ovo je jedan tekst koji želimo prikazati u prozoru.");
		labela.setOpaque(true);
		labela.setBackground(Color.YELLOW);
		getContentPane().add(labela, "dontfill");
	}

	static class Elipticna extends JComponent {
		
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			Insets ins = getInsets();
			Dimension dim = getSize();
			Rectangle r = new Rectangle(
					ins.left, 
					ins.top, 
					dim.width-ins.left-ins.right,
					dim.height-ins.top-ins.bottom);
			if(isOpaque()) {
				g.setColor(getBackground());
				g.fillRect(r.x, r.y, r.width, r.height);
			}
			Color c = getForeground();
			if(c.equals(Color.BLACK)) {
				this.setForeground(Color.RED);
			} else {
				this.setForeground(Color.BLACK);
			}
			g.setColor(getForeground());
			g.drawOval(r.x, r.y, r.width-1, r.height-1);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Prozor2 prozor = new Prozor2();
				prozor.setVisible(true);
			}
		});
	}
}
