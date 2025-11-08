import controller.LoginController;
import dao.UserDAO;
import view.LoginFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // This makes sure the UI runs smoothly
        SwingUtilities.invokeLater(() -> {
            try {
                // This gives the application the standard look of your operating system
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create and show the Login screen to start the application
            LoginFrame loginView = new LoginFrame();
            UserDAO userDAO = new UserDAO();
            // The LoginController handles creating and passing the TrackDAO later.
            new LoginController(loginView, userDAO); 
            loginView.setVisible(true);
        });
    }
}
