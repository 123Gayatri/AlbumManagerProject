package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap; // Ensures charts are drawn in a consistent order

/**
 * Dialog to display the Analytics Dashboard, featuring charts for albums by genre
 * and albums added per year.
 */
public class AnalyticsDialog extends JDialog {

    private Map<String, Integer> albumsByGenre;
    private Map<Integer, Integer> albumsByYear;

    // Define Color/Font Constants
    private final Color PRIMARY_COLOR = new Color(44, 62, 80); // Dark Blue
    private final Color SECONDARY_COLOR = new Color(236, 240, 241); // Light Grey
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);

    /**
     * Correct constructor with the required parameters.
     */
    public AnalyticsDialog(Frame owner, Map<String, Integer> albumsByGenre, Map<Integer, Integer> albumsByYear) {
        super(owner, "Analytics Dashboard", true);
        this.albumsByGenre = albumsByGenre;
        this.albumsByYear = albumsByYear;

        setSize(900, 600);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(1, 2, 20, 20)); // Two panels side-by-side
        getContentPane().setBackground(SECONDARY_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Genre Pie Chart Panel
        JPanel genrePanel = new JPanel(new BorderLayout());
        genrePanel.setBackground(Color.WHITE);
        JLabel genreTitle = new JLabel("Albums by Genre (Pie Chart)", SwingConstants.CENTER);
        genreTitle.setFont(TITLE_FONT);
        genreTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        genrePanel.add(genreTitle, BorderLayout.NORTH);
        genrePanel.add(new PieChartPanel(albumsByGenre), BorderLayout.CENTER);
        add(genrePanel);

        // 2. Year Bar Chart Panel
        JPanel yearPanel = new JPanel(new BorderLayout());
        yearPanel.setBackground(Color.WHITE);
        JLabel yearTitle = new JLabel("Albums Added Per Year (Bar Chart)", SwingConstants.CENTER);
        yearTitle.setFont(TITLE_FONT);
        yearTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        yearPanel.add(yearTitle, BorderLayout.NORTH);
        yearPanel.add(new BarChartPanel(albumsByYear), BorderLayout.CENTER);
        add(yearPanel);
    }
}

// --- Custom Components for Charts ---

/**
 * Custom JPanel for drawing a Pie Chart based on genre distribution.
 */
class PieChartPanel extends JPanel {
    private Map<String, Integer> data;
    private final Color[] COLORS = {new Color(52, 152, 219), new Color(46, 204, 113), new Color(241, 196, 15), new Color(230, 126, 34), new Color(155, 89, 182), new Color(26, 188, 156)};

    public PieChartPanel(Map<String, Integer> data) {
        this.data = new TreeMap<>(data); // Use TreeMap to ensure consistent ordering for slices
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data.isEmpty() || data.values().stream().mapToInt(Integer::intValue).sum() == 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        int diameter = Math.min(getWidth(), getHeight() * 2 / 3) - 40;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2 - 50;
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int startAngle = 0;
        int colorIndex = 0;
        int legendX = x + 10;
        int legendY = y + diameter + 30;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double ratio = (double) entry.getValue() / total;
            int angle = (int) (ratio * 360);
            
            Color color = COLORS[colorIndex % COLORS.length];
            g2.setColor(color);
            g2.fillArc(x, y, diameter, diameter, startAngle, angle);

            // Draw Legend
            g2.fillRect(legendX, legendY, 15, 15);
            g2.setColor(Color.BLACK);
            String legendText = String.format("%s (%d, %.1f%%)", entry.getKey(), entry.getValue(), ratio * 100);
            g2.drawString(legendText, legendX + 30, legendY + 13);
            
            legendY += 20;
            startAngle += angle;
            colorIndex++;
            
            // Basic overflow check for legend, move to next column if necessary
            if (legendY > getHeight() - 20) {
                legendY = y + diameter + 30;
                legendX += (getWidth() / 2);
            }
        }
        // Draw the last slice to close the circle (due to potential rounding errors)
        if (startAngle < 360) {
            g2.setColor(COLORS[colorIndex % COLORS.length]);
            g2.fillArc(x, y, diameter, diameter, startAngle, 360 - startAngle);
        }
    }
}

/**
 * Custom JPanel for drawing a Bar Chart based on albums added per year.
 */
class BarChartPanel extends JPanel {
    private Map<Integer, Integer> data;
    private final Color BAR_COLOR = new Color(52, 152, 219);

    public BarChartPanel(Map<Integer, Integer> data) {
        this.data = new TreeMap<>(data); // Use TreeMap to ensure ordering by year
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data.isEmpty() || data.values().stream().mapToInt(Integer::intValue).sum() == 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
            return;
        }

        int padding = 40;
        int labelPadding = 20;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding - labelPadding;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        
        List<Integer> years = new ArrayList<>(data.keySet());
        // CORRECTED LINE: Removed redundant 'new'
        List<Integer> counts = new ArrayList<>(data.values()); 
        int maxCount = counts.stream().mapToInt(Integer::intValue).max().orElse(1);
        
        int numBars = years.size();
        int barSpacing = 10;
        int barWidth = (chartWidth - barSpacing * (numBars + 1)) / numBars;
        
        if (barWidth < 5) { // Minimum bar width check
            barWidth = 5;
        }
        
        // Draw Y-Axis (Vertical Line)
        g2.drawLine(padding, padding, padding, getHeight() - padding - labelPadding);
        
        // Draw X-Axis (Horizontal Line)
        g2.drawLine(padding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        
        // Draw Bars
        int currentX = padding + barSpacing;
        for (int i = 0; i < numBars; i++) {
            int count = counts.get(i);
            double heightRatio = (double) count / maxCount;
            int barHeight = (int) (heightRatio * chartHeight);
            
            int y = getHeight() - padding - labelPadding - barHeight;
            
            g2.setColor(BAR_COLOR);
            g2.fillRect(currentX, y, barWidth, barHeight);
            
            // Draw Count Label on top of the bar
            g2.setColor(Color.BLACK);
            String countStr = String.valueOf(count);
            g2.drawString(countStr, currentX + barWidth / 2 - g2.getFontMetrics().stringWidth(countStr) / 2, y - 5);
            
            // Draw Year Label below the axis
            String yearLabel = String.valueOf(years.get(i));
            g2.drawString(yearLabel, currentX + barWidth / 2 - g2.getFontMetrics().stringWidth(yearLabel) / 2, getHeight() - padding + 5);
            
            currentX += barWidth + barSpacing;
        }

        // Draw Y-Axis labels (Max and 0)
        String maxCountStr = String.valueOf(maxCount);
        g2.drawString(maxCountStr, padding - g2.getFontMetrics().stringWidth(maxCountStr) - 5, padding + 10);
        g2.drawString("0", padding - g2.getFontMetrics().stringWidth("0") - 5, getHeight() - padding - labelPadding + 5);
    }
}