let loadedRating = 0;
const starChecked = "fa-star-checked";
const urlParams = new URLSearchParams(window.location.search);
const quizID = urlParams.get("quizID");

starMouseOut = (() => {
    console.log("Star Num is " + loadedRating);
    let starsDiv = document.getElementById("stars");
    let stars = starsDiv.children;
    for(let i = 0; i < stars.length; i++) {
        stars[i].classList.remove(starChecked);
    }

    for(let i = 0; i < loadedRating; i++) {
        stars[i].classList.add(starChecked);
    }
})

starMouseOver = ((starNum) => {
    let starsDiv = document.getElementById("stars");
    let stars = starsDiv.children;
    for(let i = 0; i < stars.length; i++) {
        stars[i].classList.remove(starChecked);
    }

    for(let i = 0; i < starNum; i++) {
        stars[i].classList.add(starChecked);
    }
})

sendRating = ((starNum) => {
    let form = document.getElementById("rating-form");
    console.log(form);
    let quizIDInput = document.createElement("input");
    quizIDInput.setAttribute("type", "hidden");
    quizIDInput.setAttribute("name", "quizID");
    quizIDInput.value = quizID;

    form.appendChild(quizIDInput);

    let ratingInput = document.createElement("input");
    ratingInput.setAttribute("type", "hidden");
    ratingInput.setAttribute("name", "starCount");
    ratingInput.value = starNum;

    form.appendChild(ratingInput);
    form.submit();
})

window.addEventListener("load", starMouseOut);