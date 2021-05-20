package com.utriainen.models;

import com.utriainen.models.drawables.*;

import java.util.*;

public class Game {
    private PowerMeter powerMeter;
    private Tank playerTank;
    private Ground ground;
    private List<Projectile> projectiles;
    private List<Explosion> explosions;

    private int mapHeight;
    private int mapWidth;

    private Stack<Drawable> newDrawables;
    private Stack<Drawable> drawablesToBeRemoved;

    public Stack<Drawable> getNewDrawables() {
        return newDrawables;
    }

    public void setNewDrawables(Stack<Drawable> newDrawables) {
        this.newDrawables = newDrawables;
    }

    public Stack<Drawable> getDrawablesToBeRemoved() {
        return drawablesToBeRemoved;
    }

    public void setDrawablesToBeRemoved(Stack<Drawable> drawablesToBeRemoved) {
        this.drawablesToBeRemoved = drawablesToBeRemoved;
    }

    public PowerMeter getPowerMeter() {
        return powerMeter;
    }

    public void setPowerMeter(PowerMeter powerMeter) {
        this.powerMeter = powerMeter;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public void setPlayerTank(Tank playerTank) {
        this.playerTank = playerTank;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public Game(PowerMeter powerMeter, Tank playerTank, int mapWidth, int mapHeight) {
        this.powerMeter = powerMeter;
        this.playerTank = playerTank;
        this.projectiles = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.drawablesToBeRemoved = new Stack<>();
        this.newDrawables = new Stack<>();
        this.getNewDrawables().push(getPlayerTank());
        this.getNewDrawables().push(powerMeter);
        this.ground = new Ground();
        this.getNewDrawables().push(ground);
    }

    public void removeProjectile(Projectile projectile) {
     //   projectiles.remove(projectile);
        drawablesToBeRemoved.add(projectile);
    }

    public void removeExplosion(Explosion explosion) {
   //     explosions.remove(explosion);
        drawablesToBeRemoved.add(explosion);
    }

    public void handleProjectiles(Double frameDuration) {
        /*todo note check out parallel in relation to arraylist*/
        ListIterator<Projectile> projectileIterator = projectiles.listIterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectile.updatePosition(frameDuration);
            if (projectile.testGroundCollision(ground)) {
                /*
                Idea: map(projectileType,Explosion) use static constructor Explosion.getExplosion(projectileType, Coordinates)
                 */
                Explosion explosion = projectile.getExplosion();
                ground.deformGround(explosion.getCoordinates(), explosion.getWidth()/2);
                ground.updateGround();
                explosions.add(explosion);
                newDrawables.add(projectile.getExplosion());

                removeProjectile(projectile);
                projectileIterator.remove();
            }
        }
    }

    public void handleExplosions(Double frameDuration) {
        /*todo note check out parallel in relation to arraylist*/
        ListIterator<Explosion> explosionIterator = explosions.listIterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();

            explosion.explode();
            if (explosion.isDone()) {
                removeExplosion(explosion);
                explosionIterator.remove();
            }
        }
    }

    public void handleShooting() {
        if (getPlayerTank().isFiring()) {
            Projectile newProjectile = getPlayerTank().fire();
            projectiles.add(newProjectile);
            getNewDrawables().push(newProjectile);
        }
    }

    public void updateGameLogic(Double frameDuration) {
        getPlayerTank().updatePosition(frameDuration);
        handleExplosions(frameDuration);
        handleProjectiles(frameDuration);
        handleShooting();
        getPowerMeter().update(getPlayerTank().getCoordinates(), getPlayerTank().actualFirePower());
    }
}