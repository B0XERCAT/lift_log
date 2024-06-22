public class Set {
  int weight;
  int repeats;

  // constructor to initialize Set object with weight and repeats
  Set(int weight, int repeats) {
    this.weight = weight;
    this.repeats = repeats;
  }

  // getters for weight and repeats
  public int getWeight() {
    return weight;
  }

  public int getRepeats() {
    return repeats;
  }

  // setters for weight and repeats
  public void setWeight(int weight) {
    this.weight = weight;
  }

  public void setRepeats(int repeats) {
    this.repeats = repeats;
  }

  // calculate 1RM based on weight and repeats using Epley formula
  public Integer calculate1RM() {
    if (repeats == 1)
      return weight;
    return (int) (weight * (1 + repeats / 30.0));
  }
}