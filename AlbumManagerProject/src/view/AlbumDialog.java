package view;

import model.Album;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class AlbumDialog extends JDialog {
    private JTextField artistField, titleField, yearField, genreField;
    private JCheckBox inCollectionCheckBox;
    private JButton saveButton, cancelButton, selectArtButton;
    private JLabel coverArtPathLabel;
    private String coverArtPath;
    private Album album;
    private boolean saved = false;

    public AlbumDialog(Frame owner, String dialogTitle, Album albumToEdit) {
        super(owner, dialogTitle, true);
        this.album = (albumToEdit == null) ? new Album() : albumToEdit;
        this.coverArtPath = this.album.getCoverArtPath();

        // --- CUSTOM STYLES (Matching MainFrame Theme) ---
        Color primaryColor = new Color(44, 62, 80); // Dark Blue (for button text, borders, headers)
        Color secondaryColor = new Color(236, 240, 241); // Light Grey (for dialog background)
        Color accentColor = new Color(52, 152, 219); // Bright Blue (for accents/primary action)
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(secondaryColor);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(secondaryColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        artistField = new JTextField(album.getArtistName());
        titleField = new JTextField(album.getTitle());
        yearField = new JTextField(album.getReleaseYear() > 0 ? String.valueOf(album.getReleaseYear()) : "");
        genreField = new JTextField(album.getGenre());
        inCollectionCheckBox = new JCheckBox("In My Collection", album.isInCollection());
        
        // Apply theme to CheckBox
        inCollectionCheckBox.setBackground(secondaryColor);
        inCollectionCheckBox.setFont(labelFont);
        inCollectionCheckBox.setForeground(primaryColor);
        
        // Style Text Fields
        JTextField[] fields = {artistField, titleField, yearField, genreField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                // Use bright blue accent color for the bottom border
                new MatteBorder(0, 0, 2, 0, accentColor), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }

        // Setup Labels
        JLabel artistLabel = new JLabel("Artist Name:");
        JLabel titleLabel = new JLabel("Album Title:");
        JLabel yearLabel = new JLabel("Release Year:");
        JLabel genreLabel = new JLabel("Genre:");
        JLabel coverArtLabel = new JLabel("Cover Art:");
        
        // Apply theme to Labels
        JLabel[] labels = {artistLabel, titleLabel, yearLabel, genreLabel, coverArtLabel};
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setForeground(primaryColor);
        }

        selectArtButton = new JButton("Choose Image...");
        coverArtPathLabel = new JLabel(coverArtPath != null && !coverArtPath.isEmpty() ? new File(coverArtPath).getName() : "No image selected.");
        coverArtPathLabel.setFont(fieldFont);
        coverArtPathLabel.setForeground(primaryColor);

        // GridBagLayout configuration
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(artistLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; formPanel.add(artistField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; formPanel.add(titleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; formPanel.add(yearLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; formPanel.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; formPanel.add(genreLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; formPanel.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; formPanel.add(coverArtLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 1; formPanel.add(selectArtButton, gbc);
        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 1; formPanel.add(coverArtPathLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2; formPanel.add(inCollectionCheckBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(secondaryColor);
        
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        selectArtButton.setFont(labelFont);

        // Style Save Button (Primary Action)
        saveButton.setFont(labelFont);
        saveButton.setBackground(accentColor);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // Style Cancel Button (Secondary Action)
        cancelButton.setFont(labelFont);
        cancelButton.setBackground(secondaryColor);
        cancelButton.setForeground(primaryColor);
        cancelButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // Style Choose Image Button
        selectArtButton.setBackground(primaryColor);
        selectArtButton.setForeground(Color.WHITE);
        selectArtButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        selectArtButton.addActionListener(e -> onSelectArt());
        saveButton.addActionListener(e -> onSave());
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(owner);
    }

    private void onSelectArt() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            coverArtPath = selectedFile.getAbsolutePath();
            coverArtPathLabel.setText(selectedFile.getName());
        }
    }

    private void onSave() {
        try {
            if (artistField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() || yearField.getText().trim().isEmpty() || genreField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            album.setArtistName(artistField.getText().trim());
            album.setTitle(titleField.getText().trim());
            album.setReleaseYear(Integer.parseInt(yearField.getText().trim()));
            album.setGenre(genreField.getText().trim());
            album.setInCollection(inCollectionCheckBox.isSelected());
            album.setCoverArtPath(coverArtPath);
            saved = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the year.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Album getAlbum() { return this.album; }
    public boolean wasSaved() { return this.saved; }
}
