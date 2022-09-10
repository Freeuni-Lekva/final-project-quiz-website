function addNewAnswer() {
    let newInput = document.createElement("input");
    newInput.type = "text";
    newInput.name = "answer";
    newInput.id = "answer";
    newInput.className = "u-question-input-text";
    let form = document.getElementById("answers");

    form.appendChild(document.createElement("br"));
    form.appendChild(newInput);
}