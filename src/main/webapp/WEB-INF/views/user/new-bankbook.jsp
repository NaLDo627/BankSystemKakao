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
            <h1 class="page-information">통장 생성</h1>
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
                <tbody>
                <tr>
                    <td style="width: 20%">통장 종류</td>
                    <td colspan="3" style="width: 60%">
                        <select class="custom-select mr-sm-2" style="width: 30%" id="bankbook-type">
                            <option selected value="NORMAL">일반</option>
                            <option value="CREDIT">신용대출</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td style="width: 20%">통장 별명</td>
                    <td colspan="3" style="width: 60%">
                        <input type="text" class="form-control" style="width: 50%" id="bankbook-alias" />
                    </td>
                </tr>
                <tr>
                    <input type="hidden" value="${bankUser.userNo}" id="bankbook-owner" />
                    <td colspan="3" style="text-align: right"><a class="btn btn-outline-secondary" href="/" role="button">&nbsp;취소&nbsp;</a></td>
                    <td style="width: 10%"><a class="btn btn-primary" href="#" id="btn-new-bankbook">&nbsp;생성&nbsp;</a></td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/bankbook.js"></script>
<script type="text/javascript">
    $(function () {
        if (${creditBankbookLimitReached}) {
            alert("신용통장 한도에 도달하여 더 이상 통장을 생성할 수 없습니다.\n통장을 더 생성하시려면 신용대출을 상환해 주세요.")
            location.href="/";
        }

        $("#bankbook-alias").keyup(function(event){
            var limit = 32;
            if($(this).val().length > limit)
                $(this).val($(this).val().substring(0, limit))
        });

        $("input").on("keydown", function (key) {
            if (key.keyCode === 13) {
                $("#btn-new-bankbook").trigger("click");
            }
        });
    })
</script>

</body>

</html>
