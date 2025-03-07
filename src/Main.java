import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main {
    public static final Connection connection;

    // This sets up the connection to the database
    static {
        try {
            // this contains the connection information, make sure it matches your database
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

        frame.setSize(500, 610);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Trail Database");

        JTable table = new JTable();

        JButton viewButton = new JButton("View");
        viewButton.setSize(200, 100);

        JButton addButton = new JButton("Add");
        addButton.setSize(200, 100);

        JButton showButton = new JButton("Show");
        showButton.setSize(200, 100);

        JButton analyzeButton = new JButton("Analyze");
        analyzeButton.setSize(200, 100);

        // table view queries
        JComboBox<String> viewMenu = new JComboBox<>(
                new String[]{"Trail", "User", "Pass", "County", "Feature_Type",
                        "Saved_Trails", "User_Review", "Preferred_Feature"});
        viewMenu.setSize(200, 100);

        // insert statements
        JComboBox<String> addMenu = new JComboBox<>(
                new String[]{"User", "User Review", "Saved Trail", "Preferred Feature"});
        addMenu.setSize(200, 100);

        // user input simple queries
        JComboBox<String> queryMenu = new JComboBox<>(
                new String[]{"trails with a name that contains",
                        "passes that are cheaper than",
                        "user reviews that have a rating less than",
                        "trails with at least a rating of"});
        queryMenu.setSize(400, 100);

        //analytical queries
        JComboBox<String> analyzeMenu = new JComboBox<>(
                new String[]{"Get the latest trail condition for the Pacific Crest Trail",
                        "Find the cheapest pass for each trail",
                        "List trails with user #2 preferred features and difficulty <= 3.",
                        "Display the average and count of reviews from users",
                        "Display all trails that are connected to the Pacific Crest Trail."});
        analyzeMenu.setSize(400, 100);

        JTextField queryField = new JTextField(5);
        queryField.setSize(200, 100);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add components to frame
        mainPanel.add(new JScrollPane(table));

        JPanel viewPanel = new JPanel();
        viewPanel.add(viewButton);
        viewPanel.add(viewMenu);
        mainPanel.add(viewPanel);

        JPanel addPanel = new JPanel();
        addPanel.add(addButton);
        addPanel.add(addMenu);
        mainPanel.add(addPanel);

        JPanel showPanel = new JPanel();
        showPanel.add(showButton);
        showPanel.add(queryMenu);
        showPanel.add(queryField);
        mainPanel.add(showPanel);

        JPanel analyzePanel = new JPanel();
        analyzePanel.add(analyzeButton);
        analyzePanel.add(analyzeMenu);
        mainPanel.add(analyzePanel);

        frame.add(mainPanel);
        frame.setVisible(true);

        // creates functionality for view button, gets the table info from viewMenu, and displays the database information
        viewButton.addActionListener(e -> {
            try {
                DefaultTableModel model = runQuery("SELECT * FROM " + viewMenu.getSelectedItem().toString().toLowerCase());
                table.setModel(model);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // creates functionality for add button, gets the table info from addMenu,
        // uses it in a switch statement to create a separate menu to insert info
        addButton.addActionListener(e -> {
            switch (addMenu.getSelectedItem().toString()) {
                case "User":
                    createInsertFrame(new String[]{"name", "phone", "email"}, "USER", "User Registration");
                    break;
                case "User Review":
                    createInsertFrame(new String[]{"user_id", "trail_id", "date_reviewed", "trail_rating", "review_text"}, "USER_REVIEW", "Create Trail Review");
                    break;
                case "Saved Trail":
                    createInsertFrame(new String[]{"user_id", "trail_id"}, "SAVED_TRAILS", "Save a Trail");
                    break;
                case "Preferred Feature":
                    createInsertFrame(new String[]{"user_id", "feature_name"}, "PREFERRED_FEATURE", "Add Preferred Feature");
                    break;
            }
        });

        // creates functionality for show button, gets the query info from showMenu,
        // and the user input from the queryField,
        // and uses it in a switch statement to show the result.
        // the user input is NOT checked for validity
        showButton.addActionListener(e -> {
            DefaultTableModel model = null;
            switch (queryMenu.getSelectedItem().toString()) {
                case "trails with a name that contains":
                    try {
                        model = runQuery("SELECT * FROM TRAIL WHERE trail_name LIKE '%" + queryField.getText() + "%';");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "passes that are cheaper than":
                    try {
                        model = runQuery("SELECT * FROM PASS WHERE pass_price < " + queryField.getText()
                                + " ORDER BY pass_price DESC;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "user reviews that have a rating less than":
                    try {
                        model = runQuery("SELECT * FROM USER_REVIEW WHERE trail_rating < "
                                + queryField.getText() + ";");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "trails with at least a rating of":
                    try {
                        model = runQuery("SELECT * FROM TRAIL WHERE trail_rating >= " + queryField.getText()
                                + " ORDER BY trail_rating DESC;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }
        });

        // creates functionality for analyze button, gets the query info from analyzeMenu,
        // uses it in a switch statement to show the result
        analyzeButton.addActionListener(e -> {
            DefaultTableModel model = null;
            switch (analyzeMenu.getSelectedItem().toString()) {
                case "Get the latest trail condition for the Pacific Crest Trail":
                    try {
                        model = runQuery("SELECT * " +
                                "FROM TRAIL NATURAL JOIN TRAIL_CONDITION " +
                                "WHERE trail_name = 'Pacific Crest Trail' " +
                                "ORDER BY condition_date DESC " +
                                "LIMIT 1;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Find the cheapest pass for each trail":
                    try {
                        model = runQuery("SELECT T1.trail_id, trail_name, trail_rating, pass_id, min_price, pass_type, pass_duration " +
                                "FROM " +
                                "    (SELECT T.trail_id, pass_id, min_price, pass_type, pass_duration " +
                                "    FROM   (SELECT trail_id, MIN(pass_price) as min_price " +
                                "        FROM TRAIL_PASS_OPTIONS NATURAL JOIN PASS " +
                                "        GROUP BY trail_id) AS T JOIN TRAIL_PASS_OPTIONS ON TRAIL_PASS_OPTIONS.trail_id = T.trail_id " +
                                "        NATURAL JOIN PASS WHERE pass_price = min_price) AS T1 " +
                                "        JOIN TRAIL ON TRAIL.trail_id = T1.trail_id;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "List trails with user #2 preferred features and difficulty <= 3.":
                    try {
                        model = runQuery("SELECT * " +
                                "FROM TRAIL NATURAL JOIN (" +
                                "   SELECT trail_id, feature_type " +
                                "   FROM TRAIL_FEATURE JOIN PREFERRED_FEATURE ON TRAIL_FEATURE.feature_type=PREFERRED_FEATURE.feature_name " +
                                "   WHERE user_id = 2 ) AS user2_features " +
                                "ORDER BY trail_id, feature_type;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Display the average and count of reviews from users":
                    try {
                        model = runQuery("SELECT * " +
                                "FROM (SELECT user_id, AVG(trail_rating) as average_rating, COUNT(*) as review_count " +
                                "FROM USER_REVIEW " +
                                "GROUP BY user_id) AS T NATURAL JOIN USER " +
                                "ORDER BY review_count DESC;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "Display all trails that are connected to the Pacific Crest Trail.":
                    try {
                        model = runQuery("SELECT trail_name, junction_id, direction " +
                                "FROM ( " +
                                "    SELECT * " +
                                "    FROM TRAIL " +
                                "    WHERE trail_id in " +
                                "        (SELECT distinct junction_id " +
                                "        FROM JUNCTION " +
                                "        WHERE trail_id = " +
                                "            (SELECT trail_id " +
                                "            FROM TRAIL " +
                                "            WHERE trail_name = 'Pacific Crest Trail') " +
                                "        ) " +
                                "    ) as T NATURAL JOIN JUNCTION ;");
                        table.setModel(model);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }
        });
    }


    /**
     * This method runs the provided sql query and the result from that query
     * as a DefaultTableModel
     * @param sqlQuery - provided sql query executed by the database, must be valid
     * @return DefaultTableModel - table information from the database returned by the query
     * @throws SQLException
     */
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

    /**
     * This method creates a new frame that contains all necessary labels and data enter fields,
     * used to create a new row in the provided table.
     * The user input is NOT checked for validity.
     * The table name must correspond to an existing table
     * The labels must correspond to existing columns in the provided table
     * @param labelNames Array of column names that will be inserted into
     * @param tableName Table name that will be inserted into
     * @param frameName Name for the created frame
     */
    private static void createInsertFrame(String[] labelNames, String tableName, String frameName) {
        // creates the new frame and panel
        JFrame insertFrame = new JFrame();
        JPanel insertPanel = new JPanel();
        insertFrame.setLayout(new BorderLayout());
        insertFrame.setSize(350, 300);
        insertFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        insertFrame.setTitle(frameName);
        insertFrame.add(insertPanel);
        insertPanel.setLayout(null);

        // creates the labels and text fields
        JLabel[] lableArray =  new JLabel[labelNames.length];
        JTextField[] textFieldArray = new JTextField[labelNames.length];
        for (int i = 0; i < labelNames.length; i++) {
            lableArray[i] = new JLabel(labelNames[i]);
            lableArray[i].setBounds(10,20 + (i * 30), 80, 25);
            textFieldArray[i] = new JTextField(20);
            textFieldArray[i].setBounds(100, 20 + (i * 30), 165, 25);
            insertPanel.add(lableArray[i]);
            insertPanel.add(textFieldArray[i]);
        }

        // adds a save button which constructs the sql statement from the user input and runs it
        JButton saveButton = new JButton("Save");
        saveButton.setSize(200, 100);
        saveButton.setBounds(100, 20 + (labelNames.length * 30), 80, 25);
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(e -> {
            String query = "INSERT INTO " + tableName +  "(";
            for (int i = 0; i < lableArray.length; i++) {
                query += lableArray[i].getText();
                if (i != lableArray.length - 1) {
                    query += ", ";
                }
            }
            query += ") VALUES(";
            for (int i = 0; i < textFieldArray.length; i++) {
                query += "?";
                if (i != textFieldArray.length - 1) {
                    query += ", ";
                }
            }
            query += ");";
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
            insertFrame.dispose();
        });
        insertPanel.add(saveButton);

        // adds the cancel button which just exits the window
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setSize(200, 100);
        cancelButton.setBounds(180, 20 + (labelNames.length * 30), 80, 25);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(e -> {
            insertFrame.dispose();
        });
        insertPanel.add(cancelButton);
        insertFrame.setVisible(true);
    }

}