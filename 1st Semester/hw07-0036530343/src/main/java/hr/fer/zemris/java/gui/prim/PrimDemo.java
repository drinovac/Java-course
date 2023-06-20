package hr.fer.zemris.java.gui.prim;

import hr.fer.swing.p15.Prozor7b;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * This class represents Frame with two lists.
 */
public class PrimDemo extends JFrame{

    /**
     * Constructor.
     */
    public PrimDemo() {
        setLocation(20, 50);
        setSize(400, 300);
        setTitle("Prim List");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initGUI();
    }

    /**
     * This method inits GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        PrimListModel model = new PrimListModel();

        JList<Integer> list1 = new JList<>(model);
        JList<Integer> list2 = new JList<>(model);

        JButton next = new JButton("Sljedeći");

        next.addActionListener(e -> {
            model.next();
        });

        JPanel central = new JPanel(new GridLayout(1, 0));
        central.add(new JScrollPane(list1));
        central.add(new JScrollPane(list2));

        cp.add(central, BorderLayout.CENTER);
        cp.add(next, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new PrimDemo();
            frame.pack();
            frame.setVisible(true);
        });
    }
}
