toggleNotifications = (() => {
    const bell = document.getElementById("notification-bell");
    getNotifications();
    if(document.getElementById("notifications-container").style.display === "none") {
        document.getElementById("notifications-container").style.display = "block";
        bell.className = bell.className.replace("fa-bell-o", "fa-bell");
    }
    else {
        document.getElementById("notifications-container").style.display = "none";
        bell.className = bell.className.replace("fa-bell", "fa-bell-o");
        setNotificationsSeen();
    }
})

makeNotificationInfoDiv = ((notification) => {
    const notificationDiv = document.createElement("div");
    notificationDiv.className = "notification-info";
    notificationDiv.addEventListener("click", () => {
        setNotificationsSeen();
        location.href = notification.href;
    })
    const title = document.createElement("h3");
    title.innerHTML = notification.notificationTitle;
    notificationDiv.appendChild(title);
    const desc = document.createElement("p");
    desc.innerHTML = notification.notificationDescription;
    notificationDiv.appendChild(desc);

    return notificationDiv;
})

const getNotificationsURL = "/final_project_quiz_website_war_exploded/getNotifications";
getNotifications = (async () => {
    const rawResponse = await fetch(getNotificationsURL, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });
    const content = await rawResponse.json();
    const notificationNumSub = document.getElementById("notification-number");
    notificationNumSub.innerHTML = content.totalCount;
    if(content.totalCount > 0 && document.getElementById("notifications-container").style.display === "none") {
        notificationNumSub.style.visibility = "visible";
    }
    const notificationsList = content.notifications;
    const notificationsBox = document.getElementById("notifications-box");
    notificationsBox.innerHTML = "";

    for(let i = 0; i < content.totalCount; i++) {
        notificationsBox.appendChild(makeNotificationInfoDiv(notificationsList[i]));
    }
})

setNotificationsSeen = ( async () => {
    const rawResponse = await fetch(getNotificationsURL, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });
    const notificationNumSub = document.getElementById("notification-number");
    notificationNumSub.innerHTML = "0";
    notificationNumSub.style.visibility = "hidden";
})


const poolTimeMilli = 2000;
window.onload = async function () {
    setInterval(getNotifications, poolTimeMilli);
};