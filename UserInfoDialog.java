import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class UserInfoDialog extends JDialog {
  private JTextField nameField;
  private JToggleButton maleButton;
  private JToggleButton femaleButton;
  private JTextField heightField;
  private JTextField weightField;
  private User user;

  // Constructor to initialize the UserInfoDialog
  public UserInfoDialog(Frame parent) {
    super(parent, "Lift Log", true); // Calls the parent constructor to set the title and modality
    setSize(500, 800);
    setLocationRelativeTo(null);

    // Set layout and background color for the dialog
    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);

    // Create a main panel to hold all components with padding
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20)); // Add padding

    // Create a panel for the logo with padding
    JPanel logoPanel = new JPanel();
    logoPanel.setBackground(Color.WHITE);
    logoPanel.setLayout(new BorderLayout());

    // Load and scale the logo image
    try {
      URL logoUrl = new URL(
          "https://github.com/B0XERCAT/lift_log/assets/97675977/91a3614f-5b4b-4bc9-9096-ac709b66211a");
      Image logoImage = ImageIO.read(logoUrl).getScaledInstance(240, 240, Image.SCALE_SMOOTH);
      JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
      logoLabel.setHorizontalAlignment(JLabel.CENTER);
      logoPanel.add(logoLabel, BorderLayout.CENTER);
    } catch (IOException e) {
      e.printStackTrace();
      JLabel errorLabel = new JLabel("Logo could not be loaded.");
      errorLabel.setHorizontalAlignment(JLabel.CENTER);
      logoPanel.add(errorLabel, BorderLayout.CENTER);
    }

    // Create a panel for the title and subtitle
    JPanel titlePanel = new JPanel();
    titlePanel.setBackground(Color.WHITE);
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

    // Title label
    JLabel titleLabel = new JLabel("Lift Log", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
    titleLabel.setForeground(Color.BLACK);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Subtitle labels
    JLabel subtitleLabel1 = new JLabel("Please enter the following", JLabel.CENTER);
    subtitleLabel1.setFont(new Font("Arial", Font.PLAIN, 16));
    subtitleLabel1.setForeground(Color.DARK_GRAY);
    subtitleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitleLabel2 = new JLabel("personal information for accurate result.", JLabel.CENTER);
    subtitleLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
    subtitleLabel2.setForeground(Color.DARK_GRAY);
    subtitleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add title and subtitles to the title panel
    titlePanel.add(titleLabel);
    titlePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
    titlePanel.add(subtitleLabel1);
    titlePanel.add(subtitleLabel2);

    // Form panel to hold the input fields and labels
    JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Name input
    JLabel nameLabel = new JLabel("Name");
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
    nameLabel.setForeground(Color.BLACK);
    nameField = new JTextField();
    // Add horizontal padding to text field
    nameField.setBorder(
        BorderFactory.createCompoundBorder(nameField.getBorder(), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

    // Gender input
    JLabel genderLabel = new JLabel("Gender");
    genderLabel.setFont(new Font("Arial", Font.BOLD, 16));
    genderLabel.setForeground(Color.BLACK);
    maleButton = new JToggleButton("Male");
    femaleButton = new JToggleButton("Female");

    // Group gender buttons so only one can be selected
    ButtonGroup genderGroup = new ButtonGroup();
    genderGroup.add(maleButton);
    genderGroup.add(femaleButton);

    JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    genderPanel.setBackground(Color.WHITE);
    genderPanel.add(maleButton);
    genderPanel.add(femaleButton);

    // Height input
    JLabel heightLabel = new JLabel("Height (cm)");
    heightLabel.setFont(new Font("Arial", Font.BOLD, 16));
    heightLabel.setForeground(Color.BLACK);
    heightField = new JTextField();
    // Add horizontal padding to text field
    heightField.setBorder(
        BorderFactory.createCompoundBorder(heightField.getBorder(), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

    // Weight input
    JLabel weightLabel = new JLabel("Weight (kg)");
    weightLabel.setFont(new Font("Arial", Font.BOLD, 16));
    weightLabel.setForeground(Color.BLACK);
    weightField = new JTextField();
    // Add horizontal padding to text field
    weightField.setBorder(
        BorderFactory.createCompoundBorder(weightField.getBorder(), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

    // Create a panel for the submit button with padding
    JPanel submitPanel = new JPanel();
    submitPanel.setBackground(Color.WHITE);
    submitPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

    // Submit button
    JButton submitButton = new JButton("SUBMIT");
    submitButton.setPreferredSize(new Dimension(360, 40));
    submitButton.setForeground(Color.WHITE);
    // Set background color to dark navy
    submitButton.setBackground(new Color(10, 10, 100));
    submitButton.setFont(new Font("Arial", Font.BOLD, 14));
    submitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        try {
          InputValidator.validateName(name); // Validate name
        } catch (InvalidInputException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          InputValidator.validateGender(maleButton, femaleButton); // Validate gender
        } catch (InvalidInputException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }
        User.Gender gender = maleButton.isSelected() ? User.Gender.MALE : User.Gender.FEMALE;

        String height = heightField.getText();
        try {
          InputValidator.validateHeight(height); // Validate height
        } catch (InvalidInputException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }
        String weight = weightField.getText();
        try {
          InputValidator.validateWeight(weight); // Validate weight
        } catch (InvalidInputException ex) {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
          return;
        }
        user = new User(name, gender, Double.parseDouble(height), Double.parseDouble(weight)); // Create user object
        dispose(); // Close the dialog
        WorkoutDialog workoutDialog = new WorkoutDialog(null, user, null, null, null); // Open workout dialog
        workoutDialog.setVisible(true);
      }
    });
    submitPanel.add(submitButton);

    // Add components to form panel
    formPanel.add(nameLabel);
    formPanel.add(nameField);
    formPanel.add(genderLabel);
    formPanel.add(genderPanel);
    formPanel.add(heightLabel);
    formPanel.add(heightField);
    formPanel.add(weightLabel);
    formPanel.add(weightField);

    // Add panels to main panel
    mainPanel.add(logoPanel);
    mainPanel.add(titlePanel);
    mainPanel.add(formPanel);
    mainPanel.add(submitPanel);

    // Create a wrapper panel to add padding around the main panel
    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
    wrapperPanel.setBackground(Color.WHITE);
    wrapperPanel.add(mainPanel, BorderLayout.CENTER);

    // Add the wrapper panel to the dialog
    add(wrapperPanel);

    pack();
    setResizable(false);
  }

  // Method to get the user object
  public User getUser() {
    return user;
  }
}
