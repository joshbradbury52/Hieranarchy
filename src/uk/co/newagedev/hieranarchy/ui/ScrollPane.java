package uk.co.newagedev.hieranarchy.ui;

import java.awt.Rectangle;

import uk.co.newagedev.hieranarchy.events.types.input.CursorClickEvent;
import uk.co.newagedev.hieranarchy.events.types.input.CursorMoveEvent;
import uk.co.newagedev.hieranarchy.main.Main;
import uk.co.newagedev.hieranarchy.util.Colour;
import uk.co.newagedev.hieranarchy.util.Vector2f;

public class ScrollPane extends Component {
	private ScrollBar[] scrollBars = new ScrollBar[2];
	private Container pane;

	public ScrollPane(int x, int y, int width, int height, int scrollBarDisplays) {
		super(x, y, width, height);
		if ((scrollBarDisplays & ScrollBar.HORIZONTAL) == ScrollBar.HORIZONTAL) {
			scrollBars[0] = new ScrollBar(ScrollBar.HORIZONTAL, this);
		}
		if ((scrollBarDisplays & ScrollBar.VERTICAL) == ScrollBar.VERTICAL) {
			scrollBars[1] = new ScrollBar(ScrollBar.VERTICAL, this);
		}
		pane = new Container(0, 0);
		pane.setParent(this);
	}

	public Container getPane() {
		return pane;
	}

	public int getHeight() {
		return (int) getDimensions().getHeight() - (scrollBars[0] != null ? 15 : 0);
	}

	public int getWidth() {
		return (int) getDimensions().getWidth() - (scrollBars[1] != null ? 15 : 0);
	}
	
	@Override
	public void setDimensions(int width, int height) {
		super.setDimensions(width, height);
		for (ScrollBar bar : scrollBars) {
			if (bar != null) {
				bar.updatePaneSize(width, height);
			}
		}
	}

	public void update() {
		pane.update();
	}
	
	public void cursorMove(CursorMoveEvent event) {
		int mx = event.getX(), my = event.getY();
		boolean hovering = (scrollBars[0] != null ? (my > getDisplayLocation().getY() + getHeight() && my < getDisplayLocation().getY() + getHeight() + 15) || scrollBars[0].isHeld() : false) || (scrollBars[1] != null ? (mx > getDisplayLocation().getX() + getWidth() && mx < getDisplayLocation().getX() + getWidth() + 15) || scrollBars[1].isHeld() : false);
		if (hovering) {
			Main.getCursor().setOffset(-mx - 1, -my - 1);
			pane.update();
			Main.getCursor().setOffset(0, 0);
		}
		for (ScrollBar bar : scrollBars) {
			if (bar != null) {
				bar.cursorMove(event);
			}
		}
	}
	
	public void cursorClick(CursorClickEvent event) {
		for (ScrollBar bar : scrollBars) {
			if (bar != null) {
				bar.cursorClick(event);
			}
		}
	}

	public void render() {
		Main.getScreen().renderQuad(new Rectangle((int) getDisplayLocation().getX(), (int) getDisplayLocation().getY(), (int) getDimensions().getWidth(), (int) getDimensions().getHeight()), Colour.LIGHT_LIGHT_GREY);
		for (ScrollBar bar : scrollBars) {
			if (bar != null) {
				bar.render();
			}
		}

		int xOffset = 0, yOffset = 0;

		if (scrollBars[0] != null) {
			xOffset = (int) -scrollBars[0].calculateXOffset() + (int) getDisplayLocation().getX();
		}
		if (scrollBars[1] != null) {
			yOffset = (int) -scrollBars[1].calculateYOffset() + (int) getDisplayLocation().getY();
		}

		pane.setOffset(xOffset, yOffset);

		int yOff = scrollBars[0] != null ? 15 : 0;
		Main.getScreen().startScissor(new Vector2f(getDisplayLocation().getX(), Main.HEIGHT - getDisplayLocation().getY() - (int) getDimensions().getHeight() + yOff), getWidth(), getHeight());
		pane.render();
		Main.getScreen().stopScissor();
	}
	
	@Override
	public void hide() {
		pane.hide();
		super.hide();
	}
	
	@Override
	public void show() {
		pane.show();
		super.show();
	}
}