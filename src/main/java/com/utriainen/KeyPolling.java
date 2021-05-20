package com.utriainen;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.Date;
import java.util.HashMap;

public class KeyPolling {
    private static Scene scene;

    private static final HashMap<KeyCode, Long> timedKeysCurrentlyDown = new HashMap<>();
    private static final HashMap<KeyCode, Long> releasedKeys = new HashMap<>();
    private KeyPolling() {
    }

    public static KeyPolling getInstance() {
        return new KeyPolling();
    }

    public void pollScene(Scene scene) {
        clearKeys();
        removeCurrentKeyHandlers();
        setScene(scene);
    }

    private void clearKeys() {
        releasedKeys.clear();
        timedKeysCurrentlyDown.clear();
    }

    private void removeCurrentKeyHandlers() {
        if (scene != null) {
            KeyPolling.scene.setOnKeyPressed(null);
            KeyPolling.scene.setOnKeyReleased(null);
        }
    }

    private void setScene(Scene scene) {
        KeyPolling.scene = scene;
        KeyPolling.scene.setOnKeyPressed((keyEvent -> timedKeysCurrentlyDown.put(keyEvent.getCode(),new Date().getTime())));
        KeyPolling.scene.setOnKeyReleased((keyEvent -> {
            releasedKeys.put(keyEvent.getCode(), new Date().getTime()-timedKeysCurrentlyDown.get(keyEvent.getCode()));
            timedKeysCurrentlyDown.remove(keyEvent.getCode());
        }));
    }

    public boolean isDown(KeyCode keyCode) {
        return timedKeysCurrentlyDown.containsKey(keyCode);
    }
    public boolean isReleased(KeyCode keyCode) {
        return releasedKeys.containsKey(keyCode);
    }
    public double readKeyTime(KeyCode keyCode) {
        Long currentTime = new Date().getTime();
        if(timedKeysCurrentlyDown.containsKey(keyCode)){
            return (double)(currentTime - timedKeysCurrentlyDown.replace(keyCode,currentTime))/1000.0;
        }
        return (double)releasedKeys.remove(keyCode)/1000.0;
    }

    @Override
    public String toString() {
        StringBuilder keysDown = new StringBuilder("KeyPolling on scene (").append(scene).append(")");
        for (KeyCode code : releasedKeys.keySet()) {
            keysDown.append(code.getName()).append(" ");
        }
        return keysDown.toString();
    }
}