<style>
    .navbar ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        background-color: #333;
    }

    .navbar ul li {
        float: left;
        border-right:1px solid #bbb;
    }

    .navbar ul li:last-child {
        border-right: none;
    }

    .navbar ul li a {
        display: block;
        color: white;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
    }

    .navbar ul li a:hover:not(.active) {
        background-color: #111;
    }

    .active {
        background-color: #04AA6D;
    }
</style>
<div class="navbar">
    <ul>
        <li><a href="home">Home</a></li>
        <li><a href="quizzes">Quizzes</a></li>
        <li><a href="questions">Questions</a></li>
        <li style="float:right"><a href="profile">Login</a></li>
        <li style="float:right"><a href="profile">Logout</a></li>
        <li style="float:right"><a href="profile"><i>You are logged in as <b>${username}</b></i></a></li>
    </ul>
</div>
