package bukkaa.mediahouse.ShakerGameWS.model;

import bukkaa.mediahouse.ShakerGameWS.graphics.MainView;
import bukkaa.mediahouse.ShakerGameWS.helpers.FileAccessHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer {
    private static final Logger log = LoggerFactory.getLogger(CountdownTimer.class);

    private MainView mainView;

    private Timer countdownTimer;
    private JLabel countdownLbl;
    private ImageIcon[] countdownImages;

    public CountdownTimer(MainView mainView) {
        this.mainView = mainView;

        countdownLbl = new JLabel("", SwingConstants.CENTER);
        countdownLbl.setLocation(300, 300);

        loadCountdownImages();

        countdownTimer = new Timer(1200, new TimerTick());
        log.info("New CountdownTimer created!");
    }

    public JLabel getCountdownLbl() {
        return countdownLbl;
    }

    public void startCountdown() {
        countdownTimer.start();

//        SwingUtilities.invokeLater(() -> {
//            background.add(countdownLbl, 1);
//        });
    }

//----------------------------------------------------------------------------------------------------------------------

    private void loadCountdownImages() {
        log.info("Loading countdown images...");

        String[] images = FileAccessHelper.getCountdownImages();

        countdownImages = new ImageIcon[images.length];

        for (int i = 0; i < images.length; i++) {
            String path2Img = Constants.COUNTDOWN_FOLDER + "/" + images[i];

            try {
                countdownImages[i] = new ImageIcon(path2Img);
            } catch (Exception e) {
                log.error("Error in loading countdown image <" + path2Img + ">", e);
            }
            log.debug("Countdown image loaded: {}\t{} x {}", countdownImages[i].getDescription(), countdownImages[i].getIconWidth(), countdownImages[i].getIconHeight());
        }

        log.info("{} countdown images loaded", images.length);
    }


    private class TimerTick implements ActionListener {
        int countdown = countdownImages.length;

        @Override
        public void actionPerformed(ActionEvent e) {
            countdown--;
            log.debug("Countdown: {}", countdown);

            if (countdown != -1) {
                countdownLbl.setIcon(countdownImages[countdown]);

                log.debug("Countdown image set: {}", countdownImages[countdown]);

                if (countdown == 0) {
                    mainView.setGameStarted(true);
                    log.info("The game started!");
                }
            }
            else {
                countdownLbl.setVisible(false);
                countdownTimer.stop();
                countdownLbl = null;

                log.debug("CountdownTimer destroyed");
            }
        }
    }
}
