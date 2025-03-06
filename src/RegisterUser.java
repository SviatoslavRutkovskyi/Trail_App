import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterUser {
    private JFrame frame;
    private JLabel nameLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JTextField nameTextField;
    private JTextField phoneTextField;
    private JTextField emailTextField;
    private JButton saveButton;
    private JButton cancelButton;

    public RegisterUser() {
        frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.setSize(350, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("User Registration");
        frame.add(panel);

        createComponents(panel);

        frame.setVisible(true);
    }

    private void createComponents(JPanel panel) {
        panel.setLayout(null);

        nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(10,20, 80, 25);
        nameTextField = new JTextField(20);
        nameTextField.setBounds(100, 20, 165, 25);
        panel.add(nameLabel);
        panel.add(nameTextField);

        phoneLabel = new JLabel("Phone: ");
        phoneLabel.setBounds(10, 50, 80, 25);
        phoneTextField = new JTextField(15);
        phoneTextField.setBounds(100, 50, 165, 25);
        panel.add(phoneLabel);
        panel.add(phoneTextField);

        emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(10, 80, 80, 25);
        emailTextField = new JTextField(30);
        emailTextField.setBounds(100, 80, 165, 25);
        panel.add(emailLabel);
        panel.add(emailTextField);

        saveButton = new JButton("Save");
        saveButton.setSize(200, 100);
        saveButton.setBounds(100, 130, 80, 25);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(new buttonListener());
        panel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setSize(200, 100);
        cancelButton.setBounds(180, 130, 80, 25);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new buttonListener());
        panel.add(cancelButton);
    }

    private class buttonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Save")) {
                insertData();
                frame.dispose();
            }
            if (command.equals("Cancel")) {
                frame.dispose();
            }
        }
    }

    private void insertData() {
        // insert data into table
    }
}
