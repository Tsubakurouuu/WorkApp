function updateClock() {
	var now = new Date();
	var hours = now.getHours();
	var minutes = now.getMinutes();
	var seconds = now.getSeconds();
	
	// 2桁の表記に変換
	if (hours < 10) {
    	hours = '0' + hours;
    }
    
    // 2桁の表記に変換
	if (minutes < 10) {
    	minutes = '0' + minutes;
    }
    
	// 2桁の表記に変換
	if (seconds < 10) {
    	seconds = '0' + seconds;
    }

  	var time = hours + ':' + minutes + ':' + seconds;
  	document.getElementById('clock').textContent = time;
}


setInterval(updateClock, 1000);
