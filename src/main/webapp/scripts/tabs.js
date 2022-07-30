function openProfileTab(btnId, tabName) {
    console.log("id: " + btnId + " tab: " + tabName);
    let tabContents = document.getElementsByClassName("tab-content");
    for(let tabContent of tabContents) {
        tabContent.style.display = "none";
    }

    let tabButtons = document.getElementsByClassName("tab-button");
    for (let tabBtn of tabButtons) {
        tabBtn.className = tabBtn.className.replace(" active", "")
    }

    document.getElementById(tabName).style.display = "block";
    document.getElementById(btnId).className += " active";
}