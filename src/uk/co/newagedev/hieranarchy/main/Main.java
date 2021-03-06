package uk.co.newagedev.hieranarchy.main;

import com.google.gson.Gson;

import uk.co.newagedev.hieranarchy.events.EventHub;
import uk.co.newagedev.hieranarchy.graphics.OpenGLScreen;
import uk.co.newagedev.hieranarchy.graphics.Screen;
import uk.co.newagedev.hieranarchy.graphics.SpriteRegistry;
import uk.co.newagedev.hieranarchy.input.Controller;
import uk.co.newagedev.hieranarchy.input.Cursor;
import uk.co.newagedev.hieranarchy.input.KeyBinding;
import uk.co.newagedev.hieranarchy.input.Mouse;
import uk.co.newagedev.hieranarchy.scheduler.TaskScheduler;
import uk.co.newagedev.hieranarchy.state.StateManager;
import uk.co.newagedev.hieranarchy.util.Logger;
import uk.co.newagedev.hieranarchy.util.Vector2f;

public class Main {

	public static final String TITLE = "Hieranarchy";

	public static final Gson GSON = new Gson();

	public static int WIDTH = 1024, HEIGHT = 576;

	public static final int SPRITE_WIDTH = 64, SPRITE_HEIGHT = 64;

	public static float SCALE = 1;

	private static OpenGLScreen screen;

	private static Cursor cursor;

	private Thread thread;

	private boolean running;

	public void init() {
		Mouse.init();
		initResources();
		initStates();
		initBindings();
		EventHub.init();
	}

	public void initResources() {
		SpriteRegistry.registerSprite("bg", "Projects/testing/Assets/Textures/background.png");
		SpriteRegistry.registerSprite("play", "assets/textures/play.png");
		SpriteRegistry.registerSprite("reset", "assets/textures/reset.png");
		SpriteRegistry.registerSprite("pause", "assets/textures/pause.png");
		SpriteRegistry.registerSprite("edit", "assets/textures/edit.png");
		SpriteRegistry.registerSprite("addMap", "assets/textures/newmap.png");
		SpriteRegistry.registerSprite("deleteMap", "assets/textures/deletemap.png");
		SpriteRegistry.registerSprite("exit", "assets/textures/exit.png");
		SpriteRegistry.registerSprite("save", "assets/textures/save.png");
		SpriteRegistry.registerSprite("new tile", "assets/textures/newtile.png");
		SpriteRegistry.registerSprite("arrow", "assets/textures/arrow.png");
		SpriteRegistry.registerSprite("arrow hover", "assets/textures/arrow hover.png");
		SpriteRegistry.registerSprite("cursor", "assets/textures/cursor.png");
	}

	public void initBindings() {
		KeyBinding.bindKey("Left", KeyBinding.KEY_LEFT);
		KeyBinding.bindKey("Right", KeyBinding.KEY_RIGHT);
		KeyBinding.bindKey("Up", KeyBinding.KEY_UP);
		KeyBinding.bindKey("Down", KeyBinding.KEY_DOWN);
		KeyBinding.bindKey("editmapplay", KeyBinding.KEY_ENTER);
		KeyBinding.bindKey("SelectPrevTile", KeyBinding.KEY_Z);
		KeyBinding.bindKey("SelectNextTile", KeyBinding.KEY_X);
		KeyBinding.bindKey("Unhide Mouse", KeyBinding.KEY_ESCAPE);

		cursor = new Cursor(WIDTH / 2, HEIGHT / 2);
	}

	public void initStates() {
		StartMenuState state = new StartMenuState();
		StateManager.pushCurrentState(state);
	}

	public static void main(String[] args) {
		Main hieranarchy = new Main();
		screen = new OpenGLScreen();
		hieranarchy.init();
		hieranarchy.start();
	}

	public synchronized void start() {
		thread = new Thread("Hieranarchy");
		running = true;
		run();
	}

	public synchronized void stop() {
		cleanup();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Logger.error(e.getMessage());
			for (Object obj : e.getStackTrace()) {
				Logger.error(obj);
			}
		}
		running = false;
	}

	public void update() {
		Mouse.update();
		cursor.update();
		Controller.update();
		KeyBinding.update();
		TaskScheduler.update();
		StateManager.updateStates();
	}

	public void render() {
		screen.renderInit();
		StateManager.renderStates();
		if (screen.isCursorVisible()) {
			screen.renderSpriteIgnoringCamera("cursor", new Vector2f(cursor.getX(), cursor.getY()));
		}
		screen.postRender();
	}

	public static Screen getScreen() {
		return screen;
	}

	public static Cursor getCursor() {
		return cursor;
	}

	public void run() {
		long secondTime = System.currentTimeMillis();
		int fps = 0;
		while (running) {
			update();
			render();
			fps++;
			if (System.currentTimeMillis() - secondTime >= 1000) {
				screen.setTitle(TITLE + "     FPS: " + fps);
				secondTime += 1000;
				fps = 0;
			}
			if (screen.shouldClose()) {
				stop();
			}
		}
	}

	public void cleanup() {
		SpriteRegistry.clear();
		screen.cleanup();
	}
}
