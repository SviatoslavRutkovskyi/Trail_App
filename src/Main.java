import java.awt.FlowLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main {
    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project10_db",
                    "root", "1231");
            System.out.println("Database connected!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Trail Database");

        JTable table = new JTable();

        JButton trailButton = new JButton("View Trails");
        trailButton.setSize(200, 100);
        JButton userButton = new JButton("View Users");
        userButton.setSize(200, 100);

        frame.add(new JScrollPane(table));
        frame.add(trailButton);
        frame.add(userButton);
        frame.setVisible(true);

        // temporary until new button and action listener at setup:
        RegisterUser testRegistration = new RegisterUser();

        trailButton.addActionListener(e -> {
            try {
                DefaultTableModel model = runQuery("SELECT * FROM trail");
                table.setModel(model);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        userButton.addActionListener(e -> {
            try {
                DefaultTableModel model = runQuery("SELECT * FROM user");
                table.setModel(model);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });



        }
    private static DefaultTableModel runQuery(String sqlQuery) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet table_data = statement.executeQuery(sqlQuery);
        ResultSetMetaData table_cols = table_data.getMetaData();
        String[] cols = new String[table_cols.getColumnCount()];
        for (int i = 0; i < table_cols.getColumnCount(); i++) {
            cols[i] = table_cols.getColumnName(i + 1);
        }
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(cols);
        while (table_data.next()) {
            String[] row = new String[cols.length];
            for (int i = 0; i < cols.length; i++) {
                row[i] = table_data.getString(i + 1);
            }
            model.addRow(row);
        }
        return model;
    }
}