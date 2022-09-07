let questions = []

let searchData = {category: -1, page: 0, showMine: false}

onClickAdd = ( (id, title) => {
    let question = {
        questionId: id,
        title: title,
        timedLength: -1,
    };

    questions.push(question);

   //draw
});

onClickRemove = ( (event, index) => {
    event.target.parentElement.remove();
    questions.splice(index, 1);
});

getCategory = ( () => {
    return document.getElementById("category").value
});

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

    console.log(content);
});

searchQuestions = ( () => {
    searchData.page = 0
    fetchQuestions();
});

nextPage = ( () => {
    searchData.page++;
    fetchQuestions();
});