package org.squonk.chemaxon.io;

import chemaxon.marvin.MolPrinter;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MolPrint {
    static BufferedImage createTestImage() throws IOException {
        // Create a molecule
        Molecule mol = MolImporter.importMol("CN1C=NC2=C1C(=O)N(C)C(=O)N2C");
        // Create a writable image
        BufferedImage im = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        // Clear background
        g.setColor(Color.white);
        g.fillRect(0, 0, im.getWidth(), im.getHeight());
        // Draw the bounding rectangle
        g.setColor(Color.red);
        Rectangle rect = new Rectangle(20, 20, 360, 200);
        g.draw(rect);
        // Paint the molecule
        MolPrinter molPrinter = new MolPrinter(mol);
        molPrinter.setScale(molPrinter.maxScale(rect)); // fit image in the rectangle
        molPrinter.setBackgroundColor(Color.white);
        molPrinter.paint(g, rect);
        return im;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage im = createTestImage();
        ImageIO.write(im, "png", new File("/Users/timbo/tmp/test.png"));
    }
}