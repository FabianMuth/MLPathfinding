import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings extends JPanel {
    private int agentCount = 100;
    private int agentLifespan = 200;
    private int simulationSpeed = 50;
    private int agentSpeed = 3;
    private int randomness = 25;
    private int heatMapFalloff = 50;
    private int bestAgentCount = 2;
    private boolean displayHeatmap = false;

    public Settings() {
        setLayout(new GridLayout(11, 1));

        addSliderWithLabel("Agent Count:", 1, 300, agentCount);
        addSliderWithLabel("Agent Lifespan:", 1, 300, agentLifespan);
        addSliderWithLabel("Simulation Speed:", 1, 250, simulationSpeed);
        addSliderWithLabel("Agent Speed:", 1, 10, agentSpeed);
        addSliderWithLabel("Randomness:", 0, 100, randomness);
        addSliderWithLabel("Heatmap Falloff:", 1, 100, heatMapFalloff);
        addSliderWithLabel("Best Agent Count:", 1, 10, bestAgentCount);

        JCheckBox displayHeatmapCheckbox = new JCheckBox("Display Heatmap");
        displayHeatmapCheckbox.addActionListener(e -> {
            displayHeatmap = displayHeatmapCheckbox.isSelected();
        });

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            System.out.println("Start button clicked");
        });

        add(displayHeatmapCheckbox);
        add(new JLabel());
        add(startButton);
    }

    private void addSliderWithLabel(String label, int min, int max, int initialValue) {
        JPanel sliderPanel = new JPanel(new BorderLayout());

        JLabel valueLabel = new JLabel(formatValue(initialValue));
        JSlider slider = new JSlider(min, max, initialValue);
        slider.setOrientation(JSlider.HORIZONTAL);
        slider.setBorder(BorderFactory.createTitledBorder(label));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int newValue = slider.getValue();
                valueLabel.setText(formatValue(newValue));
            }
        });

        sliderPanel.add(valueLabel, BorderLayout.WEST);
        sliderPanel.add(slider, BorderLayout.CENTER);
        add(sliderPanel);
    }

    private String formatValue(int value) {
        return String.format("%03d", value);
    }

    public int getAgentCount() {
        return agentCount;
    }

    public int getAgentLifespan() {
        return agentLifespan;
    }

    public int getSimulationSpeed() {
        return simulationSpeed;
    }

    public int getAgentSpeed() {
        return agentSpeed;
    }

    public int getRandomness() {
        return randomness;
    }

    public int getHeatMapFalloff() {
        return heatMapFalloff;
    }

    public int getBestAgentCount() {
        return bestAgentCount;
    }

    public boolean isDisplayHeatmap() {
        return displayHeatmap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Settings Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Settings settingsPanel = new Settings();
            frame.add(settingsPanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
