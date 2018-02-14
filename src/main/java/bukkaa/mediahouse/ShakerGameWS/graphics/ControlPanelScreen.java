package bukkaa.mediahouse.ShakerGameWS.graphics;

import bukkaa.mediahouse.ShakerGameWS.controller.GraphicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import static bukkaa.mediahouse.ShakerGameWS.controller.GameController.playersMap;
import static bukkaa.mediahouse.ShakerGameWS.model.Constants.*;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

class ControlPanelScreen {
    private static final Logger log = LoggerFactory.getLogger(ControlPanelScreen.class);

    private static final Dimension MAX_FIELD_SIZE = new Dimension(100, 25);

    private MainView mainView;
    private JFrame controlPanelFrame = new JFrame("Shaker Game");

    private JTable playersTable;

    private JLabel lblGameDisplayId;
    private JLabel lblWelcomeScreenImg;
    private JLabel lblGameScreenImg;
    private JLabel lblFont;
    private JLabel lblFontColor;
    private JSpinner fontSizeSpinner;
    private JSpinner spinnerDistance;
    private JSpinner autolaunchSpinner;

    ControlPanelScreen(MainView mainView) {
        this.mainView = mainView;

        initControlPanel();
        controlPanelFrame.setVisible(true);
    }

    private void initControlPanel() {
        controlPanelFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        controlPanelFrame.setSize(new Dimension(400, 360));
        controlPanelFrame.setLocation(500, 100);

        JPanel common = new JPanel();
        common.setLayout(new BoxLayout(common, X_AXIS));
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BoxLayout(leftPane, Y_AXIS));
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, Y_AXIS));
        common.add(leftPane);
        common.add(rightPane);

        lblGameDisplayId = new JLabel("Экран для показа");
        lblGameDisplayId.setAlignmentX(0.0f);
        leftPane.add(lblGameDisplayId);

// GAME DISPLAY
        JComboBox<String> columnList = new JComboBox<>(GraphicManager.getScreenNames());
        columnList.setSelectedIndex(0);
        columnList.setAlignmentX(0.0f);
        columnList.setMaximumSize(MAX_FIELD_SIZE);

        columnList.addActionListener(e -> {
            JComboBox cb = (JComboBox)e.getSource();
            int id = cb.getSelectedIndex();
            mainView.setGameDisplayId(id);
            lblGameDisplayId.setText("Экран для показа: " + id);
            mainView.setProperty("game_display_id", String.valueOf(id));
        });
        leftPane.add(columnList);

// ВЫБОР ПРЕДСТАРТОВГО ФОНА
        lblWelcomeScreenImg = new JLabel("Выбрать стартовый фон");
        lblWelcomeScreenImg.setAlignmentX(0.0f);
        leftPane.add(lblWelcomeScreenImg);
        JButton btnChooseWelcomeScreenImg = new JButton("Выбрать стартовый фон");
        btnChooseWelcomeScreenImg.setAlignmentX(0.0f);
        btnChooseWelcomeScreenImg.addActionListener(e -> {
            FileDialog fDialog = new FileDialog(controlPanelFrame, "Открыть", 0);
            fDialog.setVisible(true);
            if (fDialog.getFile() != null) {
                WELCOME_SCREEN_IMG = fDialog.getDirectory() + fDialog.getFile();
                lblWelcomeScreenImg.setText("Выбран " + fDialog.getFile());
                mainView.reloadWelcomeScreenBackgroundImage();
                mainView.setProperty("welcome_screen_img", WELCOME_SCREEN_IMG);
                log.info("Выбран стартовый фон: {}", WELCOME_SCREEN_IMG);
            }
        });
        leftPane.add(btnChooseWelcomeScreenImg);

// ВЫБОР ИГРОВОГО ФОНА
        lblGameScreenImg = new JLabel("Выбрать фон игры PNG");
        lblGameScreenImg.setAlignmentX(0.0f);
        leftPane.add(lblGameScreenImg);
        JButton btnChooseBack = new JButton("Выбрать фон игры");
        btnChooseBack.setAlignmentX(0.0f);
        btnChooseBack.addActionListener(e -> {
            FileDialog fDialog = new FileDialog(controlPanelFrame, "Открыть", 0);
            fDialog.setFile("*.png");
            fDialog.setVisible(true);
            if (fDialog.getFile() != null) {
                GAME_SCREEN_IMG = fDialog.getDirectory() + fDialog.getFile();
                lblGameScreenImg.setText("Выбран " + fDialog.getFile());
                mainView.reloadGameScreenBackgroundImage();
                mainView.setProperty("game_screen_img", GAME_SCREEN_IMG);
                log.info("Выбран игровой фон: {}", GAME_SCREEN_IMG);
            }
        });
        leftPane.add(btnChooseBack);

// ШРИФТ
        lblFont = new JLabel("Выбрать шрифт TTF");
        lblFont.setAlignmentX(0.0f);
        leftPane.add(lblFont);
        JButton btnChooseFont = new JButton("Выбрать шрифт");
        btnChooseFont.setAlignmentX(0.0f);

        btnChooseFont.addActionListener(e -> {
            FileDialog fDialog = new FileDialog(controlPanelFrame, "Открыть", 0);
            fDialog.setFile("*.ttf");
            fDialog.setVisible(true);

            GAME_FONT = fDialog.getDirectory() + fDialog.getFile();
            mainView.loadGameFont();
            lblFont.setText("Выбран " + fDialog.getFile());
            mainView.setProperty("game_font", GAME_FONT);
            log.info("Выбран шрифт игры: {}", GAME_FONT);
        });
        leftPane.add(btnChooseFont);

// Цветовая палитра
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setChooserPanels(
                new AbstractColorChooserPanel[]{
                        ColorChooserComponentFactory.getDefaultChooserPanels()[3]
                });

        lblFontColor = new JLabel("Выбран цвет " + getPickedColorValues());
        leftPane.add(lblFontColor);

        JButton colorChooserBtn = new JButton("Выбрать цвет текста");
        colorChooserBtn.addActionListener(e ->
                JColorChooser.createDialog(
                    null,
                    "Цветовая схема RGB",
                    true,
                    colorChooser,
                    ok -> {
                        GAME_FONT_COLOR = colorChooser.getColor();
                        lblFontColor.setText("Выбран цвет " + getPickedColorValues());
                        mainView.setColor(GAME_FONT_COLOR.getRed(), GAME_FONT_COLOR.getGreen(), GAME_FONT_COLOR.getBlue());
                        log.info("Выбран цвет шрифта: {}", GAME_FONT_COLOR);
                    },
                    null).setVisible(true));
        leftPane.add(colorChooserBtn);

// размер шрифта
        JPanel fontSizePanel = new JPanel();
        fontSizePanel.setLayout(new BoxLayout(fontSizePanel, X_AXIS));
        fontSizePanel.setAlignmentX(-10.0f);

        JLabel lblFontSize = new JLabel("Размер шрифта  ");
        fontSizePanel.add(lblFontSize);

        SpinnerNumberModel fontSizeModel = new SpinnerNumberModel(GAME_FONT_SIZE, 1, 500, 1);
        fontSizeSpinner = new JSpinner(fontSizeModel);
        fontSizeSpinner.setMaximumSize(new Dimension(80, 20));
        fontSizeSpinner.setAlignmentX(1.0f);
        fontSizeSpinner.addChangeListener(e -> {
            GAME_FONT_SIZE = (int) fontSizeSpinner.getValue();
            mainView.setProperty("game_font_size", String.valueOf(GAME_FONT_SIZE));
            log.info("Выбран размер шрифта: {}", GAME_FONT_SIZE);
        });
        fontSizePanel.add(fontSizeSpinner);

        leftPane.add(fontSizePanel);

// таблица с игроками
        playersTable = new JTable(new PlayersTableModel());

        JTableHeader playersTableHeader = playersTable.getTableHeader();
        manageColumns(playersTableHeader);

        playersTableHeader.setAlignmentX(0.0f);
        leftPane.add(playersTableHeader);
        playersTable.setAlignmentX(0.0f);
        leftPane.add(playersTable);

// дистанция
        JPanel distancePanel = new JPanel();
        distancePanel.setLayout(new BoxLayout(distancePanel, X_AXIS));
        distancePanel.setAlignmentX(1.0f);

        JLabel lblDistance = new JLabel("Дистанция  ");
        distancePanel.add(lblDistance);

        SpinnerNumberModel modelDistance = new SpinnerNumberModel(DISTANCE, 1.0, 5000.0, 1);
        spinnerDistance = new JSpinner(modelDistance);
        spinnerDistance.setValue(1000.0);
        spinnerDistance.setMaximumSize(new Dimension(80, 25));
        spinnerDistance.setAlignmentX(1.0f);
        spinnerDistance.addChangeListener(e -> {
            DISTANCE = (double) spinnerDistance.getValue();
            mainView.setProperty("distance", String.valueOf(DISTANCE));
            log.info("Дистанция установлена: {}", DISTANCE);
        });
        distancePanel.add(spinnerDistance);

        rightPane.add(distancePanel);

// WELCOME SCREEN
        JButton btnShowWelcomeScreen = new JButton("Предзапуск");
        btnShowWelcomeScreen.setAlignmentX(3f);
        btnShowWelcomeScreen.setAlignmentY(20f);
        btnShowWelcomeScreen.addActionListener(e -> mainView.toggleWelcomeScreen());
        rightPane.add(btnShowWelcomeScreen);

        JButton btnUpdateWelcomeScrn = new JButton("Обновить экран");
        btnUpdateWelcomeScrn.setAlignmentX(3f);
        btnUpdateWelcomeScrn.setAlignmentY(20.0f);
        btnUpdateWelcomeScrn.addActionListener( e -> mainView.updatePlayersOnWelcomeScreen());
        rightPane.add(btnUpdateWelcomeScrn);

// поле автосброс
        JLabel lblAutolaunch = new JLabel("Автосброс, сек");
        lblAutolaunch.setAlignmentX(1.0f);
        rightPane.add(lblAutolaunch);

        int[] delay = new int[1];
        delay[0] = 0;
        SpinnerNumberModel delaySpinnerModel = new SpinnerNumberModel(delay[0], 0, 10_000, 1);
        autolaunchSpinner = new JSpinner(delaySpinnerModel);
        autolaunchSpinner.setMaximumSize(MAX_FIELD_SIZE);
        autolaunchSpinner.setAlignmentX(1.0f);
        autolaunchSpinner.addChangeListener(e -> {
            delay[0] = (Integer) autolaunchSpinner.getValue();
            mainView.setWelcomeScreenAutoLaunchDelay(delay[0]);
            mainView.setProperty("welcome_screen_delay", String.valueOf(delay[0]));
        });
        rightPane.add(autolaunchSpinner);

// GAME START
        JButton btnStart = new JButton("Начать игру");
        btnStart.setAlignmentX(3f);
        btnStart.setAlignmentY(20.0f);
        btnStart.addActionListener(e -> mainView.prepareToStart());
        rightPane.add(btnStart);

// GAME DROP
        JButton btnDrop = new JButton("Сбросить игру");
        btnDrop.setAlignmentX(3f);
        btnDrop.addActionListener(e -> mainView.restartGame());
        rightPane.add(btnDrop);

// APPLY CONFIG
        JButton btnApplyConfig = new JButton("Загрузить конфигурацию");
        btnApplyConfig.setAlignmentX(3f);
        btnApplyConfig.addActionListener(e -> applyConfiguration());
        rightPane.add(btnApplyConfig);

        rightPane.add(Box.createVerticalGlue());
        controlPanelFrame.getContentPane().add(common);
        controlPanelFrame.validate();
    }

    void updatePlayersTable() {
        playersTable.repaint();
    }

    void showWarning(String text) {
        JOptionPane.showMessageDialog(controlPanelFrame, text, "ОШИБКА", JOptionPane.WARNING_MESSAGE);
    }


//----------------------------------------------------------------------------------------------------------------------

    private void applyConfiguration() {
        try {
            int id = Integer.parseInt(mainView.getProperty("game_display_id"));
            mainView.setGameDisplayId(id);
            lblGameDisplayId.setText("Экран для показа: " + id);
        } catch (NumberFormatException e) {
            log.error("ID игрового дисплея не загружен");
        }

        try {
            DISTANCE = Double.parseDouble(mainView.getProperty("distance"));
            spinnerDistance.setValue(DISTANCE);
        } catch (NumberFormatException e) {
            log.error("Дистанция не загружена");
        }

        try {
            GAME_FONT = mainView.getProperty("game_font");
            lblFont.setText("Выбран ..." + GAME_FONT.substring(GAME_FONT.length() - 15));
        } catch (Exception e) {
            log.error("Шрифт не загружен");
        }

        try {
            GAME_FONT_COLOR = mainView.getColor();
            lblFontColor.setText("Выбран цвет " + getPickedColorValues());
        } catch (Exception e) {
            log.error("Цвет шрифта не загружен");
        }

        try {
            GAME_FONT_SIZE = Integer.parseInt(mainView.getProperty("game_font_size"));
            fontSizeSpinner.setValue(GAME_FONT_SIZE);
        } catch (NumberFormatException e) {
            log.error("Размер шрифт не загружен");
        }

        try {
            GAME_SCREEN_IMG = mainView.getProperty("game_screen_img");
            lblGameScreenImg.setText("Выбран ..." + GAME_SCREEN_IMG.substring(GAME_SCREEN_IMG.length() - 15));
        } catch (Exception e) {
            log.error("Игровой фон не загружен");
        }

        try {
            WELCOME_SCREEN_IMG = mainView.getProperty("welcome_screen_img");
            lblWelcomeScreenImg.setText("Выбран ..." + WELCOME_SCREEN_IMG.substring(WELCOME_SCREEN_IMG.length() - 15));
        } catch (Exception e) {
            log.error("Предстартовый фон не загружен");
        }

        try {
            int delay = Integer.parseInt(mainView.getProperty("welcome_screen_delay"));
            mainView.setWelcomeScreenAutoLaunchDelay(delay);
            autolaunchSpinner.setValue(delay);
        } catch (NumberFormatException e) {
            log.error("Задержка предстартового экрана не загружена");
        }
    }

    private String getPickedColorValues() {
        return GAME_FONT_COLOR.toString().substring(14);
    }

    private void manageColumns(JTableHeader playersTableHeader) {
        playersTableHeader.getColumnModel().getColumn(0).setHeaderValue("ID");
        playersTableHeader.getColumnModel().getColumn(0).setMaxWidth(20);

        playersTableHeader.getColumnModel().getColumn(1).setHeaderValue("Имя");
        playersTableHeader.getColumnModel().getColumn(2).setHeaderValue("Персонаж");
        playersTableHeader.getColumnModel().getColumn(3).setHeaderValue("Сдвиг по Y");
    }

    private static class PlayersTableModel extends AbstractTableModel {

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3; //|| columnIndex == 4;
        }

        @Override
        public int getRowCount() {
            return 4;
        }

        @Override
        public int getColumnCount() {
            return 4;//5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0: return playersMap.get(rowIndex) == null ? "" : playersMap.get(rowIndex).getId();
                case 1: return playersMap.get(rowIndex) == null ? "" : playersMap.get(rowIndex).getPlayerName();
                case 2: return playersMap.get(rowIndex) == null ? "" : playersMap.get(rowIndex).getTextureName();
                case 3: return playersMap.get(rowIndex) == null ? "" : playersMap.get(rowIndex).getPercentOffsetY();
                case 4: return playersMap.get(rowIndex) == null ? "" : playersMap.get(rowIndex).getSpriteSize();
            }
            return "не определена";
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3) {
                playersMap.get(rowIndex).setPercentOffsetY(Float.parseFloat((String) aValue));
            }
            if (columnIndex == 4) {
                playersMap.get(rowIndex).resizeSprite(Integer.parseInt((String) aValue));
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
