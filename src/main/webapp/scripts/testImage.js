function testImage(picSelector) {
    let images = document.querySelectorAll(picSelector)
    for(let img of images) {
        img.src = document.getElementById("imgURL").value
    }
}