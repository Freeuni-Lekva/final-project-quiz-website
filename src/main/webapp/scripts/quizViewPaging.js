const maxQuizPerPage = 1;

initPage = (() => {
    let categoryDropDown = document.getElementById("search-category");
    categoryDropDown.value = document.getElementById("c").value;

    let viewSearch = document.getElementById("view-search");
    viewSearch.value = document.getElementById("q").value;

    let pageNum = document.getElementById("pageNum").value;
    let quizPerPage = document.getElementsByClassName("u-quiz-search-result").length;

    document.getElementById("next-page").disabled = quizPerPage < maxQuizPerPage;
    document.getElementById("prev-page").disabled = pageNum === "1";
})

quizViewNextPage = (() => {
    let pageNumInput = document.getElementById("pageNum");
    pageNumInput.value = parseInt(pageNumInput.value, 10) + 1;
})

quizViewPrevPage = (() => {
    let pageNumInput = document.getElementById("pageNum");
    pageNumInput.value = parseInt(pageNumInput.value, 10) - 1;
})

sendQuery = (() => {
    document.getElementById("q").value = document.getElementById("view-search").value;
    document.getElementById("c").value = document.getElementById("search-category").value;
})

resetPage = (() => {
    document.getElementById("pageNum").value = "1";
})


window.addEventListener("load", initPage);