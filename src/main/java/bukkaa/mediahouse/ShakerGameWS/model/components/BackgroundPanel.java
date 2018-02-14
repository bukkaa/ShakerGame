package bukkaa.mediahouse.ShakerGameWS.model.components;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JLayeredPane {
    public static final int SCALED = 0;
    public static final int TILED = 1;
    public static final int ACTUAL = 2;
    private Paint painter;
    private Image image;
    private int style = 0;
    private float alignmentX = 0.5f;
    private float alignmentY = 0.5f;
    private boolean isTransparentAdd;
    
    public BackgroundPanel(Image image) {
        this(image, 0);
    }
    
    public BackgroundPanel(Image image, int style) {
        setOpaque(isTransparentAdd = true);
        setImage(image);
        setStyle(style);
        setLayout(new BorderLayout());
    }
    
    public BackgroundPanel(Image image, int style, float alignmentX, float alignmentY) {
        isTransparentAdd = true;
        setImage(image);
        setStyle(style);
        setImageAlignmentX(alignmentX);
        setImageAlignmentY(alignmentY);
        setLayout(new BorderLayout());
    }
    
    public BackgroundPanel(Paint painter) {
        isTransparentAdd = true;
        setPaint(painter);
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setStyle(final int style) {
        this.style = style;
        this.repaint();
    }
    
    public void setPaint(final Paint painter) {
        this.painter = painter;
        this.repaint();
    }
    
    public void setImageAlignmentX(final float alignmentX) {
        this.alignmentX = ((alignmentX > 1.0f) ? 1.0f : ((alignmentX < 0.0f) ? 0.0f : alignmentX));
        this.repaint();
    }
    
    public void setImageAlignmentY(final float alignmentY) {
        this.alignmentY = ((alignmentY > 1.0f) ? 1.0f : ((alignmentY < 0.0f) ? 0.0f : alignmentY));
        this.repaint();
    }
    
    public void add(final JComponent component) {
        this.add(component, null);
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (this.image == null) {
            return super.getPreferredSize();
        }
        return new Dimension(this.image.getWidth(null), this.image.getHeight(null));
    }
    
    public void add(final JComponent component, final Object constraints) {
        if (this.isTransparentAdd) {
            this.makeComponentTransparent(component);
        }
        super.add(component, constraints);
    }
    
    public void setTransparentAdd(final boolean isTransparentAdd) {
        this.isTransparentAdd = isTransparentAdd;
    }
    
    private void makeComponentTransparent(final JComponent component) {
        component.setOpaque(false);
        if (component instanceof JScrollPane) {
            final JScrollPane scrollPane = (JScrollPane)component;
            final JViewport viewport = scrollPane.getViewport();
            viewport.setOpaque(false);
            final Component c = viewport.getView();
            if (c instanceof JComponent) {
                ((JComponent)c).setOpaque(false);
            }
        }
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.painter != null) {
            final Dimension d = this.getSize();
            final Graphics2D g2 = (Graphics2D)g;
            g2.setPaint(this.painter);
            g2.fill(new Rectangle(0, 0, d.width, d.height));
        }
        if (this.image == null) {
            return;
        }
        switch (this.style) {
            case 0: {
                drawScaled(g);
                break;
            }
            case 1: {
                drawTiled(g);
                break;
            }
            case 2: {
                drawActual(g);
                break;
            }
            default: {
                drawScaled(g);
                break;
            }
        }
    }
    
    private void drawScaled(final Graphics g) {
        Dimension d = getSize();
        g.drawImage(image, 0, 0, d.width, d.height, null);
    }
    
    private void drawTiled(final Graphics g) {
        final Dimension d = this.getSize();
        final int width = this.image.getWidth(null);
        final int height = this.image.getHeight(null);
        for (int x = 0; x < d.width; x += width) {
            for (int y = 0; y < d.height; y += height) {
                g.drawImage(this.image, x, y, null, null);
            }
        }
    }
    
    private void drawActual(final Graphics g) {
        final Dimension d = this.getSize();
        final Insets insets = this.getInsets();
        final int width = d.width - insets.left - insets.right;
        final int height = d.height - insets.top - insets.left;
        final float x = (width - this.image.getWidth(null)) * this.alignmentX;
        final float y = (height - this.image.getHeight(null)) * this.alignmentY;
        g.drawImage(this.image, (int)x + insets.left, (int)y + insets.top, this);
    }
}
