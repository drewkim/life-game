import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.Timer;

/*********************************************************************************
 * Name: Drew Kim
 * Block: F
 * Date: 10/11/13
 * Program: Game of Life. Follows typical logic for Conway's Game of Life. If 1 or 
 * less neighbors alive, cell dies. If 2, stays the same. If it has 3 it becomes alive.
 * If more than 4, the cell dies. Implemented a graphics display. Used padding on the
 * edges, not wrap around, to account for null pointer exceptions. This induced a problem
 * in graphics, where there are two extra columns on the right, and two extra rows
 * on the bottom. Users can import the cells they want to start alive through the
 * console, along with the number of generations.
 ***********************************************************************************/

public class GameOfLife extends JFrame implements ActionListener 
{
	static final int ONE = 1;
	static final int TWO = 2;
	static final int THREE = 3;
	static final int FOUR = 4;
	static final int NUM_ROWS = 50;
	static final int NUM_COLUMNS = 50;
	static final boolean DEAD = false;
	static final boolean ALIVE = true;
	static final Scanner console = new Scanner(System.in);
	static final int TOP_BAR = 22;
	static final int MAX_WIDTH = 800;
	static final int MAX_HEIGHT = 800 + TOP_BAR;
	static final int CELL_SIDELENGTH = MAX_WIDTH/NUM_COLUMNS;
	private static final int DELAY_IN_MILLISEC = 20;
	static Cell[][] grid = new Cell[NUM_ROWS][NUM_COLUMNS];

	/**
	 * Calls all the necessary methods and variable declarations
	 * @param args
	 */
	public static void main(String[] args) 
	{
		GameOfLife gl = new GameOfLife();
		gl.setSize(MAX_WIDTH, MAX_HEIGHT);
		Timer clock= new Timer(DELAY_IN_MILLISEC, gl);
		gl.setVisible(true);
		clock.start();
		initGrid(grid);
		System.out.println("Specify number of generations:");
		int numGenerations = console.nextInt();
		System.out.println("Specify cells that are alive. Use format *row* *cell* When done, enter -1 -1");
		int cellRow = 0;
		int cellColumn = 0;
		while(cellRow != -1 && cellColumn != -1)
		{
			cellRow = console.nextInt();
			cellColumn = console.nextInt();
			console.nextLine();
			if(cellRow < NUM_ROWS  && cellRow > 0 && cellColumn < NUM_COLUMNS && cellColumn > 0)
			{
				grid[cellRow][cellColumn].setInter(ALIVE);
				grid[cellRow][cellColumn].setStatus(ALIVE);
			}
		}
		print(grid);
		for(int i = 0; i < numGenerations - 1; i++)
		{
			checkNeighbors(grid);
			updateGrid(grid);
			print(grid);
		}
	}

	/**
	 * Constructor. Just sets the default close operation
	 */
	public GameOfLife()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Prints the status of the grid in the console
	 * @param grid
	 */
	static void print(Cell[][] grid)
	{
		for(int row = 1; row < NUM_ROWS - 1; row++)
		{
			for(int col = 1; col < NUM_COLUMNS - 1; col++)
			{
				if(grid[row][col].getStatus() == ALIVE)
				{
					System.out.print("0");
				}
				else if(grid[row][col].getStatus() == DEAD)
				{
					System.out.print("-");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}


	/**
	 * Checks the neighbors of every cell 
	 * @param grid
	 */
	static void checkNeighbors(Cell[][] grid)
	{
		//for loops to go through every cell
		for(int row = 1; row < NUM_ROWS - 1; row++)
		{
			for(int col = 1; col < NUM_COLUMNS - 1; col++)
			{
				//for loops to go through every neighbor
				int num_neighbors_alive = 0;
				for(int checkRow = row - 1; checkRow <= row + 1; checkRow++)
				{
					for(int checkCol = col - 1; checkCol <= col + 1; checkCol++)
					{
						//ignores itself
						if(checkCol == col && checkRow == row)
						{

						}
						else
						{
							if(grid[checkRow][checkCol].getStatus() == ALIVE)
							{
								num_neighbors_alive++;
							}
						}
					}
				}
				mercyOrDie(grid, num_neighbors_alive, row, col);
			}
		}
	}

	/**
	 * Decides the fate of each cell based on its neighbor(s)
	 * @param grid
	 * @param num_neighbors_alive
	 */
	static void mercyOrDie(Cell[][] grid, int num_neighbors_alive, int row, int col)
	{
		if(num_neighbors_alive == TWO)
		{
			
		}
		else if(num_neighbors_alive == THREE)
		{
			grid[row][col].setInter(ALIVE);
		}
		else if(num_neighbors_alive <= ONE || num_neighbors_alive >= FOUR)
		{
			grid[row][col].setInter(DEAD);
		}
	}

	/**
	 * Initializes all the cells in the grid and sets the status to dead
	 * @param grid
	 */
	static void initGrid(Cell[][] grid)
	{
		for(int row = 0; row < NUM_ROWS; row++)
		{
			for(int col = 0; col < NUM_COLUMNS; col++)
			{
				grid[row][col] = new Cell();
				grid[row][col].setInter(DEAD);
				grid[row][col].setStatus(DEAD);
			}
		}
	}

	/**
	 * Updates the status of every cell in the grid
	 * @param grid
	 */
	static void updateGrid(Cell[][] grid)
	{
		for(int row = 1; row < NUM_ROWS - 1; row++)
		{
			for(int col = 1; col < NUM_COLUMNS - 1; col++)
			{
				grid[row][col].update();
			}
		}
	}

	/**
	 * Called every DELAY_IN_MILLISECONDS
	 */
	public void actionPerformed(ActionEvent arg0) 
	{
		repaint();
	}

	/**
	 * Paints all the necessary figures on the screen
	 */
	public void paint(Graphics g)
	{
		clearScreen(g);
		drawGrid(g);
		for(int row = 1; row < NUM_ROWS - 1; row++)
		{
			for(int col = 1; col < NUM_COLUMNS - 1; col++)
			{
				if(grid[row][col].getStatus() == ALIVE)
				{
					g.setColor(Color.black);
					g.fillRect(col * CELL_SIDELENGTH - CELL_SIDELENGTH, TOP_BAR + row * CELL_SIDELENGTH - CELL_SIDELENGTH, CELL_SIDELENGTH, CELL_SIDELENGTH);
				}
			}
		}
	}

	/**
	 * Draws the lines for the grid constantly
	 * @param g
	 */
	public static void drawGrid(Graphics g)
	{
		g.setColor(Color.black);
		for(int col = 1; col < NUM_COLUMNS; col++)
		{
			g.drawLine(CELL_SIDELENGTH * col, TOP_BAR, CELL_SIDELENGTH * col, MAX_HEIGHT);
		}
		for(int row = 1; row < NUM_ROWS; row++)
		{
			g.drawLine(0, TOP_BAR + CELL_SIDELENGTH * row, MAX_WIDTH, TOP_BAR + CELL_SIDELENGTH * row);
		}
	}

	/**
	 * Clears the screen, used to give the effect of objects moving
	 * @param g
	 */
	public static void clearScreen(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
	}

}

/*********************************************************************************
 * Cell Class
 * Composed of many getters and setters used to determine the status of the 
 * cell. Status is used to determine the status of neighbors. Intermediate is used when in 
 * the middle of a generation, so testing does not get screwed up. At the end of 
 * each generation status is set to intermediate.
 **********************************************************************************/
class Cell
{
	boolean status;
	boolean intermediate;

	/**
	 * Sets cell as alive or dead
	 * @param bool
	 */
	public void setStatus(boolean bool)
	{
		status = bool;
	}

	/**
	 * Returns whether the cell is alive or dead
	 * @return
	 */
	public boolean getStatus()
	{
		return status;
	}

	/**
	 * Sets the intermediate variable as alive or dead
	 * @param bool
	 */
	public void setInter(boolean bool)
	{
		intermediate = bool;
	}

	/**
	 * Returns whether the intermediate variable is alive or dead
	 * @return
	 */
	public boolean getInter()
	{
		return intermediate;
	}

	/**
	 * Updates the status of the cell
	 */
	public void update()
	{
		status = intermediate;
	}
}
