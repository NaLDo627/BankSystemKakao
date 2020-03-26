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
            <h1 class="page-information">통장 상세 보기</h1>
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
        <div class="col-12">
            <form id="form-bankbook-detail">
                <table class="table">
                    <thead class="thead-light">
                        <tr>
                            <input type="hidden" name="bookNo" value="${bankbook.bookNo}" />
                            <th colspan="5">${bankbook.alias} - ${bankbook.type.displayName}</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="width: 20%">통장 번호</td>
                        <td colspan="4">${bankbook.bankbookId}</td>
                    </tr>
                    <tr>
                        <td>통장 종류</td>
                        <td colspan="4">${bankbook.type.displayName}</td>
                    </tr>
                    <tr>
                        <td>*현재 잔고</td>
                        <td colspan="4"><fmt:formatNumber type="number" maxFractionDigits="3" value="${bankbook.balance}" /> 원</td>
                        <input type="hidden" name="balance" value="${bankbook.balance}" />
                    </tr>
                    <tr>
                        <td>생성 날짜</td>
                        <td colspan="4"><fmt:formatDate value="${bankbook.insertDate}" pattern="yyyy-MM-dd HH:mm:ss" timeZone="Asia/Seoul" /></td>
                    </tr>
                    <tr>
                        <td>수정 날짜</td>
                        <td colspan="4"><fmt:formatDate value="${bankbook.updateDate}" pattern="yyyy-MM-dd HH:mm:ss" timeZone="Asia/Seoul" /></td>
                    </tr>
                    <tr>
                        <td colspan="2">*현재 잔고에서 마이너스 금액은 신용대출 금액입니다.</td>
                        <td style="width: 10%; text-align: right"><a class="btn btn-outline-secondary" href="/" role="button">&nbsp;이전&nbsp;</a></td>
                        <td style="width: 10%"><a class="btn btn-danger" href="#" role="button" id="btn-delete-bankbook">&nbsp;통장 삭제&nbsp;</a></td>
                        <td style="width: 10%"><a class="btn btn-primary" href="/transaction?bookNo=${bankbook.bookNo}" role="button">&nbsp;입출금 및 이체하기&nbsp;</a></td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/bankbook.js"></script>

</body>

</html>
