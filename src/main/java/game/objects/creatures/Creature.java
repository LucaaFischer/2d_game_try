package game.objects.creatures;

import game.Game;
import game.GameMap;
import game.objects.GameObject;

import java.awt.*;

public abstract class Creature extends GameObject {
    protected final Game game;
    protected double centerX;
    protected double centerY;
    protected final double radius;
    protected final double speed;
    protected Color color;

    protected int preferredDirectionX;
    protected int preferredDirectionY;
    protected int movingDirectionX;
    protected int movingDirectionY;

    public Creature(Game game, double centerX, double centerY, double radius, double speed, Color color) {
        this.game = game;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.speed = speed;
        this.color = color;
    }

    private void tickMovingDirection() {
        if (movingDirectionX == 0 && movingDirectionY == 0) {
            movingDirectionX = preferredDirectionX;
            movingDirectionY = preferredDirectionY;
        } else if (movingDirectionX != 0 && preferredDirectionX != 0) {
            movingDirectionX = preferredDirectionX;
        } else if (movingDirectionY != 0 && preferredDirectionY != 0) {
            movingDirectionY = preferredDirectionY;
        }
    }

    private void tickWallCollisions() {
        GameMap map = game.getMap();

        if (movingDirectionX == 1 && !map.isFree((int) (centerX + 0.5), (int) centerY)
                || movingDirectionX == -1 && !map.isFree((int) (centerX - 0.5), (int) centerY)) {
            centerX = (int) centerX + 0.5;
            movingDirectionX = 0;
        } else if (movingDirectionY == 1 && !map.isFree((int) centerX, (int) (centerY + 0.5))
                || movingDirectionY == -1 && !map.isFree((int) centerX, (int) (centerY - 0.5))) {
            centerY = (int) centerY + 0.5;
            movingDirectionY = 0;
        }
    }

    private void tickTurn(boolean crossedCenterX, boolean crossedCenterY) {
        boolean turnXToY = crossedCenterX && movingDirectionX != 0 && preferredDirectionY != 0 && game.getMap().isFree((int) centerX, (int) (centerY + preferredDirectionY));
    }

    public void tick() {
        tickMovingDirection();

        double newX = centerX + movingDirectionX * speed;
        double newY = centerY + movingDirectionY * speed;

        boolean crossedCenterX = Math.abs((centerX - 0.5) % 1.0 - (newX -0.5) % 1.0) > 0.5;
        boolean crossedCenterY = Math.abs((centerY - 0.5) % 1.0 - (newY -0.5) % 1.0) > 0.5;

        centerX = newX;
        centerY = newY;

        tickTurn(crossedCenterX, crossedCenterY);
        tickWallCollisions();
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }
}
