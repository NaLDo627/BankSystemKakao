<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark static-top">
    <div class="container">
        <c:choose>
            <c:when test="${pageContext.request.isUserInRole(\"USER\")}">
                <a class="navbar-brand" href="/">My Beautiful Bank</a>
            </c:when>
            <c:otherwise>
                <a class="navbar-brand" href="/admin">My Beautiful Bank</a>
            </c:otherwise>
        </c:choose>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <c:if test="${pageContext.request.isUserInRole(\"USER\")}">
                    <li class="nav-item">
                        <a class="nav-link" href="/profile">Profile</a>
                    </li>
                </c:if>
                <li class="nav-item">
                    <form method="post" action="/logout" id="form-logout">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                    <a href="#" id="btn-logout" class="nav-link">Log out</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

