import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * Nicholas Kowalski
 * CEN3024
 * 12Mar2025
 * Class: AddFighterDialog
 * Purpose: This class allows users to enter fighter data manually through a series of text boxes.
 * It also holds the methods necessary to store and verify data was entered properly.
 */

public class AddFighterDialog extends JDialog {
    private JTextField idField, nameField, aliasField, weightClassField, stanceField,
            heightField, reachField, winsField, lossesField, drawsField, noContestField;
    private boolean isAdded = false;
    private FighterData fighter;
    private ArrayList<FighterData> fighters;



    public AddFighterDialog(JFrame parent, ArrayList<FighterData> fighters) {
        super(parent, "Add Fighter", true);
        this.fighters = fighters;
        setLayout(new GridLayout(13, 2));
        setSize(400, 500);

        add (new JLabel("Enter 4-Digit fighter ID: "));
        idField = new JTextField();
        add(idField);
        add (new JLabel("Fighter Name: "));
        nameField = new JTextField();
        add(nameField);
        add (new JLabel("Fighter Alias: "));
        aliasField = new JTextField();
        add(aliasField);
        add (new JLabel("Fighter WeightClass: "));
        weightClassField = new JTextField();
        add(weightClassField);
        add (new JLabel("Fighting Stance: "));
        stanceField = new JTextField();
        add(stanceField);
        add (new JLabel("Fighter Height (inches): "));
        heightField = new JTextField();
        add(heightField);
        add (new JLabel("Fighter Reach (inches): "));
        reachField = new JTextField();
        add(reachField);
        add (new JLabel("Enter Wins: "));
        winsField = new JTextField();
        add(winsField);
        add (new JLabel("Enter Losses: "));
        lossesField = new JTextField();
        add(lossesField);
        add (new JLabel("Enter Draws: "));
        drawsField = new JTextField();
        add(drawsField);
        add (new JLabel("Enter No-Contest: "));
        noContestField = new JTextField();
        add(noContestField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFighter();
            }
        });
        add(new JLabel(""));
        add(confirmButton);

        setLocationRelativeTo(parent);

    }

    /*
     * Method: addFighter
     * Parameters: none
     * Return: none
     * Purpose: This method takes user input and from dialog boxes and adds it to the fighters list. This method
     * houses the input validation necessary for proper entries.
     */
    private void addFighter() {
        try {
            String fighterID = idField.getText().trim();
            if (fighterID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fighter ID cannot be empty");
                return;
            }
            if (fighterID.length() != 4 || !fighterID.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(this, "Invalid fighter ID, please enter a 4-digit number");
                return;
            }
            int id = Integer.parseInt(fighterID);
            for (FighterData fighter : fighters) {
                if (fighter.getFighterID() == id) {
                    JOptionPane.showMessageDialog(this, "Fighter already exists");
                    return;
                }
            }
            String fighterName = nameField.getText().trim();
            if (fighterName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fighter name cannot be empty");
                return;
            }
            String fighterAlias = aliasField.getText().trim();
            if (fighterAlias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alias cannot be empty");
                return;
            }
            String fighterWeightclass = weightClassField.getText().trim();
            if (fighterWeightclass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Weight class cannot be empty");
                return;
            }
            String fightingStance = stanceField.getText().trim();
            if (fightingStance.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stance cannot be empty");
                return;
            }
            String heightText = heightField.getText().trim();
            if (heightText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Height cannot be empty");
                return;
            }
            double fighterHeight = Double.parseDouble(heightText);
            if (fighterHeight <= 0) {
                JOptionPane.showMessageDialog(this, "Height must be greater than 0");
                return;
            }
            String reachText = reachField.getText().trim();
            if (reachText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Reach cannot be empty");
                return;
            }
            double fighterReach = Double.parseDouble(reachText);
            if (fighterReach <= 0) {
                JOptionPane.showMessageDialog(this, "Reach must be greater than 0");
                return;
            }
            String winsText = winsField.getText().trim();
            if (winsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wins cannot be empty");
                return;
            }
            int wins = Integer.parseInt(winsText);
            if (wins < 0) {
                JOptionPane.showMessageDialog(this, "Wins cannot be negative");
                return;
            }
            String lossesText = lossesField.getText().trim();
            if (lossesText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Losses cannot be empty");
                return;
            }
            int losses = Integer.parseInt(lossesText);
            if (losses < 0) {
                JOptionPane.showMessageDialog(this, "Losses cannot be negative");
                return;
            }
            String drawsText = drawsField.getText().trim();
            if (drawsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Draws cannot be empty");
                return;
            }
            int draws = Integer.parseInt(drawsText);
            if (draws < 0) {
                JOptionPane.showMessageDialog(this, "Draws cannot be negative");
                return;
            }
            String noContestText = noContestField.getText().trim();
            if (noContestText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No Contests cannot be empty");
                return;
            }
            int noContest = Integer.parseInt(noContestText);
            if (noContest < 0) {
                JOptionPane.showMessageDialog(this, "No Contests cannot be negative");
                return;
            }

            fighter = new FighterData(id, fighterName, fighterAlias, fighterWeightclass, fightingStance, fighterHeight, fighterReach,
                    wins, losses, draws, noContest);
            isAdded = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number/");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }


    /*
     * Method: isAdded
     * Parameters: None
     * Returns: boolean
     * Purpose: This method checks if the fighter was added and when the user presses the add fighter
     * button.
     */
    public boolean isAdded() {
        return isAdded;
    }

    /*
     * Method: getFighter
     * Parameters: None
     * Returns: FighterData
     * Purpose: This method returns the FighterData object created through dialog boxes
     * to the fighters list.This will only occur if input-validation is passed.
     */
    public FighterData getFighter() {
        return fighter;
    }
}
