package ca.corbett.forms.fields;

import ca.corbett.forms.FormPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * A FormField that wraps a JSpinner to allow numeric input.
 *
 * @author scorbo2
 * @since 2020-09-25
 */
public final class NumberField extends FormField {

  private final JSpinner spinner;

  /**
   * Creates an integer-based NumberField using the given starting values.
   *
   * @param labelText The label to show for the field.
   * @param initialValue The starting value.
   * @param minimum The minimum value.
   * @param maximum The maximum value.
   * @param step The increment value.
   */
  public NumberField(String labelText, int initialValue, int minimum, int maximum, int step) {
    this(labelText, new SpinnerNumberModel(initialValue, minimum, maximum, step));
  }

  /**
   * Creates a floating point NumberField using the given starting values.
   *
   * @param labelText The label to show for the field.
   * @param initialValue The starting value.
   * @param minimum The minimum value.
   * @param maximum The maximum value.
   * @param step The increment value.
   */
  public NumberField(String labelText, double initialValue, double minimum, double maximum, double step) {
    this(labelText, new SpinnerNumberModel(initialValue, minimum, maximum, step));
  }

  /**
   * Creates a NumberField using the given SpinnerModel. Normally you can use the other
   * constructors in this class as a convenience to avoid having to create the SpinnerModel
   * yourself, but this constructor is useful if you want to be able to change the
   * parameters of the spinner dynamically, for example to change the increment or to
   * set a new min or max value.
   *
   * @param labelText The label to show for the field.
   * @param model A SpinnerModel instance containing our spinner parameters.
   */
  public NumberField(String labelText, SpinnerModel model) {
    spinner = new JSpinner(model);
    spinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        fireValueChangedEvent();
      }

    });
    fieldComponent = spinner;
    fieldComponent.setPreferredSize(new Dimension(60, 22)); // arbitrary default value
    fieldLabel = new JLabel(labelText);
  }

  public Number getCurrentValue() {
    return (Number)spinner.getValue();
  }

  public void setCurrentValue(Number value) {
    spinner.setValue(value);
  }

  @Override
  public void render(JPanel container, GridBagConstraints constraints) {
    constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, componentSpacing);
    constraints.gridy++;
    constraints.gridx = FormPanel.LABEL_COLUMN;
    fieldLabel.setFont(fieldLabelFont);
    container.add(fieldLabel, constraints);

    constraints.gridx = FormPanel.CONTROL_COLUMN;
    constraints.insets = new Insets(topMargin, componentSpacing, bottomMargin, componentSpacing);
    container.add(fieldComponent, constraints);
  }
}
