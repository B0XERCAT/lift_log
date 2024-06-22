public class StrengthStandards {
  private int bodyweight;
  private int beginner;
  private int novice;
  private int intermediate;
  private int advanced;
  private int elite;

  public StrengthStandards(int bodyweight, int beginner, int novice, int intermediate, int advanced, int elite) {
    this.bodyweight = bodyweight;
    this.beginner = beginner;
    this.novice = novice;
    this.intermediate = intermediate;
    this.advanced = advanced;
    this.elite = elite;
  }

  // getters for bodyweight, beginner, novice, intermediate, advanced, and elite
  public int getBodyweight() {
    return bodyweight;
  }

  public int getBeginner() {
    return beginner;
  }

  public int getNovice() {
    return novice;
  }

  public int getIntermediate() {
    return intermediate;
  }

  public int getAdvanced() {
    return advanced;
  }

  public int getElite() {
    return elite;
  }
}
