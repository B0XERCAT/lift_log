public class Set {
  int weight;
  int repeats;

  Set(int weight, int repeats) {
    this.weight = weight;
    this.repeats = repeats;
  }

  public int getWeight() {
    return weight;
  }

  public int getRepeats() {
    return repeats;
  }

  public Integer calculate1RM() {
    // using Epley formula
    if (repeats == 1)
      return weight;
    return (int) (weight * (1 + repeats / 30.0));
  }

  @Override
  public String toString() {
    return "Weight: " + weight + " kg, Repeats: " + repeats;
  }
}