package ca.corbett.forms.validators;

/**
 * A value class that can store the results of validation on a particular field.
 *
 * @author scorbo2
 * @since 2019-11-23
 */
public class ValidationResult {

    private boolean isValid;
    private String message;

    /**
     * Creates a ValidationResult representing a valid result (no message).
     */
    public ValidationResult() {
        isValid = true;
        message = "";
    }

    /**
     * Creates a ValidationResult with the given isValid value and message.
     *
     * @param isValid Whether the field in question is considered valid.
     * @param message The validation message (should be blank if isValid==true).
     */
    public ValidationResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    /**
     * Sets the validation result according to the supplied parameters.
     *
     * @param isValid Whether the field in question is considered valid.
     * @param message The validation message (should be blank if isValid==true).
     */
    public void setResult(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    /**
     * Returns whether the validation result is okay or not.
     *
     * @return True if the field in question is valid, false if not (see getMessage() also).
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Returns a validation message if the validation result is not valid.
     * Will be an empty string if the validation result is valid.
     *
     * @return A String containing a validation message (or empty).
     */
    public String getMessage() {
        return message;
    }
}
