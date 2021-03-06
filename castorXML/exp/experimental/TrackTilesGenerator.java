package experimental;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jfreerails.client.renderer.TrackPieceRendererImpl;
import jfreerails.server.OldWorldImpl;
import jfreerails.server.parser.Track_TilesHandlerImpl;
import jfreerails.world.track.TrackConfiguration;
import jfreerails.world.track.TrackRule;

/**
 * Generates track graphic image files.
 * 
 * @author Luke
 */
public class TrackTilesGenerator extends JPanel {

    private static final long serialVersionUID = 3618982273966487859L;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane();
        frame.add(scrollPane);
        TrackTilesGenerator trackTilesGenerator = new TrackTilesGenerator();
        trackTilesGenerator.setPreferredSize(trackTilesGenerator
                .getSize4Panel());
        scrollPane.setViewportView(trackTilesGenerator);
        frame.setSize(500, 500);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private final ImageManagerImpl imageManager = new ImageManagerImpl(
            "/experimental/", "/experimental/");

    private List<TrackRule> rules;

    private TrackRenderer tr;

    CubicCurve2D.Double[] track;

    public TrackTilesGenerator() {
        Point2D.Double start, end, one, two;
        track = new CubicCurve2D.Double[3];
        track[0] = new CubicCurve2D.Double();

        start = new Point2D.Double(150, 300);
        end = new Point2D.Double(450, 150);
        one = controlPoint(start);
        two = controlPoint(end);
        track[0].setCurve(start, one, two, end);
        track[1] = TrackRenderer.createAdjacentCurve(track[0], 0, 0);
        track[2] = TrackRenderer.createAdjacentCurve(track[0], -60, -60);
        tr = new TrackRenderer();
        URL track_xml_url = OldWorldImpl.class
                .getResource("/jfreerails/data/track_tiles.xml");

        Track_TilesHandlerImpl trackSetFactory = new Track_TilesHandlerImpl(
                track_xml_url);
        rules = trackSetFactory.getRuleList();
        generateTiles();

    }

    private Point2D.Double controlPoint(Point2D.Double from) {
        double weight = 0.3;
        double x = from.getX() * weight + 300 * (1 - weight);
        double y = from.getY() * weight + 300 * (1 - weight);
        return new Point2D.Double(x, y);
    }

    private void generateTiles() {

        for (TrackRule rule : rules) {
            TrackRule.TrackCategories category = rule.getCategory();
            Image icon;
            if (category.equals(TrackRule.TrackCategories.bridge)
                    || category.equals(TrackRule.TrackCategories.station)) {
                tr.setIcon(rule.getTypeName());
                icon = tr.icon;
            } else {
                icon = null;
            }
            if (category.equals(TrackRule.TrackCategories.tunnel)) {
                tr.tunnel = true;
            } else {
                tr.tunnel = false;
            }
            tr.doubleTrack = rule.isDouble();

            for (int i = 0; i < 512; i++) {
                if (rule.testTrackPieceLegality(i)) {

                    String fileName = TrackPieceRendererImpl.generateFilename(
                            i, rule.getTypeName());
                    TrackConfiguration conf = TrackConfiguration
                            .from9bitTemplate(i);

                    Image smallImage = imageManager.newBlankImage(60, 60);
                    Graphics2D g2 = (Graphics2D) smallImage.getGraphics();
                    tr.paintTrackConf(g2, conf);

                    // Draw icon. Used for bridges and stations.
                    if (null != icon) {
                        int x = 30 - icon.getWidth(null) / 2;
                        int y = 30 - icon.getHeight(null) / 2;
                        g2.drawImage(icon, x, y, null);
                    }

                    g2.dispose();
                    imageManager.setImage(fileName, smallImage);
                }
            }

        }
        try {
            imageManager.writeAllImages();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Dimension getSize4Panel() {
        int height = 90 * rules.size();
        int width = 0;
        int lastWidth = 0;
        for (TrackRule rule : rules) {
            width = Math.max(width, lastWidth);
            lastWidth = 0;
            Iterator<TrackConfiguration> it = rule
                    .getLegalConfigurationsIterator();
            while (it.hasNext()) {
                lastWidth += 60;
            }
        }
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (TrackRule rule : rules) {

            String typeName = rule.getTypeName();
            typeName += rule.isDouble() ? " (Double) " : " (Single)";

            g.drawString(typeName, 10, 10);
            g.translate(0, 30);
            Graphics2D g2 = (Graphics2D) g.create();

            for (int i = 0; i < 512; i++) {
                if (rule.testTrackPieceLegality(i)) {
                    String fileName = TrackPieceRendererImpl.generateFilename(
                            i, rule.getTypeName());
                    Image tile;
                    try {
                        tile = imageManager.getImage(fileName);
                        g2.drawImage(tile, 0, 0, null);
                        g2.translate(60, 0);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

            g.translate(0, 60);

        }

    }

}