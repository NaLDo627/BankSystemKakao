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
            <h1 class="page-information">사용자 조회 & 검색</h1>
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
        <div class="col-8">
            <div class="row">
                <div class="col-5">
                    <h2 class="page-item">사용자 목록</h2>
                </div>
                <!-- /.col-5 -->
                <div class="col-7">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <select class="custom-select" id="select-user-search">
                                <option value="0" selected>이름</option>
                                <option value="1">id</option>
                            </select>
                        </div>
                        <input type="text" class="form-control" aria-label="Text input with dropdown button" id="input-user-search">
                        <div class="input-group-append">
                            <a href="#" id="btn-user-search" class="btn btn-outline-secondary" type="button">검색</a>
                        </div>
                    </div>
                </div>
                <!-- /.col-7 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-12">
                    <div class="list-group" id="user-list">

                    </div>
                </div>
                <!-- /.col-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /.col-8 -->
    </div>
    <!-- /.row -->
</div>

<%@include file="../../includes/footer.jsp"%>
<script type="text/javascript" src="/resources/js/user.js"></script>
<script type="text/javascript">
    var bankUsers = ${bankUsers};
    function renderUserList(keyword, type) {
        $("#user-list").empty();
        bankUsers.forEach(function (bankUser) {
            if (type === 0 && !bankUser.name.includes(keyword))
                return false;  /* continue */

            if (type === 1 && !bankUser.id.includes(keyword))
                return false;  /* continue */

            $("#user-list").append(
                String.format("<a href=\"/user-detail/{0}\" class=\"list-group-item list-group-item-action\">{1} - {2} - {3}</a>",
                    bankUser.userNo, bankUser.id, bankUser.name,
                    bankUser.rank === "NORMAL" ? "일반" : bankUser.rank));
        })
    }
    $(function () {
        renderUserList("", 0);
    })
</script>

</body>
</html>
