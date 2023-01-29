package hr.fer.swing.p05;

import java.awt.*;

import javax.swing.JComponent;

public class MojLayout implements LayoutManager {

	JComponent c;
	String ogranicenje;
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		System.out.println("AddLayoutComponent");
		if(!name.equals("fill") && !name.equals("dontfill")) throw new RuntimeException();
		ogranicenje=name;
		c = (JComponent)comp;
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		System.out.println("RemoveLayoutComponent");
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		System.out.println("PLS");
		if(c==null) return new Dimension(10,10);
		
		Dimension dim = c.getPreferredSize();
		if(ogranicenje.equals("fill")) {
			return dim;
		} else {
			return new Dimension(r(dim.width/0.8),r(dim.height/0.8));
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		System.out.println("MLS");
		return new Dimension(10,10);
	}

	@Override
	public void layoutContainer(Container parent) {
		System.out.println("LC");
		if(c==null) return;
		Dimension dim = parent.getSize();
		if(ogranicenje.equals("fill")) {
			c.setBounds(0,0,dim.width,dim.height);
		} else {
			c.setBounds(r(0.1*dim.width+0.5),r(0.1*dim.height),r(0.8*dim.width),r(0.8*dim.height));
			
		}
		
	}
	
	private static int r(double d) {
		return (int)(d+0.5);
	}

}
