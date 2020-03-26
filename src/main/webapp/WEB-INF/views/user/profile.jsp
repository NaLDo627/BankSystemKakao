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
            <h1 class="page-information">내 정보 변경</h1>
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
            <form id="profile-form">
                <input type="hidden" name="userNo" value="${bankUser.userNo}" />
                <table class="table">
                    <thead class="thead-light">
                        <tr>
                            <th colspan="4">내 정보</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td style="width: 20%">아이디</td>
                        <td colspan="3"><input type="text" class="form-control-plaintext" style="width: 50%" id="user-id" name="id" value="${bankUser.id}" readonly/></td>
                    </tr>
                    <tr>
                        <td>비밀번호</td>
                        <td colspan="3"><input type="password" class="form-control" style="width: 50%" id="user-pw" name="password" /></td>
                    </tr>
                    <tr>
                        <td>비밀번호 확인</td>
                        <td colspan="3"><input type="password" class="form-control" style="width: 50%" id="user-pw-confirm" /></td>
                    </tr>
                    <tr>
                        <td>이름</td>
                        <td colspan="3"><input type="text" class="form-control" style="width: 50%" id="user-name" name="name" value="${bankUser.name}" /></td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td colspan="3"><input type="text" class="form-control" style="width: 50%" id="user-phone" name="phone" value="${bankUser.phone}" /></td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td colspan="3"><input type="text" class="form-control" style="width: 50%" id="user-email" name="email" value="${bankUser.email}" /></td>
                    </tr>
                    <tr>
                        <td>등급</td>
                        <td colspan="3">${bankUser.rank.displayName}</td>
                    </tr>
                    <tr>
                        <td colspan="4" style="text-align: right"><a class="btn btn-primary" href="#" role="button" id="btn-profile">&nbsp;정보 수정&nbsp;</a></td>
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
<script type="text/javascript" src="/resources/js/user.js"></script>
</body>

</html>
