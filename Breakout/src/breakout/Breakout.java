package breakout;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGActorCollisionListener;
import ch.aplu.jgamegrid.GGMouse;
import ch.aplu.jgamegrid.GGMouseListener;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Breakout extends GameGrid implements GGMouseListener, GGActorCollisionListener {

	private static final long serialVersionUID = 1L;

	public boolean gameOver;
//	private long startTime;
	private Paddle paddle;
	private Ball ball;

	private int level;
	private boolean levelEnd;
	private int leben;
	private long startTime;
	private long roundTime;
	private long bonusTime;
	private int blocksHit;

	ArrayList<Block> blocks;

	private int cycle = -1;

	public Breakout() {
		super(800, 600, 1, true);
		gameOver = true;
		setTitle("Breakout");
		setSimulationPeriod(3);

		paddle = new Paddle(this);
		ball = new Ball(this);

		addMouseListener(this, GGMouse.move | GGMouse.lClick);

		show();
		getBg().setBgColor(Color.LIGHT_GRAY);
		getBg().drawText("Click to start", new Point(getWidth() / 2 - 80, getHeight() / 2));
		doRun();
	}

	public static void main(String[] args) {
		new Breakout();
	}

	@Override
	public void reset() {
		super.reset();
		gameOver = false;
		level = 1;

		levelStart(level);
	}

	private int gameOverTime = -1;

	@Override
	public void act() {
		if (gameOver) {
			if (gameOverTime == getNbCycles()) {

				getBg().drawText("You lost. New game?", new Point(getWidth() / 2 - 100, getHeight() / 2));

			}

		} else if (levelEnd) {
			removeActor(paddle);
			removeActor(ball);
			getBg().setBgColor(Color.LIGHT_GRAY);
			if (level == 3) {
				getBg().drawText("You won all Level!", new Point(getWidth() / 2 - 90, getHeight() / 2));
				getBg().drawText("Click for a new game.", new Point(getWidth() / 2 - 100, getHeight() / 2 + 30));
			} else {
				getBg().drawText("Level " + level + " won, click to continue.",
						new Point(getWidth() / 2 - 150, getHeight() / 2));
			}
		} else if (!gameOver) {
			long leftTime = roundTime - (System.currentTimeMillis() - startTime) + bonusTime;
			getBg().setBgColor(Color.DARK_GRAY);

			getBg().drawText("Level: " + level, new Point(10, 30));

			getBg().drawText("Lifes: " + leben, new Point(getWidth() / 4, 30));

			getBg().drawText("Time left: " + leftTime / 1000 / 60 + ":" + leftTime / 1000 % 60,
					new Point(getWidth() * 2 / 4, 30));

			getBg().drawText("Blocks hit: " + blocksHit, new Point(getWidth() * 3 / 4 + 20, 30));

			if ((int) (leftTime / 1000) == 0) {
				gameOver();
			}
		}
	}

	public void levelStart(int lvl) {
		levelEnd = false;
		leben = 3;
		blocksHit = 0;
		bonusTime = 0;
		roundTime = (long) (2 * 60 * 1000);
		addActor(ball, new Location(400, 450));
		addActor(paddle, new Location(getWidth() / 2, getHeight() - 20));
		ball.addCollisionActor(paddle);
		generateLevel(lvl);
		getBg().setBgColor(Color.DARK_GRAY);
		ball.reset();
		startTime = System.currentTimeMillis();
	}

	@Override
	public boolean mouseEvent(GGMouse mouse) {
		switch (mouse.getEvent()) {
		case GGMouse.lClick:
//			System.out.println("Klick");
			if (gameOver) {
				reset();
			} else if (levelEnd) {
				if (level == 3) {
					reset();
				} else {
					level++;
					levelStart(level);
				}
			}
			break;

		case GGMouse.move:
			if (!gameOver) {
				paddle.setValidLocation(new Location(mouse.getX(), getHeight() - 20));
				break;
			}
		}
		return false;
	}

	@Override
	public int collide(Actor arg0, Actor arg1) {
//		System.out.println("Kollision:");
		if (arg1 instanceof Paddle) {

//			System.out.println("cycle vor Prüfung: " + cycle);
//			cycle = getNbCycles();
			if (getNbCycles() > cycle + 100) {

//				System.out.println(getNbCycles());

				cycle = getNbCycles();

//				System.out.println(cycle);

				ball.resolveCollision(arg1);
			}
		} else if (arg1 instanceof Block) {
//			System.out.println("Block");
			ball.resolveCollision(arg1);
			arg1.hide();
			bonusTime = bonusTime + 2000;
			blocksHit++;
			blocks.remove(arg1);
//			System.out.println("Blöcke übrig: " + blocks.size());
			if (blocks.size() == 0) {
				levelEnd = true;
			}
		}
		return 0;
	}

	public void gameOver() {
//		System.out.println("Game Over");
		removeActor(paddle);
		removeActor(ball);
		for (Block b : blocks) {
			removeActor(b);
		}
		gameOver = true;
		gameOverTime = getNbCycles() + 1000 / getSimulationPeriod();
	}

	public void generateLevel(int lvl) {
//		System.out.println("Generiere Level " + lvl);

		blocks = new ArrayList<>();

		int maxX = 8;
		int maxY = 8;

//		int maxX = 2;
//		int maxY = 2;

		if (lvl == 1) {
			for (int x = 1; x < maxX; x++) {
				for (int y = 1; y < maxY; y++) {
					if (x == 1 || y == 1 || x == maxX - 1 || y == maxY - 1) {
						addBlock(getWidth() * x / maxX, getHeight() * y / maxY / 2 + 20);
					}
				}
			}
		} else if (lvl == 2) {
			for (int x = 1; x < maxX; x++) {
				for (int y = 1; y < maxY; y++) {
					if (x == y) {
						if ((getWidth() * x / maxX) == ((getWidth() * (maxX - x) / maxX))
								&& (getHeight() * y / maxY / 2 + 20) == (getHeight() * y / maxY / 2 + 20)) {
							addBlock(getWidth() * x / maxX, getHeight() * y / maxY / 2 + 20);
							// System.out.println("Füge Block ein in: " + x + ", " + y);
						} else {
							addBlock(getWidth() * x / maxX, getHeight() * y / maxY / 2 + 20);
							addBlock(getWidth() * (maxX - x) / maxX, getHeight() * y / maxY / 2 + 20);
						}
					}
				}
			}
		} else if (lvl == 3) {
			for (int x = 1; x < maxX; x++) {
				for (int y = 1; y < maxY; y++) {
					if (x == 1 || y == 1 || x == maxX - 1 || y == maxY - 1 || x == maxX / 2 || y == maxY / 2) {
//						System.out.println("Füge Block ein in: " + x + ", " + y);
						addBlock(getWidth() * x / maxX, getHeight() * y / maxY / 2 + 20);
					}
				}
			}
		}
	}

	private void addBlock(int x, int y) {
		Block block = new Block(this);
		addActor(block, new Location(x, y));
		ball.addCollisionActor(block);
		blocks.add(block);
	}

	public void lebenVerloren() {
		leben--;
//		System.out.println(leben);
		if (leben == 0) {
			gameOver();
		} else {
			ball.reset();
//			System.out.println("Ball Reset");
			getBg().setBgColor(Color.DARK_GRAY);
		}

	}
}
