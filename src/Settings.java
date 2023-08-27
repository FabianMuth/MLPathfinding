import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings extends JPanel {
    private AgentDrawer agentDrawer;
    private int maxGenerations = 500;
    private int agentCount = 150;
    private int agentLifespan = 200;
    private int simulationSpeed = 50;
    private int agentSpeed = 3;
    private int randomness = 25;
    private float heatMapFalloff = 0.95f;
    private int bestAgentCount = 2;
    private boolean displayHeatmap = true;

    public Settings(AgentDrawer agentDrawer) {
        this.agentDrawer = agentDrawer;

        setLayout(new GridLayout(12, 1));

        addSliderWithLabel("Max Generations:", 1, 500, maxGenerations);
        addSliderWithLabel("Agent Count:", 1, 300, agentCount);
        addSliderWithLabel("Agent Lifespan:", 1, 300, agentLifespan);
        addSliderWithLabel("Simulation Speed:", 1, 250, simulationSpeed);
        addSliderWithLabel("Agent Speed:", 1, 10, agentSpeed);
        addSliderWithLabel("Randomness:", 0, 100, randomness);
        addSliderWithLabel("Heatmap Falloff:", 1, 100, 95);
        addSliderWithLabel("Best Agent Count:", 1, 10, bestAgentCount);

        JCheckBox displayHeatmapCheckbox = new JCheckBox("Display Heatmap");
        displayHeatmapCheckbox.setSelected(displayHeatmap);
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

                if (label.equals("Max Generations:")) {
                    maxGenerations = newValue;
                } else if (label.equals("Agent Count:")) {
                    agentCount = newValue;
                } else if (label.equals("Agent Lifespan:")) {
                    agentLifespan = newValue;
                } else if (label.equals("Simulation Speed:")) {
                    simulationSpeed = newValue;
                    agentDrawer.timer.setDelay(simulationSpeed);
                } else if (label.equals("Agent Speed:")) {
                    agentSpeed = newValue;
                    agentDrawer.setAgentSpeed();
                } else if (label.equals("Randomness:")) {
                    randomness = newValue;
                    agentDrawer.setAgentRandomness();
                } else if (label.equals("Heatmap Falloff:")) {
                    heatMapFalloff = newValue/100.0f;
                } else if (label.equals("Best Agent Count:")) {
                    bestAgentCount = newValue;
                }
            }
        });

        sliderPanel.add(valueLabel, BorderLayout.WEST);
        sliderPanel.add(slider, BorderLayout.CENTER);
        add(sliderPanel);
    }

    private String formatValue(int value) {
        return String.format("%03d", value);
    }

    public int getMaxGenerations() {
        return maxGenerations;
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

    public float getHeatMapFalloff() {
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
            JFrame frame = new JFrame("Settings");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Settings settingsPanel = new Settings(new AgentDrawer());
            frame.add(settingsPanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
