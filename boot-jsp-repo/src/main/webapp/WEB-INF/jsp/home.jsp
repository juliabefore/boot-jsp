<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Home page</title>
</head>
<body>
<h1>Hello</h1>

<c:if test="${currentUser.id != null}">
<a href="/user/${currentUser.id}">View myself</a>
</c:if>

<br/>

<c:if test="${currentUser.role == 'ADMIN'}">
    <spring:url value="/users" var="users" />
    <a href="${users}" title="users">List of users</a>
</c:if>

<c:if test="${currentUser.id != null}">
    <form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit">Log out</button>
    </form>

    <br/>
    <sec:authorize access="isAuthenticated()">
    <h2>Welcome ${currentUser.user.email}</h2>
    </sec:authorize>

    <spring:url value="/user/${currentUser.id}/update" var="update" />
    <a href="${update}" title="update">Update</a>

    <spring:url value="/user/${currentUser.id}/delete" var="delete" />
    <a href="${delete}" title="delete">Remove</a>
</c:if>

<br />
<br />
<c:if test="${currentUser.id == null}">
<spring:url value="/login" var="signIn" />
<a href="${signIn}" title="signIn">SignIn</a>

<spring:url value="/user/create" var="signUp" />
<a href="${signUp}" title="signUp">SignUp</a>

</c:if>

</body>
</html>