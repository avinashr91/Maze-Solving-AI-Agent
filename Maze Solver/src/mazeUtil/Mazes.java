package mazeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/*
 * Utility Class to create a maze - Assumes it is a square maze and Start is the first stop left cell and destination is the last right cell at the bottom
 */ 
public class Mazes {

	/**
	 * @param args
	 */
	public int maze[][][]; // Actual Maze Third dimension represents the border
	public int size; // Dimension of the maze
	boolean visited[][]; // Track Visited maze locations
	int borderCount[][];
	boolean solution[][]; // Track Maze solution path
	public String mazeStructure[][]; // Maze Structure log
	Stack<String> st = new Stack<String>(); // Stack for solution
	boolean done = false;
	public String[] solStack; // String array returned for solution
	Random rn = new Random(); // Random Number Generator
	int[][] fscore;
	int[][] gscore;
	List<String>solutionList;
	public String[] astarSolStack;
	public int pathCount;
	public int aStarPathCount;
	/*
	 * Constructor. Calls the function to construct the maze and to solve the maze
	 * In the 3D array the third index defines the borders
	 * 0 - left
	 * 1 - top
	 * 2 - right
	 * 3 - bottom
	 */
	public Mazes(int n)
	{
		size = n;
		maze = new int[n][n][4]; // Three Dimensional Array to store the squares and the four borders - Value 0 Indicates the border is closed 1 indicates the border is open.
		visited = new boolean[n+2][n+2];
		borderCount = new int[n+2][n+2];
		solution = new boolean[n][n];
		mazeStructure = new String[n][n];
		pathCount = 0;
		aStarPathCount = 0;
		setBasics();
		construct(1,1);
		//removeLoops();
		setMazeStructure();
		display();
		solve(0,0,'A');
		solStack = new String[st.size()];
		for(int i =0; i < solStack.length; i++)
		{
			solStack[i] = st.get(i);
		}
		fscore = new int[size][size];
		gscore = new int[size][size]; 
		this.solutionList = aStar(0,0,size-1,size-1);
		Collections.reverse(solutionList);
		System.out.println("TreMaux");
		//System.out.println(st.toString());
		System.out.println("TreMaux Distance "+st.size());
		astarSolStack = new String[solutionList.size()];
		for(int i =0; i < solutionList.size(); i++)
		{
			astarSolStack[i] = solutionList.get(i);
		}
		System.out.println("A Star");
		//System.out.println(solutionList);
		System.out.println(" A * Distance "+solutionList.size());
	}
	/*
	 *  Set Start and end index
	 */
	public void setBasics()
	{
		for(int row = 0; row < size+2; row++)
		{
			visited[row][0] = true;
			visited[row][size+1] = true;
			
			borderCount[row][0] =1;
			borderCount[row][size+1] = 1;
		}
		for(int col = 0; col < size+2; col++)
		{
			visited[0][col] = true;
			visited[size+1][col] = true;
			borderCount[0][col] = 1;
			borderCount[size+1][col] = 1;
			
		}
		maze[0][0][1] = 1; // Setting the first Cell as start
		maze[size-1][size-1][3] = 1; // Setting the last cell as end;
	}
	/*
	 * Recursive Function to construct the maze such that there is a path from start to end
	 */
	public void construct(int row,int col)
	{
		borderCount[row][col] = 1;
		// mark this cell as current cell and make it visited
		visited[row][col] = true;
		// While there is an unvisited neighbor cell
		//while(!visited[row][col+1] ||!visited[row+1][col] ||!visited[row-1][col] ||!visited[row][col-1])
		
		while(borderCount[row][col+1] == 0 ||borderCount[row+1][col] == 0 ||  borderCount[row-1][col] == 0 || borderCount[row][col-1] == 0)
		{
			// Pick a random cell that is unvisited and make that as current cell and start the search
			while(true)
			{
				int randomCell = rn.nextInt(4)+1; // Randomization (Range 1 - 4)
				if(randomCell == 1 && borderCount[row][col+1] == 0)
				{
					// Open the border between the current cell and the random neighbor cell
					maze[row-1][col-1][2] = 1;
					maze[row-1][col][0] = 1;
					construct(row,col+1);
					break;
				}
				else if(randomCell == 2 && borderCount[row+1][col] == 0)
				{
					// Open the border between the current cell and the random neighbor cell
					maze[row-1][col-1][3] = 1;
					maze[row][col-1][1] = 1;
					construct(row+1,col);
					break;
				}
				else if(randomCell == 3 && borderCount[row][col-1] == 0)
				{
					// Open the border between the current cell and the random neighbor cell
					maze[row-1][col-2][2] = 1;
					maze[row-1][col-1][0] = 1;
					construct(row,col-1);
					break;
				}
				else if(randomCell == 4 && borderCount[row-1][col] == 0)
				{
					// Open the border between the current cell and the random neighbor cell
					maze[row-2][col-1][3] = 1;
					maze[row-1][col-1][1] = 1;
					construct(row-1,col);
					break;
				}
			}
		}
		
	}
	/*
	 * Remove loops in the Maze to create multiple solutions
	 *
	public void removeLoops()
	{
		for(int i =0; i < maze.length -1;i++)
		{
			for(int j =0; j< maze[0].length-1; j++)
			{
				int sum = 0;
				for(int k = 0; k<=3;k++)
				{
					sum+= maze[i][j][k];
				}
				if(sum == 1)
				{
					if(maze[i][j][2] == 0) // Check for Right Borders
					{
						maze[i][j][2] = 1;
						maze[i][j+1][0] = 1;
					}
					else if(maze[i][j][3] == 0)
					{
						maze[i][j][3] = 1;
						maze[i+1][j][1] = 1;
					}
				}
				
			}
		}
	}
	*/
	/*
	 *  Displays the maze in console
	 */
	public void display()
	{
		String direction[]=new String[]{"left","top","right","bottom"};
		for(int i = 0 ; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				//System.out.println("Row "+i);
				//System.out.println("Column "+j);
				for(int k = 0; k < 4; k++)
				{
					//System.out.print(direction[k]+" ");
					//System.out.println(maze[i][j][k]);
				}
			}
		}
		 for (int x = 0; x <=size; x++)
	            for (int y = 0; y <= size; y++)
	                visited[x][y] = false;
	        done = false;
		
	}
	/*
	 * Sets the structure of the maze for the JS code to traverse
	 */
	public void setMazeStructure()
	{
		for(int i =0; i<size; i++)
		{
			for(int j =0; j<size;j++)
			{
				mazeStructure[i][j]="c";
				for(int k = 0; k<4; k++)
				{
					mazeStructure[i][j] += (maze[i][j][k] == 0) ? "" :k;
				}
			}
		}
		
	}
	/*
	 *  Solving the maze - Displays the row and column index in an orderly manner from source to destination
	 */
	public boolean solve(int x,int y,char direction)
	{
			if(x < 0 || y < 0 || x >=size || y >= size) return false;
			
			if(x == size -1 && y == size - 1)
			{
				if(isSafe(x,y,direction) == true)
				{
					st.push("c"+x+"#"+y);
					solution[x][y] = true;
					return true;
				}
				else
				{
					return false;
				}
			}
			
			if(isSafe(x,y,direction) == true)
			{
				
				if(visited[x][y]) return false;
				
				visited[x][y] = true;
				st.push("c"+x+"#"+y);
				pathCount++;
				if(solve(x,y+1,'E')) 
				{
					solution[x][y+1] = true;
					return true;
				}
				if(solve(x+1,y,'S')) 
				{
					solution[x+1][y] = true;
					return true;
				}
				if(solve(x,y-1,'W')) 
				{
					solution[x][y-1] = true;
					return true;
				}
				if(solve(x-1,y,'N')) 
				{
					solution[x-1][y] = true;
					return true;
				}
				st.pop();
				return false;
			}
		
		
			
		return false;
		
		
	}
	
	public List<String> aStar(int startX, int startY, int goalX, int goalY) 
	{
		Set<String> closedSet = new LinkedHashSet<String>();
		Set<String> openSet = new LinkedHashSet<String>();
		Map<String,String> cameFromMap = new LinkedHashMap<String,String>();
		String goalPos = goalX + "#" + goalY;
		
	
		for(int i =0; i< size; i++)
		{
			for(int j =0; j < size; j++)
			{
				fscore[i][j] = Integer.MAX_VALUE;
			}
			
		}
		fscore[startX][startY] = heuristic(startX,startY,goalX,goalY);
		gscore[startX][startY] = 0;
		openSet.add(startX+"#"+startY);
		while(!openSet.isEmpty())
		{
			String current = minimumNode(openSet);
			String[] currentArray = current.split("#");
			int currentX = Integer.parseInt(currentArray[0]);
			int currentY = Integer.parseInt(currentArray[1]);
			// if Current == goal Check
			if(current.equals(goalPos))
			{
				return reconstruct_path(cameFromMap,current);
			}
			openSet.remove(current);
			closedSet.add(current);
			Set<String> neighbourSet = findValidNeighbours(current);
			for(String neighbour: neighbourSet)
			{
				String[] neighbourArray = neighbour.split("#");
				int neighbourX = Integer.parseInt(neighbourArray[0]);
				int neighbourY = Integer.parseInt(neighbourArray[1]);
				if(closedSet.contains(neighbour))
				{
					continue;
				}
				int tentative_gScore  = gscore[currentX][currentY] + 1;
				if(!openSet.contains(neighbour))
				{
					openSet.add(neighbour);
					aStarPathCount++;
				}
				else if(tentative_gScore >= gscore[neighbourX][neighbourY])
				{
					continue;
				}
				cameFromMap.put(neighbour,current);
				gscore[neighbourX][neighbourY] = tentative_gScore ;
				fscore[neighbourX][neighbourY] = tentative_gScore + heuristic(neighbourX,neighbourY,goalX,goalY);
				
			}
			
			
		}
		return null;
		
	}
	
	private List<String> reconstruct_path(Map<String, String> cameFromMap,
			String current) 
	{
		// TODO Auto-generated method stub

		List<String> solutionList = new ArrayList<String>();
		solutionList.add("c"+current);
		while(cameFromMap.containsKey(current))
		{
			current = cameFromMap.get(current);
			solutionList.add("c"+current);
		}
		return solutionList;
	}
	private Set<String> findValidNeighbours(String current) 
	{
		// TODO Auto-generated method stub
		Set<String> neighbours = new LinkedHashSet<String>();
		String[] currentArray = current.split("#");
		int currentX = Integer.parseInt(currentArray[0]);
		int currentY= Integer.parseInt(currentArray[1]);
		if(isSafe(currentX,currentY+1,'E'))
		{
			neighbours.add(currentX + "#"+ (currentY+1));
		}
		if(isSafe(currentX+1,currentY,'S'))
		{
			neighbours.add((currentX+1) + "#" + currentY);
		}
		if(isSafe(currentX,currentY-1,'W'))
		{
			neighbours.add(currentX + "#"+(currentY-1));
		}
		if(isSafe(currentX-1,currentY,'N'))
		{
			neighbours.add((currentX-1)+ "#"+currentY);
		}
		return neighbours;
	}
	private String minimumNode(Set<String> openSet) 
	{
		String minPosition = null;
		for(String p: openSet) 
		{
			if(minPosition == null) {
				minPosition = p;
				continue;
			}
			String pArray[] = p.split("#");
			String minPosArray[] = minPosition.split("#");
			int pX = Integer.parseInt(pArray[0]);
			int pY = Integer.parseInt(pArray[1]);
			int minPosX = Integer.parseInt(minPosArray[0]);
			int minPosY = Integer.parseInt(minPosArray[1]);
			if(fscore[pX][pY]< fscore[minPosX][minPosY]) minPosition = p;
		};
		return minPosition;
	}
	public int heuristic(int currentX,int currentY,int goalX,int goalY) {
		return manhattanDistance(currentX,currentY,goalX,goalY);
	}
	
	public int manhattanDistance(int currentX,int currentY,int goalX,int goalY)
	{
		 int d1 = Math.abs(currentX - goalX);
		 int d2 = Math.abs(currentY - goalY);
		 return d1 + d2;
	}
	
	/*
	 *  Check if the maze's x,y position is accessible from direction
	 */
	public boolean isSafe(int x,int y,char direction)
	{
		//System.out.println("Calling Safe" + x + y + direction);
		return (x >= 0 && x < size && y >= 0 &&y < size && ((direction == 'A' && maze[x][y][1] == 1) || (maze[x][y][0] == 1 && direction == 'E')|| (maze[x][y][2] == 1 && direction == 'W')|| (maze[x][y][1] == 1 && direction == 'S')|| (maze[x][y][3] == 1 && direction == 'N')));
	}
	
	public static void main(String args[])
	{
		Mazes m = new Mazes(25);
		System.out.println("TreMaux" +m.pathCount);
		System.out.println("A*" +m.aStarPathCount);
	}

}
