package hr.fer.zemris.java.gui.calc.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This class represents button with two functions.
 */
public class TrigButton extends JButton {
    /**
     * Default button label.
     */
    private String firstButtonLabel;
    /**
     * Alternative button label.
     */
    private String alternativeButtonLabel;
    /**
     * Default button action.
     */
    private ActionListener listener;
    /**
     * Alternative button action.
     */
    private ActionListener inverseListener;

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public TrigButton(String text, String alternativeButtonLabel, ActionListener listener, ActionListener inverseListener) {
        super(text);
        this.firstButtonLabel = text;
        this.alternativeButtonLabel = alternativeButtonLabel;
        this.listener = listener;
        this.inverseListener = inverseListener;

        addActionListener(listener);
        setBackground(Color.lightGray);
    }

    /**
     * This method changes button label and function.
     */
    public void changeLabel() {
        if(getText().equals(firstButtonLabel)) {
            setText(alternativeButtonLabel);
            removeActionListener(listener);
            addActionListener(inverseListener);
        } else {
            setText(firstButtonLabel);
            removeActionListener(inverseListener);
            addActionListener(listener);
        }

    }

}
