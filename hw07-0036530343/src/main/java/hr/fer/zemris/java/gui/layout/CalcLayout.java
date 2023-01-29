package hr.fer.zemris.java.gui.layout;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents specific layout for our implementation of calculator.
 */
public class CalcLayout implements LayoutManager2 {

    /**
     * Margin between layout elements.
     */
    int margin;

    /**
     * Default constructor. Margin set to 0.
     */
    public CalcLayout() {
        this.margin = 0;
    }

    /**
     * Components storage
     */
    Map<RCPosition, Component> components = new HashMap<>();

    /**
     * Constructor.
     * @param margin space between elements
     */
    public CalcLayout(int margin) {
        this.margin = margin;
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     *
     * @param comp        the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(constraints instanceof String) {
            RCPosition constraintsParsed = RCPosition.parse((String) constraints);

            if (components.get(constraintsParsed) != null) {
                throw new CalcLayoutException();
            }

            components.put(constraintsParsed, comp);

        } else if(!(constraints instanceof RCPosition)) {
            throw new IllegalArgumentException();
        } else if(constraints == null) {
            throw new NullPointerException();
        } else if (constraints instanceof RCPosition) {
            RCPosition constraintsParsed = (RCPosition) constraints;

            if (components.get(constraintsParsed) != null) {
                throw new CalcLayoutException();
            }
            components.put(constraintsParsed, comp);
        }


    }

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @param target the target container
     * @return the maximum size of the container
     * @see Component#getMaximumSize
     * @see LayoutManager
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return getLayoutSize(target, "maximum");
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the x-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the y-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     *
     * @param target the target container
     */
    @Override
    public void invalidateLayout(Container target) {

    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component {@code comp} to the layout,
     * associating it
     * with the string specified by {@code name}.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        components.entrySet().forEach(e ->  {
                if (e.getValue().equals(comp)) {
                    components.remove(e.getKey(), e.getValue());
                }
        });
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @return the preferred dimension for the container
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(parent, "preferred");
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @return the minimum dimension for the container
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {

        return getLayoutSize(parent, "minimum");
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {

        int width = (int) Math.floor((parent.getWidth() - 6 * margin )/ 7);
        int height = (int) Math.floor((parent.getHeight() - 4 * margin )/ 5);

        for (Map.Entry<RCPosition, Component> entry : components.entrySet()) {
            
            RCPosition position = entry.getKey();

            if (position.getColumn() == 1 && position.getRow() == 1) {

                entry.getValue().setBounds(0, 0, 5 * width + 4 * margin, height);
                continue;
            }

            entry.getValue().setBounds((position.getColumn() - 1) * (width + margin),
                    (position.getRow() - 1) * (height + margin), width, height);
        }

    }

    /**
     * This method calculates container dimension.
     * @param parent Container reference
     * @param type Type of Dimension
     * @return Dimension
     */
    private Dimension getLayoutSize(Container parent, String type) {
        int height = 0;
        int width = 0;

        for(Map.Entry<RCPosition, Component> entry: components.entrySet()) {
            int entryHeight = 0;
            int entryWidth = 0;

            if("preferred".equals(type)) {
                entryHeight = entry.getValue().getPreferredSize().height;
                entryWidth = entry.getValue().getPreferredSize().width;
            } else if("minimum".equals(type)) {
                entryHeight = entry.getValue().getMinimumSize().height;
                entryWidth = entry.getValue().getMinimumSize().width;
            } else if("maximum".equals(type)) {
                entryHeight = entry.getValue().getMaximumSize().height;
                entryWidth = entry.getValue().getMaximumSize().width;
            }

            if (entry.getKey().getColumn() == 1 && entry.getKey().getRow() == 1) {
                entryWidth = entryWidth - 4 * margin;
                entryWidth /= 5;
            }

            if(entryHeight > height) {
                height = entryHeight;
            }

            if(entryWidth > width) {
                width = entryWidth;
            }

        }
        Insets insets = parent.getInsets();

        return new Dimension(width * 7 + margin * 6 + insets.left + insets.right, height * 5 + margin * 4 + insets.top + insets.bottom);
    }
}
