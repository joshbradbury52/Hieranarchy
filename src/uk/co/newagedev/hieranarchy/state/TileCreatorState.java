package uk.co.newagedev.hieranarchy.state;

import java.util.HashMap;
import java.util.Map;

import uk.co.newagedev.hieranarchy.testing.Main;
import uk.co.newagedev.hieranarchy.ui.Button;
import uk.co.newagedev.hieranarchy.ui.ButtonRunnable;
import uk.co.newagedev.hieranarchy.ui.Component;
import uk.co.newagedev.hieranarchy.ui.Container;
import uk.co.newagedev.hieranarchy.ui.Label;
import uk.co.newagedev.hieranarchy.ui.TextBox;
import uk.co.newagedev.hieranarchy.ui.TickBox;


public class TileCreatorState extends State {
	
	private Container container;
	private TextBox tileName, sprite;
	private TickBox connectedTextures;
	private String stateFrom;
	
	public TileCreatorState(String stateFrom) {
		this.stateFrom = stateFrom;
		
		container = new Container(0, 0, Main.WIDTH, Main.HEIGHT);
		
		tileName = new TextBox(150, 20, 200, 50);
		container.addComponent(tileName);
		
		Label tileNameLabel = new Label("Tile Name", 20, 25);
		container.addComponent(tileNameLabel);
		
		connectedTextures = new TickBox(524, 50, false);
		container.addComponent(connectedTextures);
		
		Label connectedTexturesLabel = new Label("Connected Textures", 20, 55);
		container.addComponent(connectedTexturesLabel);
		
		sprite = new TextBox(150, 80, 200, 50);
		container.addComponent(sprite);
		
		Label spriteLabel = new Label("Sprite", 20, 85);
		container.addComponent(spriteLabel);
		
		Button createTile = new Button("Create Tile", (Main.WIDTH - 150) / 2, Main.HEIGHT - 140, 150, 50, false, new ButtonRunnable() {
			@Override
			public void run() {
				closeTileCreator();
				EditorState editor = (EditorState) StateManager.getState(stateFrom);
				Map<String, Object> props = getProps();
				
				Main.project.addObjectToMap(editor.getCurrentMap().getMapStore().getName(), props);
			}
		});
		container.addComponent(createTile);
		
		Button cancel = new Button("Cancel", (Main.WIDTH - 150) / 2, Main.HEIGHT - 70, 150, 50, false, new ButtonRunnable() {
			@Override
			public void run() {
				closeTileCreator();
			}
		});
		container.addComponent(cancel);
	}
	
	public void closeTileCreator() {
		Main.setCurrentState(stateFrom);
		StateManager.removeState(getName());
	}
	
	public Map<String, Object> getProps() {
		Map<String, Object> props = new HashMap<String, Object>();
		
		if (connectedTextures.isTicked()) {
			props.put("connected-textures", true);
		}
		
		props.put("type", "tile");
		props.put("sprite", sprite.getText().toLowerCase());
		props.put("name", tileName.getText().toLowerCase());
		return props;
	}
	
	@Override
	public void render() {
		Main.getScreen().renderQuad(0, 0, Main.WIDTH, Main.HEIGHT, Component.VERY_LIGHT);
		container.render();
	}

	@Override
	public void update() {
		container.update();
	}
}
