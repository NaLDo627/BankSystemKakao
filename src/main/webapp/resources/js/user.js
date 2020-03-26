$(document).ready(function () {
    /* /register */
    $("#btn-register").on("click", function () {
        /* Password 체크 */
        var password = $("#user-pw").val();
        var passwordConfirm = $("#user-pw-confirm").val();
        if (password !== passwordConfirm) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        }

        /* required 체크 */
        var $required = $("#register-form [required]");
        for (var i = 0; i < $required.length; i++) {
            var $ele = $($required[i]);
            if (!$ele.val()) {
                var msg = "알 수 없는 오류";
                if ($ele.attr("name") === "name") {
                    msg = "이름을 입력해 주십시오";
                } else if ($ele.attr("name") === "id") {
                    msg = "아이디를 입력해 주십시오";
                } else if ($ele.attr("name") === "password") {
                    msg = "비밀번호를 입력해 주십시오";
                }
                alert(msg);
                $ele.focus();
                return false;
            }
        }

        /* 이메일 유효성 체크 */
        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        var emailComplete = $("#user-email").val();
        if (emailComplete !== "" && emailComplete.match(regExp) == null) {
            alert("이메일 입력을 확인해 주세요");
            return false;
        }

        $.ajax({
            url: "/api/user",
            data: JSON.stringify($("#register-form").serializeObject()),
            type: "POST",
            contentType: "application/json",
            dataType: "json"
        })
        .done(function(json) {
            /* message가 duplicate라면 아이디 중복 */
            if(json.message === "duplicate") {
                alert("아이디가 중복입니다. 다른 아이디를 선택해 주세요.");
                return;
            }

            alert("회원가입이 완료되었습니다. 로그인 해주세요.");
            location.href="/login";
        })
        .fail(function(xhr, status, errorThrown) {
            if (status === "canceled")
                return;

            alert("회원가입 중 에러가 발생하였습니다.")
            console.log(xhr)
            console.log(status)
            console.log(errorThrown)
        })
        .always(function () {
            setTimeout(function () { isAjaxing = false; }, 1000);
        });
    });

    /* /profile */
    $("#btn-profile").on("click", function () {
        /* Password 체크 */
        var password = $("#user-pw").val();
        var passwordConfirm = $("#user-pw-confirm").val();
        if (password !== passwordConfirm) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return false;
        }

        /* 이메일 유효성 체크 */
        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        var emailComplete = $("#user-email").val();
        if (emailComplete !== "" && emailComplete.match(regExp) == null) {
            alert("이메일 입력을 확인해 주세요");
            return false;
        }

        $.ajax({
            url: "/api/user",
            data: JSON.stringify($("#profile-form").serializeObject()),
            type: "PUT",
            contentType: "application/json",
            dataType: "json"
        })
            .done(function(json) {
                /* message가 duplicate라면 아이디 중복 */
                alert("회원 정보가 수정되었습니다.");
                location.href="/";
            })
            .fail(function(xhr, status, errorThrown) {
                if (status === "canceled")
                    return;

                alert("회원정보 수정 중 에러가 발생하였습니다.")
                console.log(xhr)
                console.log(status)
                console.log(errorThrown)
            })
            .always(function () {
                setTimeout(function () { isAjaxing = false; }, 1000);
            });
    });

    /* /user-search */
    $("#input-user-search").on("keyup", function () {
        renderUserList($(this).val(), Number($("#select-user-search").val()))
    });

    $("#btn-user-search").on("click", function () {
        renderUserList($("#input-user-search").val(), Number($("#select-user-search").val()))
    });

    /* /user-detail */
    $("#btn-user-detail-transaction").on("click", function () {
        var bankbookId = $("#select-user-detail-bankbooks").val();
        if (bankbookId === null)
            bankbookId = "";
        location.href = "/transaction?receiveBookId=" + bankbookId;
    });

    /* /admin/user-detail */
    $("#btn-admin-modify-user").on("click", function () {
        var userNo = $("input[name='userNo']").val();
        var userRank = $("#select-user-rank").val();
        $.ajax({
            url: "/api/user/change-rank",
            data: "rank=" + userRank +"&userNo=" + userNo,
            type: "PUT",
            dataType: "json"
        })
        .done(function(json) {
            alert("등급이 변경되었습니다.");
            location.reload();
        })
        .fail(function(xhr, status, errorThrown) {
            if (status === "canceled")
                return;

            alert("등급 변경 중 에러가 발생하였습니다.")
            console.log(xhr)
            console.log(status)
            console.log(errorThrown)
        })
        .always(function () {
            setTimeout(function () { isAjaxing = false; }, 1000);
        });
    });

    $("#btn-admin-delete-user").on("click", function () {
        if(!confirm("사용자가 삭제됩니다\n*연결된 통장이 있다면 같이 삭제됩니다"))
            return false;

        var userNo = $("input[name='userNo']").val();
        $.ajax({
            url: "/api/user",
            data: "userNo=" + userNo,
            type: "DELETE",
            dataType: "json"
        })
            .done(function(json) {
                alert("사용자가 삭제되었습니다.");
                location.href="/admin"
            })
            .fail(function(xhr, status, errorThrown) {
                if (status === "canceled")
                    return;

                alert("사용자 삭제 중 에러가 발생하였습니다.")
                console.log(xhr)
                console.log(status)
                console.log(errorThrown)
            })
            .always(function () {
                setTimeout(function () { isAjaxing = false; }, 1000);
            });
    });
});