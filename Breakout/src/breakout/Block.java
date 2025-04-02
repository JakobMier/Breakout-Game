package breakout;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;

public class Block extends Actor {

	private Breakout gg;

	public Block(Breakout gg) {
		super("assets/block.png");
		this.gg = gg;
		setCollisionRectangle(new Point(0, 0), 60, 20);
		addActorCollisionListener(gg);
	}
}
