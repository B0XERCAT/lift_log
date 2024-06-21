public class Set {
  int weight;
  int repeats;

  Set(int weight, int repeats) {
    this.weight = weight;
    this.repeats = repeats;
  }

  @Override
  public String toString() {
    return "Weight: " + weight + " kg, Repeats: " + repeats;
  }
}