package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.net.Inet4Address;
import java.util.*;

/**
 * This class represents our implementation of ListModel
 */
public class PrimListModel implements ListModel<Integer> {
    /**
     * Elements list.
     */
    private List<Integer> list;
    /**
     * Elements data listeners.
     */
    private List<ListDataListener> promatraci = new ArrayList<>();

    /**
     * Constructor.
     */
    public PrimListModel() {
        list = new LinkedList<>();
        list.add(1);
    }

    /**
     * This method adds next prime number to list.
     */
    public void next() {
        int last = list.get(list.size()-1);
        while(true) {
            last++;
            if(isPrime(last)) {
                list.add(last);
                ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size(), list.size());
                for(ListDataListener l : promatraci) {
                    l.intervalAdded(event);
                }
                break;
            }
        }

    }

    /**
     * This method checks if number is prime.
     * @param num number that is checked
     * @return true if prime, false otherwise
     */
    private boolean isPrime(int num) {
        if(num<=1) {
            return false;
        }
        for(int i=2;i<=num/2;i++) {
            if((num%i)==0) return  false;
        }
        return true;
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize() {
        return list.size();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    @Override
    public Integer getElementAt(int index) {
        return list.get(index);
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        promatraci.add(l);
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        promatraci.remove(l);
    }
}
