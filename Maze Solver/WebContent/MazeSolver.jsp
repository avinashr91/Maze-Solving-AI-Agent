<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import = "mazeUtil.Mazes" %>
<html>
<head>
<title>Maze Solver</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel = "stylesheet" type = "text/css" href = "mazeStyle.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
</head>
<body>
<%
	// Call the Mazes java class to load the maze and the solution
	int size = Integer.parseInt(request.getParameter("dimension"));
	Mazes maze = new Mazes(size); // Get the parameter from first html
	String mazeArray[][] = maze.mazeStructure;
	String mazeSolution[] = maze.solStack;
	String mazeStarSolution[] = maze.astarSolStack;
%>
<script type = "text/javascript">
// Copy the values from jsp array to javascript array
var maze = new Array();
var solution = new Array();
var aStarsolution = new Array();
for(var i = 0; i < <%=size%>; i++)
{
	maze[i] = new Array();
}
<%
for(int i = 0; i < mazeArray.length; i++)
{
	for(int j =0; j < mazeArray.length; j++)
	{ %>
	
		maze[<%=i%>][<%=j%>] = '<%=mazeArray[i][j]%>'; 	
	<%}
}

for(int i = 0; i< mazeSolution.length; i++)
{%>
	solution[<%=i%>] = '<%=mazeSolution[i]%>';
<%}


for(int i = 0; i< mazeStarSolution.length; i++)
{%>
	aStarsolution[<%=i%>] = '<%=mazeStarSolution[i]%>';
<%}
%>
</script>
<button id = "Solve1" style = 'display:none;' onClick = "clearMap('TRE',solution,aStarsolution)">Solve Maze TreMaux </button>
<button id = "Solve2" style = 'display:none;' onClick = "clearMap('AStar',aStarsolution,solution)">Solve Maze A* </button>
<button id = "Back" style = 'display:none;' onClick = "location.href='index.html'">Back </button>
<br> <br>
<div id = "Start" style='display:none;'> START </div>
<br>
<table id = "maze">
<tbody> </tbody>
</table>
<script type = "text/javascript">
// Generate Maze
for(var i =0; i < maze.length; i++)
{
	 $('#maze > tbody').append("<tr>");
	for(var j=0; j< maze[0].length; j++)
	{
		var selector = maze[i][j];
		var tableCell = 'c'+i+"#"+j;
		//console.log(tableCell);
		if(solution.indexOf(tableCell) != -1)
		{
			$('#maze > tbody').append("<td id = '"+tableCell+"'class='"+selector+"'>&nbsp;</td>");
		}
		else
		{
			$('#maze > tbody').append("<td id = '"+tableCell+"' class='"+selector+"'>&nbsp;</td>");
		}
		
	}
	 $('#maze > tbody').append("</tr>");
}
$('#maze > tbody').append("<tr>");
for(var i = 0; i < maze[0].length; i++)
{
	if(i != maze[0].length - 1)
	{
		$('#maze > tbody').append("<td style = 'border:0;'>&nbsp;</td>");	
	}
	else
	{
		$('#maze > tbody').append("<td style = 'border:0;'>END</td>");	
	}
}
	
$('#maze > tbody').append("</tr>");
document.getElementById("Solve1").style.display="inline";
document.getElementById("Solve2").style.display="inline";
document.getElementById("Back").style.display="inline";
var startElement = document.getElementById("Start");
startElement.style.display = "block";

function clearMap(value,mySolution,alternateSolution)
{
	for(var i = 0; i<alternateSolution.length; i++)
	{
		var element = document.getElementById(alternateSolution[i]);
		element.bgColor = "FFFFFF";
	}
	setSolution(0,mySolution,mySolution.length);
}
var i = 0, howManyTimes = solution.length;
// Solution draw for maze
function setSolution(i,mySolution,howManyTimes) {
    var element = document.getElementById(mySolution[i]);
	element.bgColor = "00FF00";
    i++;
    if( i < howManyTimes ){
        setTimeout( function () 
        		{
        			setSolution(i,mySolution,howManyTimes);}, 50);
        
        		}
    }
   



	
</script>

</body>
</html>