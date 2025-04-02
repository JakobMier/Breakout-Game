package breakout;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class Paddle extends Actor {

	private Breakout gg;

	private GGVector position;

	public Paddle(Breakout gg) {
		super("assets/move.png");
		this.gg = gg;
		setCollisionCircle(new Point(0, 15), 30);
		addActorCollisionListener(gg);
		position = new GGVector(0, 1000);
	}

	@Override
	public void act() {
		if (!gg.gameOver) {
			position = toPosition(getLocation());
		}
	}

	private GGVector toPosition(Location location) {
		return new GGVector(location.x, location.y);
	}

	public void setValidLocation(Location location) {
		if (location.getX() < 30) {
			location.x = 30;
		} else if (location.getX() > (gg.getWidth() - 30)) {
			location.x = gg.getWidth() - 30;
		}
		setLocation(location);
	}
}
