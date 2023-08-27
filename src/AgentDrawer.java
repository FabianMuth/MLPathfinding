import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AgentDrawer extends JFrame {
    Settings settings;
    JPanel mainDrawingPanel;
    int mainDrawingPanelWidth, mainDrawingPanelHeight;
    ArrayList<Agent> agents;
    static int currentStep = 0;
    Timer timer;
    int currentGeneration = 1;
    float bestDistance = 9999;
    ArrayList<Agent> bestAgents = new ArrayList<>();
    HashMap<Integer, Integer> bestSteps;
    int goalX, goalY, goalWidth, goalHeight;
    BufferedImage heatMapImage;
    HeatmapDisplay heatmapDisplay;

    public AgentDrawer() {
        super("MLPathfinding");

        mainDrawingPanelWidth = 600;
        mainDrawingPanelHeight = 600;

        getContentPane().setBackground(Color.WHITE);
        setSize(800, mainDrawingPanelHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        settings = new Settings(this);

        agents = new ArrayList<>();
        bestSteps = new HashMap<>();

        mainDrawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGoal(g);
                drawBest(g);
                drawAgents(g);
            }
        };

        mainDrawingPanel.setPreferredSize(new Dimension(mainDrawingPanelWidth, mainDrawingPanelHeight));
        mainDrawingPanel.setBackground(Color.LIGHT_GRAY);

        goalWidth = mainDrawingPanelWidth/4;
        goalHeight = 30;
        goalX = mainDrawingPanelWidth/2-mainDrawingPanelWidth/4/2;
        goalY = mainDrawingPanelHeight - goalHeight;


        heatMapImage = new BufferedImage(mainDrawingPanelWidth,mainDrawingPanelHeight, BufferedImage.TYPE_INT_ARGB);
        heatmapDisplay = new HeatmapDisplay(mainDrawingPanelWidth, mainDrawingPanelHeight);
        heatmapDisplay.setVisible(true);

        add(mainDrawingPanel, BorderLayout.CENTER);
        add(settings, BorderLayout.WEST);
        pack();
    }

    public void updateAgentsPos() {
        for (Agent a : agents) {
            a.moveNextStep();
        }
    }

    public void setAgentsGoal() {
        for (Agent a : agents) {
            a.setGoal(goalX, goalY, goalWidth, goalHeight);
        }
    }

    public void saveHeatMap() {
        if(!settings.isDisplayHeatmap()) return;
        try {
            ImageIO.write(heatMapImage, "PNG", new File("agent_heatmap.png"));
            decreaseOpacity(heatMapImage, settings.getHeatMapFalloff());
            heatmapDisplay.updateHeatmap();
            System.out.println("saving heatmap");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decreaseOpacity(BufferedImage image, float opacityFactor) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int newAlpha = Math.max(0, (int) (alpha * opacityFactor));
                int newRGB = (rgb & 0xFFFFFF) | (newAlpha << 24);
                image.setRGB(x, y, newRGB);
            }
        }
    }

    public void drawAgents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D g2dHeatmap = heatMapImage.createGraphics();
        for (Agent a : agents) {
            g2d.setColor(a.getColor());
            g2d.fillRect(a.getX(), a.getY(), 20, 20);
            //g2dHeatmap.setColor(new Color(255, 95, 30, 10));
            //g2dHeatmap.setColor(new Color(255,255,255, 10));
            g2dHeatmap.setColor(new Color(a.getColor().getRed(),a.getColor().getGreen(),a.getColor().getBlue(), 10));
            g2dHeatmap.fillRect(a.getX() - 2, a.getY() - 2, 4, 4);

            if (a.isFinished()) {
                timer.stop();
                saveHeatMap();
                break;
            }
        }
    }

    public void drawBest(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        int h = (int) (mainDrawingPanel.getHeight() - bestDistance + goalHeight/2 + 10);
        g2d.drawLine(0, h, mainDrawingPanel.getWidth(), h);
    }

    public void drawGoal(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0,255,50));
        g2d.fillRect(goalX, goalY, goalWidth, goalHeight);
    }

    /*public void paint(Graphics g) {
        super.paint(g);
        drawGoal(g);
        drawBest(g);
        drawAgents(g);
    }*/

    public void initAgents() {
        for(int i = 0; i < settings.getAgentCount(); i++) {
            agents.add(new Agent(0,0, settings.getAgentLifespan()));
        }
        setAgentsGoal();
    }

    public void evolveAgents() {
        int batchSize = agents.size() / settings.getBestAgentCount();

        for (int i = 0; i < settings.getBestAgentCount(); i++) {
            Agent bestAgent = bestAgents.get(i);

            int startIndex = i * batchSize;
            int endIndex = Math.min(startIndex + batchSize, agents.size());

            for (int j = startIndex; j < endIndex; j++) {
                agents.get(j).evolve(bestAgent.getCurrentSteps());
                agents.get(j).setColor(bestAgent.getColor());
            }
        }
    }

    private void evaluateBestAgent() {
        for (Agent a : agents) {
            if(a.distanceToGoal() <= bestDistance) {
                System.out.println(a.distanceToGoal());
                bestDistance = a.distanceToGoal();
                bestSteps = a.getCurrentSteps();
            }
        }
        System.out.println("Best dist: " + bestDistance);
    }

    private void evaluateBestAgents() {
        for(Agent a : agents) {
            if(a.distanceToGoal() <= bestDistance) {
                System.out.println(a.distanceToGoal());
                bestDistance = a.distanceToGoal();
            }
            if (bestAgents.size() < settings.getBestAgentCount()) {
                bestAgents.add(a);
            } else {
                float currentDist = a.distanceToGoal();

                Iterator<Agent> bestIterator = bestAgents.iterator();
                while (bestIterator.hasNext()) {
                    Agent ba = bestIterator.next();
                    if (currentDist < ba.distanceToGoal()) {
                        bestIterator.remove();
                        bestAgents.add(a);
                        break;
                    }
                }
            }
        }
    }

    public void setAgentSpeed() {
        int agentSpeed = settings.getAgentSpeed();
        for (Agent a : agents) {
            a.setSpeed(agentSpeed);
        }
    }

    public void setAgentRandomness() {
        int randomnessValue = settings.getRandomness();
        for (Agent a : agents) {
            a.setRandomness(randomnessValue);
        }
    }

    public void setAgentsPos(int x, int y) {
        for (Agent a : agents) {
            a.setX(x);
            a.setY(y);
        }
    }

    public static void main(String[] args) {
        AgentDrawer ag = new AgentDrawer();
        SwingUtilities.invokeLater(() -> {
            ag.setVisible(true);
            ag.initAgents();
            ag.setAgentsPos(ag.mainDrawingPanel.getWidth() / 2, 200);

            ActionListener timerListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ag.updateAgentsPos();
                    ag.repaint();
                    currentStep++;
                    if (currentStep >= ag.settings.getAgentLifespan()) {
                        ag.timer.stop();
                        ag.evaluateBestAgents();
                        ag.evolveAgents();
                        ag.setAgentsPos(ag.mainDrawingPanel.getWidth() / 2, 200);
                        ag.saveHeatMap();

                        if (ag.currentGeneration < ag.settings.getMaxGenerations()) {
                            ag.currentGeneration++;
                            currentStep = 0;
                            ag.timer.start();
                        }
                    }
                }
            };

            ag.timer = new Timer(ag.settings.getSimulationSpeed(), timerListener);
            ag.timer.start();
        });
    }
}
