package com.utriainen.controllers;

import com.utriainen.GameLoop;
import com.utriainen.KeyPolling;
import com.utriainen.Renderer;
import com.utriainen.models.*;
import com.utriainen.models.drawables.PowerMeter;
import com.utriainen.models.drawables.Tank;
import com.utriainen.models.room.Coordinates;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    public Canvas gameCanvas;
    public AnchorPane gameAnchor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseCanvas();
        //player tank
        Tank tank = new Tank(25, 15, new Coordinates(500, 865), false, Color.GREEN);
        //player tank power meter
        PowerMeter powerMeter = new PowerMeter(tank.getCoordinates(), 5, 0, Color.RED);
        //Holds all the logical game pieces exc. logic derived from keyboard input.
        Game game = new Game(powerMeter, tank, gameCanvas.widthProperty().intValue(), gameCanvas.heightProperty().intValue() );

        Renderer renderer = new Renderer(this.gameCanvas);
        renderer.setBackground(new Image(getClass().getResourceAsStream("/images/landscape.png")));

        GameLoop timer = new GameLoop() {
            @Override
            public void tick(long animationTime) {
                renderer.prepare();
                while (!game.getNewDrawables().isEmpty()) renderer.addDrawable(game.getNewDrawables().pop());
                while (!game.getDrawablesToBeRemoved().isEmpty()) renderer.removeDrawable(game.getDrawablesToBeRemoved().pop());
                updatePlayerMovement(game.getPlayerTank());
                game.updateGameLogic(frameDurationProperty().get());
                renderer.render();

            }
        };
        timer.start();
    }

    private void initialiseCanvas() {
        gameCanvas.widthProperty().bind(gameAnchor.widthProperty());
        gameCanvas.heightProperty().bind(gameAnchor.heightProperty());
    }

    public void updatePlayerMovement(Tank tank) {
        KeyPolling keyManager = KeyPolling.getInstance();
        tank.getVector().setVelocityX(0);
        if (keyManager.isDown(KeyCode.DOWN)) {
            tank.setAimDirection(Math.min(tank.getAimDirection() + 0.013089969, Math.PI));
        }
        if (keyManager.isDown(KeyCode.UP)) {
            tank.setAimDirection(Math.max(tank.getAimDirection() - 0.013089969, 0));
        }
        if (keyManager.isDown(KeyCode.RIGHT)) {
            tank.getVector().setVelocityX(60);
        }
        if (keyManager.isDown(KeyCode.LEFT)) {
            tank.getVector().setVelocityX(-60);
        }
        if (keyManager.isDown(KeyCode.SPACE)) {
            tank.increasePowerMeter(keyManager.readKeyTime(KeyCode.SPACE));
        }
        if (keyManager.isReleased(KeyCode.SPACE)) {
            tank.increasePowerMeter(keyManager.readKeyTime(KeyCode.SPACE));
            tank.setFiring(true);
        }
    }
}
