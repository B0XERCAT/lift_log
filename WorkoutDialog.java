import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDialog extends JDialog {
    private JTabbedPane tabbedPane;
    private List<JPanel> exercisePanels;
    private List<Set> deadliftSets;
    private List<Set> benchSets;
    private List<Set> squatSets;
    private Boolean isValidFormat;

    // Constructor to initialize the WorkoutDialog
    public WorkoutDialog(Frame parent, User user, List<Set> initialSquatSets, List<Set> initialDeadliftSets,
            List<Set> initialBenchSets) {
        super(parent, "Lift Log", true); // Calls the parent constructor to set the title and modality
        tabbedPane = new JTabbedPane();
        exercisePanels = new ArrayList<>();
        deadliftSets = initialDeadliftSets != null ? new ArrayList<>(initialDeadliftSets) : new ArrayList<>();
        benchSets = initialBenchSets != null ? new ArrayList<>(initialBenchSets) : new ArrayList<>();
        squatSets = initialSquatSets != null ? new ArrayList<>(initialSquatSets) : new ArrayList<>();
        isValidFormat = true;

        // Create panels for each exercise
        JPanel benchPressPanel = createExercisePanel("bench.png", "Bench Press", benchSets);
        JPanel deadliftPanel = createExercisePanel("deadlift.png", "Deadlift", deadliftSets);
        JPanel squatPanel = createExercisePanel("squat.png", "Barbell Squat", squatSets);

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

    // Creates an exercise panel with image, title, and controls for adding/deleting
    // sets
    private JPanel createExercisePanel(String imagePath, String exerciseName, List<Set> sets) {
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

        // Add action listeners for the buttons
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

        // Add sets from the provided list or default set if no sets are provided
        if (sets != null && !sets.isEmpty()) {
            for (Set set : sets) {
                addSet(panel, buttonPanel, set);
            }
        } else {
            addSet(panel, buttonPanel);
        }

        return panel;
    }

    // Creates a JLabel with a resized image
    private JLabel createImageLabel(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon("assets/" + imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        return new JLabel(resizedIcon);
    }

    // Adds a new set to the panel
    private void addSet(JPanel panel, JPanel buttonPanel) {
        addSet(panel, buttonPanel, null);
    }

    // Adds a new set to the panel with optional initial values
    private void addSet(JPanel panel, JPanel buttonPanel, Set set) {
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

        // Create text fields for weight and repeats
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

    // Deletes the last set from the panel
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

    // Get the next index for adding a set
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

    // Collects workout details from the UI and stores them in the appropriate lists
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
