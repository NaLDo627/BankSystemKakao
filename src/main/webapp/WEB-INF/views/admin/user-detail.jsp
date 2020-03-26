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
            <h1 class="page-information">사용자 상세 보기</h1>
        </div>
        <div class="clear"></div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row clear">
        &nbsp;
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-12">
            <table class="table">
                <input type="hidden" value="${bankUser.userNo}" name="userNo" />
                <thead class="thead-light">
                <tr>
                    <th colspan="4">${bankUser.name} - ${bankUser.rank.displayName}</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width: 20%">아이디</td>
                    <td colspan="3">${bankUser.id}</td>
                </tr>
                <tr>
                    <td style="width: 20%">이름</td>
                    <td colspan="3">${bankUser.name}</td>
                </tr>
                <tr>
                    <td>등급</td>
                    <td colspan="3">
                        <select class="custom-select mr-sm-2" style="width: 30%" id="select-user-rank">
                            <option value="NORMAL">일반</option>
                            <option value="VIP">VIP</option>
                            <option value="VVIP">VVIP</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td>이메일</td>
                    <td colspan="3">${bankUser.email == null || bankUser.email == "" ? "-" : bankUser.email}</td>
                </tr>
                <tr>
                    <td>전화번호</td>
                    <td colspan="3">${bankUser.phone == null || bankUser.phone == "" ? "-" : bankUser.phone}</td>
                </tr>
                <tr>
                    <td>소유 통장</td>
                    <td colspan="3">
                        <div class="form-group">
                            <select class="form-control" size="4" id="select-user-detail-bankbooks">
                                <c:forEach var="bankbook" items="${bankbooks}" varStatus="status">
                                    <option value="${bankbook.bankbookId}">${bankbook.type.displayName} - ${bankbook.bankbookId}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 10%; text-align: right" colspan="2"><a class="btn btn-outline-secondary" href="/admin" role="button">&nbsp;이전&nbsp;</a></td>
                    <td style="width: 10%"><a class="btn btn-danger" href="#" role="button" id="btn-admin-delete-user">&nbsp;사용자 삭제&nbsp;</a></td>
                    <td style="width: 10%"><a class="btn btn-primary" href="#" role="button" id="btn-admin-modify-user">&nbsp;저장&nbsp;</a></td>
                </tr>
                </tbody>
            </table>
        </div>
        <!-- /.col-12 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/user.js"></script>
<script type="text/javascript">
    $(function () {
        $("#select-user-rank").val("${bankUser.rank}");
        $("#select-user-rank option[value=\"${bankUser.rank}\"]").attr("selected", "true");
    })
</script>

</body>

</html>
