import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultDialog extends JDialog {
  public ResultDialog(Frame parent, List<Set> squatSets, List<Set> deadliftSets, List<Set> benchSets,
      User user) {
    super(parent, "Workout Results", true);
    setLayout(new BorderLayout());

    JPanel resultPanel = new JPanel();
    resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

    resultPanel.add(new JLabel("User Information: " + user));

    addSetToPanel(resultPanel, "Bench Press Sets", benchSets);
    addSetToPanel(resultPanel, "Deadlift Sets", deadliftSets);
    addSetToPanel(resultPanel, "Squat Sets", squatSets);

    JScrollPane scrollPane = new JScrollPane(resultPanel);
    add(scrollPane, BorderLayout.CENTER);

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> dispose());
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(closeButton);
    add(buttonPanel, BorderLayout.SOUTH);

    setSize(400, 600);
    setLocationRelativeTo(parent);
  }

  private void addSetToPanel(JPanel panel, String title, List<Set> sets) {
    panel.add(new JLabel(title));
    for (Set set : sets) {
      panel.add(new JLabel(set.toString()));
    }
    panel.add(Box.createVerticalStrut(10)); // Add some space between different exercises
  }
}
