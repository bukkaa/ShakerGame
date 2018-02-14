package bukkaa.mediahouse.ShakerGameWS.graphics;

import bukkaa.mediahouse.ShakerGameWS.controller.GameController;
import bukkaa.mediahouse.ShakerGameWS.helpers.Config;
import bukkaa.mediahouse.ShakerGameWS.model.Constants;
import bukkaa.mediahouse.ShakerGameWS.model.ShakeGamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static bukkaa.mediahouse.ShakerGameWS.model.Constants.GAME_SCREEN_IMG;
import static bukkaa.mediahouse.ShakerGameWS.model.Constants.WELCOME_SCREEN_IMG;

public class MainView {
    private static final Logger log = LoggerFactory.getLogger(MainView.class);

    private GameController controller;

    private Config config;

    private GameScreen          gameScreen;
    private WelcomeScreen       welcomeScreen;

    private ControlPanelScreen  controlPanel;
    private int     gameScreenId;
    private boolean welcomeScreenIsShowing = false;

    private boolean gameScreenIsShowing    = false;
    private Font          gameFont;
    private ImageIcon     welcomeScreenBackImage;

    private BufferedImage gameScreenBackImage;
    private int welcomeScreenAutoLaunchDelay = -1;


    public MainView() {
        controlPanel = new ControlPanelScreen(this);

        loadGameFont();
        loadWelcomeScreenBackgroundImage();
        loadGameScreenBackgroundImage();
    }

    public void movePlayer(int playerId, int delta) {
        gameScreen.movePlayer(playerId, delta);
    }

    public void updatePlayersList() {
        evaluatePlayersPosition();
        controlPanel.updatePlayersTable();
        if (welcomeScreen != null){
            welcomeScreen.drawConnectedPlayers();
        }
    }

    void prepareToStart() {
        gameScreen = new GameScreen(this, gameScreenId, gameScreenBackImage);
        if (GameController.playersMap.size() > 1) {

            if (welcomeScreen != null) {
                welcomeScreen.hideScreen();
            }
            gameScreen.showScreen();
        } else {
            controlPanel.showWarning("Мало игроков!");
        }
    }

    void reloadGameScreenBackgroundImage() {
        loadGameScreenBackgroundImage();
        if (gameScreen != null) {
            gameScreen.setBackgroundImage(gameScreenBackImage);
        }
    }

    void reloadWelcomeScreenBackgroundImage() {
        loadWelcomeScreenBackgroundImage();
        if (welcomeScreen != null) {
            welcomeScreen.setBackgroundImage(welcomeScreenBackImage);
        }
    }

    void loadGameFont() {
        try {
            gameFont = Font.createFont(0, new File(Constants.GAME_FONT));
        } catch (FontFormatException | IOException e) {
            log.error("Error in loading font " + Constants.GAME_FONT, e);
            gameFont = new Font("SansSerif", Font.BOLD, 50);
        }
        log.info("Game font is: {}", gameFont);
    }

    Font getGameFont() {
        return gameFont;
    }

    void toggleWelcomeScreen() {
        if (welcomeScreenIsShowing) {
            hideWelcomeScreen();
        } else {
            showWelcomeScreen();
        }
    }

    void setGameDisplayId(int gameScreenId) {
        this.gameScreenId = gameScreenId;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public boolean isGameFinished() {
        return gameScreen != null && gameScreen.isFinished();
    }

    public void setGameStarted(boolean gameStarted) {
        controller.gameStarted = gameStarted;
    }

    void restartGame() {
        controller.restartGame();
        if (gameScreen != null) {
            gameScreen.restartGame();
            gameScreen = null;
        }
        if (welcomeScreen != null) {
            welcomeScreen.clean();
        }
        ShakeGamer.dropCounter();
    }

    void updatePlayersOnWelcomeScreen(){
        welcomeScreen.drawConnectedPlayers();
    }

    public boolean isWelcomeScreenIsShowing() {
        return welcomeScreenIsShowing;
    }

    void setWelcomeScreenIsShowing(boolean welcomeScreenIsShowing) {
        this.welcomeScreenIsShowing = welcomeScreenIsShowing;
    }

    public boolean isGameScreenIsShowing() {
        return gameScreenIsShowing;
    }

    public void setGameScreenIsShowing(boolean gameScreenIsShowing) {
        this.gameScreenIsShowing = gameScreenIsShowing;
    }

    int getWelcomeScreenAutoLaunchDelay() {
        return welcomeScreenAutoLaunchDelay;
    }

    void setWelcomeScreenAutoLaunchDelay(int welcomeScreenAutoLaunchDelay) {
        this.welcomeScreenAutoLaunchDelay = welcomeScreenAutoLaunchDelay;
        log.info("Welcome Screen autolaunch delay is set to {} sec", welcomeScreenAutoLaunchDelay);
    }

    void shiftScreensWithDelay() {
        if (welcomeScreenAutoLaunchDelay > 0) {
            Timer shiftScreensTimer = new Timer(
                    welcomeScreenAutoLaunchDelay * 1000,
                    e -> {
                        showWelcomeScreen();
                        restartGame();
                    });
            shiftScreensTimer.setRepeats(false);
            shiftScreensTimer.start();
        }
    }

    void setProperty(String key, String value) {
        config.setProperty(key, value);
    }

    public void setColor(int red, int green, int blue) {
        String colorStr = red + " " + green + " " + blue;
        setProperty("game_font_color", colorStr);
    }

    String getProperty(String key) {
        return config.getProperty(key);
    }

    public Color getColor() {
        String[] arr = getProperty("game_font_color").split(" ");
        return new Color(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
    }

//----------------------------------------------------------------------------------------------------------------------
    private void evaluatePlayersPosition() {
        switch (GameController.playersMap.size()) {
            case 1:
                GameController.playersMap.get(0).setPercentOffsetY(7.0f);
                break;
            case 2:
                GameController.playersMap.get(0).setPercentOffsetY(7.0f);
                GameController.playersMap.get(1).setPercentOffsetY(32.0f);
                break;
            case 3:
                GameController.playersMap.get(0).setPercentOffsetY(7.0f);
                GameController.playersMap.get(1).setPercentOffsetY(32.0f);
                GameController.playersMap.get(2).setPercentOffsetY(58.0f);
                break;
            case 4:
                GameController.playersMap.get(0).setPercentOffsetY(7);
                GameController.playersMap.get(1).setPercentOffsetY(32.0f);
                GameController.playersMap.get(2).setPercentOffsetY(58.0f);
                GameController.playersMap.get(3).setPercentOffsetY(83.0f);
                break;
        }
    }

    private void loadWelcomeScreenBackgroundImage(){
        if (WELCOME_SCREEN_IMG != null && !WELCOME_SCREEN_IMG.equals("")) {
            try {
                welcomeScreenBackImage = new ImageIcon(WELCOME_SCREEN_IMG);
            } catch (Exception e) {
                log.error("Error in loading Welcome Screen background image", e);
            }
            log.info("Welcome Screen background Image loaded: {}\t{} x {}", welcomeScreenBackImage.getDescription(), welcomeScreenBackImage.getIconWidth(), welcomeScreenBackImage.getIconHeight());
        }
    }

    private void loadGameScreenBackgroundImage(){
        if (GAME_SCREEN_IMG != null && !GAME_SCREEN_IMG.equals("")) {
            try {
                gameScreenBackImage = ImageIO.read(new File(GAME_SCREEN_IMG));

            } catch (IOException e) {
                log.error("Error in loading Game Screen background image", e);
            }
            log.info("Game Screen background Image loaded: {}", gameScreenBackImage);
        }
    }

    private void showWelcomeScreen() {
        if (welcomeScreen == null) {
            welcomeScreen = new WelcomeScreen(this, gameScreenId, welcomeScreenBackImage);
        }
        welcomeScreen.showScreen();
    }

    private void hideWelcomeScreen() {
        if (welcomeScreen != null) {
            welcomeScreen.hideScreen();
        }
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
