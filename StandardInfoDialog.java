import javax.swing.*;
import java.awt.*;

public class StandardInfoDialog extends JDialog {

  public StandardInfoDialog(Frame parent) {
    super(parent, "Strength Standards", true);
    setLayout(new BorderLayout());
    setSize(800, 880);
    setLocationRelativeTo(parent);

    JTabbedPane tabbedPane = new JTabbedPane();

    tabbedPane.addTab("Bench", createExercisePanel("bench"));
    tabbedPane.addTab("Deadlift", createExercisePanel("deadlift"));
    tabbedPane.addTab("Squat", createExercisePanel("squat"));

    add(tabbedPane, BorderLayout.CENTER);

    JButton closeButton = new JButton("Close");
    closeButton.setPreferredSize(new Dimension(160, 40));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(new Color(10, 10, 100));
    closeButton.setFont(new Font("Arial", Font.BOLD, 14));
    closeButton.addActionListener(e -> dispose());
    add(closeButton, BorderLayout.SOUTH);
  }

  private JPanel createExercisePanel(String exercise) {
    JPanel panel = new JPanel(new GridLayout(2, 1));

    JPanel malePanel = new JPanel(new BorderLayout());
    JLabel maleLabel = new JLabel("Male", JLabel.CENTER);
    maleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    malePanel.add(maleLabel, BorderLayout.NORTH);
    malePanel.add(new JScrollPane(createTable(exercise, User.Gender.MALE)), BorderLayout.CENTER);

    JPanel femalePanel = new JPanel(new BorderLayout());
    JLabel femaleLabel = new JLabel("Female", JLabel.CENTER);
    femaleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    femalePanel.add(femaleLabel, BorderLayout.NORTH);
    femalePanel.add(new JScrollPane(createTable(exercise, User.Gender.FEMALE)), BorderLayout.CENTER);

    panel.add(malePanel);
    panel.add(femalePanel);

    return panel;
  }

  private JTable createTable(String exercise, User.Gender gender) {
    String[] columnNames = { "Bodyweight", "Beginner", "Novice", "Intermediate", "Advanced", "Elite" };
    Object[][] data = getData(exercise, gender);

    return new JTable(data, columnNames);
  }

  private Object[][] getData(String exercise, User.Gender gender) {
    StrengthStandards[] standards = null;

    switch (gender) {
      case MALE:
        switch (exercise.toLowerCase()) {
          case "deadlift":
            standards = StrengthStandardsData.MALE_DEADLIFT_STANDARDS;
            break;
          case "bench":
            standards = StrengthStandardsData.MALE_BENCH_STANDARDS;
            break;
          case "squat":
            standards = StrengthStandardsData.MALE_SQUAT_STANDARDS;
            break;
        }
        break;
      case FEMALE:
        switch (exercise.toLowerCase()) {
          case "deadlift":
            standards = StrengthStandardsData.FEMALE_DEADLIFT_STANDARDS;
            break;
          case "bench":
            standards = StrengthStandardsData.FEMALE_BENCH_STANDARDS;
            break;
          case "squat":
            standards = StrengthStandardsData.FEMALE_SQUAT_STANDARDS;
            break;
        }
        break;
    }

    if (standards == null) {
      throw new IllegalArgumentException("Invalid gender or exercise type");
    }

    Object[][] data = new Object[standards.length][6];

    for (int i = 0; i < standards.length; i++) {
      data[i][0] = standards[i].getBodyweight();
      data[i][1] = standards[i].getBeginner();
      data[i][2] = standards[i].getNovice();
      data[i][3] = standards[i].getIntermediate();
      data[i][4] = standards[i].getAdvanced();
      data[i][5] = standards[i].getElite();
    }

    return data;
  }
}
