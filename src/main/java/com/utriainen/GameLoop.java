package com.utriainen;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class GameLoop extends AnimationTimer {


    long pauseStart;
    long animationStart;
    long lastFrameDurationNano = 0L;
    DoubleProperty animationDuration = new SimpleDoubleProperty(0L);
    DoubleProperty frameDuration = new SimpleDoubleProperty(0L);
    boolean isPaused;
    boolean isActive;

    boolean pauseScheduled;
    boolean playScheduled;
    boolean restartScheduled;

    public abstract void tick(long relativeNow);

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isActive() {
        return isActive;
    }

    public DoubleProperty animationDurationProperty() {
        return animationDuration;
    }
    public DoubleProperty frameDurationProperty() {
        return frameDuration;
    }
    @Override
    public void start() {
        super.start();
        restartScheduled = true;
    }

    @Override
    public void stop() {
        super.stop();
        pauseStart = 0;
        isPaused = false;
        isActive = false;
        pauseScheduled = false;
        playScheduled = false;
        animationDuration.set(0);
    }

    @Override
    public void handle(long now) {
        if (restartScheduled) {
            isPaused = false;
            animationStart = now;
            restartScheduled = false;
        }
        if (pauseScheduled) {
            pauseStart = now;
            isPaused = true;
            pauseScheduled = false;
        }
        if (playScheduled) {
            animationStart += (now - pauseStart);
            isPaused = false;
            playScheduled = false;
        }
        if (!isPaused) {
            long animDuration = now - animationStart;
            animationDuration.set(animDuration / 1e9);
            frameDuration.set((animDuration - lastFrameDurationNano)/1e9);
            lastFrameDurationNano = animDuration;
            tick(animDuration);
        }
    }

    public void play() {
        if (isPaused) {
            playScheduled = true;
        }
    }

    public void pause() {
        if (!isPaused) {
            pauseScheduled = true;
        }
    }

}
