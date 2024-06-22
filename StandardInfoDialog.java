import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StandardInfoDialog extends JDialog {

  private User user;

  // Constructor to initialize the dialog with user and exercise information
  public StandardInfoDialog(Frame parent, double squat1RM, double deadlift1RM, double bench1RM, User user) {
    super(parent, "Strength Standards", true); // Calls the parent constructor to set the title and modality
    this.user = user; // Sets the user object

    setLayout(new BorderLayout());
    setSize(800, 880); // Sets the size of the dialog
    setLocationRelativeTo(null); // Centers the dialog on the screen

    JTabbedPane tabbedPane = new JTabbedPane();

    // Adds tabs for each exercise
    tabbedPane.addTab("Bench", createExercisePanel("bench", bench1RM));
    tabbedPane.addTab("Deadlift", createExercisePanel("deadlift", deadlift1RM));
    tabbedPane.addTab("Squat", createExercisePanel("squat", squat1RM));

    add(tabbedPane, BorderLayout.CENTER);

    // Creates and adds a close button
    JButton closeButton = new JButton("Close");
    closeButton.setPreferredSize(new Dimension(160, 40));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(new Color(10, 10, 100));
    closeButton.setFont(new Font("Arial", Font.BOLD, 14));
    closeButton.addActionListener(e -> dispose()); // Closes the dialog on click
    add(closeButton, BorderLayout.SOUTH);
  }

  // Creates a panel for each exercise
  private JPanel createExercisePanel(String exercise, double max1RM) {
    JPanel panel = new JPanel(new GridLayout(2, 1));

    // Adds gender-specific panels for each exercise
    panel.add(createGenderPanel(exercise, max1RM, User.Gender.MALE));
    panel.add(createGenderPanel(exercise, max1RM, User.Gender.FEMALE));

    return panel;
  }

  // Creates a panel for a specific gender and exercise
  private JPanel createGenderPanel(String exercise, double max1RM, User.Gender gender) {
    JPanel genderPanel = new JPanel(new BorderLayout());
    JLabel genderLabel = new JLabel(gender == User.Gender.MALE ? "Male" : "Female", JLabel.CENTER);
    genderLabel.setFont(new Font("Arial", Font.BOLD, 18));
    genderPanel.add(genderLabel, BorderLayout.NORTH);

    JTable table = createTable(exercise, gender, max1RM); // Creates the table with strength standards
    genderPanel.add(new JScrollPane(table), BorderLayout.CENTER);

    // Highlights the user's corresponding cell in the table
    if (user.getGender() == gender) {
      highlightTableCell(table, exercise, max1RM);
    }

    return genderPanel;
  }

  // Creates a table with the strength standards data
  private JTable createTable(String exercise, User.Gender gender, double max1RM) {
    String[] columnNames = { "Bodyweight", "Beginner", "Novice", "Intermediate", "Advanced", "Elite" };
    Object[][] data = getData(exercise, gender);

    JTable table = new JTable(data, columnNames);

    // Center aligns all columns in the table
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    return table;
  }

  // Highlights the specific cell in the table based on the user's performance
  private void highlightTableCell(JTable table, String exercise, double max1RM) {
    Object[][] data = getData(exercise, user.getGender());
    int closestRow = findClosestRow(data, user.getWeight()); // Finds the closest row based on user's weight
    int levelColumn = findLevelColumn(data, closestRow, max1RM); // Finds the appropriate column based on 1RM

    // Custom renderer to highlight the cell
    DefaultTableCellRenderer highlightRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
          int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(JLabel.CENTER);
        if (row == closestRow && column == levelColumn) {
          c.setBackground(new Color(200, 240, 255)); // Sets background color to highlight the cell
          c.setFont(new Font("Arial", Font.BOLD, 13)); // Sets font to bold
        } else {
          c.setBackground(Color.WHITE);
          c.setForeground(Color.BLACK);
        }
        return c;
      }
    };

    table.getColumnModel().getColumn(levelColumn).setCellRenderer(highlightRenderer); // Applies the custom renderer
  }

  // Retrieves data for the specified exercise and gender
  private Object[][] getData(String exercise, User.Gender gender) {
    StrengthStandards[] standards = getStrengthStandards(exercise, gender);

    Object[][] data = new Object[standards.length][6];

    for (int i = 0; i < standards.length; i++) {
      // Convert int to double
      data[i][0] = (double) standards[i].getBodyweight();
      data[i][1] = (double) standards[i].getBeginner();
      data[i][2] = (double) standards[i].getNovice();
      data[i][3] = (double) standards[i].getIntermediate();
      data[i][4] = (double) standards[i].getAdvanced();
      data[i][5] = (double) standards[i].getElite();
    }

    return data;
  }

  // Retrieves strength standards based on exercise and gender
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

  // Finds the row in the data that is closest to the user's weight
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

  // Finds the column corresponding to the user's strength level
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
