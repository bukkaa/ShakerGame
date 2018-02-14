package bukkaa.mediahouse.ShakerGameWS.model.components;

import java.awt.image.BufferedImage;

public class Frame
{
    private BufferedImage frame;
    private int duration;
    
    public Frame(final BufferedImage frame, final int duration) {
        this.frame = frame;
        this.duration = duration;
    }
    
    public BufferedImage getFrame() {
        return this.frame;
    }
    
    public void setFrame(final BufferedImage frame) {
        this.frame = frame;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(final int duration) {
        this.duration = duration;
    }
}
