import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ResultDialog extends JDialog {
  private User user;

  public ResultDialog(Frame parent, List<Set> squatSets, List<Set> deadliftSets, List<Set> benchSets, User user) {
    super(parent, "Lift Log", true);
    this.user = user;

    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);

    JPanel resultPanel = new JPanel();
    resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
    resultPanel.setBorder(new EmptyBorder(50, 0, 30, 0));
    resultPanel.setBackground(Color.WHITE);

    JLabel titleLabel = new JLabel("1RM");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    resultPanel.add(titleLabel);

    JLabel subtitleLabel1 = new JLabel("1RM is the maximum amount of weight");
    subtitleLabel1.setFont(new Font("Arial", Font.PLAIN, 14));
    subtitleLabel1.setAlignmentX(CENTER_ALIGNMENT);

    JLabel subtitleLabel2 = new JLabel("that a person can lift for one repetition.");
    subtitleLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
    subtitleLabel2.setAlignmentX(CENTER_ALIGNMENT);

    resultPanel.add(subtitleLabel1);
    resultPanel.add(subtitleLabel2);

    JPanel oneRmPanel = new JPanel(new GridLayout(3, 2, 0, 0));
    oneRmPanel.setBackground(Color.WHITE);
    oneRmPanel.setBorder(new EmptyBorder(30, 100, 30, 60));
    add1RMInfo(oneRmPanel, "Bench", benchSets);
    add1RMInfo(oneRmPanel, "Deadlift", deadliftSets);
    add1RMInfo(oneRmPanel, "Squat", squatSets);
    resultPanel.add(oneRmPanel);

    JSeparator separator = new JSeparator();
    separator.setForeground(Color.GRAY);
    resultPanel.add(separator);

    JLabel strengthStandardLabel = new JLabel("Strength Standards");
    strengthStandardLabel.setFont(new Font("Arial", Font.BOLD, 22));
    strengthStandardLabel.setAlignmentX(CENTER_ALIGNMENT);

    JLabel userInfoLabel = new JLabel("(" + user.getWeight() + "kg " + user.getGender().toString().substring(0, 1)
        + user.getGender().toString().substring(1).toLowerCase() + ")");
    userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 22));
    userInfoLabel.setAlignmentX(CENTER_ALIGNMENT);

    resultPanel.add(strengthStandardLabel);
    resultPanel.add(userInfoLabel);

    // add some space
    resultPanel.add(Box.createRigidArea(new Dimension(0, 20)));

    resultPanel.add(createStrengthLevelLabel());

    JPanel strengthPanel = new JPanel(new GridLayout(3, 2, 0, 0));
    strengthPanel.setBackground(Color.WHITE);
    strengthPanel.setBorder(new EmptyBorder(30, 70, 30, 50));
    addStrengthStandard(strengthPanel, "Bench", calculateStrengthLevel("bench", benchSets));
    addStrengthStandard(strengthPanel, "Deadlift", calculateStrengthLevel("deadlift", deadliftSets));
    addStrengthStandard(strengthPanel, "Squat", calculateStrengthLevel("squat", squatSets));
    resultPanel.add(strengthPanel);

    JButton infoButton = new JButton("Check Standards Info.");
    infoButton.setPreferredSize(new Dimension(360, 40));
    infoButton.setForeground(Color.WHITE);
    // background color dark navy
    infoButton.setBackground(new Color(10, 10, 100));
    infoButton.setFont(new Font("Arial", Font.BOLD, 14));
    infoButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // open standard info dialog
        StandardInfoDialog standardInfoDialog = new StandardInfoDialog(parent);
        standardInfoDialog.setVisible(true);
      }
    });
    infoButton.setAlignmentX(CENTER_ALIGNMENT);
    resultPanel.add(infoButton);

    add(resultPanel, BorderLayout.CENTER);

    setSize(500, 800);
    setLocationRelativeTo(parent);
  }

  private JPanel createStrengthLevelLabel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.setBackground(Color.WHITE);

    StrengthLevel.LevelColor[] levels = StrengthLevel.LevelColor.values();
    for (int i = 0; i < levels.length; i++) {
      JLabel levelLabel = new JLabel(levels[i].getLabel());
      levelLabel.setFont(new Font("Arial", Font.BOLD, 14));
      levelLabel.setForeground(levels[i].getColor());
      panel.add(levelLabel);

      if (i < levels.length - 1) {
        panel.add(new JLabel(" - "));
      }
    }

    panel.setAlignmentX(CENTER_ALIGNMENT);
    return panel;
  }

  private void add1RMInfo(JPanel panel, String exerciseName, List<Set> sets) {
    double max1RM = calculateMax1RM(sets);
    JLabel exerciseLabel = new JLabel(exerciseName + ":");
    exerciseLabel.setFont(new Font("Arial", Font.BOLD, 22));
    panel.add(exerciseLabel);

    JLabel valueLabel = new JLabel(max1RM + "kg");
    valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
    panel.add(valueLabel);
  }

  private void addStrengthStandard(JPanel panel, String exerciseName, StrengthLevel.LevelColor level) {
    JLabel exerciseLabel = new JLabel(exerciseName + ":");
    exerciseLabel.setFont(new Font("Arial", Font.BOLD, 18));
    panel.add(exerciseLabel);

    JLabel levelLabel = new JLabel(level.getLabel());
    levelLabel.setFont(new Font("Arial", Font.BOLD, 18));
    levelLabel.setForeground(level.getColor());
    panel.add(levelLabel);
  }

  private StrengthLevel.LevelColor calculateStrengthLevel(String exercise, List<Set> sets) {
    double max1RM = calculateMax1RM(sets);
    return StrengthStandardsData.getStrengthLevel(user.getGender(), exercise, user.getWeight(), max1RM);
  }

  private double calculateMax1RM(List<Set> sets) {
    double max1RM = 0;
    for (Set set : sets) {
      double current1RM = set.calculate1RM();
      if (current1RM > max1RM) {
        max1RM = current1RM;
      }
    }
    return max1RM;
  }
}
