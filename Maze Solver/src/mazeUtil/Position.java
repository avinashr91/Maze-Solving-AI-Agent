package mazeUtil;

public class Position 
{
	int x;
	int y;
	int fScore;
	int gScore;
	public int getgScore() {
		return gScore;
	}
	public void setgScore(int gScore) {
		this.gScore = gScore;
	}
	public int getfScore() {
		return fScore;
	}
	public void setfScore(int fScore) {
		this.fScore = fScore;
	}
	public Position(int startX,int startY)
	{
		x = startX;
		y = startY;
	}
	@Override
	public boolean equals(Object o) 
	{
		if(!(o instanceof Position)) {
			return false;
		}
		if(o==this) return true;
		Position position = (Position)o;
		if(position.x==this.x && position.y==this.y) return true;
		else return false;
	}
	
	
	
}
