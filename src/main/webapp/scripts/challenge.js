postChallenge = (async () => {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);

    const rawResponse = await fetch('challenge', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: document.querySelector('#challenge-username').value,
            time: document.querySelector('#challenge-time').value,
            quizID: urlParams.get('quizID')
        })
    });
    return await rawResponse.json();
});

onChallengeClick = async function () {
    postChallenge().then((value) => {
        let error = document.querySelector('#challenge-error')
        if (value.success) {
            error.setAttribute('class', 'u-error-success');
        } else {
            error.setAttribute('class', 'u-error-fail');
        }
        error.innerHTML = value.message;
    });
};