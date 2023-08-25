import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HeatmapDisplay extends JFrame {
    private JLabel heatmapLabel;
    public static int cunt = 5;

    public HeatmapDisplay(int width, int height) {
        super("Heatmap Display");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color darkBackgroundColor = new Color(30, 30, 30);
        getContentPane().setBackground(darkBackgroundColor);

        heatmapLabel = new JLabel();
        heatmapLabel.setBackground(darkBackgroundColor);
        heatmapLabel.setOpaque(true);
        heatmapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(heatmapLabel);

        updateHeatmap();
    }

    public void updateHeatmap() {
        try {
            BufferedImage heatmapImage = ImageIO.read(new File("agent_heatmap.png"));
            heatmapLabel.setIcon(new ImageIcon(heatmapImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HeatmapDisplay heatmapDisplay = new HeatmapDisplay(600, 600);
            heatmapDisplay.setVisible(true);

            Timer timer = new Timer(250, e -> heatmapDisplay.updateHeatmap());
            timer.start();
        });
    }
}
