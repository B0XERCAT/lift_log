import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StandardInfoDialog extends JDialog {

  private User user;

  public StandardInfoDialog(Frame parent, double squat1RM, double deadlift1RM, double bench1RM, User user) {
    super(parent, "Strength Standards", true);
    this.user = user;

    setLayout(new BorderLayout());
    setSize(800, 880);
    setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();

    tabbedPane.addTab("Bench", createExercisePanel("bench", bench1RM));
    tabbedPane.addTab("Deadlift", createExercisePanel("deadlift", deadlift1RM));
    tabbedPane.addTab("Squat", createExercisePanel("squat", squat1RM));

    add(tabbedPane, BorderLayout.CENTER);

    JButton closeButton = new JButton("Close");
    closeButton.setPreferredSize(new Dimension(160, 40));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(new Color(10, 10, 100));
    closeButton.setFont(new Font("Arial", Font.BOLD, 14));
    closeButton.addActionListener(e -> dispose());
    add(closeButton, BorderLayout.SOUTH);
  }

  private JPanel createExercisePanel(String exercise, double max1RM) {
    JPanel panel = new JPanel(new GridLayout(2, 1));

    panel.add(createGenderPanel(exercise, max1RM, User.Gender.MALE));
    panel.add(createGenderPanel(exercise, max1RM, User.Gender.FEMALE));

    return panel;
  }

  private JPanel createGenderPanel(String exercise, double max1RM, User.Gender gender) {
    JPanel genderPanel = new JPanel(new BorderLayout());
    JLabel genderLabel = new JLabel(gender == User.Gender.MALE ? "Male" : "Female", JLabel.CENTER);
    genderLabel.setFont(new Font("Arial", Font.BOLD, 18));
    genderPanel.add(genderLabel, BorderLayout.NORTH);

    JTable table = createTable(exercise, gender, max1RM);
    genderPanel.add(new JScrollPane(table), BorderLayout.CENTER);

    // Apply highlight only to the table corresponding to the user's gender
    if (user.getGender() == gender) {
      highlightTableCell(table, exercise, max1RM);
    }

    return genderPanel;
  }

  private JTable createTable(String exercise, User.Gender gender, double max1RM) {
    String[] columnNames = { "Bodyweight", "Beginner", "Novice", "Intermediate", "Advanced", "Elite" };
    Object[][] data = getData(exercise, gender);

    JTable table = new JTable(data, columnNames);

    // Apply center alignment to all columns
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    return table;
  }

  private void highlightTableCell(JTable table, String exercise, double max1RM) {
    Object[][] data = getData(exercise, user.getGender());
    int closestRow = findClosestRow(data, user.getWeight());
    int levelColumn = findLevelColumn(data, closestRow, max1RM);

    // Apply custom renderer to highlight the specific cell
    DefaultTableCellRenderer highlightRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(JLabel.CENTER);
        if (row == closestRow && column == levelColumn) {
          c.setBackground(new Color(200, 240, 255));
          c.setFont(new Font("Arial", Font.BOLD, 13));
        } else {
          c.setBackground(Color.WHITE);
          c.setForeground(Color.BLACK);
        }
        return c;
      }
    };

    table.getColumnModel().getColumn(levelColumn).setCellRenderer(highlightRenderer);
  }

  private Object[][] getData(String exercise, User.Gender gender) {
    StrengthStandards[] standards = getStrengthStandards(exercise, gender);

    Object[][] data = new Object[standards.length][6];

    for (int i = 0; i < standards.length; i++) {
      data[i][0] = (double) standards[i].getBodyweight(); // Convert int to double
      data[i][1] = (double) standards[i].getBeginner(); // Convert int to double
      data[i][2] = (double) standards[i].getNovice(); // Convert int to double
      data[i][3] = (double) standards[i].getIntermediate(); // Convert int to double
      data[i][4] = (double) standards[i].getAdvanced(); // Convert int to double
      data[i][5] = (double) standards[i].getElite(); // Convert int to double
    }

    return data;
  }

  private StrengthStandards[] getStrengthStandards(String exercise, User.Gender gender) {
    switch (gender) {
      case MALE:
        switch (exercise.toLowerCase()) {
          case "deadlift":
            return StrengthStandardsData.MALE_DEADLIFT_STANDARDS;
          case "bench":
            return StrengthStandardsData.MALE_BENCH_STANDARDS;
          case "squat":
            return StrengthStandardsData.MALE_SQUAT_STANDARDS;
        }
        break;
      case FEMALE:
        switch (exercise.toLowerCase()) {
          case "deadlift":
            return StrengthStandardsData.FEMALE_DEADLIFT_STANDARDS;
          case "bench":
            return StrengthStandardsData.FEMALE_BENCH_STANDARDS;
          case "squat":
            return StrengthStandardsData.FEMALE_SQUAT_STANDARDS;
        }
        break;
    }
    throw new IllegalArgumentException("Invalid gender or exercise type");
  }

  private int findClosestRow(Object[][] data, double weight) {
    int closestRow = -1;
    double minDiff = Double.MAX_VALUE;
    for (int i = 0; i < data.length; i++) {
      double bodyweight = (double) data[i][0];
      double diff = Math.abs(bodyweight - weight);
      if (diff < minDiff) {
        minDiff = diff;
        closestRow = i;
      }
    }
    return closestRow;
  }

  private int findLevelColumn(Object[][] data, int closestRow, double max1RM) {
    int levelColumn = -1;
    for (int i = 1; i < data[closestRow].length; i++) {
      double levelValue = (double) data[closestRow][i];
      if (max1RM >= levelValue) {
        levelColumn = i;
      } else {
        break;
      }
    }
    return levelColumn;
  }
}
