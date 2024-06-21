public class User {
  public enum Gender {
    MALE, FEMALE
  }

  private String name;
  private Gender gender;
  private double height;
  private double weight;

  public User(String name, Gender gender, double height, double weight) {
    this.name = name;
    this.gender = gender;
    this.height = height;
    this.weight = weight;
  }

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

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", gender=" + gender +
        ", height=" + height +
        ", weight=" + weight +
        '}';
  }
}
