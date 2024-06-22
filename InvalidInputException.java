// Throw an exception when the input is invalid.
public class InvalidInputException extends Exception {
  public InvalidInputException(String message) {
    super(message);
  }
}