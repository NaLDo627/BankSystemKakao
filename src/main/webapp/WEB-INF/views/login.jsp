<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../includes/header.jsp"%>

<body>

<!-- Page Content -->
<div class="login-form">
    <form method="post" action="/login">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <h2 class="text-center">은행 입출금 시스템</h2>
        <div class="form-group ${error != null ? 'has-error' : ''}">
            <label for="user-id">아이디</label>
            <input type="text" class="form-control" id="user-id" name="id" required="required" />
        </div>
        <div class="form-group ${error != null ? 'has-error' : ''}">
            <label for="user-pw">비밀번호</label>
            <input type="password" class="form-control" id="user-pw" name="password" required="required" />
        </div>
        <div class="form-group">
            <button id="btn-login" class="btn btn-primary btn-block enter-key">로그인</button>
        </div>
        <div class="form-group">
            <a href="/register" class="btn btn-primary btn-block">회원가입</a>
        </div>
    </form>
</div>

<%@include file="../includes/footer.jsp"%>
<script type="text/javascript">
    $(function () {
        var urlParams = getUrlParams();
        if (urlParams.error === "true") {
            alert("아이디와 비밀번호가 일치하지 않습니다.")
        }
    })
</script>

</body>

</html>
