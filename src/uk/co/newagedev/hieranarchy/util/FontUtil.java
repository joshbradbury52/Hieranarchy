package uk.co.newagedev.hieranarchy.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import uk.co.newagedev.hieranarchy.graphics.SpriteRegistry;
import uk.co.newagedev.hieranarchy.graphics.TextObject;
import uk.co.newagedev.hieranarchy.testing.Main;

public class FontUtil {

	public static TextObject getStringFromFont(Font font, String text, Color colour) {
		String name = font.getFontName() + "-" + font.getSize() + "-" + text;

		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getHeight();
		g.dispose();

		int imageWidth = (int) Math.pow(2, (int) Math.ceil(MathUtil.logab(2, width))), imageHeight = (int) Math.pow(2, (int) Math.ceil(MathUtil.logab(2, width)));

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setColor(colour);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();

		SpriteRegistry.registerImage(name, image);
		return new TextObject(name, text, font, width, height, imageWidth, imageHeight);
	}

	public static TextObject getStringFromFont(Font font, String text) {
		return getStringFromFont(font, text, Color.BLACK);
	}

	public static void renderText(TextObject text, int x, int y) {
		Main.getScreen().renderSpriteIgnoringCamera(text.getSprite(), new Vector2f(x - text.getWidth() / 2, y - text.getHeight() / 2));
	}
}
