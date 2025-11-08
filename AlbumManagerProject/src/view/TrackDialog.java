package view;

import model.Track;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 * Dialog for adding or editing individual tracks.
 * This is primarily used by the 'TRACKER' role.
 */
public class TrackDialog extends JDialog {
    private JTextField trackNumberField;
    private JTextField trackTitleField;
    private JCheckBox favoriteCheckBox; // NEW CHECKBOX
    private JButton saveButton;
    private JButton cancelButton;
    private Track track;
    private boolean saved = false;
    /**
     * Constructor for the Track Dialog.
     * @param owner The parent frame/dialog.
     * @param albumTitle The title of the album the track belongs to, for display in the title bar.
     * @param trackToEdit The Track object to edit (null for a new track).
     */
    public TrackDialog(Window owner, String albumTitle, Track trackToEdit) {
        super(owner, trackToEdit == null ? "Add Track to " + albumTitle : "Edit Track for " + albumTitle, ModalityType.DOCUMENT_MODAL);
        this.track = (trackToEdit == null) ? new Track() : trackToEdit;

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(15, 15));

        // --- Styling Constants ---
        Color primaryColor = new Color(52, 152, 219); // Blue
        Color secondaryColor = new Color(236, 240, 241); // Light grey
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // 3 rows for number, title, and favorite checkbox
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        formPanel.setBackground(secondaryColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Initialize fields with existing data if editing
        trackNumberField = new JTextField(track.getTrackNumber() > 0 ? String.valueOf(track.getTrackNumber()) : "");
        trackTitleField = new JTextField(track.getTrackTitle());
        favoriteCheckBox = new JCheckBox("Mark as Favorite", track.isFavorite()); // Initialize favorite checkbox

        // Style Text Fields
        JTextField[] fields = {trackNumberField, trackTitleField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, primaryColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
        // Style CheckBox
        favoriteCheckBox.setBackground(secondaryColor);
        favoriteCheckBox.setFont(labelFont);
        favoriteCheckBox.setForeground(primaryColor);
        
        // Initialize Labels
        JLabel numberLabel = new JLabel("Track Number:");
        JLabel titleLabel = new JLabel("Track Title:");
        numberLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);

        // Add components to form panel
        formPanel.add(numberLabel);
        formPanel.add(trackNumberField);
        formPanel.add(titleLabel);
        formPanel.add(trackTitleField);
        formPanel.add(new JLabel("")); // Empty cell for alignment
        formPanel.add(favoriteCheckBox); // Add favorite checkbox
        
        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(secondaryColor);
        saveButton = new JButton(trackToEdit == null ? "Save Track" : "Update Track");
        cancelButton = new JButton("Cancel");
        // Style Buttons
        saveButton.setFont(labelFont);
        saveButton.setBackground(primaryColor);
        saveButton.setForeground(Color.WHITE);

        cancelButton.setFont(labelFont);
        cancelButton.setBackground(secondaryColor);
        cancelButton.setForeground(primaryColor);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        // Add Listeners
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());
    }
    private void onSave() {
        try {
            if (trackNumberField.getText().trim().isEmpty() || trackTitleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Track Number and Title are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Input Validation
            int number = Integer.parseInt(trackNumberField.getText().trim());

            track.setTrackNumber(number);
            track.setTrackTitle(trackTitleField.getText().trim());
            track.setFavorite(favoriteCheckBox.isSelected()); // Set favorite status

            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the track number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Track getTrack() { return this.track; }
    public boolean wasSaved() { return this.saved; }

    public void addSaveButtonListener(ActionListener listener) { saveButton.addActionListener(listener); }
}