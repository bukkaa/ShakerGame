package bukkaa.mediahouse.ShakerGameWS;

import bukkaa.mediahouse.ShakerGameWS.controller.GameController;
import bukkaa.mediahouse.ShakerGameWS.graphics.MainView;
import bukkaa.mediahouse.ShakerGameWS.helpers.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "bukkaa.mediahouse.ShakerGameWS")
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final GameController controller = new GameController();
    private static final MainView mainView = new MainView();
    private static final Config config = new Config();

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        controller.setShakerGameView(mainView); //TODO 16.04.2017 21:18 by bukkaa: make with @Beans and @Autowired
        controller.setConfig(config);

        mainView.setController(controller); //TODO 16.04.2017 21:18 by bukkaa: make with @Beans and @Autowired
        mainView.setConfig(config);

        log.info("ShakerGame application started!");
    }

    public static GameController getGameController() {
        return controller;
    }
}
