package ca.corbett.forms;

import ca.corbett.forms.fields.ComboField;
import ca.corbett.forms.fields.LabelField;
import ca.corbett.forms.fields.NumberField;
import ca.corbett.forms.fields.PanelField;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a dialog that can be shown to the user to allow
 * selection of a font with style properties.
 *
 * @author scorbo2
 * @since 2025-04-07
 */
public final class FontDialog extends JDialog {

    public static final Font INITIAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    private DefaultListModel<String> fontListModel;
    private JList<String> fontList;
    private boolean wasOkayed;

    private ComboField typeField;
    private ComboField styleField;
    private NumberField sizeField;
    private LabelField sampleLabel;
    private Font selectedFont;
    private Font initialFont;

    public FontDialog(Frame owner) {
        super(owner, "Choose font");
        setLocationRelativeTo(owner);
        initComponents();
    }

    public FontDialog(Dialog owner) {
        super(owner, "Choose font");
        setLocationRelativeTo(owner);
        initComponents();
    }

    public FontDialog(Window owner) {
        super(owner, "Choose font");
        setLocationRelativeTo(owner);
        initComponents();
    }

    public FontDialog() {
        super((Frame) null, "Choose font");
        initComponents();
    }

    public boolean wasOkayed() {
        return wasOkayed;
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    @Override
    public void setVisible(boolean vis) {
        if (vis) {
            populateUIFromFont(initialFont);
            fontTypeChanged();
        }
        super.setVisible(vis);
    }

    /**
     * Creates and shows a FontDialog with the given initialFont selected
     * by default (if it exists). If the user okays the dialog, whatever
     * font was selected by the user is returned. If the user cancels the
     * dialog, the initialFont that was passed in is returned.
     *
     * @param owner       The owner frame so we can set our location.
     * @param initialFont The font to be initially selected.
     * @return The user's font selection if OK was pressed, or initialFont otherwise.
     */
    public static Font showDialog(Component owner, Font initialFont) {
        FontDialog dialog = null;
        if (owner instanceof Frame) {
            dialog = new FontDialog((Frame) owner);
        } else if (owner instanceof Window) {
            dialog = new FontDialog((Window) owner);
        } else {
            Frame frame = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, owner);
            if (frame != null) {
                dialog = new FontDialog(frame);
            }
        }
        if (dialog == null) {
            dialog = new FontDialog();
            dialog.setLocationRelativeTo(owner);
        }

        dialog.initialFont = initialFont;
        dialog.selectedFont = initialFont;
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return dialog.wasOkayed() ? dialog.getSelectedFont() : initialFont;
    }

    private void fontChanged() {
        String fontFamily = fontList.getSelectedValue() == null ? selectedFont.getFamily() : fontList.getSelectedValue();
        int size = (Integer) sizeField.getCurrentValue();
        int style;
        switch (styleField.getSelectedIndex()) {
            case 1:
                style = Font.BOLD;
                break;
            case 2:
                style = Font.ITALIC;
                break;
            case 3:
                style = Font.BOLD + Font.ITALIC;
                break;
            default:
                style = Font.PLAIN;
        }
        selectedFont = new Font(fontFamily, style, size);

        String labelText = fontFamily.length() > 11 ? fontFamily.substring(0, 11) + "..." : fontFamily;
        sampleLabel.setText(labelText);
        sampleLabel.setFont(new Font(fontFamily, style, 14));
    }

    private void fontTypeChanged() {
        fontListModel.clear();
        switch (typeField.getSelectedIndex()) {
            case 0:
                fontListModel.addAll(List.of(Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED, Font.DIALOG, Font.DIALOG_INPUT));

                // Not guaranteed that the selected font will exist in this list:
                if (!isBuiltInFont(selectedFont)) {
                    fontList.setSelectedIndex(0);
                    fontChanged();
                }

                // Now we can select it:
                fontList.setSelectedValue(selectedFont.getFamily(), true);
                break;
            case 1:
                fontListModel.addAll(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

                // Guaranteed that the selected font will exist in this list, so select it:
                fontList.setSelectedValue(selectedFont.getFamily(), true);
                break;
        }

    }

    private void populateUIFromFont(Font font) {
        selectedFont = font;
        typeField.setSelectedIndex(isBuiltInFont(selectedFont) ? 0 : 1);
        fontList.setSelectedValue(font.getFamily(), true);
        sizeField.setCurrentValue(font.getSize());
        switch (font.getStyle()) {
            case Font.PLAIN:
                styleField.setSelectedIndex(0);
                break;
            case Font.BOLD:
                styleField.setSelectedIndex(1);
                break;
            case Font.ITALIC:
                styleField.setSelectedIndex(2);
                break;
            default:
                styleField.setSelectedIndex(3);
        }
    }

    private void initComponents() {
        initialFont = INITIAL_FONT;
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(new Dimension(280, 380));
        setResizable(false);
        setLayout(new BorderLayout());
        add(buildFontChooserPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFontChooserPanel() {
        FormPanel formPanel = new FormPanel(FormPanel.Alignment.TOP_LEFT);

        AbstractAction changeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontChanged();
            }
        };

        List<String> options = new ArrayList<>();
        options.add("Java built-in fonts");
        options.add("System installed fonts");
        typeField = new ComboField("Type:", options, 0, false);
        typeField.setTopMargin(16);
        typeField.addValueChangedAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontTypeChanged();
            }
        });
        formPanel.addFormField(typeField);

        PanelField panelField = new PanelField();
        JPanel panel = panelField.getPanel();
        panel.setLayout(new BorderLayout());
        fontListModel = new DefaultListModel<>();
        fontList = new JList<>(fontListModel);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.addListSelectionListener(e -> fontChanged());
        JScrollPane scrollPane = new JScrollPane(fontList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        panel.add(scrollPane, BorderLayout.CENTER);
        formPanel.addFormField(panelField);

        options = new ArrayList<>();
        options.add("Plain");
        options.add("Bold");
        options.add("Italic");
        options.add("Bold+Italic");
        styleField = new ComboField("Style:", options, 0, false);
        styleField.addValueChangedAction(changeAction);
        formPanel.addFormField(styleField);

        sizeField = new NumberField("Size:", 12, 6, 300, 2);
        sizeField.addValueChangedAction(changeAction);
        formPanel.addFormField(sizeField);

        sampleLabel = new LabelField("Sample:", "Sample text");
        sampleLabel.setFont(INITIAL_FONT.deriveFont(16f));
        formPanel.addFormField(sampleLabel);

        formPanel.render();
        return formPanel;
    }

    private void closeDialog(boolean okayed) {
        wasOkayed = okayed;
        setVisible(false);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(buildButton("OK", e -> closeDialog(true)));
        panel.add(buildButton("Cancel", e -> closeDialog(false)));
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        return panel;
    }

    private JButton buildButton(String label, ActionListener action) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(90, 23));
        button.addActionListener(action);
        return button;
    }

    private boolean isBuiltInFont(Font font) {
        return (font.getFamily().equals(Font.DIALOG) ||
                font.getFamily().equals(Font.SANS_SERIF) ||
                font.getFamily().equals(Font.SERIF) ||
                font.getFamily().equals(Font.DIALOG_INPUT) ||
                font.getFamily().equals(Font.MONOSPACED));
    }
}
