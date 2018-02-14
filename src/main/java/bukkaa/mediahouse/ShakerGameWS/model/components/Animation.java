package bukkaa.mediahouse.ShakerGameWS.model.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    private static final Logger log = LoggerFactory.getLogger(Animation.class);

    public int id;
    private int frameCount = 0;
    private int frameDelay;
    private int currentFrame = 0;
    private int animationDirection = 1;
    private int totalFrames;
    private boolean stopped;
    private List<Frame> frames = new ArrayList<>();
    
    public Animation(int id, BufferedImage[] frames, int frameDelay) {
        this.id = id;
        this.frameDelay = frameDelay;
        this.stopped = true;

        for (BufferedImage frame : frames) {
            addFrame(frame, frameDelay);
        }

        totalFrames = this.frames.size();
    }
    
    public void start() {
        if (!this.stopped) {
            return;
        }
        if (this.frames.size() == 0) {
            return;
        }
        this.stopped = false;
    }
    
    public void stop() {
        if (this.frames.size() == 0) {
            return;
        }
        this.stopped = true;
    }
    
    public void restart() {
        if (this.frames.size() == 0) {
            return;
        }
        this.stopped = false;
        this.currentFrame = 0;
    }
    
    public void reset() {
        this.stopped = true;
        this.frameCount = 0;
        this.currentFrame = 0;
    }
    
    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            log.error("Invalid duration: {}", duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }
        frames.add(new Frame(frame, duration));
        currentFrame = 0;
    }
    
    public BufferedImage getSprite() {
        return this.frames.get(this.currentFrame).getFrame();
    }
    
    public boolean update() {
        boolean toUpdate = false;
        if (!stopped) {
            ++frameCount;
            if (frameCount > frameDelay) {
                toUpdate = true;
                frameCount = 0;
                currentFrame += animationDirection;
                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                }
                else if (currentFrame < 0) {
                    currentFrame = this.totalFrames - 1;
                }
            }
        }
        return toUpdate;
    }
}
