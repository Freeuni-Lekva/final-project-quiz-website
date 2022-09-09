
function startTimer(duration, display) {
    let timer = duration, minutes, seconds, hours;
    setInterval(function () {
        timer--;
        hours   = Math.floor(timer / 3600);
        minutes = Math.floor((timer % 3600) / 60);
        seconds = timer % 60;

        if (timer < 0) {
            window.location.href = "http://localhost:8080/final_project_quiz_website_war_exploded/activeQuiz"
            hours = minutes = seconds = 0;
        }
        hours = hours < 10 ? "0" + hours : hours;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = hours.toString() + ":" + minutes.toString() + ":" + seconds.toString();

    }, 1000);
}

fetchTimer = (async () => {
    const rawResponse = await fetch('getTimer', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    return await rawResponse.json();
});

window.onload = async function () {
    let display = document.querySelector('#timer');
    fetchTimer().then((value) => {
        startTimer(value.timeLeft, display);
    });
};