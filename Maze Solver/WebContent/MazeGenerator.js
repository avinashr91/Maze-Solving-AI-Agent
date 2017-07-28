var i = 0; 
function setSolution(solution) {
	var howManyTimes = solution.length;
    var element = document.getElementById(solution[i]);
	element.bgColor = "00FF00";
    i++;
    if( i < howManyTimes ){
        setTimeout( setSolution, 50);
    }
}