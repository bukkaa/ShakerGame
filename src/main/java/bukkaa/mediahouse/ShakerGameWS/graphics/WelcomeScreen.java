package bukkaa.mediahouse.ShakerGameWS.graphics;

import bukkaa.mediahouse.ShakerGameWS.controller.GraphicManager;
import bukkaa.mediahouse.ShakerGameWS.model.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

import static bukkaa.mediahouse.ShakerGameWS.controller.GameController.playersMap;
import static javax.swing.BoxLayout.Y_AXIS;

class WelcomeScreen {
    private static final Logger log = LoggerFactory.getLogger(WelcomeScreen.class);

    private MainView mainView;

    private JFrame mainFrame;
    private int idScreen;
    private int screenWidth = 500;
    private int screenHeight = 500;
    private int offsetX = (int) (screenWidth / 100.0f * Constants.OFFSET_X_PERCENT);
    private ImageIcon backgroundImage;
    private JLabel background;

    WelcomeScreen(MainView mainView, Integer idScreen, ImageIcon welcomeScreenBackImage) {
        this.mainView = mainView;
        this.idScreen = idScreen;
        backgroundImage = welcomeScreenBackImage;
    }

    void setBackgroundImage(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    void showScreen() {
        init();
        GraphicManager.showOnScreen(idScreen, mainFrame);
        drawConnectedPlayers();
        mainView.setWelcomeScreenIsShowing(true);
    }

    void hideScreen() {
        mainView.setWelcomeScreenIsShowing(false);

        if (mainFrame != null) {
            mainFrame.setVisible(false);
            mainFrame.removeAll();
            mainFrame = null;
        }
    }

    void drawConnectedPlayers() {
        playersMap.forEach((id, player) -> {
            int offsetY = (int) (screenHeight * (player.getPercentOffsetY() / 100.0f) - 100);
            player.setLocation(offsetX, offsetY);
            background.add(player);
            log.info("Player <{}> location is ({}; {})", player.getPlayerName(), offsetX, offsetY);
        });
        background.repaint();
    }

    void clean() {
        background.removeAll();
    }

//----------------------------------------------------------------------------------------------------------------------

    private void init() {
        mainFrame = new JFrame();
        Dimension screenResolution = GraphicManager.getScreenResolution(idScreen);
        screenHeight = screenResolution.height;
        screenWidth = screenResolution.width;
        mainFrame.setLocation(800, 200);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(screenResolution);
        mainFrame.setLayout(new BorderLayout());

        background = new JLabel(
                new ImageIcon(
                        backgroundImage.getImage()
                                .getScaledInstance(
                                        screenResolution.width,
                                        screenResolution.height,
                                        Image.SCALE_DEFAULT)));

        mainFrame.add(background);
        background.setLayout(new BoxLayout(background, Y_AXIS));
        mainFrame.validate();
    }
}
