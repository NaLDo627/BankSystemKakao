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
            <h1 class="page-information">입출금 및 이체</h1>
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
            <div class="row">
                <div class="col-sm-4 col-8">
                    <h2>통장을 선택해 주세요</h2>
                    <select class="custom-select" id="select-transaction-bankbook">
                        <option value="0" selected>통장 선택</option>
                        <c:forEach var="bankbook" items="${bankbooks}" varStatus="status">
                            <option value="${bankbook.bookNo}" balance="${bankbook.balance}" bookid="${bankbook.bankbookId}" booktype="${bankbook.type}">
                                    ${bankbook.alias} ( <fmt:formatNumber type="number" maxFractionDigits="3" value="${bankbook.balance}" /> 원 )
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <!-- /.col-4 -->
            </div>
            <!-- /.row -->
            <div class="row clear">
                <br/>
            </div>
            <!-- /.row -->
            <div class="row" id="div-left-balance-wrapper" style="display: none;">
                <div class="col-sm-4 col-8">
                    <h4>잔고</h4>
                    <div id="div-left-balance">

                    </div>
                </div>
                <!-- /.col-4 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
    <div class="row clear">
        <br/><br/>
    </div>
    <!-- /.row -->
    <div class="row" id="div-transaction-wrapper" style="display: none;">
        <div class="col-12">
            <div class="row">
                <div class="col-sm-8 col-12">
                    <nav>
                        <div class="nav nav-tabs" id="nav-tab" role="tablist">
                            <a class="nav-item nav-link active" id="nav-withdraw-tab" data-toggle="tab" href="#nav-withdraw" role="tab" aria-controls="nav-withdraw" aria-selected="true">입출금</a>
                            <a class="nav-item nav-link" id="nav-transfer-tab" data-toggle="tab" href="#nav-transfer" role="tab" aria-controls="nav-transfer" aria-selected="false">이체</a>
                        </div>
                    </nav>
                    <div class="tab-content" id="nav-tabContent">
                        <div class="tab-pane fade show active" id="nav-withdraw" role="tabpanel" aria-labelledby="nav-withdraw-tab">
                            <form id="frm-withdraw">
                                <input type="hidden" name="requestUserNo" value="${bankUser.userNo}" />
                                <input type="hidden" name="transactionBookId" value="" />
                                <div class="form-group">
                                    <label for="withdraw-or-deposit">종류</label>
                                    <select class="custom-select" id="withdraw-or-deposit" name="type">
                                        <option value="DEPOSIT" selected>입금</option>
                                        <option value="WITHDRAW">출금</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="withdraw-amount">금액</label>
                                    <input type="text" class="form-control" id="withdraw-amount" name="amount" required>
                                </div>
                                <div class="form-group">
                                    <label for="withdraw-memo">메모</label>
                                    <textarea class="form-control" id="withdraw-memo" name="memo" rows="6"></textarea>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-4 offset-8" style="text-align: right">
                                        <a href="javascript:if(confirm('거래가 취소됩니다'))location.href='/'" class="btn btn-outline-secondary btn-cancel">취소</a>&nbsp;
                                        <a href="#" id="btn-withdraw" class="btn btn-primary" form="#frm-withdraw">거래 실행</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="tab-pane fade" id="nav-transfer" role="tabpanel" aria-labelledby="nav-transfer-tab">
                            <form id="frm-transfer">
                                <input type="hidden" name="requestUserNo" value="${bankUser.userNo}" />
                                <input type="hidden" name="transactionBookId" value="" />
                                <input type="hidden" name="type" value="TRANSFER" />
                                <div class="form-group">
                                    <label for="transfer-receive-bankbook">수취 통장</label>
                                    <input type="text" class="form-control" id="transfer-receive-bankbook" name="receiverBookId" value="${receiveBookId}" required>
                                </div>
                                <div class="form-group">
                                    <label for="transfer-amount">금액</label>
                                    <input type="text" class="form-control" id="transfer-amount" name="amount" required>
                                </div>
                                <div class="form-group">
                                    <label for="transfer-memo">메모</label>
                                    <textarea class="form-control" id="transfer-memo" name="memo" rows="6"></textarea>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-4 offset-8" style="text-align: right">
                                        <a href="javascript:if(confirm('거래가 취소됩니다'))location.href='/'" class="btn btn-outline-secondary btn-cancel">취소</a>&nbsp;
                                        <a href="#" id="btn-transfer" class="btn btn-primary" form="#frm-transfer">거래 실행</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <!-- /.col-8 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/transaction.js"></script>
<script type="text/javascript">
    var isTransferChargeOccurs = ${isTransferChargeOccurs};
    var isEmptyBankbooks = ${bankbooks.size() == 0};
    var transactionBookNo = ${transactionBookNo};
    var receiveBookId = "${receiveBookId}";

    function renderTransactionWrapper(bookNo) {
        if (Number(bookNo) === 0) {
            $("#div-left-balance-wrapper").hide();
            $("#div-transaction-wrapper").hide();
            return false;
        }

        $("#div-left-balance").empty();
        var balance = $("#select-transaction-bankbook option:selected").attr("balance");
        $("#div-left-balance").html(balance.numberFormat() + " 원");
        var bankbookId = $("#select-transaction-bankbook option:selected").attr("bookid");
        $("input[name='transactionBookId']").val(bankbookId);
        $("input[name='amount']").val(0);
        $("input[name='memo']").val("");
        // $("input[name='receiverBookId']").val("");
        $("#withdraw-or-deposit").val("DEPOSIT");
        $("#div-left-balance-wrapper").show();
        $("#div-transaction-wrapper").show();
    }

    $("#select-transaction-bankbook").on("change", function () {
        renderTransactionWrapper($(this).val())
    });
</script>

</body>
</html>
