package hr.fer.zemris.java.gui.calc.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class represents button with one function.
 */
public class CalcButton extends JButton {
    /**
     * Button action.
     */
    ActionListener listener;

    /**
     * Creates a button with text and action
     *
     * @param text the text of the button
     * @param listener onclick action
     */
    public CalcButton(String text, ActionListener listener) {
        super(text);
        this.listener = listener;
        addActionListener(listener);


        setBackground(Color.lightGray);
        try {
            Integer.parseInt(text);
            setFont(getFont().deriveFont(30f));
        } catch (NumberFormatException ignored) {

        }


    }
}
