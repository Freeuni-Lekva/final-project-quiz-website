<%@ page import="com.project.website.Objects.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ page import="com.project.website.DAOs.CategoryDAO" %>
<%@ page import="com.project.website.DAOs.AnnouncementDAO" %>
<%@ page import="com.project.website.DAOs.AnnouncementDAOSQL" %>
<%@ page import="com.project.website.Objects.Category" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.project.website.Objects.Announcement" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<c:if test="${sessionScope.admin == null || sessionScope.admin == false}">
    <c:redirect url="home"/>
</c:if>
<html>
<head>
    <title>Quiz Website</title>
    <style>
        .column {
            width: 25%;
            margin: 1%;
            min-height: 300px;
            background: #04AA6D;
        }
        h2 {
            text-align: center;
        }
        table, tr, th, td {
            border: solid 1px black;
        }
    </style>
</head>
<%
    UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
    CategoryDAO categoryDAO = (CategoryDAO) request.getServletContext().getAttribute(CategoryDAO.ATTR_NAME);
    AnnouncementDAO announcementDAO = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);

    // User logic
    String userQuery = "";
    String userQueryCount = "10";
    if (request.getParameter("user-q") != null)
        userQuery = request.getParameter("user-q");
    if (request.getParameter("user-qc") != null && !request.getParameter("user-qc").equals(""))
        userQueryCount = request.getParameter("user-qc");
    if (request.getParameter("demote") != null) {
        long id = Integer.parseInt(request.getParameter("demote"));
        userDAO.removeAdminPrivileges(id);
    }
    if (request.getParameter("promote") != null) {
        long id = Integer.parseInt(request.getParameter("promote"));
        userDAO.promoteToAdmin(id);
    }
    List<User> users = userDAO.searchUsers("%"+userQuery+"%");
    users = users.subList(0, Math.min(users.size(), Integer.parseInt(userQueryCount)));
    request.setAttribute("users", users);

    /* Category logic */
    String categoryQuery = "";
    String categoryQueryCount = "10";
    if (request.getParameter("category-q") != null)
        categoryQuery = request.getParameter("category-q");
    if (request.getParameter("category-qc") != null && !request.getParameter("category-qc").equals(""))
        categoryQueryCount = request.getParameter("category-qc");
    if (request.getParameter("delete-category") != null) {
        int id = Integer.parseInt(request.getParameter("delete-category"));
        categoryDAO.deleteCategory(id);
    }
    if (request.getParameter("insert-category") != null) {
        String newCategory = request.getParameter("insert-category");
        categoryDAO.insertCategory(new Category(newCategory));
    }
    String finalQuery = categoryQuery;
    List<Category> categories = categoryDAO.getAllCategories().stream().filter(c -> c.getCategoryName().toLowerCase().contains(finalQuery.toLowerCase())).collect(Collectors.toList());
    categories = categories.subList(0, Math.min(categories.size(), Integer.parseInt(categoryQueryCount)));
    request.setAttribute("categories", categories);

    /* Announcement logic */
    String announcementQuery = "";
    String announcementQueryCount = "10";
    if (request.getParameter("announcement-q") != null)
        announcementQuery = request.getParameter("announcement-q");
    if (request.getParameter("announcement-qc") != null && !request.getParameter("announcement-qc").equals(""))
        announcementQueryCount = request.getParameter("announcement-qc");
    if (request.getParameter("delete-announcement") != null) {
        int id = Integer.parseInt(request.getParameter("delete-announcement"));
        announcementDAO.deleteAnnouncementById(id);
    }
    String finalAnnouncementQuery = announcementQuery;
    List<Announcement> announcements = announcementDAO.getAllAnnouncements().stream().filter(a -> a.getTitle().toLowerCase().contains(finalAnnouncementQuery.toLowerCase()) || a.getText().toLowerCase().contains(finalAnnouncementQuery.toLowerCase())).collect(Collectors.toList());
    announcements = announcements.subList(0, Math.min(announcements.size(), Integer.parseInt(announcementQueryCount)));
    request.setAttribute("announcements", announcements);
%>
<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<div class="sub-body" style="margin: 10px;">
    <h1 style="text-align: center;">Admin Menu</h1>
    <div class="column-container" style="display: flex">
        <div class="column">
            <h2>Users</h2>
            <form action="admin" method="get" style="display: flex">
                <input type="text" name="user-q" value="<% out.write(userQuery); %>" placeholder="Search query" style="float: left; margin-left: 20px;"/>
                <input type="number" name="user-qc" value="<% out.write(userQueryCount); %>" style="width: 40px;"/>
                <input type="submit" value="Search"/>
            </form>
            <table style="margin: auto;">
                <tr>
                    <th>Profile Picture</th>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Admin</th>
                    <th>Delete</th>
                </tr>
                <c:forEach items="${requestScope.users}" var="user">
                    <tr>
                        <td><img src="${user.profilePicURL}" alt="profile_pic" height="64" width="64"></td>
                        <td><a href="profile?id=${user.id}"><c:out value="${user.username}"/></a></td>
                        <td><c:out value="${user.firstName}"/></td>
                        <td><c:out value="${user.lastName}"/></td>
                        <c:choose>
                            <c:when test="${user.admin == true}">
                                <td><a href="admin?demote=<c:out value="${user.id}"/>">Demote</a></td>
                            </c:when>
                            <c:otherwise>
                                <td><a href="admin?promote=<c:out value="${user.id}"/>">Promote</a></td>
                            </c:otherwise>
                        </c:choose>
                        <td><a href="admin?delete-user=${user.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="column">
            <h2>Categories</h2>
            <form action="admin" method="get" style="display: flex">
                <input type="text" name="category-q" value="<% out.write(categoryQuery); %>" placeholder="Search query" style="float: left; margin-left: 20px;"/>
                <input type="number" name="category-qc" value="<% out.write(categoryQueryCount); %>" style="width: 40px;"/>
                <input type="submit" value="Search"/>
            </form>
            <form action="admin" method="get" style="display: flex">
                <input type="text" name="insert-category" placeholder="New Category" style="float: left; margin-left: 20px;"/>
                <input type="submit" value="Add"/>
            </form>
            <table style="margin: auto;">
                <tr>
                    <th>ID</th>
                    <th>Category</th>
                    <th>Delete</th>
                </tr>
                <c:forEach items="${requestScope.categories}" var="category">
                    <tr>
                        <td><c:out value="${category.id}"/></td>
                        <td><c:out value="${category.categoryName}"/></td>
                        <td><a href="admin?delete-category=${category.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="column">
            <h2>Announcements</h2>
            <form action="admin" method="get" style="display: flex">
                <input type="text" name="announcement-q" value="<% out.write(announcementQuery); %>" placeholder="Search query" style="float: left; margin-left: 20px;"/>
                <input type="number" name="announcement-qc" value="<% out.write(announcementQueryCount); %>" style="width: 40px;"/>
                <input type="submit" value="Search"/>
                <br>
            </form>
            <div style="margin: auto; display: flex; justify-content: center;">
                <button onclick="clickedNewAnnouncement()">
                     <a id="button-new-announcement">Post New Announcement</a>
                </button>
            </div>
            <table style="margin: auto;">
                <tr>
                    <th>ID</th>
                    <th>Creator ID</th>
                    <th>Title</th>
                    <th>Time</th>
                    <th>Delete</th>
                </tr>
                <c:forEach items="${requestScope.announcements}" var="announcement">
                    <tr>
                        <td><c:out value="${announcement.id}"/></td>
                        <td><c:out value="${announcement.creatorId}"/></td>
                        <td><a href="announcement?id=<c:out value="${announcement.id}"/>"><c:out value="${announcement.title}"/></a></td>
                        <td><c:out value="${announcement.creationTime}"/></td>
                        <td><a href="admin?delete-announcement=${announcement.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<div style="position:absolute; height: 50vh; width: 40vw; top:25vh; bottom:25vh; left:30vw; right: 30vw; background: #E2DCC8; border-radius: 25px; display: none;" id="new-announcement-div">
    <button style="position: absolute; top: 10px; right: 10px;" onclick="closedNewAnnouncement()">Close</button>
    <form action="admin" method="post" style="position: relative; top: 50px; display: grid;">
        <input type="text" name="announcement-title" placeholder="Announcement Title" required>
        <textarea name="announcement-text" placeholder="Announcement Text" required rows="10" style="resize: vertical;"></textarea>
        <input type="submit" value="Announce">
    </form>
</div>
<script>
    function clickedNewAnnouncement() {
        document.getElementById("new-announcement-div").style.display = "block";
    }
    function closedNewAnnouncement() {
        document.getElementById("new-announcement-div").style.display = "none";
    }
</script>
</body>
</html>
