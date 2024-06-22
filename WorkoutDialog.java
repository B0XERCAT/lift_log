import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDialog extends JDialog {
    private JTabbedPane tabbedPane;
    private List<JPanel> exercisePanels;
    private List<Set> deadliftSets;
    private List<Set> benchSets;
    private List<Set> squatSets;
    private Boolean isValidFormat;

    public WorkoutDialog(Frame parent, User user) {
        super(parent, "Lift Log", true);
        tabbedPane = new JTabbedPane();
        exercisePanels = new ArrayList<>();
        deadliftSets = new ArrayList<>();
        benchSets = new ArrayList<>();
        squatSets = new ArrayList<>();

        JPanel benchPressPanel = createExercisePanel("bench.png", "Bench Press");
        JPanel deadliftPanel = createExercisePanel("deadlift.png", "Deadlift");
        JPanel squatPanel = createExercisePanel("squat.png", "Barbell Squat");

        exercisePanels.add(benchPressPanel);
        exercisePanels.add(deadliftPanel);
        exercisePanels.add(squatPanel);

        tabbedPane.addTab("Bench Press", benchPressPanel);
        tabbedPane.addTab("Deadlift", deadliftPanel);
        tabbedPane.addTab("Barbell Squat", squatPanel);

        JButton completeButton = new JButton("Complete Workout");
        completeButton.setForeground(Color.WHITE);
        // background color dark navy
        completeButton.setBackground(new Color(10, 10, 100));
        completeButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isValidFormat = true;
                collectWorkoutDetails();
                if (isValidFormat == false) {
                    return;
                }
                dispose();
                ResultDialog resultDialog = new ResultDialog(null, squatSets, deadliftSets, benchSets, user);
                resultDialog.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(completeButton);

        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private JPanel createExercisePanel(String imagePath, String exerciseName) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(exerciseName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JLabel imageLabel = createImageLabel(imagePath, 240, 240);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
        panel.add(imageLabel, gbc);
        gbc.gridwidth = 1;

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
        panel.add(buttonPanel, gbc);

        addSetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSet(panel, buttonPanel);
            }
        });

        deleteSetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSet(panel, buttonPanel);
            }
        });

        // Add default set
        addSet(panel, buttonPanel);

        return panel;
    }

    private JLabel createImageLabel(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon("assets/" + imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        return new JLabel(resizedIcon);
    }

    private void addSet(JPanel panel, JPanel buttonPanel) {
        int index = getNextIndex(panel, buttonPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

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
        } else if (index > 8) {
            JOptionPane.showMessageDialog(this, "Maximum number of sets reached.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField weightField = new JTextField(8);
        JTextField repeatField = new JTextField(8);

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

    private void deleteSet(JPanel panel, JPanel buttonPanel) {
        int index = getNextIndex(panel, buttonPanel) - 1;

        if (index > 1) { // Ensure at least one set remains
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

    private int getNextIndex(JPanel panel, JPanel buttonPanel) {
        int maxIndex = 1;
        for (Component component : panel.getComponents()) {
            if (component != buttonPanel) {
                GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
                if (gbc.gridy >= maxIndex + 3) { // Adjust to start from the fourth row
                    maxIndex = gbc.gridy + 1 - 3;
                }
            }
        }
        return maxIndex > 1 ? maxIndex : 1;
    }

    private void collectWorkoutDetails() {
        deadliftSets.clear();
        benchSets.clear();
        squatSets.clear();

        for (int i = 0; i < exercisePanels.size(); i++) {
            JPanel panel = exercisePanels.get(i);
            String exerciseName = tabbedPane.getTitleAt(i);

            List<Set> currentSets = null;
            if (exerciseName.equals("Deadlift")) {
                currentSets = deadliftSets;
            } else if (exerciseName.equals("Bench Press")) {
                currentSets = benchSets;
            } else if (exerciseName.equals("Barbell Squat")) {
                currentSets = squatSets;
            }

            if (currentSets != null) {
                for (Component component : panel.getComponents()) {
                    GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
                    if (component instanceof JTextField && gbc.gridx == 1) {
                        JTextField weightField = (JTextField) component;
                        JTextField repeatField = (JTextField) panel
                                .getComponent(panel.getComponentZOrder(component) + 1);
                        try {
                            String weight = weightField.getText();
                            String repeats = repeatField.getText();
                            InputValidator.validateWeight(weight);
                            InputValidator.validateRepeats(repeats);
                            currentSets.add(new Set(Integer.parseInt(weight), Integer.parseInt(repeats)));
                        } catch (InvalidInputException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), exerciseName + " Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);
                            isValidFormat = false;
                            return;
                        }
                    }
                }
            }
        }
    }
}
