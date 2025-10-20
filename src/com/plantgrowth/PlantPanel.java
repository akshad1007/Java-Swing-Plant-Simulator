package com.plantgrowth;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * PlantPanel draws a full plant (roots, stem, leaves, flower) programmatically
 * using Graphics2D so the plant is always visible and scales nicely.
 * It exposes setStage(int) to change growth stage and includes a simple
 * animation when stage changes.
 */
public class PlantPanel extends JPanel {
    private int stage = 0; // 0..6 (0..3 growth, 4..6 dying -> 6 = dead)
    private float animScale = 1.0f;
    private Timer animTimer;

    public PlantPanel() {
        setBackground(new Color(240, 248, 255)); // Light blue background
        setPreferredSize(new Dimension(500, 320));
        setOpaque(true);

        // Simple animation timer
        animTimer = new Timer(40, new ActionListener() {
            private int ticks = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                ticks++;
                animScale = 1.0f + 0.05f * (float)Math.sin(ticks * 0.3);
                if (ticks > 30) {
                    animTimer.stop();
                    ticks = 0;
                    animScale = 1.0f;
                }
                repaint();
            }
        });

        // MouseAdapter example: click inside panel toggles a small bounce animation
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // trigger a short animated pulse
                if (!animTimer.isRunning()) {
                    animTimer.start();
                }
            }
        });
    }

    public void setStage(int stage) {
        // allow stages up to 6 (including dying/dead stages)
        this.stage = Math.max(0, Math.min(stage, 6));
        // start animation
        if (!animTimer.isRunning()) animTimer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // center coordinates
        int cx = w/2;
        int groundY = (int)(h*0.65);

        // draw soil
        g2.setColor(new Color(169, 117, 77));
        g2.fillRect(0, groundY, w, h-groundY);

        // draw roots (varying with dying stages)
        Stroke old = g2.getStroke();
        if (stage <= 3) {
            g2.setColor(new Color(120, 80, 50));
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(cx, groundY, cx-30, groundY+40);
            g2.drawLine(cx, groundY, cx+30, groundY+40);
            g2.drawLine(cx-10, groundY, cx-50, groundY+60);
            g2.drawLine(cx+10, groundY, cx+50, groundY+60);
        } else if (stage == 4) {
            // roots starting to shrink
            g2.setColor(new Color(110, 70, 40));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawLine(cx, groundY, cx-20, groundY+30);
            g2.drawLine(cx, groundY, cx+20, groundY+30);
        } else if (stage == 5) {
            // more root loss
            g2.setColor(new Color(100, 60, 35));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(cx, groundY, cx-15, groundY+20);
            g2.drawLine(cx, groundY, cx+15, groundY+20);
        } else {
            // dead - minimal broken roots
            g2.setColor(new Color(90, 50, 30));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(cx-5, groundY+5, cx-20, groundY+25);
        }
        g2.setStroke(old);

        // stem base - grows with stage
        // stem and leaves adjust when dying
        int stemHeight;
        if (stage <= 3) {
            stemHeight = 30 + stage * 40;
        } else if (stage == 4) {
            stemHeight = 30 + 3 * 40 - 20; // slightly shorter
        } else if (stage == 5) {
            stemHeight = 30 + 3 * 40 - 40; // much shorter
        } else {
            stemHeight = 20; // dead stump
        }
        int stemTopY = groundY - (int)(stemHeight * animScale);

        if (stage <= 3) {
            g2.setColor(new Color(34, 139, 34));
            g2.setStroke(new BasicStroke(8));
            g2.drawLine(cx, groundY, cx, stemTopY);
        } else if (stage == 4) {
            g2.setColor(new Color(120, 100, 50));
            g2.setStroke(new BasicStroke(6));
            g2.drawLine(cx, groundY, cx, stemTopY);
        } else if (stage == 5) {
            g2.setColor(new Color(110, 90, 45));
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(cx, groundY, cx, stemTopY);
        } else {
            // dead stump
            g2.setColor(new Color(100, 65, 40));
            g2.setStroke(new BasicStroke(10));
            g2.drawLine(cx, groundY, cx, stemTopY + 10);
        }

        // leaves depending on stage
        g2.setStroke(new BasicStroke(1));
        // leaves
        switch (stage) {
            case 1 -> {
                g2.setColor(new Color(50, 205, 50));
                g2.fillOval(cx-70, groundY-30 - stage*10, 60, 30);
                g2.fillOval(cx+10, groundY-30 - stage*10, 60, 30);
            }
            case 2 -> {
                g2.setColor(new Color(34, 139, 34));
                g2.fillOval(cx-90, groundY-60 - stage*10, 70, 35);
                g2.fillOval(cx+20, groundY-60 - stage*10, 70, 35);
            }
            case 3 -> {
                g2.setColor(new Color(34, 139, 34));
                g2.fillOval(cx-90, groundY-60 - stage*10, 80, 40);
                g2.fillOval(cx+10, groundY-60 - stage*10, 80, 40);
            }
            case 4 -> {
                // leaves droop and brown
                g2.setColor(new Color(139, 69, 19));
                g2.fillOval(cx-60, groundY-20, 50, 20);
                g2.fillOval(cx+10, groundY-20, 50, 20);
            }
            case 5 -> {
                // very few small leaves
                g2.setColor(new Color(120, 60, 20));
                g2.fillOval(cx-30, groundY-10, 30, 12);
            }
            default -> {
                // dead - no leaves
            }
        }

        // flower / top decoration
        switch (stage) {
            case 1 -> {
                g2.setColor(new Color(255, 240, 245));
                int budY = stemTopY - 10;
                g2.fillOval(cx-8, budY, 16, 16);
            }
            case 2 -> {
                g2.setColor(new Color(255, 182, 193));
                int flowerY2 = stemTopY - 20;
                g2.fillOval(cx-15, flowerY2, 24, 24);
            }
            case 3 -> {
                g2.setColor(new Color(255, 105, 180));
                int flowerY = stemTopY - 30;
                g2.fillOval(cx-30, flowerY, 24, 24);
                g2.fillOval(cx, flowerY, 24, 24);
                g2.setColor(new Color(255, 215, 0));
                g2.fillOval(cx-10, flowerY+8, 20, 20);
            }
            case 4 -> {
                // wilted flower
                g2.setColor(new Color(205, 92, 92));
                int flowerY4 = stemTopY - 20;
                g2.fillOval(cx-15, flowerY4, 20, 12);
            }
            case 5 -> {
                // no flower, maybe a dark bud
                g2.setColor(new Color(120, 60, 60));
                int flowerY5 = stemTopY - 10;
                g2.fillOval(cx-8, flowerY5, 10, 8);
            }
            default -> {
                // dead - nothing on top
            }
        }

        // seed indicator for stage 0
        if (stage == 0) {
            g2.setColor(new Color(102, 51, 0));
            g2.fillOval(cx-6, groundY-10, 12, 8);
        }

        g2.dispose();
    }
}
