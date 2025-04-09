package ca.corbett.forms.fields;

import ca.corbett.forms.FormPanel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * This started as an example of a custom FormField implementation for the documentation, but
 * it may actually be useful so I have decided to include it in the swing-forms library itself.
 *
 * @author scorbo2
 */
public class FontStyleField extends FormField {

    public static final String[] fontFamilies = {"Serif", "SansSerif", "MonoSpaced"};
    private JPanel wrapperPanel;
    private JComboBox fontChooser;
    private JToggleButton btnBold;
    private JToggleButton btnItalic;
    private JToggleButton btnUnderline;
    private JPanel btnColor;
    private JSpinner sizeSpinner;
    private ImageIcon iconB;
    private ImageIcon iconI;
    private ImageIcon iconU;

    private final ItemListener itemListener = e -> fireValueChangedEvent();

    private final MouseListener colorButtonMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!btnColor.isEnabled()) {
                return;
            }
            Color newColor = JColorChooser.showDialog(((JPanel) e.getSource()), "Choose color", btnColor.getBackground());
            if (newColor != null) {
                btnColor.setBackground(newColor);
                fireValueChangedEvent();
            }
        }
    };

    private final ChangeListener fontButtonChangeListener = e -> fireValueChangedEvent();

    public FontStyleField(String label) {
        this(label, "SansSerif", false, false, false);
    }

    public FontStyleField(String label, String initialValue) {
        this(label, initialValue, false, false, false);
    }

    public FontStyleField(String label, String initialValue, boolean initialBold, boolean initialItalic, boolean initialUnderline) {
        int selectedIndex = -1;
        for (int i = 0; i < fontFamilies.length; i++) {
            if (fontFamilies[i].equals(initialValue)) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex == -1) {
            selectedIndex = 0;
        }

        URL urlB = getClass().getResource("/ca/corbett/swing-forms/images/icon_b.png");
        iconB = (urlB == null) ? null : new ImageIcon(urlB);
        URL urlI = getClass().getResource("/ca/corbett/swing-forms/images/icon_i.png");
        iconI = (urlI == null) ? null : new ImageIcon(urlI);
        URL urlU = getClass().getResource("/ca/corbett/swing-forms/images/icon_u.png");
        iconU = (urlU == null) ? null : new ImageIcon(urlU);

        fieldLabel = new JLabel(label);
        wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        fontChooser = new JComboBox(fontFamilies);
        fontChooser.setEditable(false);
        fontChooser.setSelectedIndex(selectedIndex);
        fontChooser.addItemListener(itemListener);
        wrapperPanel.add(fontChooser);
        btnColor = new JPanel();
        btnColor.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        btnColor.setBackground(Color.BLACK);
        btnColor.setPreferredSize(new Dimension(24, 24));
        btnColor.addMouseListener(colorButtonMouseListener);
        wrapperPanel.add(btnColor);
        btnBold = (iconB == null) ? new JToggleButton("B") : new JToggleButton(iconB);
        btnBold.setPreferredSize(new Dimension(26, 26));
        btnBold.setFont(btnBold.getFont().deriveFont(Font.PLAIN, 10f));
        btnBold.addChangeListener(fontButtonChangeListener);
        btnBold.setSelected(initialBold);
        wrapperPanel.add(btnBold);
        btnItalic = (iconI == null) ? new JToggleButton("I") : new JToggleButton(iconI);
        btnItalic.setPreferredSize(new Dimension(26, 26));
        btnItalic.setFont(btnItalic.getFont().deriveFont(Font.PLAIN, 10f));
        btnItalic.addChangeListener(fontButtonChangeListener);
        btnItalic.setSelected(initialItalic);
        wrapperPanel.add(btnItalic);
        btnUnderline = (urlU == null) ? new JToggleButton("U") : new JToggleButton(iconU);
        btnUnderline.setPreferredSize(new Dimension(26, 26));
        btnUnderline.setFont(btnUnderline.getFont().deriveFont(Font.PLAIN, 10f));
        btnUnderline.addChangeListener(fontButtonChangeListener);
        btnUnderline.setSelected(initialUnderline);
        wrapperPanel.add(btnUnderline);
        sizeSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 48, 2));
        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                fireValueChangedEvent();
            }

        });
        wrapperPanel.add(sizeSpinner);
        fieldComponent = wrapperPanel;
    }

    public String getFontName() {
        return (String) fontChooser.getSelectedItem();
    }

    public void setFontName(String s) {
        fontChooser.setSelectedItem(s);
    }

    public Color getFontColor() {
        return btnColor.getBackground();
    }

    public void setFontColor(Color c) {
        btnColor.setBackground(c);
    }

    public boolean isBold() {
        return btnBold.isSelected();
    }

    public void setBold(boolean b) {
        btnBold.setSelected(b);
    }

    public boolean isItalic() {
        return btnItalic.isSelected();
    }

    public void setItalic(boolean i) {
        btnItalic.setSelected(i);
    }

    public boolean isUnderline() {
        return btnUnderline.isSelected();
    }

    public void setUnderline(boolean u) {
        btnUnderline.setSelected(u);
    }

    public int getFontSize() {
        return (Integer) sizeSpinner.getValue();
    }

    public void setFontSize(int s) {
        sizeSpinner.setValue(s);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fontChooser.setEnabled(enabled);
        btnBold.setEnabled(enabled);
        btnItalic.setEnabled(enabled);
        btnUnderline.setEnabled(enabled);
        btnColor.setEnabled(enabled);
        sizeSpinner.setEnabled(enabled);
    }

    @Override
    public void render(JPanel container, GridBagConstraints constraints) {
        constraints.gridy++;
        constraints.gridx = FormPanel.LABEL_COLUMN;
        constraints.insets = new Insets(topMargin, leftMargin, bottomMargin, componentSpacing);
        fieldLabel.setFont(fieldLabelFont);
        container.add(fieldLabel, constraints);

        constraints.gridx = FormPanel.CONTROL_COLUMN;
        constraints.insets = new Insets(topMargin, componentSpacing, bottomMargin, componentSpacing);
        container.add(wrapperPanel, constraints);
    }
}
