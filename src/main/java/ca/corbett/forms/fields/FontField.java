package ca.corbett.forms.fields;

import ca.corbett.forms.FontDialog;
import ca.corbett.forms.FormPanel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Intended as a proper replacement for FontStyleField, which I'm not happy with.
 * This form field presents options for choosing a font from any of the installed
 * fonts on the system, along with font style and size properties.
 * Makes use of the new FontDialog for this purpose.
 *
 * @author scorbo2
 * @since 2025-04-07
 */
public final class FontField extends FormField {

    private final JLabel sampleLabel;
    private final JButton button;
    private ActionListener actionListener;
    private Font selectedFont;

    /**
     * Creates a FontField with the given label text and default initial settings.
     *
     * @param labelText The text to show for the field label.
     */
    public FontField(String labelText) {
        this(labelText, FontDialog.INITIAL_FONT);
    }

    /**
     * Creates a FontField with the given label text and the given initial Font.
     *
     * @param labelText   The text to show for the field label.
     * @param initialFont The Font to set for the initial value.
     */
    public FontField(String labelText, Font initialFont) {
        selectedFont = (initialFont == null) ? FontDialog.INITIAL_FONT : initialFont;
        fieldLabel = new JLabel(labelText);
        button = new JButton("Change");
        sampleLabel = new JLabel();
        setSelectedFont(selectedFont);
        fieldComponent = button;
    }

    /**
     * Returns the Font selected by the user, or the default Font if the user
     * did not make a selection.
     *
     * @return The selected Font.
     */
    public Font getSelectedFont() {
        return selectedFont;
    }

    /**
     * Sets the selected Font. Overwrites whatever the user has chosen before now.
     *
     * @param font The Font to select.
     */
    public void setSelectedFont(Font font) {
        selectedFont = font;
        sampleLabel.setFont(font.deriveFont(12f));
        sampleLabel.setText(trimFontName(font.getFamily()));
    }

    /**
     * Renders this field into the given container.
     *
     * @param container   The containing form panel.
     * @param constraints The GridBagConstraints to use.
     */
    @Override
    public void render(JPanel container, GridBagConstraints constraints) {
        constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, componentSpacing);
        constraints.gridy++;
        constraints.gridx = FormPanel.LABEL_COLUMN;
        fieldLabel.setFont(fieldLabelFont);
        container.add(fieldLabel, constraints);

        constraints.gridx = FormPanel.CONTROL_COLUMN;
        button.setPreferredSize(new Dimension(95, 23));
        button.setFont(selectedFont.deriveFont(12f));
        JPanel wrapper = new JPanel();
        wrapper.setBackground(container.getBackground());
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.add(sampleLabel);
        wrapper.add(new JLabel(" ")); // spacer
        wrapper.add(button);

        if (actionListener != null) {
            button.removeActionListener(actionListener);
        }
        actionListener = getActionListener(container);
        button.addActionListener(actionListener); // UTIL-147 avoid adding it twice

        constraints.fill = 0;
        constraints.insets = new Insets(topMargin, componentSpacing, bottomMargin, componentSpacing);
        container.add(wrapper, constraints);
    }

    private ActionListener getActionListener(final JPanel panel) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font font = FontDialog.showDialog(panel, selectedFont);
                if (!font.equals(selectedFont)) {
                    setSelectedFont(font);
                    fireValueChangedEvent();
                }
            }
        };
    }

    private String trimFontName(String fontName) {
        final int LIMIT = 18;
        return fontName.length() > LIMIT ? fontName.substring(0, LIMIT) + "..." : fontName;
    }
}
