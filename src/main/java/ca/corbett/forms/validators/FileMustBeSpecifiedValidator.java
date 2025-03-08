package ca.corbett.forms.validators;

import ca.corbett.forms.fields.FormField;
import ca.corbett.forms.fields.FileField;
import javax.swing.JTextField;

/**
 * Similar to NonBlankFieldValidator for TextFields, this FieldValidator implementation
 * insists that a FileField cannot contain a blank value. Note that this validation is 
 * in addition to the SelectionType for the FileField in question (e.g. if you specify 
 * ExistingFile and also add this FieldValidator, then a file must be specified AND exist.
 * If you specify ExistingFile but don't add this FieldValidator, then the given file
 * must only exist if one is specified... blank value will return null in that case).
 * 
 * @author scorbo2
 * @since 2020-10-13
 */
public class FileMustBeSpecifiedValidator extends FieldValidator<FormField> {

  public FileMustBeSpecifiedValidator(FileField field) {
    super(field);
  }
  
  @Override
  public ValidationResult validate() {
    JTextField textField = (JTextField)field.getFieldComponent();
    if (textField.getText().trim().isEmpty()) {
      return new ValidationResult(false, "Value cannot be blank.");
    }
    return new ValidationResult();
  }
}
