package uk.co.newagedev.hieranarchy.ui;

import java.awt.Point;
import java.awt.Rectangle;

import uk.co.newagedev.hieranarchy.events.types.input.CursorClickEvent;
import uk.co.newagedev.hieranarchy.events.types.input.CursorMoveEvent;
import uk.co.newagedev.hieranarchy.graphics.Sprite;
import uk.co.newagedev.hieranarchy.graphics.SpriteRegistry;
import uk.co.newagedev.hieranarchy.input.Mouse;
import uk.co.newagedev.hieranarchy.main.Main;
import uk.co.newagedev.hieranarchy.util.Colour;
import uk.co.newagedev.hieranarchy.util.Vector2f;

public class Button extends Component {

	private boolean hover = false, toolTip = false, toolTipDisplay = false;

	private ButtonRunnable task;

	private String image = "", text = "";

	private int toolTipX;

	public Button(String text, int x, int y, int width, int height, boolean toolTip, ButtonRunnable task) {
		super(x, y, width, height);
		changeText(text);
		this.task = task;
		this.task.setButton(this);
		this.toolTip = toolTip;
	}

	public void setImage(String sprite) {
		this.image = sprite;
	}

	public void render() {
		Rectangle buttonDimensions = new Rectangle(new Point((int) getDisplayLocation().getX(), (int) getDisplayLocation().getY()), getDimensions());
		Main.getScreen().renderQuad(buttonDimensions, Colour.DARK_GREY);
		buttonDimensions.grow(-1, -1);
		Main.getScreen().renderQuad(buttonDimensions, (hover ? Colour.GREY : Colour.LIGHT_GREY));

		Vector2f loc = getDisplayLocation().clone().add((int) getDimensions().getWidth() / 2, (int) getDimensions().getHeight() / 2);

		if (image != "") {
			Sprite sprite = SpriteRegistry.getSprite(image);
			Main.getScreen().renderSpriteIgnoringCamera(image, loc.subtract(sprite.getWidth() / 2, sprite.getHeight() / 2));
		} else {
			componentFont.renderText(text, (int) loc.getX(), (int) loc.getY());
		}

		if (toolTipDisplay && toolTip) {
			int textHeight = componentFont.getTextHeight(text), textWidth = componentFont.getTextWidth(text);
			int toolTipY = (int) (getParent().getDisplayLocation().getY() + getParent().getDimensions().getHeight() + 10);
			Main.getScreen().renderQuad(new Rectangle(toolTipX, toolTipY - textHeight + 14, textWidth + 14, textHeight + 14), Colour.DARK_GREY);
			Main.getScreen().renderQuad(new Rectangle(toolTipX + 2, toolTipY + 16 - textHeight, textWidth + 10, textHeight + 10), Colour.GREY);

			componentFont.renderText(text, toolTipX + textWidth / 2 + 7, toolTipY - textHeight / 2 + 21);
		}
	}

	public void update() {

	}

	public void cursorMove(CursorMoveEvent event) {
		hover = false;
		if (event.getX() > getDisplayLocation().getX() && event.getX() < getDisplayLocation().getX() + getDimensions().getWidth()) {
			if (event.getY() > getDisplayLocation().getY() && event.getY() < getDisplayLocation().getY() + getDimensions().getHeight()) {
				hover = true;
			}
		}
		if (hover) {
			if (event.getUpdatesSinceLastMovement() > 30 && !toolTipDisplay) {
				toolTipDisplay = true;
			}
		} else {
			if (toolTipDisplay) {
				toolTipDisplay = false;
			}
		}
	}

	public void cursorClick(CursorClickEvent event) {
		if (event.isButtonReleasing(Mouse.BUTTON_LEFT)) {
			if (hover) {
				task.run();
			}
		}
	}

	public void changeText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
