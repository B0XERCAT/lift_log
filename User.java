public class User {
  // Enum to represent gender
  public enum Gender {
    MALE, FEMALE
  }

  // Private fields for user properties
  private String name;
  private Gender gender;
  private double height;
  private double weight;

  // Constructor to initialize User object with name, gender, height, and weight
  public User(String name, Gender gender, double height, double weight) {
    this.name = name;
    this.gender = gender;
    this.height = height;
    this.weight = weight;
  }

  // Getters for name, gender, height, weight
  public String getName() {
    return name;
  }

  public Gender getGender() {
    return gender;
  }

  public double getHeight() {
    return height;
  }

  public double getWeight() {
    return weight;
  }

  // Setters for name, gender, height, weight
  public void setName(String name) {
    this.name = name;
  }

  // Setter for gender
  public void setGender(User.Gender gender) {
    this.gender = gender;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }
}
