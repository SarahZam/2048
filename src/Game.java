/*
 * Name 1: Sarah Zaman 
 * Name 2: Prinaya Choubey
 * Username1: szaman 
 * Username2: pchoubey 
 * Project 1
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game extends JPanel {
	//defines all the variables involved in the game



	Scanner scan = new Scanner(System.in);
	private static final int size = 100;
	private static final int margin = 35;
	private static int moves = 0;
	private static boolean needAddTile;
	private Square[] square1;
	boolean win = false;
	boolean lose = false;
	int score = 0;

	//main method to launch the game
	public static void main(String[] args) {
		JFrame game = new JFrame();
		game.setTitle("2048");
		game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		game.setSize(600, 600);
		game.setResizable(false);

		game.add(new Game());

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
	//class defined for the squares in the game
	static class Square {
		int value;

		public Square() {
			this(0);
		}

		public Square(int num) {
			value = num;
		}

		public boolean empty() {
			return value == 0;
		}

	}


	public Game() {
		setPreferredSize(new Dimension(340, 400));
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				int code = e.getKeyCode();
				//key event to reset the game 
				//This key event uses a method called "reset()" to reset the game 
				if (code == KeyEvent.VK_R) {
					System.out.println("Do you want to reset the game? Answer with Y/N");
					String ans = scan.next();
					if(ans.equals("Y")) {
						reset();
					}
					else if(ans.equals("N")) {
						System.out.println("Continue game");

					}
					else {
						System.out.println("That was not a valid answer. Please press R again");
					}

				}
				//key event to quit the game
				//uses system.exit(0) to exit the game
				if(code == KeyEvent.VK_Q) {
					System.out.println("Do you want to quit the game? Answer with Y/N");
					String ans = scan.next();
					if(ans.equals("Y")) {
						System.exit(0);
					}
					else if(ans.equals("N")) {
						System.out.println("Continue game");

					}
					else {
						System.out.println("That was not a valid answer. Please press R again");
					}

				}
				if (!move()) {
					lose = true;
				}
				//Asks keys to respond if the game has not been won or lost
				//uses a key listener for this
				if (!win && !lose) {
					if (code == KeyEvent.VK_RIGHT) {
						right();
					}
					else if (code == KeyEvent.VK_LEFT) {
						left();
					}
					else if (code == KeyEvent.VK_UP) {
						up();
					}
					else if (code == KeyEvent.VK_DOWN) {
						down();
					}
					else {
						System.out.println("Not a valid move");

					}
					
				}


				if (!win && !move()) {
					lose = true;
				}

				repaint();
			}
		});
		reset();
	}
	private void setLine(int index, Square[] re) {
		System.arraycopy(re, 0, square1, index * 4, 4);
	}

	//method to reset the game when commanded by the user
	public void reset() {
		score = 0;
		win = false;
		lose = false;
		square1 = new Square[4 * 4];
		for (int i = 0; i < square1.length; i++) {
			square1[i] = new Square();
		}
		addSquare();
		addSquare();
	}
	
	//for max number
	public int maxInt(int[] list) {
		int max = Integer.MIN_VALUE;
		for(int i : list) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}
	

	
	//method to move tiles to left and printing max number 
	public void left() {
		int[] maxOfEachLine = new int[4];
		needAddTile = false;
		for (int i = 0; i < 4; i++) {
			Square[] line = getLine(i);
			Square[] merged = mergedLines(moveLine(line));
			maxOfEachLine[i] = max(merged);
			setLine(i, merged);
			if (!needAddTile && !comparison(line, merged)) {
				needAddTile = true;
			}
		}
		
		System.out.println("Max Tile On Board: "+ maxInt(maxOfEachLine));

		if (needAddTile) {
			addSquare();
		}
		if(needAddTile == false) {
			System.out.println("Not a valid move");
		}
		else {
			System.out.println("Moved left!");
			moves++;
			System.out.println("Total moves made: " + moves);
		}
	}

	public void right() {
		square1 = rotation(180);
		left();
		square1 = rotation(180);
		if(needAddTile == false) {
			System.out.println("Not a valid move");
		}
		else {
			System.out.println("Moved right!");
			moves++;
			System.out.println("Total moves made: " + moves);
		}
	}

	public void up() {
		square1 = rotation(270);
		left();
		square1 = rotation(90);
		if(needAddTile == false) {
			System.out.println("Not a valid move");
		}
		else {
			System.out.println("Moved Up!");
			moves++;
			System.out.println("Total moves made: " + moves);
		}
	}

	public void down() {
		square1 = rotation(90);
		left();
		square1 = rotation(270);
		if(needAddTile == false) {
			System.out.println("Not a valid move");
		}
		else {
			System.out.println("Moved down!");
			moves++;
			System.out.println("Total moves made: " + moves);
		}
	}

	public Square tileAt(int x, int y) {
		return square1[x + y * 4];
	}
	//this method adds squares wherever needed
	public void addSquare() {
		List<Square> list = availableSpace();
		if (!availableSpace().isEmpty()) {
			int index = (int) (Math.random() * list.size()) % list.size();
			Square emptyTime = list.get(index);
			emptyTime.value = Math.random() < 0.8 ? 2 : 4; //0.8 chance of 2 generating while 0.2 chance of 4 generating
		}
	}
	//This method forms a list of blank squares
	public List<Square> availableSpace() {
		final List<Square> list = new ArrayList<Square>(16);
		for (Square t : square1) {
			if (t.empty()) {
				list.add(t);
			}
		}
		return list;
	}
	//to check whether a certain square is filled by a number or not
	public boolean filled() {
		return availableSpace().size() == 0;
	}
	//This method checks whether a particular square is filled or not
	boolean move() {
		if (!filled()) {
			return true;
		}
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				Square t = tileAt(x, y);
				if ((x < 3 && t.value == tileAt(x + 1, y).value)
						|| ((y < 3) && t.value == tileAt(x, y + 1).value)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean comparison(Square[] line1, Square[] line2) {
		if (line1 == line2) {
			return true;
		} else if (line1.length != line2.length) {
			return false;
		}

		for (int i = 0; i < line1.length; i++) {
			if (line1[i].value != line2[i].value) {
				return false;
			}
		}
		return true;
	}
	//sets the axis of the squares
	//uses the Math method in order to set this
	public Square[] rotation(int angle) {
		Square[] newSquare = new Square[4 * 4];
		int offsetX = 3, offsetY = 3;
		if (angle == 90) {
			offsetY = 0;
		} else if (angle == 270) {
			offsetX = 0;
		}

		double rad = Math.toRadians(angle);
		int cos = (int) Math.cos(rad);
		int sin = (int) Math.sin(rad);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				//To set positions of all the squares 
				int newX = (x * cos) - (y * sin) + offsetX;
				int newY = (x * sin) + (y * cos) + offsetY;
				newSquare[(newX) + (newY) * 4] = tileAt(x, y);
			}
		}
		return newSquare;
	}
	//Forms an array of squares 
	public Square[] moveLine(Square[] oldLine) {
		LinkedList<Square> l = new LinkedList<Square>();
		for (int i = 0; i < 4; i++) {
			if (!oldLine[i].empty())
				l.addLast(oldLine[i]);
		}
		if (l.size() == 0) {
			return oldLine;
		} else {
			Square[] newLine = new Square[4];
			sizesetter(l, 4);
			for (int i = 0; i < 4; i++) {
				newLine[i] = l.removeFirst();
			}
			return newLine;
		}
	}
	//to add the two squares when they are of the same number

	public Square[] mergedLines(Square[] oldl) {
		LinkedList<Square> list = new LinkedList<Square>();
		ArrayList<Integer> list2 = new ArrayList<Integer>();

		int num =0;

		for (int i = 0; i < 4 && !oldl[i].empty(); i++) {
			num = oldl[i].value;


			if (i < 3 && oldl[i].value == oldl[i + 1].value) {
				num *= 2;
				score += num;
				int target = 2048;
				if (num == target) {
					win = true;
				}
				i++;
			}
			list.add(new Square(num));
			list2.add(new Integer(num));

		}
		
		if (list.size() == 0) {
			return oldl;
		} else {
			sizesetter(list, 4);
			return list.toArray(new Square[4]);

		}

	}
	
	//creating max 
	private int max(Square[] list) {
		int max = Integer.MIN_VALUE;
		for(Square i : list) {
			if (i.value > max) {
				max = i.value;
			}
		}
		return max;
	}

	private static void sizesetter(java.util.List<Square> l, int s) {
		while (l.size() != s) {
			l.add(new Square());
		}
	}

	private Square[] getLine(int index) {
		Square[] result = new Square[4];
		for (int i = 0; i < 4; i++) {
			result[i] = tileAt(i, index);
		}
		return result;
	}

	//method "paint" and method "drawsquare" work for the graphics of the game
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				drawsquare(g, square1[x + y * 4], x, y);
			}
		}
	}

	private void drawsquare(Graphics g, Square square1, int x, int y) {

		int value = square1.value;
		int xMar = offsetCoors(x);
		int yMar = offsetCoors(y);
		//settings for the squares themselves
		g.setColor(Color.CYAN);
		g.fillRect(xMar, yMar, size, size);
		//sets the dimensions for the numbers inside the squares 
		g.setColor(Color.RED);
		final int size = value < 100 ? 40 : value < 1000 ? 30 : 24;
		final Font font = new Font("AmercanTypewriter", Font.BOLD, size);
		g.setFont(font);

		String s = String.valueOf(value);
		final FontMetrics FM = getFontMetrics(font);

		final int w = FM.stringWidth(s);
		final int h = -(int) FM.getLineMetrics(s, g).getBaselineOffsets()[2];

		if (value != 0)
			g.drawString(s, xMar + (size - w) / 2, yMar + size - (size - h) / 2 - 2);
		//sets the dimensions for the display of the score

		//Settings for the end game window 
		if (win || lose) {
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(78, 139, 202));
			g.setFont(new Font("AmercanTypewriter", Font.BOLD, 48));
			if (win) {
				g.drawString("You win", 150, 250);
			}
			if (lose) {
				g.drawString("Game Over", 150, 250);
			}

		}
		g.setFont(new Font("AmercanTypewriter", Font.BOLD, 30));
		g.setColor(Color.WHITE);
		g.drawString("Your score: " + score, 20, 560);
	}

	private static int offsetCoors(int arg) {
		return arg * (margin + size) + margin;
	}



}
