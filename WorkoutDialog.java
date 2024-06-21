import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDialog extends JDialog {
    private JTabbedPane tabbedPane;
    private List<JPanel> exercisePanels;

    public WorkoutDialog(Frame parent, User user) {
        super(parent, "Lift Log", true);
        tabbedPane = new JTabbedPane();
        exercisePanels = new ArrayList<>();

        JPanel benchPressPanel = createExercisePanel("bench.png");
        JPanel deadliftPanel = createExercisePanel("deadlift.png");
        JPanel squatPanel = createExercisePanel("squat.png");

        exercisePanels.add(benchPressPanel);
        exercisePanels.add(deadliftPanel);
        exercisePanels.add(squatPanel);

        tabbedPane.addTab("Bench Press", benchPressPanel);
        tabbedPane.addTab("Deadlift", deadliftPanel);
        tabbedPane.addTab("Barbell Squat", squatPanel);

        JButton completeButton = new JButton("Complete Workout");
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printWorkoutDetails();
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

    private JPanel createExercisePanel(String imagePath) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel imageLabel = createImageLabel(imagePath, 240, 240);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Adjust gridwidth for 3 columns
        panel.add(imageLabel, gbc);
        gbc.gridwidth = 1;

        JButton addSetButton = new JButton("Add Set");
        JButton deleteSetButton = new JButton("Delete Set");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSetButton);
        buttonPanel.add(deleteSetButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
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

        JTextField weightField = new JTextField(5);
        JTextField repeatField = new JTextField(5);

        gbc.gridx = 0;
        gbc.gridy = index; // Start from index directly
        panel.add(new JLabel(String.valueOf(index)), gbc);

        gbc.gridx = 1;
        panel.add(weightField, gbc);

        gbc.gridx = 2;
        panel.add(repeatField, gbc);

        // Move button panel down
        gbc.gridx = 0;
        gbc.gridy = index + 1;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);

        panel.revalidate();
        panel.repaint();
    }

    private void deleteSet(JPanel panel, JPanel buttonPanel) {
        int index = getNextIndex(panel, buttonPanel) - 1;

        if (index > 0) {
            List<Component> componentsToRemove = new ArrayList<>();
            for (Component component : panel.getComponents()) {
                GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
                if (gbc.gridy == index) {
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
            gbc.gridy = index;
            gbc.gridwidth = 3;
            panel.add(buttonPanel, gbc);

            panel.revalidate();
            panel.repaint();
        }
    }

    private int getNextIndex(JPanel panel, JPanel buttonPanel) {
        int maxIndex = 1;
        for (Component component : panel.getComponents()) {
            if (component != buttonPanel) {
                GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
                if (gbc.gridy >= maxIndex) {
                    maxIndex = gbc.gridy + 1;
                }
            }
        }
        return maxIndex > 1 ? maxIndex : 1;
    }

    private void printWorkoutDetails() {
        for (int i = 0; i < exercisePanels.size(); i++) {
            JPanel panel = exercisePanels.get(i);
            String exerciseName = tabbedPane.getTitleAt(i);
            System.out.println(exerciseName + ":");
            for (Component component : panel.getComponents()) {
                GridBagConstraints gbc = ((GridBagLayout) panel.getLayout()).getConstraints(component);
                if (component instanceof JTextField && gbc.gridx == 1) {
                    JTextField weightField = (JTextField) component;
                    JTextField repeatField = (JTextField) panel.getComponent(panel.getComponentZOrder(component) + 1);
                    System.out.println("Weight: " + weightField.getText() + " kg, Repeats: " + repeatField.getText());
                }
            }
            System.out.println();
        }
    }
}
