import javax.swing.JToggleButton;

public class InputValidator {
  public static void validateName(String name) throws InvalidInputException {
    // check if name is null
    if (name == null || name.trim().isEmpty()) {
      throw new InvalidInputException("Name field is empty.");
    }
    // check if name contains non-alphabet characters
    if (!name.matches("[a-zA-Z\\s]+")) {
      throw new InvalidInputException("Name field must not contain non-alphabet characters.");
    }
    if (name.length() > 15) {
      throw new InvalidInputException("Name field must not exceed 15 characters.");
    }
  }

  public static void validateGender(JToggleButton maleButton, JToggleButton femaleButton) throws InvalidInputException {
    if (!maleButton.isSelected() && !femaleButton.isSelected()) {
      throw new InvalidInputException("Please select a gender.");
    }
  }

  public static void validateWeight(String weight) throws InvalidInputException {
    try {
      double w = Double.parseDouble(weight);
      if (w <= 0) {
        throw new InvalidInputException("Weight must be a positive number");
      }
    } catch (NumberFormatException e) {
      throw new InvalidInputException("Invalid weight format");
    }
  }

  public static void validateHeight(String height) throws InvalidInputException {
    try {
      double h = Double.parseDouble(height);
      if (h <= 0) {
        throw new InvalidInputException("Height must be a positive number");
      }
    } catch (NumberFormatException e) {
      throw new InvalidInputException("Invalid height format");
    }
  }

  public static void validateRepeats(String repeats) throws InvalidInputException {
    try {
      double r = Double.parseDouble(repeats);
      if (r <= 0) {
        throw new InvalidInputException("Repeats must be a positive number");
      }
    } catch (NumberFormatException e) {
      throw new InvalidInputException("Invalid repeats format");
    }
  }
}