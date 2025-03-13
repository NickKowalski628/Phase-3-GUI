import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * Nicholas Kowalski
 * CEN3024
 * 12Mar2025
 * Class:UFCAppGUI
 * Purpose: This class creates the GUI for the UFC Database that lets users interact with it
 * through a series of buttons and text-boxes that record their input and store it within the
 * fighters list.
 *
 */
public class UFCAppGUI extends JFrame {
    private JLabel UFCDatabase;
    private JPanel FighterGUI;
    private JTable fighterTable;
    private JButton btnLoad, btnAddFighter, btnDisplayAll, btnUpdateInfo, btnRemoveFighter,
            btnPercentSort, btnExit;
    private ArrayList<FighterData> fighters = new ArrayList<>();
    private DefaultTableModel tableModel;

    public UFCAppGUI() {
        setContentPane(FighterGUI);
        setTitle("UFC Fighter Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set resolution to 1200x600 to accommdate for the amount of fields and data present in table
        setBounds(100, 100, 1200, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //Initialize the JTable with columns
        String[] columnNames = {"ID", "Name", "Alias", "Weight Class", "Stance", "Height", "Reach",
                "Wins", "Losses", "Draws", "No Contest", "Win %"};
        tableModel = new DefaultTableModel(columnNames, 0) {

            /*
             * Method: isCellEditable
             * Parameters: int row, int column
             * Returns: boolean
             * Purpose: This method ensures that users cannot edit text on the table that is presented in the GUI
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fighterTable.setModel(tableModel);

        //Removed padding to ensure button spacing is correct
        btnLoad.setMargin(new java.awt.Insets(0,0,0,0));
        btnAddFighter.setMargin(new java.awt.Insets(0,0,0,0));
        btnDisplayAll.setMargin(new java.awt.Insets(0,0,0,0));
        btnUpdateInfo.setMargin(new java.awt.Insets(0,0,0,0));
        btnPercentSort.setMargin(new java.awt.Insets(0,0,0,0));
        btnRemoveFighter.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnExit.setMargin(new java.awt.Insets(0, 0, 0, 0));


        //Button listeners
        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFighterData();
            }
        });


        btnAddFighter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            AddFighterDialog dialog = new AddFighterDialog(UFCAppGUI.this, fighters);
                    dialog.setVisible(true);
                    if (dialog.isAdded()){
                        fighters.add(dialog.getFighter());
                        updateTable();

                    }
            }
        });

        btnDisplayAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });


        btnUpdateInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = fighterTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UFCAppGUI.this, "Please select a fighter to update");
                    return;
                }
                FighterData fighter = fighters.get(row);
                UpdateFighterDialog dialog = new UpdateFighterDialog(UFCAppGUI.this, fighter);
                dialog.setVisible(true);
                updateTable();
            }
        });



        btnRemoveFighter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = fighterTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UFCAppGUI.this, "Select a fighter to remove");
                    return;
                }
                int confirmation = JOptionPane.showConfirmDialog(UFCAppGUI.this, "Are you sure you'd like to remove this fighter?");
                if (confirmation == JOptionPane.YES_OPTION) {
                    fighters.remove(row);
                    updateTable();
                }
            }
        });

        btnPercentSort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Collections.sort(fighters, new Comparator<FighterData>() {
                    @Override
                    public int compare(FighterData f1, FighterData f2) {
                        double winPercentage1 = f1.getWinPercentage();
                        double winPercentage2 = f2.getWinPercentage();
                        return Double.compare(winPercentage2, winPercentage1);
                    }
                });
                updateTable();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    /*
     * Method: loadFighterData
     * Parameters: none
     * Return: none
     * Purpose: This method allows the user to select a .txt, and csv file to load fighter data using the fileChooser
     * to load into the application. fileChooser was implemented to streamline the GUI process
     */
    private void loadFighterData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt", "csv"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.exists() || !file.isFile()) {
                JOptionPane.showMessageDialog(this, "Invalid file. Please try again.");
                return;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    FighterData fighter = new FighterData (0, "", "", "",
                            "", 0, 0, 0, 0, 0, 0);
                    fighter.readFromFile(line);
                    fighters.add(fighter);
                }
                JOptionPane.showMessageDialog(this, "Successfully loaded fighter data.");
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid file format, please try again.");
            }catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file.");
            }
        }
    }

    /*
     * Method: updateTable
     * Parameters: none
     * Return: none
     * Purpose: This method displays the entire fighters list while also including their win percentage.
     * This method also handles division by zero by displaying a message if a fighter has 0 fights rather than
     * causing an error.
     */
    private void updateTable() {
        tableModel.setRowCount(0);
        if (fighters.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No fighters found.");
            return;
        }
        for (FighterData fighter : fighters) {
            double winPercentage = 0;
            int totalFights = fighter.getWins() + fighter.getLosses() + fighter.getDraws();
            if (totalFights != 0) {
                winPercentage = ((double) fighter.getWins() / totalFights) * 100;
            }
            String winPercentageStr = (totalFights == 0) ? "No fights Recorded" : String.format("%.2f%%", fighter.getWinPercentage());
            Object[] row = {
                    fighter.getFighterID(),
                    fighter.getFighterName(),
                    fighter.getAlias(),
                    fighter.getWeightClass(),
                    fighter.getFightingStance(),
                    String.format("%.0f", fighter.getFighterHeight()),
                    String.format("%.0f", fighter.getFighterReach()),
                    fighter.getWins(),
                    fighter.getLosses(),
                    fighter.getDraws(),
                    fighter.getNoContest(),
                    winPercentageStr
            };
            tableModel.addRow(row);
        }

    }

    /*
     * Method: main
     * Parameters: None
     * Returns: None
     * This method launches the GUI to allow the user to interact with the database.
     */

    public static void main(String[] args) {
        new UFCAppGUI();
    }

}
