package hr.fer.zemris.java.gui.calc.Calculator;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layout.CalcLayout;
import hr.fer.zemris.java.gui.layout.RCPosition;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents Calculator implementation.
 */
public class Calculator extends JFrame {

    /**
     * CalcModel implementation.
     */
    private CalcModel calcModel = new CalcModelImpl();

    /**
     * List of buttons with double functions.
     */
    private List<TrigButton> inverseOperations = new ArrayList<>();
    /**
     * Stack.
     */
    private Stack<Double> stack = new Stack<>();

    /**
     * Constructor.
     */
    public Calculator() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(550, 400);
        setLocation(20,20);
        setTitle("Calculator");
        initGUI();
    }

    /**
     * This method inits GUI.
     */
    private void initGUI() {
        Container cp = getContentPane();

        JPanel p = new JPanel(new CalcLayout(3));
        p.setBackground(Color.WHITE);

        JLabel ekran = new JLabel(calcModel.toString());
        ekran.setBackground(Color.YELLOW);
        ekran.setOpaque(true);
        ekran.setHorizontalAlignment(SwingConstants.RIGHT);
        ekran.setFont(ekran.getFont().deriveFont(30f));
        ekran.setBorder(new LineBorder(Color.BLACK));
        calcModel.addCalcValueListener(model -> {
            ekran.setText(model.toString());
        });

        p.add(ekran, "1,1");

        p.add(new CalcButton("0", e -> calcModel.insertDigit(0)), "5,3");
        p.add(new CalcButton("1", e -> calcModel.insertDigit(1)), "4,3");
        p.add(new CalcButton("2", e -> calcModel.insertDigit(2)), "4,4");
        p.add(new CalcButton("3", e -> calcModel.insertDigit(3)), "4,5");
        p.add(new CalcButton("4", e -> calcModel.insertDigit(4)), "3,3");
        p.add(new CalcButton("5", e -> calcModel.insertDigit(5)), "3,4");
        p.add(new CalcButton("6", e -> calcModel.insertDigit(6)), "3,5");
        p.add(new CalcButton("7", e -> calcModel.insertDigit(7)), "2,3");
        p.add(new CalcButton("8", e -> calcModel.insertDigit(8)), "2,4");
        p.add(new CalcButton("9", e -> calcModel.insertDigit(9)), "2,5");

        p.add(new CalcButton("+/-", e -> calcModel.swapSign()), "5,4");
        p.add(new CalcButton(".", e -> calcModel.insertDecimalPoint()), "5,5");
        p.add(new CalcButton("clr", e -> calcModel.clear()), "1,7");
        p.add(new CalcButton("res", e -> calcModel.clearAll()), "2,7");
        p.add(new CalcButton("push", e -> {
            stack.push(calcModel.getValue());
        }), "3,7");
        p.add(new CalcButton("pop", e -> {
            if(!stack.empty()) {
                calcModel.setValue(stack.pop());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Stog je prazan",
                        "Pogreška",
                        JOptionPane.WARNING_MESSAGE);
            }
        }), "4,7");

        p.add(new CalcButton("=", e -> {
            if (calcModel.isActiveOperandSet() && calcModel.getPendingBinaryOperation() != null) {
                calcModel.setValue(calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
                calcModel.setPendingBinaryOperation(null);
            }
        }), "1,6");
        p.add(new CalcButton("/", e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((left, right) -> left / right);
            calcModel.clear();
        }), "2,6");
        p.add(new CalcButton("*", e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((left, right) -> left * right);
            calcModel.clear();
        }), "3,6");
        p.add(new CalcButton("-", e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((left, right) -> left - right);
            calcModel.clear();
        }), "4,6");
        p.add(new CalcButton("+", e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((Double::sum));
            calcModel.clear();
        }), "5,6");

        JCheckBox invButton = new JCheckBox("Inv");
        invButton.addActionListener( e -> {
            for (TrigButton operation : inverseOperations) {
                operation.changeLabel();
            }
        });
        invButton.setBackground(Color.WHITE);
        p.add(invButton, "5,7");


        p.add(new CalcButton("1/x", e -> {
            calcModel.setValue(Math.pow(calcModel.getValue(), -1));
        }), "2,1");

        TrigButton log = new TrigButton("log", "10^", e -> {
            calcModel.setValue(Math.log10(calcModel.getValue()));
        }, e -> {
            calcModel.setValue(Math.pow(10, calcModel.getValue()));
        });
        inverseOperations.add(log);
        p.add(log, new RCPosition(3,1));

        TrigButton ln = new TrigButton("ln", "e^", e -> {
            calcModel.setValue(Math.log(calcModel.getValue()));
        }, e -> {
            calcModel.setValue(Math.pow(Math.E, calcModel.getValue()));
        });
        inverseOperations.add(ln);
        p.add(ln, new RCPosition(4,1));

        TrigButton pow = new TrigButton("x^n", "x^(1/n)", e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((left, right) -> Math.pow(left, right));
        }, e -> {
            operatorChecker();
            calcModel.setPendingBinaryOperation((left, right) -> Math.pow(left, 1 / right));
        });
        inverseOperations.add(pow);
        p.add(pow, new RCPosition(5,1));

        TrigButton sin = new TrigButton("sin", "arcsin", e -> {
            calcModel.setValue(Math.sin(calcModel.getValue()));
        }, e -> {
            calcModel.setValue(Math.asin(calcModel.getValue()));
        });
        inverseOperations.add(sin);
        p.add(sin, new RCPosition(2,2));

        TrigButton cos = new TrigButton("cos", "arccos", e -> {
            calcModel.setValue(Math.cos(calcModel.getValue()));
        }, e -> {
            calcModel.setValue(Math.acos(calcModel.getValue()));
        });
        inverseOperations.add(cos);
        p.add(cos, new RCPosition(3,2));

        TrigButton tg = new TrigButton("tg", "arctg", e -> {
            calcModel.setValue(Math.tan(calcModel.getValue()));
        }, e -> {
            calcModel.setValue(Math.atan(calcModel.getValue()));
        });
        inverseOperations.add(tg);
        p.add(tg, new RCPosition(4,2));

        TrigButton ctg = new TrigButton("ctg", "arcctg", e -> {
            calcModel.setValue(Math.pow(Math.tan(calcModel.getValue()), -1));
        }, e -> {
            calcModel.setValue(Math.PI / 2 - Math.atan(calcModel.getValue()));
        });
        inverseOperations.add(ctg);
        p.add(ctg, new RCPosition(5,2));

        cp.add(p);
        cp.setPreferredSize(p.getPreferredSize());
    }

    /**
     * This method checks if operation is scheduled.
     */
    private void operatorChecker() {
        if(calcModel.getPendingBinaryOperation() == null) {
            calcModel.setActiveOperand(calcModel.getValue());
            calcModel.setValue(calcModel.getValue());
        } else if (calcModel.isActiveOperandSet()) {
            calcModel.setValue(calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
            calcModel.setActiveOperand(calcModel.getValue());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            new Calculator().setVisible(true);
        });
    }
}
