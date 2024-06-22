import java.awt.Color;

public class StrengthLevel {
    // Enum for level color
    public enum LevelColor {
        BEGINNER("Beginner", new Color(165, 42, 42)),
        NOVICE("Novice", Color.GRAY),
        INTERMEDIATE("Intermediate", new Color(184, 134, 11)),
        ADVANCED("Advanced", Color.GREEN),
        ELITE("Elite", Color.BLUE);

        private final String label;
        private final Color color;

        LevelColor(String label, Color color) {
            this.label = label;
            this.color = color;
        }

        // getters for label and color
        public String getLabel() {
            return label;
        }

        public Color getColor() {
            return color;
        }
    }
}
