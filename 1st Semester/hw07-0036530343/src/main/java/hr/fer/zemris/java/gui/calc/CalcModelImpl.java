package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * This class represents CalcModel implementation.
 */
public class CalcModelImpl implements CalcModel {

    /**
     * Listeners list.
     */
    private List<CalcValueListener> listeners = new ArrayList<>();
    /**
     * Calculator states.
     */
    private boolean editable = true;

    private boolean negative;
    /**
     * Calculator input as String.
     */
    private String input = "";
    /**
     * Calculator input as double.
     */
    private double inputNum = 0.0;
    /**
     * Calculator output.
     */
    private String frozenValue = null;
    /**
     * First operand.
     */
    private double activeOperand;
    /**
     * Flag for input of multiple zeros.
     */
    private boolean exc;
    /**
     * Flag if active operand is set.
     */
    private boolean activeOperandSet;
    /**
     * Current operation.
     */
    private DoubleBinaryOperator pendingOperation = null;

    /**
     * Prijava promatrača koje treba obavijestiti kada se
     * promijeni vrijednost pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(l);
    }

    /**
     * Odjava promatrača s popisa promatrača koje treba
     * obavijestiti kada se promijeni vrijednost
     * pohranjena u kalkulatoru.
     *
     * @param l promatrač; ne smije biti <code>null</code>
     * @throws NullPointerException ako je za <code>l</code> predana vrijednost <code>null</code>
     */
    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(l);
    }

    /**
     * Vraća trenutnu vrijednost koja je pohranjena u kalkulatoru.
     *
     * @return vrijednost pohranjena u kalkulatoru
     */
    @Override
    public double getValue() {
        return inputNum;
    }

    /**
     * Upisuje decimalnu vrijednost u kalkulator. Vrijednost smije
     * biti i beskonačno odnosno NaN. Po upisu kalkulator
     * postaje needitabilan.
     *
     * @param value vrijednost koju treba upisati
     */
    @Override
    public void setValue(double value) {
        this.inputNum = value;
        input = String.valueOf(value);
        editable = false;
        freezeValue(input);
        notifyAllListeners();
    }

    /**
     * Vraća informaciju je li kalkulator editabilan (drugim riječima,
     * smije li korisnik pozivati metode {@link #swapSign()},
     * {@link #insertDecimalPoint()} te {@link #insertDigit(int)}).
     *
     * @return <code>true</code> ako je model editabilan, <code>false</code> inače
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * Resetira trenutnu vrijednost na neunesenu i vraća kalkulator u
     * editabilno stanje.
     */
    @Override
    public void clear() {
        this.input = "";
        this.frozenValue = "";
        this.inputNum = 0.0;
        editable = true;
        notifyAllListeners();
    }

    /**
     * Obavlja sve što i {@link #clear()}, te dodatno uklanja aktivni
     * operand i zakazanu operaciju.
     */
    @Override
    public void clearAll() {
        clear();
        activeOperandSet = false;
        pendingOperation = null;
        notifyAllListeners();
    }

    /**
     * Mijenja predznak unesenog broja.
     *
     * @throws CalculatorInputException ako kalkulator nije editabilan
     */
    @Override
    public void swapSign() throws CalculatorInputException {
        if(!isEditable()) {
            throw new CalculatorInputException();
        }
        negative = !negative;

        inputNum *= -1;
        if(inputNum != 0) {
            input = String.valueOf(inputNum);
        }
        this.frozenValue = input;
        notifyAllListeners();
    }

    /**
     * Dodaje na kraj trenutnog broja decimalnu točku.
     *
     * @throws CalculatorInputException ako nije još unesena niti jedna znamenka broja,
     *                                  ako broj već sadrži decimalnu točku ili ako kalkulator nije editabilan
     */
    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if(!isEditable() || input.endsWith(".") || ("".equals(input) && !exc)) {
            throw new CalculatorInputException();
        }

        if(exc) {
            input += "0";
            exc = false;
        }

        input += ".";

        try {
            inputNum = Double.parseDouble(input);
        } catch (IllegalArgumentException exc) {
            throw new CalculatorInputException();
        }

        this.frozenValue = null;
        notifyAllListeners();
    }

    /**
     * U broj koji se trenutno upisuje na kraj dodaje poslanu znamenku.
     * Ako je trenutni broj "0", dodavanje još jedne nule se potiho
     * ignorira.
     *
     * @param digit znamenka koju treba dodati
     * @throws CalculatorInputException ako bi dodavanjem predane znamenke broj postao prevelik za konačan prikaz u tipu {@link Double}, ili ako kalkulator nije editabilan.
     * @throws IllegalArgumentException ako je <code>digit &lt; 0</code> ili <code>digit &gt; 9</code>
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if(!isEditable()) {
            throw new CalculatorInputException();
        }
        if(digit == 0) {
            exc = true;
        }
        try {
            inputNum = Double.parseDouble(input + digit);
            if(!Double.isFinite(inputNum)){
                throw new CalculatorInputException();
            }
            if(!("".equals(input) && digit == 0)){
                input += digit;
            } else {
                exc = true;
            }
        } catch (NumberFormatException exc) {
            throw new CalculatorInputException();
        }
        this.frozenValue = input;
        notifyAllListeners();
    }

    /**
     * Provjera je li upisan aktivni operand.
     *
     * @return <code>true</code> ako je aktivani operand upisan, <code>false</code> inače
     */
    @Override
    public boolean isActiveOperandSet() {
        return activeOperandSet;
    }

    /**
     * Dohvat aktivnog operanda.
     *
     * @return aktivni operand
     * @throws IllegalStateException ako aktivni operand nije postavljen
     */
    @Override
    public double getActiveOperand() throws IllegalStateException {
        if(!isActiveOperandSet()) {
            throw new IllegalStateException();
        }
        return this.activeOperand;
    }

    /**
     * Metoda postavlja aktivni operand na predanu vrijednost.
     * Ako kalkulator već ima postavljen aktivni operand, predana
     * vrijednost ga nadjačava.
     *
     * @param activeOperand vrijednost koju treba pohraniti kao aktivni operand
     */
    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        activeOperandSet = true;
    }

    /**
     * Uklanjanje zapisanog aktivnog operanda.
     */
    @Override
    public void clearActiveOperand() {
        activeOperandSet = false;
    }

    /**
     * Dohvat zakazane operacije.
     *
     * @return zakazanu operaciju, ili <code>null</code> ako nema zakazane operacije
     */
    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return this.pendingOperation;
    }

    /**
     * Postavljanje zakazane operacije. Ako zakazana operacija već
     * postoji, ovaj je poziv nadjačava predanom vrijednošću.
     *
     * @param op zakazana operacija koju treba postaviti; smije biti <code>null</code>
     */
    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    @Override
    public void freezeValue(String value) {
        this.frozenValue = value;
    }

    @Override
    public boolean hasFrozenValue() {
        return this.frozenValue != null && !this.frozenValue.equals("");
    }


    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     * @apiNote In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * The string output is not necessarily stable over time or across
     * JVM invocations.
     * @implSpec The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     */
    @Override
    public String toString() {
        if(!"".equals(input) && !hasFrozenValue()) {
            return input;
        } else if(this.inputNum == Double.POSITIVE_INFINITY) {
            return "Infinity";
        } else if(this.inputNum == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        } else if(hasFrozenValue()) {
            if(frozenValue.endsWith(".0")) {
                return frozenValue.substring(0,frozenValue.length()-2);
            } else {
                return frozenValue;
            }
        } else if(negative) {
            return "-0";
        } else {
            return "0";
        }
    }

    private void notifyAllListeners() {
        for(CalcValueListener listener: listeners) {
            listener.valueChanged(this);
        }
    }
}
