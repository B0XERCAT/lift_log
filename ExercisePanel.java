import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ExercisePanel extends JPanel {
  protected List<Set> sets;
  protected String exerciseName;

  // Constructor for the ExercisePanel class
  public ExercisePanel(String imageUrl, String exerciseName, List<Set> sets) {
    this.exerciseName = exerciseName;
    this.sets = sets;
    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Add the exercise name as a title label
    JLabel titleLabel = new JLabel(exerciseName);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
    add(titleLabel, gbc);
    gbc.gridwidth = 1;

    // Add the exercise image
    JLabel imageLabel = createImageLabel(imageUrl, 240, 240);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
    add(imageLabel, gbc);
    gbc.gridwidth = 1;

    // Create and add buttons for adding and deleting sets
    JButton addSetButton = new JButton("Add Set");
    addSetButton.setBackground(new Color(10, 100, 10));
    addSetButton.setForeground(Color.WHITE);
    JButton deleteSetButton = new JButton("Delete Set");
    deleteSetButton.setBackground(new Color(100, 10, 10));
    deleteSetButton.setForeground(Color.WHITE);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.add(addSetButton);
    buttonPanel.add(deleteSetButton);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
    add(buttonPanel, gbc);

    // Add action listeners for the buttons
    addSetButton.addActionListener(e -> addSet(this, buttonPanel));
    deleteSetButton.addActionListener(e -> deleteSet(this, buttonPanel));

    // If sets are provided, add them to the panel, otherwise add a default set
    if (sets != null && !sets.isEmpty()) {
      for (Set set : sets) {
        addSet(this, buttonPanel, set);
      }
    } else {
      addSet(this, buttonPanel);
    }
  }

  // Helper method to create a JLabel with a resized image from a URL
  private JLabel createImageLabel(String imageUrl, int width, int height) {
    try {
      URL url = new URL(imageUrl);
      Image originalImage = ImageIO.read(url);
      Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      ImageIcon resizedIcon = new ImageIcon(resizedImage);
      return new JLabel(resizedIcon);
    } catch (IOException e) {
      e.printStackTrace();
      return new JLabel("Image not available");
    }
  }

  // Method to add a set to the panel without initial values
  protected void addSet(JPanel panel, JPanel buttonPanel) {
    addSet(panel, buttonPanel, null);
  }

  // Method to add a set to the panel with optional initial values
  protected void addSet(JPanel panel, JPanel buttonPanel, Set set) {
    int index = getNextIndex(panel, buttonPanel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Add labels for weight and repeat at the first set
    if (index == 1) {
      JLabel weightLabel = new JLabel("Weight (kg)");
      weightLabel.setHorizontalAlignment(SwingConstants.CENTER);
      weightLabel.setForeground(Color.GRAY);
      gbc.gridx = 1;
      gbc.gridy = index + 2; // Adjust to start from the third row
      panel.add(weightLabel, gbc);

      JLabel repeatLabel = new JLabel("Repeat");
      repeatLabel.setHorizontalAlignment(SwingConstants.CENTER);
      repeatLabel.setForeground(Color.GRAY);
      gbc.gridx = 2;
      panel.add(repeatLabel, gbc);
    } else if (index > 8) { // Limit to 8 sets
      JOptionPane.showMessageDialog(this, "Maximum number of sets reached.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JTextField weightField = new JTextField(8);
    JTextField repeatField = new JTextField(8);

    // Set initial values if provided
    if (set != null) {
      weightField.setText(String.valueOf(set.getWeight()));
      repeatField.setText(String.valueOf(set.getRepeats()));
    }

    gbc.gridx = 0;
    gbc.gridy = index + 3; // Adjust to start from the fourth row
    panel.add(new JLabel(String.valueOf(index)), gbc);

    gbc.gridx = 1;
    panel.add(weightField, gbc);

    gbc.gridx = 2;
    panel.add(repeatField, gbc);

    // Move button panel down
    gbc.gridx = 0;
    gbc.gridy = index + 4;
    gbc.gridwidth = 3;
    panel.add(buttonPanel, gbc);

    panel.revalidate();
    panel.repaint();
  }

  // Method to delete the last set from the panel
  protected void deleteSet(JPanel panel, JPanel buttonPanel) {
    int index = getNextIndex(panel, buttonPanel) - 1;

    if (index > 1) {
      List<Component> componentsToRemove = new ArrayList<>();
      for (Component component : panel.getComponents()) {
        GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
        if (gbc.gridy == index + 3) {
          componentsToRemove.add(component);
        }
      }
      for (Component component : componentsToRemove) {
        panel.remove(component);
      }

      // Move button panel up
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(5, 5, 5, 5);
      gbc.gridx = 0;
      gbc.gridy = index + 3;
      gbc.gridwidth = 3;
      panel.add(buttonPanel, gbc);

      panel.revalidate();
      panel.repaint();
    } else {
      JOptionPane.showMessageDialog(this, "There should be at least 1 set.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Helper method to get the next index for adding a set
  protected int getNextIndex(JPanel panel, JPanel buttonPanel) {
    int maxIndex = 1;
    for (Component component : panel.getComponents()) {
      if (component != buttonPanel) {
        GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
        if (gbc.gridy >= maxIndex + 3) {
          maxIndex = gbc.gridy + 1 - 3;
        }
      }
    }
    return maxIndex > 1 ? maxIndex : 1;
  }
}

// Class for the Bench Press panel
class BenchPressPanel extends ExercisePanel {
  public BenchPressPanel(String imageUrl, List<Set> sets) {
    super(imageUrl, "Bench Press", sets);
  }
}

// Class for the Deadlift panel
class DeadliftPanel extends ExercisePanel {
  public DeadliftPanel(String imageUrl, List<Set> sets) {
    super(imageUrl, "Deadlift", sets);
  }
}

// Class for the Squat panel
class SquatPanel extends ExercisePanel {
  public SquatPanel(String imageUrl, List<Set> sets) {
    super(imageUrl, "Barbell Squat", sets);
  }
}
