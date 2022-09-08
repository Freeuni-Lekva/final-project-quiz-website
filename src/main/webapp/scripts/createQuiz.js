let questions = []

let searchData = {category: -1, page: 0, showMine: false}

onClickAdd = ( (questionData) => {
    questions.push(questionData);
    const tableBody = document.getElementById("added-questions-table-body");
    const newRow = tableBody.insertRow();
    newRow.insertCell(0).innerHTML = questionData.id;
    newRow.insertCell(1).innerHTML = questionData.title;

    const creatorLink = document.createElement("a");
    creatorLink.setAttribute("href", "profile?id=" + questionData.creatorID);
    creatorLink.innerHTML = questionData.creatorName;
    newRow.insertCell(2).appendChild(creatorLink);
    newRow.insertCell(3).innerHTML = "plchldr";
   //draw
});

onClickRemove = ( (event, index) => {
    event.target.parentElement.remove();
    questions.splice(index, 1);
});

getCategory = ( () => {
    return document.getElementById("category").value
});

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
    const rawResponse = await fetch('getQuestions', {
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
    // let headerRow = newTable.insertRow();
    // headerRow.insertCell(0).innerHTML = "";
    // headerRow.insertCell(1).innerHTML = "ID";
    // headerRow.insertCell(2).innerHTML = "Title";
    // headerRow.insertCell(3).innerHTML = "Creator";

    for(let i = 0; i < content.length; i++) {
        let row = newTable.insertRow(i);
        row.insertCell(0).appendChild(makeMoveButton(i, content[i]));
        row.insertCell(1).innerHTML = content[i].id;
        row.insertCell(2).innerHTML = content[i].title;

        const creatorLink = document.createElement("a");
        creatorLink.setAttribute("href", "profile?id=" + content[i].creatorID);
        creatorLink.innerHTML = content[i].creatorName;
        row.insertCell(3).appendChild(creatorLink);
    }
});

searchQuestions = ( () => {
    searchData.page = 0
    fetchQuestions();
});

nextPage = ( () => {
    searchData.page++;
    fetchQuestions();
});