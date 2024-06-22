import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create and show the user info dialog
                UserInfoDialog userInfoDialog = new UserInfoDialog(null);
                userInfoDialog.setLocationRelativeTo(null);
                userInfoDialog.setVisible(true);
            }
        });
    }
}
