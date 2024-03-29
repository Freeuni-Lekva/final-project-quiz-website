let searchData = {category: -1, query: "", page: 0, showMine: false}

const MAX_QUESTIONS = 50;
let questionCount = 0;

onClickAdd = ( (questionData) => {
    const tableBody = document.getElementById("added-questions-table-body");
    const newRow = tableBody.insertRow();
    newRow.insertCell(0).innerHTML = questionData.id;
    newRow.insertCell(1).innerHTML = questionData.title;

    const creatorLink = document.createElement("a");
    creatorLink.setAttribute("href", "profile?id=" + questionData.creatorID);
    creatorLink.innerHTML = questionData.creatorName;
    newRow.insertCell(2).appendChild(creatorLink);
    newRow.insertCell(3).innerHTML = questionData.categoryName;
    const upBtn = makeUpButton();
    const downBtn = makeDownButton();
    const swapCell = newRow.insertCell(4);
    swapCell.appendChild(upBtn);
    swapCell.appendChild(downBtn);
    const removeBtn = document.createElement("button");
    removeBtn.setAttribute("type", "button");
    removeBtn.innerHTML = "x";
    removeBtn.addEventListener("click", (event) => {
      onClickRemove(event);
    })
    newRow.insertCell(5).appendChild(removeBtn);
    const hiddenInput = document.createElement("input");
    hiddenInput.setAttribute("type", "hidden");
    hiddenInput.setAttribute("name", "question");
    hiddenInput.setAttribute("value", questionData.id);
    newRow.appendChild(hiddenInput)
});

makeUpButton = ( () => {
    let button = document.createElement("button");
    button.setAttribute("type", "button");
    button.innerHTML = "^";
    button.addEventListener("click", (event) => {
        let row = event.target.parentNode.parentNode;
        let prevRow = row.previousSibling;
        let parent = row.parentNode;

        if(prevRow)
            parent.insertBefore(row, prevRow)
    })
    return button;
})

makeDownButton = ( () => {
    let button = document.createElement("button");
    button.setAttribute("type", "button");
    button.innerHTML = "v";
    button.addEventListener("click", (event) => {
        let row = event.target.parentNode.parentNode;
        let nextRow = row.nextSibling;
        let parent = row.parentNode;

        if(nextRow)
            parent.insertBefore(nextRow, row)
    })
    return button;
})

validateQuizCreateForm = ( () => {
    let questionIDs = document.forms["creationForm"]["question"];
    if(!questionIDs) {
        alert("At least one question must be added");
        return false;
    }
    return true;
})

onClickRemove = ( (event) => {
    event.target.parentElement.parentElement.remove();
});

getCategory = ( () => {
    return document.getElementById("search-category").value
});

getQuery = ( () => {
    return document.getElementById("search-query").value;
})

makeMoveButton = ( (rowNum, questionData) => {
    let result = document.createElement("button");
    result.addEventListener("click", () => {
        onClickAdd(questionData);
    });
    result.innerHTML = "<-";
    return result;
})

fetchQuestions = (async () => {
    searchData.category = getCategory();
    searchData.query = getQuery();
    const rawResponse = await fetch('../getQuestions', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(searchData)
    });
    const content = await rawResponse.json();

    const oldTable = document.getElementById("search-results");
    const newTable = document.createElement("tbody");
    newTable.setAttribute("id", "search-results");
    document.getElementById("search-results-table").replaceChild(newTable, oldTable);

    for(let i = 0; i < content.length; i++) {
        let row = newTable.insertRow(i);
        row.insertCell(0).appendChild(makeMoveButton(i, content[i]));
        row.insertCell(1).innerHTML = content[i].id;
        row.insertCell(2).innerHTML = content[i].title;
        row.insertCell(3).innerHTML = content[i].categoryName;

        const creatorLink = document.createElement("a");
        creatorLink.setAttribute("href", "profile?id=" + content[i].creatorID);
        creatorLink.innerHTML = content[i].creatorName;
        row.insertCell(4).appendChild(creatorLink);
    }

    questionCount = content.length;
});

searchQuestions = ( () => {
    searchData.page = 0
    fetchQuestions();
    document.getElementById("next-page").disabled = questionCount < MAX_QUESTIONS;
    document.getElementById("prev-page").disabled = true;
});

nextPage = ( () => {
    searchData.page++;
    fetchQuestions();
    document.getElementById("next-page").disabled = questionCount < MAX_QUESTIONS;
    document.getElementById("prev-page").disabled = false;
});

prevPage = ( () => {
    searchData.page--;
    fetchQuestions();
    document.getElementById("next-page").disabled = questionCount < MAX_QUESTIONS;
    document.getElementById("prev-page").disabled = searchData.page === 0;
})