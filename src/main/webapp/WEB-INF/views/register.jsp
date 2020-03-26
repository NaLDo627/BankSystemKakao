<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<%@include file="../includes/header.jsp"%>

<body>

<!-- Page Content -->
<div class="register-form">
    <form method="post" id="register-form">
        <div class="form-group required">
            <label for="user-id" class="control-label">아이디</label>
            <input type="text" class="form-control" id="user-id" name="id" required="required" />
        </div>
        <div class="form-group required">
            <label for="user-pw" class="control-label">비밀번호</label>
            <input type="password" class="form-control" id="user-pw" name="password" required="required" />
        </div>
        <div class="form-group required">
            <label for="user-pw-confirm" class="control-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="user-pw-confirm" required="required" />
        </div>
        <div class="form-group required">
            <label for="user-name" class="control-label">이름</label>
            <input type="text" class="form-control" id="user-name" name="name" required="required" />
        </div>
        <div class="form-group">
            <label for="user-email">이메일</label>
            <input type="text" class="form-control" id="user-email" name="email" />
        </div>
        <div class="form-group">
            <label for="user-phone">전화번호</label>
            <input type="text" class="form-control" id="user-phone" name="phone" />
        </div>
        <div class="form-group">
            <a href="#" id="btn-register" class="btn btn-primary btn-block enter-key">회원가입</a>
        </div>
        <div class="form-group">
            <a href="/login" class="btn btn-info btn-block">뒤로가기</a>
        </div>
    </form>
</div>

<%@include file="../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/user.js"></script>
<script type="text/javascript">
    $(function () {
        $("#user-id").keyup(function(event){
            var limit = 32;

            /* id is only english, number and dot(.) */
            if (!(event.keyCode >=37 && event.keyCode<=40)) {
                var inputVal = $(this).val();
                $(this).val(inputVal.replace(/[^a-zA-Z0-9.]/gi,''));
            }

            if($(this).val().length > limit)
                $(this).val($(this).val().substring(0, limit))
        });

        $("#user-pw, #user-pw-confirm").keyup(function () {
            var limit = 64;
            if($(this).val().length > limit)
                $(this).val($(this).val().substring(0, limit))
        })

        $("#user-name, #user-email, #user-phone").keyup(function () {
            var limit = 32;
            if($(this).val().length > limit)
                $(this).val($(this).val().substring(0, limit))
        })

        $("input").on("keydown", function (key) {
            if (key.keyCode === 13) {
                $("#btn-register").trigger("click");
            }
        });
    });
</script>

</body>

</html>
