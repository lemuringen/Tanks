package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Projectile extends Entity {
    public boolean isDetonated = false;
    public boolean isDone = false;
    private Explosion explosion = null;
    public Projectile(int width, boolean isAffectedByGravity, Coordinates coordinates, Vector vector, Color color) {
        super(width, width, isAffectedByGravity, coordinates, vector, color);
    }

    public Explosion getExplosion(){
        if(explosion == null){
            explosion = new Explosion(getCoordinates().clone(), getWidth()*20, getHeight()*20,Color.RED);
        }
        return explosion;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.fillOval(getCoordinates().getX(), getCoordinates().getY(), getWidth(), getHeight());

    }

    @Override
    public boolean testGroundCollision(Ground ground) {
        return ground.getGround().contains(getCoordinates().getX(), getCoordinates().getY());
    }
}
