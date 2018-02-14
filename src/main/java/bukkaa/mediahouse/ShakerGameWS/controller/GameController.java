package bukkaa.mediahouse.ShakerGameWS.controller;

import bukkaa.mediahouse.ShakerGameWS.graphics.MainView;
import bukkaa.mediahouse.ShakerGameWS.helpers.Config;
import bukkaa.mediahouse.ShakerGameWS.model.GamerAccelerationData;
import bukkaa.mediahouse.ShakerGameWS.model.ShakeGamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bukkaa on 09.04.2017.
 */

public class GameController {
    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private MainView view;

    public static Map<Integer, ShakeGamer> playersMap = new HashMap<>();
    public boolean gameStarted  = false;
    private Config config;

    public int signUpNewGamer(String name, String texture) {
        ShakeGamer newPlayer;

        if (!gameStarted && playersMap.size() < 4) {
            newPlayer = new ShakeGamer(name, texture, false);
            playersMap.put(newPlayer.getId(), newPlayer);

            view.updatePlayersList();

            log.info("New gamer signed up: {}", newPlayer);
            log.debug("List of gamers on-line: {}", playersMap.values());
        } else {
            newPlayer = new ShakeGamer(name, texture, -1);
            log.info("Game is already started. Player: {}", newPlayer);
        }

        return newPlayer.getId();
    }

    public String writeAccelerationData(GamerAccelerationData data) {
        if (gameStarted && !isGameFinished() && data.getId() != -1) {
            int delta = calculateMovingDelta(data.getAx(), data.getAy());

            log.trace("New shake data from <{}>: {}", playersMap.get(data.getId()).getPlayerName(), data);
            log.trace("Calculated delta moving for <{}>: {}", playersMap.get(data.getId()).getPlayerName(), delta);

            view.movePlayer(data.getId(), delta);
        }

        return isGameFinished() ? "finish" : "go";
    }

    private int calculateMovingDelta(float ax, float ay) {
        return (int) (Math.abs(ax) + Math.abs(ay)) / 20;
    }

    public void setShakerGameView(MainView shakeGameView) {
        this.view = shakeGameView;
    }

    public boolean isGameFinished() {
        return view.isGameFinished();
    }

    public void restartGame() {
        playersMap.clear();
        view.updatePlayersList();
        gameStarted = false;
        log.info("Game restated");
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
