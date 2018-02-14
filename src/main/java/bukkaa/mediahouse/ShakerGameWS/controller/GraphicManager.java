package bukkaa.mediahouse.ShakerGameWS.controller;

import javax.swing.*;
import java.awt.*;

public class GraphicManager {
    private static final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static final GraphicsDevice[] gs = ge.getScreenDevices();

    public static void showOnScreen(int screenDeviceId, JFrame frame) {
        if (gs.length == 0) {
            throw new RuntimeException("Screen Manager: No Screens Found");
        }

        if (screenDeviceId >= 0 && screenDeviceId < gs.length) {
            int widthSum = 0;

            for (int i = 0; i < screenDeviceId; ++i) {
                widthSum += getScreenResolution(i).width;
            }

            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
            frame.setLocation(widthSum, 0);
            frame.setVisible(true);
        }
    }
    
    public static String[] getScreenNames() {
        String[] names = new String[gs.length];

        for (int i = 0; i < gs.length; i++) {
            names[i] = gs[i].getIDstring();
        }
        return names;
    }
    
    public static Dimension getScreenResolution(int screenDeviceId) {
        if (screenDeviceId >= 0 && screenDeviceId < gs.length) {
            return new Dimension(gs[screenDeviceId].getDisplayMode().getWidth(), gs[screenDeviceId].getDisplayMode().getHeight());
        }
        return new Dimension(0, 0);
    }
}
