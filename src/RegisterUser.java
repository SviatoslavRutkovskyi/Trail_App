import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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
    private JPanel panel;

    public RegisterUser() {
        frame = new JFrame();
        panel = new JPanel();
        frame.setLayout(new BorderLayout());
        frame.setSize(350, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("User Registration");
        frame.add(panel);

//        createComponents(panel);
        panel.setLayout(null);
        createInsert(new String[]{"name", "phone", "email"}, "USER");
//        createInsert(new String[]{"user_id", "trail_id", "date_review", "trail_rating", "review_text"}, "USER_REVIEW");


        frame.setVisible(true);
    }

    private void createComponents(JPanel panel) {
        panel.setLayout(null);
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
    private void createInsert(String[] labelNames, String table) {
        // insert data into table
        JLabel[] lableArray =  new JLabel[labelNames.length];
        System.out.println(Arrays.toString(labelNames));
        JTextField[] textFieldArray = new JTextField[labelNames.length];
        for (int i = 0; i < labelNames.length; i++) {
            lableArray[i] = new JLabel(labelNames[i]);
            lableArray[i].setBounds(10,20 + (i * 30), 80, 25);
            textFieldArray[i] = new JTextField(20);
            textFieldArray[i].setBounds(100, 20 + (i * 30), 165, 25);
            panel.add(lableArray[i]);
            panel.add(textFieldArray[i]);
        }
        saveButton = new JButton("Save");
        saveButton.setSize(200, 100);
        saveButton.setBounds(100, 20 + (labelNames.length * 30), 80, 25);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(e -> {
            String query = "INSERT INTO " + table +  "(";
//            String query = "INSERT INTO " + table + "(";
            for (int i = 0; i < lableArray.length; i++) {
                query += lableArray[i].getText();
                if (i != lableArray.length - 1) {
                    query += ", ";
                }
            }
            query += ") VALUES("; //"?, ?, ?, ?);";
            for (int i = 0; i < textFieldArray.length; i++) {
                query += "?";
                if (i != textFieldArray.length - 1) {
                    query += ", ";
                }
            }
            query += ");";
            System.out.println(query);
            try {
                PreparedStatement ps = Main.connection.prepareStatement(query);
                for (int i = 0; i < textFieldArray.length; i++) {
                    ps.setString(i + 1, textFieldArray[i].getText());
                }
                ps.addBatch();
                ps.executeBatch();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
        });
        panel.add(saveButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setSize(200, 100);
        cancelButton.setBounds(180, 20 + (labelNames.length * 30), 80, 25);
        cancelButton.setActionCommand("Cancel");
//        cancelButton.addActionListener(new buttonListener());
        cancelButton.addActionListener(e -> {

            frame.dispose();
        });
        panel.add(cancelButton);
    }
}
