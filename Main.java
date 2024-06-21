import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserInfoDialog userInfoDialog = new UserInfoDialog(null);
                userInfoDialog.setVisible(true);
                if (userInfoDialog.isSucceeded()) {
                    User user = userInfoDialog.getUser();
                    System.out.println("User Information: " + user);
                    WorkoutDialog workoutDialog = new WorkoutDialog(null, user);
                    workoutDialog.setVisible(true);
                }
            }
        });
    }
}
