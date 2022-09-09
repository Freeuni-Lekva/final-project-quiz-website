
let choiceID = 1
function addNewChoice() {
    choiceID++;
    let newInput = document.createElement("input");
    newInput.type = "text";
    newInput.name = "choice";
    newInput.id = "choice" + choiceID;
    newInput.className = "u-question-input-text u-answer-choice";
    newInput.addEventListener('change', function() {setAnswer(this.id)})
    let form = document.getElementById("answers");

    let newCheckBox = document.createElement("input");
    newCheckBox.type = "checkbox";
    newCheckBox.name = "answer";
    newCheckBox.id = "correct" + choiceID;
    newCheckBox.className = "u-correct-checkbox";

    form.appendChild(document.createElement("br"));
    form.appendChild(newInput);
    form.appendChild(newCheckBox);
}

function setAnswer(id) {
    let checkBoxID = "correct" + id.substring(6);
    console.log(checkBoxID);
    document.getElementById(checkBoxID).value = document.getElementById(id).value;
}