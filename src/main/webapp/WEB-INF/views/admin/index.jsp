<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../../includes/header.jsp"%>

<body>
<%@include file="../../includes/navigation-bar.jsp"%>

<!-- Page Content -->
<div class="container">
    <div class="row">
        <div class="col-12">
            &nbsp;
            <h1 class="page-information">관리자 페이지입니다.</h1>
        </div>
        <div class="clear"></div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
    <div class="row clear">
        &nbsp;
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-6">
            <h2 class="page-item">전체 통장 목록</h2>
            <div class="list-group">
                <c:forEach var="bankbook" items="${bankbooks}" varStatus="status">
                    <a href="/admin/bankbook-detail/${bankbook.bookNo}" class="list-group-item list-group-item-action">
                            ${bankbook.ownerUserNo.name} - ${bankbook.alias} - ${bankbook.type.displayName}
                    </a>
                </c:forEach>
            </div>
        </div>
        <!-- /.col-lg-6 -->
        <div class="col-lg-6">
            <h2 class="page-item">사용자 목록</h2>
            <div class="list-group">
                <c:forEach var="bankUser" items="${bankUsers}" varStatus="status">
                    <a href="/admin/user-detail/${bankUser.userNo}" class="list-group-item list-group-item-action">
                            ${bankUser.name} - ${bankUser.id}
                    </a>
                </c:forEach>
            </div>
        </div>
        <!-- /.col-lg-6 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>

</body>
</html>
