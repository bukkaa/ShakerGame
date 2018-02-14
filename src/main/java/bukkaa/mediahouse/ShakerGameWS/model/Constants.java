package bukkaa.mediahouse.ShakerGameWS.model;

import java.awt.*;

public class Constants {
    public static final float OFFSET_X_PERCENT = 5.0f;

    public static final int TILE_HEIGHT = 270;

    public static double DISTANCE = 1000.0;

    public static Color GAME_FONT_COLOR = new Color(247, 227, 48);
    public static int   GAME_FONT_SIZE = 100;

    public static String CONFIG_FILE_PATH    = "resources/shaker.properties";

    public static String COUNTDOWN_FOLDER    = "resources/обратный отсчет";
    public static String WEB_BACK_FOLDER     = "resources/фоны сайта";
    public static String GAME_BACK_FOLDER    = "resources/фоны игры";

    public static String SPRITE_FOLDER       = "resources/sprite";
    public static String GAME_FONT           = "resources/font.ttf";
    public static String GAME_SCREEN_IMG     = GAME_BACK_FOLDER + "/back.png";
    public static String WELCOME_SCREEN_IMG  = GAME_BACK_FOLDER + "/welcome_back.png";

    //TODO 30.07.2017 19:24 by bukkaa: сделать установку полей через метод, с одновременным занесением в Config

//    static void setProperty(String fieldName, String value) {
//        try {
//            Field field = Constants.class.getDeclaredField(fieldName);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//    }
}