package bukkaa.mediahouse.ShakerGameWS.graphics;

import bukkaa.mediahouse.ShakerGameWS.controller.CacheSprite;
import bukkaa.mediahouse.ShakerGameWS.controller.GraphicManager;
import bukkaa.mediahouse.ShakerGameWS.model.Constants;
import bukkaa.mediahouse.ShakerGameWS.model.ShakeGamer;
import bukkaa.mediahouse.ShakerGameWS.model.components.BackgroundPanel;
import bukkaa.mediahouse.ShakerGameWS.model.CountdownTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static bukkaa.mediahouse.ShakerGameWS.controller.GameController.playersMap;

class GameScreen {
    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);

    private MainView mainView;

    private CountdownTimer countdownTimer;

    private JFrame mainFrame = new JFrame();
    private int idScreen;
    private int screenWidth = 500;
    private int screenHeight = 500;
    private int offsetX = (int)(screenWidth / 100.0f * Constants.OFFSET_X_PERCENT);
    private double perPixel = Constants.DISTANCE / (screenWidth - 2 * offsetX);
    private Font scaledWinFont;
    private boolean finished = false;
    private BackgroundPanel background;

    GameScreen(MainView mainView, int idScreen, BufferedImage gameScreenBackImage) {
        this.idScreen = idScreen;
        this.mainView = mainView;

        createBackgroundPanel(gameScreenBackImage);

        countdownTimer = new CountdownTimer(mainView);
    }

    void setBackgroundImage(BufferedImage background) {
        createBackgroundPanel(background);
    }

    boolean isFinished() {
        return finished;
    }

    void showScreen() {
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension dim = GraphicManager.getScreenResolution(idScreen);
        screenWidth = (int)dim.getWidth();
        screenHeight = (int)dim.getHeight();

        mainFrame.setSize(new Dimension(screenWidth, screenHeight));
        mainFrame.setPreferredSize(new Dimension(screenWidth, screenHeight));

        registerFont();

        CacheSprite.removeAll();

        prepareShake();

        SwingUtilities.invokeLater(() -> {
            mainFrame.getContentPane().add(countdownTimer.getCountdownLbl(), 1);
        });

        mainFrame.getContentPane().add(background);
        mainFrame.validate();
        GraphicManager.showOnScreen(idScreen, mainFrame);
        mainFrame.setFocusable(true);
        mainFrame.requestFocus();

        countdownTimer.startCountdown();
    }

    void movePlayer(int playerId, int delta) {
        ShakeGamer player = playersMap.get(playerId);

        player.addToSumLevel(delta);

        if (player.getX() >= screenWidth - offsetX - player.sprite.SpriteWidth) {
            if (player.getX() == player.getFutureX()) {
                finishGame(playerId);
                player.sprite.setAnimationFinishing();
                log.info("Player <{}> has run {}", player, player.getSumLevel());
            }
            return;
        }

        int sum = player.getSumLevel();
        if (player.savedSumLevel != sum) {
            player.savedSumLevel = sum;
            player.sprite.setAnimationRunning();
        }
        if (player.sprite.isRunning()) {
            int x = (int) ((sum / perPixel) + offsetX);
            int spriteSpeed = (x - player.getX()) / 5;

            if (spriteSpeed < 0) spriteSpeed = 0;

            if (player.getX() != x) {
                if (player.getX() + spriteSpeed >= screenWidth - offsetX - player.sprite.SpriteWidth)
                    spriteSpeed = screenWidth - offsetX - player.getX() - player.sprite.SpriteWidth + 1;

                player.setFutureX(player.getX() + spriteSpeed);
                int finalSpriteSpeed = spriteSpeed;
                SwingUtilities.invokeLater(() -> player.setLocation(player.getX() + finalSpriteSpeed, player.getY()));
            }
        }
    }

    void restartGame() {
        finished = true;
        background.removeAll();
        mainFrame.removeAll();

        mainFrame.dispose();
    }

//----------------------------------------------------------------------------------------------------------------------

    private FontRenderContext frc = new FontRenderContext(null, true, true);

    private void prepareShake() {
        playersMap.forEach((id, player) -> {
            int y = (int) (screenHeight * (player.getPercentOffsetY() / 100.0f) - 100);
            player.setLocation(offsetX, y);
            log.info("Player <{}> location is ({}; {})", player.getPlayerName(), offsetX, y);

            mainFrame.add(player);
        });
    }

    private void finishGame(int winIdPlayer) {
        if (!finished) {
            finished = true;
            log.info("Game is finished");

            for (Map.Entry<Integer, ShakeGamer> gamerEntry : playersMap.entrySet()) {
                if (gamerEntry.getValue().getId() != winIdPlayer) {
                    gamerEntry.getValue().setVisible(false);
                    log.info("{} is invisible", gamerEntry.getValue());
                }
            }

            showTextWin(winIdPlayer);
        }
    }

    private int getTextWidth(String text) {
        return (int) scaledWinFont.createGlyphVector(
                frc,
                text != null ?
                        text :
                        "Выиграл ААААААААААААА")
                .getLogicalBounds()
                .getWidth();
    }

    private int getTextHeight(String text) {
        return (int) scaledWinFont
                        .createGlyphVector(frc, text)
                        .getLogicalBounds()
                        .getHeight();
    }

    private void showTextWin(int winIdPlayer) {
        ShakeGamer winner = playersMap.get(winIdPlayer);
        log.info("Winner is: [{}] {}", winIdPlayer, winner.getPlayerName());

        JLabel winTextLabel = new JLabel("Победитель", SwingConstants.CENTER);
        winTextLabel.setFont(scaledWinFont);
        int winTextWidth = getTextWidth("Победитель ");
        int winTextHeight = getTextHeight("Победитель ");
        winTextLabel.setSize(winTextWidth, winTextHeight);
        winTextLabel.setForeground(Constants.GAME_FONT_COLOR);

        int winnerNameWidth = getTextWidth(winner.getPlayerName() + "  ");
        int winnerNameHeight = getTextHeight(winner.getPlayerName() + "  ");
        JLabel winnerNameLabel = new JLabel(winner.getPlayerName(), SwingConstants.CENTER);
        winnerNameLabel.setFont(scaledWinFont);
        winnerNameLabel.setSize(winnerNameWidth, winnerNameHeight);
        winnerNameLabel.setForeground(Constants.GAME_FONT_COLOR);

        SwingUtilities.invokeLater(() -> {
            background.add(winTextLabel, 0);
            background.add(winnerNameLabel, 0);
            winTextLabel.setLocation((screenWidth - winTextWidth) / 2, (screenHeight - winTextHeight * 2 - winnerNameHeight * 2) / 2);
            winnerNameLabel.setLocation((screenWidth - winnerNameWidth) / 2, (screenHeight - winTextHeight) / 2);
        });

        int spriteWidth = winner.sprite.SpriteWidth;
        int spriteHeight = winner.sprite.SpriteHeight;
        int offset = 30;
        int top = (screenHeight - winTextHeight - spriteHeight - offset) / 2;
        moveSpriteToCenter(winner, (screenWidth - spriteWidth) / 2, top + winTextHeight + offset + 130);

        mainView.shiftScreensWithDelay();
    }

    private void moveSpriteToCenter(ShakeGamer winner, int xSpriteToCenter, int ySpriteToCenter) {
        List<Timer> timers = new ArrayList<>(1);
        Timer timer = new Timer(100, e -> {
            int xSpeed = (winner.getX() - xSpriteToCenter) / 5;
            int ySpeed = (winner.getY() - ySpriteToCenter) / 5;

            if (xSpeed == 0 && ySpeed == 0) {
                timers.forEach(Timer::stop);
            }
            else {
                SwingUtilities.invokeLater(() -> winner.setLocation(winner.getX() - xSpeed, winner.getY() - ySpeed));
            }
        });

        timers.add(timer);
        timer.start();
    }

    private void registerFont() {
        Font loadedFont = mainView.getGameFont();

        scaledWinFont = loadedFont.deriveFont(((float) Constants.GAME_FONT_SIZE));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(scaledWinFont);

        log.info("Game font registered {}", scaledWinFont);
    }

    private void createBackgroundPanel(BufferedImage backgroundImage) {
        (background = new BackgroundPanel(backgroundImage)).setBackground(Color.CYAN);
    }
}
