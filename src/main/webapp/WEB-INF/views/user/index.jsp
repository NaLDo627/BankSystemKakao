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
            <h1 class="page-information">환영합니다! ${bankUser.name}님</h1>
            <a class="btn btn-primary" href="/transaction" role="button">입출금 및 이체하기</a>&nbsp;
            <a class="btn btn-primary" href="/user-search" role="button">사용자 조회 & 검색</a>
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
            <div class="row">
                <div class="col-9">
                    <h2 class="page-item">내 통장 목록</h2>
                </div>
                <!-- /.col-9 -->
                <div class="col-3">
                    <a class="btn btn-primary" href="/new-bankbook" role="button">통장 생성</a>
                </div>
                <!-- /.col-3 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-12">
                    <div class="list-group">
                        <c:forEach var="bankbook" items="${bankbooks}" varStatus="status">
                            <a href="/bankbook-detail/${bankbook.bookNo}" class="list-group-item list-group-item-action">
                                    ${bankbook.alias} ( <fmt:formatNumber type="number" maxFractionDigits="3" value="${bankbook.balance}" /> 원 ) - ${bankbook.type.displayName}
                            </a>
                        </c:forEach>
                    </div>
                </div>
                <!-- /.col-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.col-lg-6 -->
        <div class="col-lg-6">
            <h2 class="page-item">최근 거래 내역</h2>
            <div class="list-group">
                <c:forEach var="history" items="${transactionHistorys}" varStatus="status">
                    <a href="/transaction-detail/${history.historyNo}" class="list-group-item list-group-item-action">
                            ${history.transactionBankbookAlias} - ${history.type.displayName} -
                                <fmt:formatDate value="${history.transactionDate}" pattern="yyyy-MM-dd" timeZone="Asia/Seoul" />
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
