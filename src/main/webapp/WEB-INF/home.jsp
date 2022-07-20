<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Website</title>
</head>
<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<div class="sub-body" style="margin: 10px;">
    <h1>Quiz Website</h1>
    <div class="announcements">
        <h2>Announcements</h2>
        <ul>
            ${announcementListItems}
        </ul>
    </div>
    <div class="popular-quizzes">
        <h3>Popular quizzes</h3>
        <ul>
            ${popularQuizzesListItems}
        </ul>
    </div>
    <div class="recent-quizzes">
        <h3>Most recent quizzes</h3>
        <ul>
            ${recentQuizzesListItems}
        </ul>
    </div>
    <div class="quizzes-taken">
        <h3>Quizzes you have taken recently</h3>
        <ul>
            ${quizzesTakenByUserListItems}
        </ul>
    </div>
    <div class="quizzes-created">
        <h3>Quizzes you have created recently</h3>
        <ul>
            ${quizzesCreatedByUserListItems}
        </ul>
    </div>
    <div class="achievements-of-user">
        <h3>Your achievements</h3>
        <ul>
            ${userAchievementsListItems}
        </ul>
    </div>
    <div class="received-messages">
        <h3>Messages</h3>
        <ul>
            ${receivedMessagesListItems}
        </ul>
    </div>
    <div class="friends-activities">
        <!-- TODO -->
    </div>
</div>
</body>
</html>
