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
            <h1 class="page-information">거래 상세 보기</h1>
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
            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th colspan="4">${transactionHistory.transactionBankbookAlias}</th>
                    </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width: 20%">거래 번호</td>
                    <td colspan="3">${transactionHistory.historyNo}</td>
                </tr>
                <tr>
                    <td>거래 종류</td>
                    <td colspan="3">${transactionHistory.type.displayName}</td>
                </tr>
                <c:if test="${transactionHistory.type.toString().equals('TRANSFER')}">
                    <tr>
                        <td>송금자</td>
                        <td colspan="3">${transactionHistory.requestUserName}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>통장 번호</td>
                    <td colspan="3">${transactionHistory.requestBookId}</td>
                </tr>

                <c:if test="${transactionHistory.type.toString().equals('TRANSFER')}">
                    <tr>
                        <td>수취자</td>
                        <td colspan="3">${transactionHistory.receivedUserName}</td>
                    </tr>
                    <tr>
                        <td>수취 통장</td>
                        <td colspan="3">${transactionHistory.receiverBookId}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>요청 거래 금액</td>
                    <td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${transactionHistory.requestAmount}" /> 원</td>
                </tr>
                <tr>
                    <td>발생 수수료</td>
                    <td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${transactionHistory.transactionFee}" /> 원</td>
                </tr>
                <tr>
                    <td>실거래 금액</td>
                    <td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${transactionHistory.actualAmount}" /> 원</td>
                </tr>
                <tr>
                    <td>요청 전 잔액</td>
                    <td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${transactionHistory.previousBalance}" /> 원</td>
                </tr>
                <tr>
                    <td>요청 후 잔액</td>
                    <td colspan="3"><fmt:formatNumber type="number" maxFractionDigits="3" value="${transactionHistory.remainBalance}" /> 원</td>
                </tr>
                <tr>
                    <td>메모</td>
                    <td colspan="3">${transactionHistory.memo == null || transactionHistory.memo == "" ? "-" : transactionHistory.memo}</td>
                </tr>
                <tr>
                    <td>거래 날짜</td>
                    <td colspan="3"><fmt:formatDate value="${transactionHistory.transactionDate}" pattern="yyyy-MM-dd HH:mm:ss" timeZone="Asia/Seoul" /></td>
                </tr>
                <tr>
                    <td colspan="4" style="text-align: right"><a class="btn btn-primary" href="/" role="button">&nbsp;확인&nbsp;</a></td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>

</body>

</html>
