import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserInfoDialog userInfoDialog = new UserInfoDialog(null);
                userInfoDialog.setVisible(true);
            }
        });
    }
}
