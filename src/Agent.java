import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Agent {
    private int x, y, width, height, speed;
    private int lifespan;
    private int currentStep;
    private boolean isAlive;
    private HashMap<Integer, Integer> previousSteps;
    private HashMap<Integer, Integer> currentSteps;
    private Random r;
    private int randomness;
    private int generation;
    private Color color;

    private int goalX, goalY, goalWidth, goalHeight;
    private float distanceToGoal;
    private boolean isFinished;

    public Agent() {
        this.x = 0;
        this.y = 0;
        this.width = 20;
        this.height = 20;
        this.speed = 3;
        this.lifespan = 100;
        this.currentStep = 0;
        isAlive = true;
        previousSteps = new HashMap<>();
        currentSteps = new HashMap<>();
        r = new Random();
        randomness = 25;
        generation = 0;
        distanceToGoal = 99999;
        isFinished = false;
        color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }

    public Agent(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Agent(int x, int y, int lifespan) {
        this(x, y);
        this.lifespan = lifespan;
    }

    public void evolve(HashMap<Integer, Integer> bestSteps) {
        generation++;
        previousSteps = bestSteps;
        currentSteps = new HashMap<>();
        currentStep = 0;
        isAlive = true;
    }

    int calculateNextStep() {
        if(generation == 0) {
            return r.nextInt(4);
        }

        if(r.nextInt(100) <= randomness) {
            return r.nextInt(4);
        } else {
            try {
                return previousSteps.get(currentStep);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return r.nextInt(4);
            }
        }
    }

    void moveNextStep() {
        if(!isAlive) return;
        int nextStep = calculateNextStep();
        switch (nextStep) {
            case 0: x -= 1*speed;
            currentSteps.put(currentStep, 0);
                break;
            case 1: x += 1*speed;
                currentSteps.put(currentStep, 1);
                break;
            case 2: y -= 1*speed;
                currentSteps.put(currentStep, 2);
                break;
            case 3: y += 1*speed;
                currentSteps.put(currentStep, 3);
                break;
            default:
                System.out.println("not moving");
                break;
        }
        currentStep++;
        if (currentStep >= lifespan || isOverlappingGoal()) {
            isAlive = false;
            if (isOverlappingGoal()) {
                isFinished = true;
            }
        }
    }

    public void setGoal(int x, int y, int w, int h) {
        this.goalX = x;
        this.goalY = y;
        this.goalWidth = w;
        this.goalHeight = h;
    }

    public float distanceToGoal() {
        double deltaX = goalX+goalWidth/2 - x+width/2;
        double deltaY = goalY+goalHeight/2 - y+height/2;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        distanceToGoal = (float)distance;
        return distanceToGoal;
    }

    public boolean isOverlappingGoal() {
        boolean overlapX = (x < goalX + goalWidth) && (x + width > goalX);
        boolean overlapY = (y < goalY + goalHeight) && (y + height > goalY);

        return overlapX && overlapY;
    }

    boolean isAlive() {
        return isAlive;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRandomness(int rdm) {
        this.randomness = rdm;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color col) {
        int range = 50;
        int red = col.getRed() + r.nextInt(-range, range);
        int green = col.getGreen() + r.nextInt(-range, range);
        int blue = col.getBlue() + r.nextInt(-range, range);

        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        this.color = new Color(red, green, blue);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public HashMap<Integer, Integer> getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(HashMap<Integer, Integer> currentSteps) {
        this.currentSteps = currentSteps;
    }
}
