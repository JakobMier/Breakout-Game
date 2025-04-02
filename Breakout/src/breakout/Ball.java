package breakout;

import java.awt.Point;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGVector;
import ch.aplu.jgamegrid.Location;

public class Ball extends Actor {

	private Breakout gg;
	private GGVector position;
	private GGVector velocity;

	public GGVector getPosition() {
		return position.clone();
	}

	public GGVector getVelocity() {
		return velocity.clone();
	}

	public void setVelocity(GGVector vel) {
		this.velocity = vel;
	}

	public Ball(Breakout gg) {
		super("assets/kugel_weiss.gif");
		this.gg = gg;
		setCollisionCircle(new Point(0, 0), 10);
		addActorCollisionListener(gg);
	}

	public void reset() {

		velocity = new GGVector(0, 0);
		position = new GGVector(400, 350);

		delay(1000);

		velocity = new GGVector(0, 1);
//		velocity = new GGVector((Math.random() - 0.5) * 20, (Math.random()) * 10);

		// Geschwindigkeit normieren

		double norm = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);

//		System.out.println("StartX: " + velocity.x + " StartY:  " + velocity.y + " NormF: " + norm);

		velocity.x = velocity.x / norm;
		velocity.y = velocity.y / norm;

//		position = toPosition(getLocation());
//		System.out.println("NormX: " + velocity.x + " NormY:  " + velocity.y + " StartAlpha: "
//				+ Math.atan2(velocity.y, velocity.x) * 180 / Math.PI);

	}

	private Location toLocation(GGVector position) {
		return new Location((int) (position.x), (int) (position.y));
	}

	@Override
	public void act() {
		if (gg.gameOver) {

		} else {
			if (getX() < 18) {
				velocity = new GGVector(-1 * velocity.x, velocity.y);
			} else if (getX() > (gg.getWidth() - 18)) {
				velocity = new GGVector(-1 * velocity.x, velocity.y);
			}
			if (getY() < 18) {
				velocity = new GGVector(velocity.x, -1 * velocity.y);
			} else if (getY() > gg.getHeight() - 18) {
				gg.lebenVerloren();
			}
			position = position.add(velocity.mult(1));
			Location location = toLocation(position);
			setLocation(location);
		}
	}

	public void resolveCollision(Actor arg0) {
		if (arg0 instanceof Paddle) {
//			GGVector delta = getPosition().sub(new GGVector(arg0.getX(), arg0.getY()));
//			GGVector mtd = delta;
//			GGVector v = getVelocity();
//			mtd.normalize();
//			double vn = v.dot(mtd);
//
//			if (vn < 0) {
			pointBallCollision(new Location(arg0.getX(), gg.getHeight() + 20));
//			System.out.println("Paddle");
//			}

		} else if (arg0 instanceof Block) {

			int x = arg0.getX();
			int y = arg0.getY();
//			System.out.println("Block erkannt");
//			System.out.println("Block: " + x + ", " + y + " Ball: " + position.x + ", " + position.y);
			if (x - 30 < position.x && position.x < x + 30) { // oben und unten
				velocity.y = -1 * velocity.y;
			} else if (y - 10 < position.y && position.y < y + 10) { // links und rechts
				velocity.x = -1 * velocity.x;
			} else if (position.x >= x + 30 && position.y <= y - 10) { // recht oben
//				System.out.println("rechts oben");
				pointBallCollision(new Location(x, y));
			} else if (position.x >= x + 30 && position.y >= y - 10) { // rechts unten
//				System.out.println("rechts unten");
				pointBallCollision(new Location(x, y));
			} else if (position.x <= x + 30 && position.y <= y - 10) { // links oben
//				System.out.println("links oben");
				pointBallCollision(new Location(x, y));
			} else if (position.x <= x + 30 && position.y >= y - 10) { // links unten
//				System.out.println("links unten");
				pointBallCollision(new Location(x, y));
			}
		}

	}

	public void pointBallCollision(Location point) {

		double alpha1 = Math.atan2(velocity.y, velocity.x);

		double beta = Math.atan2((position.x - point.x), (point.y - position.y));

		double alpha2 = 2 * beta - alpha1;

//		System.out.println("ColVeloX: " + velocity.x + " ColVeloY: " + velocity.y + " ColAlpha1: "
//				+ alpha1 * 180 / Math.PI + " PointX: " + point.x + " PointY: " + point.y + " BallX: " + position.x
//				+ " BallY: " + position.y + " dX: " + (position.x - point.x) + " dY: " + (position.y - point.y)
//				+ " ColBeta: " + beta * 180 / Math.PI + " ColAlpha2: " + alpha2 * 180 / Math.PI);

		velocity.x = Math.cos(alpha2);
		velocity.y = Math.sin(alpha2);
//		System.out.println(velo1.x + "; " + velo1.y);
	}
}