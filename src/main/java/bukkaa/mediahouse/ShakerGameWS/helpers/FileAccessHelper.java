package bukkaa.mediahouse.ShakerGameWS.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static bukkaa.mediahouse.ShakerGameWS.model.Constants.*;

public class FileAccessHelper {
    private static final Logger log = LoggerFactory.getLogger(FileAccessHelper.class);

    private static final File spritesFolder = new File(SPRITE_FOLDER);

    public static String[] getCountdownImages() {
        String[] fileNames = new File(COUNTDOWN_FOLDER).list((dir, name) -> !name.startsWith("!"));
        log.info("List of countdown images available: {}", Arrays.toString(fileNames));
        return fileNames;
    }

    public static Map<String, String> getSpriteMap() {
        String[] spriteNames = spritesFolder.list((dir, name) -> !name.startsWith("!"));

        if (spriteNames == null) {
            log.error("Отсутствуют доступные наборы спрайтов в <{}>", SPRITE_FOLDER);
        } else {
            HashMap<String, String> spriteMap = new HashMap<>(spriteNames.length);

            Arrays.stream(spriteNames).forEach(name -> {
                try {
                    spriteMap.put(name, getSpriteLabelWebPath(name));
                } catch (NullPointerException npe) {
                    if (log.isDebugEnabled()) {
                        log.error("Папка <" + SPRITE_FOLDER + File.separator + name + "> пуста или не существует", npe);
                    } else {
                        log.error("Папка <{}/{}> пуста или не существует", SPRITE_FOLDER, name);
                    }
                }
            });
            return spriteMap;
        }
        return null;
    }

    private static String getSpriteLabelWebPath(String spriteName){
        File spriteFolder = new File(spritesFolder, spriteName);

        String[] allFilesList = spriteFolder.list();
        if (allFilesList == null || allFilesList.length == 0) {
            log.error("No files in '{}' folder", spriteFolder);
            return "";
        }

        String[] labelFiles = spriteFolder.list((dir, filename) -> filename.contains("label") || filename.startsWith("#"));

        String labelName = (labelFiles != null && labelFiles.length > 0) ?
                labelFiles[0] :
                allFilesList[0];

        return makeSpriteWebPath(spriteName, labelName);
    }

    private static String makeSpriteWebPath(String spriteName, String labelName) {
        String result = String.format("/sprite/%s/%s", spriteName, labelName);
        log.info("Sprite <{}> result web path = '{}'", spriteName, result);

        return result;
    }

    public static String getSpritesFolderAbsPath() {
        String result = new File(SPRITE_FOLDER).getAbsolutePath() + File.separator;
        log.info("Sprite folder absolute path = {}", result);

        return result;
    }

    public static String getWebBackgroundFolderAbsPath() {
        String result = new File(WEB_BACK_FOLDER).getAbsolutePath() + File.separator;
        log.info("Web background images folder absolute path = {}", result);

        return result;
    }

    public static String getBackgroundImageWebPath() {
        File webBackgroundsFolder = new File(WEB_BACK_FOLDER);

        String[] allFilesList = webBackgroundsFolder.list();
        if (allFilesList == null || allFilesList.length == 0) {
            log.error("No files in '{}' folder", webBackgroundsFolder);
            return "";
        }

        String[] backgroundFiles = webBackgroundsFolder.list((dir, name) -> name.startsWith("#"));

        String backgroundImg = (backgroundFiles != null && backgroundFiles.length > 0) ?
                backgroundFiles[0] :
                allFilesList[0];

        String webPath = String.format("/back_img/%s", backgroundImg);
        log.info("Web background image result web path = '{}'", webPath);

        return webPath;
    }
}
