let loadedRating = 2
const starChecked = "fa-star-checked";

starMouseOut = (() => {
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

})