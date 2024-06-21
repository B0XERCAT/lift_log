public class Set {
  int weight;
  int repeats;

  Set(int weight, int repeats) {
    this.weight = weight;
    this.repeats = repeats;
  }

  public Integer calculate1RM() {
    // using Epley formula
    return (int) (weight * (1 + repeats / 30.0));
  }

  @Override
  public String toString() {
    return "Weight: " + weight + " kg, Repeats: " + repeats;
  }
}