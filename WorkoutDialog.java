import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDialog extends JDialog {
    private JTabbedPane tabbedPane;
    private List<ExercisePanel> exercisePanels;
    private List<Set> deadliftSets;
    private List<Set> benchSets;
    private List<Set> squatSets;
    private Boolean isValidFormat;

// Constructor to initialize the WorkoutDialog
    public WorkoutDialog(Frame parent, User user, List<Set> initialSquatSets, List<Set> initialDeadliftSets,
            List<Set> initialBenchSets) {
        super(parent, "Lift Log", true);
        tabbedPane = new JTabbedPane();
        exercisePanels = new ArrayList<>();
        deadliftSets = initialDeadliftSets != null ? new ArrayList<>(initialDeadliftSets) : new ArrayList<>();
        benchSets = initialBenchSets != null ? new ArrayList<>(initialBenchSets) : new ArrayList<>();
        squatSets = initialSquatSets != null ? new ArrayList<>(initialSquatSets) : new ArrayList<>();
        isValidFormat = true;

        ExercisePanel benchPressPanel = new BenchPressPanel(
                "https://github.com/B0XERCAT/lift_log/assets/97675977/afc1e956-0e8a-4a2d-a6eb-753c6a2725ba",
                benchSets);
        ExercisePanel deadliftPanel = new DeadliftPanel(
                "https://github.com/B0XERCAT/lift_log/assets/97675977/16d017a2-668c-4bc2-b7e8-c6e1029e8373",
                deadliftSets);
        ExercisePanel squatPanel = new SquatPanel(
                "https://github.com/B0XERCAT/lift_log/assets/97675977/0f2ca605-5cc3-435c-9a10-a7f4c42797d4",
                squatSets);

        exercisePanels.add(benchPressPanel);
        exercisePanels.add(deadliftPanel);
        exercisePanels.add(squatPanel);

// Add tabs for each exercise
        tabbedPane.addTab("Bench Press", benchPressPanel);
        tabbedPane.addTab("Deadlift", deadliftPanel);
        tabbedPane.addTab("Barbell Squat", squatPanel);

// Create buttons
        JButton completeButton = new JButton("Complete Workout");
        completeButton.setForeground(Color.WHITE);
// Set background color to dark navy
        completeButton.setBackground(new Color(10, 10, 100));
        completeButton.setFont(new Font("Arial", Font.BOLD, 14));
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isValidFormat = true;
                collectWorkoutDetails();
                if (!isValidFormat) {
                    return;
                }
                dispose();
                ResultDialog resultDialog = new ResultDialog(null, squatSets, deadliftSets, benchSets, user);
                resultDialog.setVisible(true);
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(10, 100, 10));
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> {
            isValidFormat = true;
            collectWorkoutDetails();
            if (!isValidFormat) {
                return;
            }
            saveWorkoutDetails();
        });

        JButton loadButton = new JButton("Load");
        loadButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(100, 10, 10));
        loadButton.setFont(new Font("Arial", Font.BOLD, 14));
        loadButton.addActionListener(e -> loadWorkoutDetails(user));

// Add buttons to panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(completeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

// Add components to the dialog
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void collectWorkoutDetails() {
        deadliftSets.clear();
        benchSets.clear();
        squatSets.clear();

        for (ExercisePanel panel : exercisePanels) {
            String exerciseName = panel.exerciseName;
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

// Saves workout details to a file
    private void saveWorkoutDetails() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Workout Details");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                for (Set set : squatSets) {
                    writer.write("Squat " + set.getWeight() + " " + set.getRepeats());
                    writer.newLine();
                }
                for (Set set : deadliftSets) {
                    writer.write("Deadlift " + set.getWeight() + " " + set.getRepeats());
                    writer.newLine();
                }
                for (Set set : benchSets) {
                    writer.write("Bench " + set.getWeight() + " " + set.getRepeats());
                    writer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save workout details.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Loads workout details from a file
    private void loadWorkoutDetails(User user) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Workout Details");
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            List<Set> loadedSquatSets = new ArrayList<>();
            List<Set> loadedDeadliftSets = new ArrayList<>();
            List<Set> loadedBenchSets = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        String exercise = parts[0];
                        int weight = Integer.parseInt(parts[1]);
                        int repeats = Integer.parseInt(parts[2]);
                        Set set = new Set(weight, repeats);
                        switch (exercise) {
                            case "Squat":
                                loadedSquatSets.add(set);
                                break;
                            case "Deadlift":
                                loadedDeadliftSets.add(set);
                                break;
                            case "Bench":
                                loadedBenchSets.add(set);
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to load workout details.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            WorkoutDialog newDialog = new WorkoutDialog(null, user, loadedSquatSets, loadedDeadliftSets,
                    loadedBenchSets);
            newDialog.setVisible(true);
        }
    }
}
