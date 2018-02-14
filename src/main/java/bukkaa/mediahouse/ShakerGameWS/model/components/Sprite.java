package bukkaa.mediahouse.ShakerGameWS.model.components;

import bukkaa.mediahouse.ShakerGameWS.controller.CacheSprite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static bukkaa.mediahouse.ShakerGameWS.model.Constants.SPRITE_FOLDER;
import static bukkaa.mediahouse.ShakerGameWS.model.Constants.TILE_HEIGHT;

public class Sprite {
    private static final Logger log = LoggerFactory.getLogger(Sprite.class);

    private int percentSize = 100;
    private int id;
    private Animation standing;
    private Animation running;
    private Animation finishing;
    private Animation current;
    public int SpriteWidth;
    public int SpriteHeight;
    
    public Sprite(int id, String name, boolean mirror) {
        this.id = id;
        init(name, mirror);
    }

    public Sprite(int id, int size, String name, boolean mirror) {
        this.id = id;
        percentSize = size < 0 ? 100 : size;

        init(name, mirror);
    }

    private void init(String name, boolean mirror) {
        int kStand;
        int kRun;
        int kFinish;
        String prefixStand = "Idle";
        String prefixRun = "Run";
        String prefixFinish = "Jump";

        kStand = getCountSpriteAnimation(name, prefixStand);
        if (kStand == 0) {
            prefixStand = "Stand";
            kStand = getCountSpriteAnimation(name, prefixStand);
        }
        kRun = getCountSpriteAnimation(name, prefixRun);
        kFinish = getCountSpriteAnimation(name, prefixFinish);

        if (kFinish == 0) {
            prefixFinish = "Finish";
            kFinish = getCountSpriteAnimation(name, prefixFinish);
        }

        CacheSprite.CacheRecord cacheRecord = CacheSprite.getCache(name);

        BufferedImage[] animationStand;
        BufferedImage[] animationRun;
        BufferedImage[] animationFinish;

        if (cacheRecord == null) {
            animationStand = new BufferedImage[kStand];
            animationRun = new BufferedImage[kRun];
            animationFinish = new BufferedImage[kFinish];

            for (int i = 0; i < animationStand.length; i++) {
                animationStand[i] = loadScaledImage(name + "/" + prefixStand + " (" + (i + 1) + ")", mirror);
                if (animationStand[i].getWidth() > SpriteWidth) {
                    SpriteWidth = animationStand[i].getWidth();
                }
                if (animationStand[i].getHeight() > SpriteHeight) {
                    SpriteHeight = animationStand[i].getHeight();
                }
            }
            log.info("Fully loaded frames: {}/{}", name, prefixStand);

            for (int i = 0; i < animationRun.length; i++) {
                animationRun[i] = loadScaledImage(name + "/" + prefixRun + " (" + (i + 1) + ")", mirror);
            }
            log.info("Fully loaded frames: {}/{}", name, prefixRun);

            for (int i = 0; i < animationFinish.length; i++) {
                animationFinish[i] = loadScaledImage(name + "/" + prefixFinish + " (" + (i + 1) + ")", mirror);
            }
            log.info("Fully loaded frames: {}/{}", name, prefixFinish);

            CacheSprite.CacheRecord rec = new CacheSprite.CacheRecord();
            rec.stand = animationStand;
            rec.finish = animationFinish;
            rec.run = animationRun;
            rec.spriteName = name;
            CacheSprite.addCache(rec);
        }
        else {
            animationStand = cacheRecord.stand;
            animationRun = cacheRecord.run;
            animationFinish = cacheRecord.finish;
            for (int i = 0; i < kStand; ++i) {
                if (animationStand[i].getWidth() > SpriteWidth) {
                    SpriteWidth = animationStand[i].getWidth();
                }
                if (animationStand[i].getHeight() > SpriteHeight) {
                    SpriteHeight = animationStand[i].getHeight();
                }
            }
        }
        standing = new Animation(1, animationStand, 5);
        running = new Animation(2, animationRun, 5);
        finishing = new Animation(3, animationFinish, 5);
        (current = standing).start();
    }

    private static int getCountSpriteAnimation(String sprite, String prefix) {
        File folderSprite = new File("resources/sprite/" + sprite);
        File[] listOfSprites = folderSprite.listFiles();
        int dotFile = 0;

        assert listOfSprites != null;
        for (File listOfSprite : listOfSprites) {
            if (listOfSprite.isFile() && listOfSprite.getName().startsWith(prefix)) {
                ++dotFile;
            }
        }
        return dotFile;
    }
    
    private BufferedImage loadScaledImage(String filename, boolean mirror) {
        BufferedImage img = loadImage(filename);
        if (img != null) {
            log.debug("Original - H x W: <{}> ({} x {})", filename, img.getHeight(), img.getWidth());

            double scale = ((double) TILE_HEIGHT) / ((double) img.getHeight());
            int width = (int) Math.round(img.getWidth() * scale);

            log.debug("Tile height: {}\tScale factor: {}", TILE_HEIGHT, scale);

            img = resize(img, width, TILE_HEIGHT);

            log.debug("New - H x W: <{}> ({} x {})", filename, img.getHeight(), img.getWidth());

            if (mirror) {
                AffineTransform at = new AffineTransform();
                at.concatenate(AffineTransform.getScaleInstance(-1.0, 1.0));
                at.concatenate(AffineTransform.getTranslateInstance(-img.getWidth(), 0.0));
                BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), 2);
                Graphics2D g = newImage.createGraphics();
                g.transform(at);
                g.drawImage(img, 0, 0, null);
                g.dispose();
                img = newImage;
            }
        }
        return img;
    }

    private BufferedImage loadImage(String file) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(new File(SPRITE_FOLDER + "/" + file + ".png"));
            log.debug("Image loaded: {}/{}.png", SPRITE_FOLDER, file);
        }
        catch (IOException e) {
            log.error("Error loading image: " + SPRITE_FOLDER + "/" + file + ".png ", e);
        }
        return sprite;
    }
    
    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, 4);
        BufferedImage dimg = new BufferedImage(newW, newH, 2);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
    
    public void setAnimationStanding() {
        if (current.id == standing.id) {
            return;
        }
        current.stop();
        current.reset();
        (current = standing).start();
    }
    
    public void setAnimationRunning() {
        if (current.id == running.id) {
            return;
        }
        current.stop();
        (current = running).start();
    }
    
    public void setAnimationFinishing() {
        if (current.id == finishing.id) {
            return;
        }
        current.stop();
        current.reset();
        (current = finishing).start();
    }

    public int getImageSize() {
        return percentSize;
    }

    public void setImageSize(int size) {
        this.percentSize = size;
    }

    public boolean isRunning() {
        return current.id == running.id;
    }
    
    public Animation getCurrentAnimation() {
        return current;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean update() {
        return current.update();
    }
    
    public void drawSprite(Graphics g, int x, int y) {
        g.drawImage(current.getSprite(), x, y, null);
    }
    
    public void drawSprite(Graphics g, int x, int y, int scaleX, int scaleY) {
        g.drawImage(current.getSprite(), x, y, scaleX, scaleY, null);
    }
}
