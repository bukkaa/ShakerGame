// 
// Decompiled by Procyon v0.5.30
// 

package bukkaa.mediahouse.ShakerGameWS.model;

import bukkaa.mediahouse.ShakerGameWS.controller.CacheSprite;
import bukkaa.mediahouse.ShakerGameWS.model.components.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ShakeGamer extends JComponent {
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    public Sprite sprite;
    public int savedSumLevel = 0;

    private int id;
    private String playerName;
    private String textureName;
    private boolean mirror;
    private Timer timer;
    private int sumLevel = 0;
    private int futureX = 0;
    private float percentOffsetY;
    
    public ShakeGamer(String playerName, String textureName, boolean mirror) {
        id = COUNT.getAndIncrement();
        this.playerName = playerName;
        this.textureName = textureName;
        this.mirror = mirror;

        sprite = new Sprite(id, textureName, mirror);
        createTimer();
        setSize(sprite.SpriteWidth, sprite.SpriteHeight);
    }

    public ShakeGamer(String playerName, String textureName, int id) {
        this.playerName = playerName;
        this.textureName = textureName;
        this.id = id;
    }

    private void createTimer() {
        timer = new Timer(20, e -> {
            boolean toUpdate = sprite.update();
            if (toUpdate) {
                paintImmediately(0, 0, sprite.SpriteWidth, sprite.SpriteHeight);
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        sprite.drawSprite(g, 0, 0);
    }
    
    public void dispose() {
        timer.stop();
        sprite = null;
    }
    
    public int getSumLevel() {
        return sumLevel;
    }
    
    public void addToSumLevel(int delta) {
        sumLevel += delta;
    }
    
    public void setFutureX(int X) {
        futureX = X;
    }
    
    public int getFutureX() {
        return futureX;
    }

    @Override
    public String toString() {
        return "{id: <" + id +
                ">, name: <" + playerName +
                ">, texture: <" + textureName + ">}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public float getPercentOffsetY() {
        return percentOffsetY;
    }

    public void setPercentOffsetY(float percentOffsetY) {
        this.percentOffsetY = percentOffsetY;
    }

    public static void dropCounter() {
        COUNT.set(0);
    }

    public void setSpriteSize(int size) {
        sprite.setImageSize(size);
    }

    public int getSpriteSize() {
        return sprite.getImageSize();
    }

    public void resizeSprite(int newSize) {
        sprite = null;
        timer = null;

        CacheSprite.removeAll();
        sprite = new Sprite(id, newSize, textureName, mirror);
        createTimer();
    }
}
