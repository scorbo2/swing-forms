package ca.corbett.forms.demo.panels;

import ca.corbett.forms.FormPanel;
import ca.corbett.forms.fields.FontStyleField;
import ca.corbett.forms.fields.LabelField;

import javax.swing.JPanel;
import java.awt.Font;

public class CustomFieldPanel extends PanelBuilder {
    @Override
    public String getTitle() {
        return "Custom fields";
    }

    @Override
    public JPanel build() {
        FormPanel formPanel = new FormPanel();

        LabelField headerLabel = new LabelField("Creating a custom FormField implementation");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setTopMargin(24);
        headerLabel.setBottomMargin(24);
        formPanel.addFormField(headerLabel);

        String text = "<html>Sometimes the built-in form field components just aren't<br/>"
                + "good enough, and you need to build something custom.<br/><br/>"
                + "Here is an example of a custom font style field<br/>"
                + "that allows the user to select various font properties<br/>"
                + "all in one single FormField:</html>";
        headerLabel = LabelField.createPlainHeaderLabel(text);
        formPanel.addFormField(headerLabel);

        FontStyleField fontField = new FontStyleField("Font:");
        fontField.setTopMargin(8);
        fontField.setBottomMargin(12);
        formPanel.addFormField(fontField);

        headerLabel = LabelField.createPlainHeaderLabel("Full source code for this component is included!");
        formPanel.addFormField(headerLabel);

        formPanel.render();
        return formPanel;
    }
}
