$(document).ready(function () {
    /* /new-bankbook */
    $("#btn-new-bankbook").on("click", function () {
        if ($("#bankbook-alias").val().trim().length === 0) {
            alert("통장 별명을 입력하세요.")
            return false;
        }

        $.ajax({
            url: "/api/bankbook",
            data: JSON.stringify({
                ownerUserNo: Number($("#bankbook-owner").val()),
                alias: $("#bankbook-alias").val(),
                type: $("#bankbook-type").val()
            }),
            type: "POST",
            contentType: "application/json",
            dataType: "json"
        })
        .done(function(json) {
            alert("통장이 생성되었습니다.");
            location.href="/";
        })
        .fail(function(xhr, status, errorThrown) {
            if (status === "canceled")
                return;

            alert("통장 생성 중 에러가 발생하였습니다.")
            console.log(xhr)
            console.log(status)
            console.log(errorThrown)
        })
        .always(function () {
            setTimeout(function () { isAjaxing = false; }, 1000);
        });
    });

    /* /bankbook-detail */
    $("#btn-delete-bankbook").on("click", function () {
        if (Number($("input[name='balance']").val()) !== 0) {
            alert("통장 잔고가 0원이 아니라면 통장을 삭제할 수 없습니다.")
            return false;
        }

        if (!confirm("통장을 삭제합니다."))
            return false;

        $.ajax({
            url: "/api/bankbook",
            data: "bookNo=" + $("input[name='bookNo']").val(),
            type: "DELETE",
            dataType: "json"
        })
        .done(function(json) {
            alert("통장이 삭제되었습니다.");
            location.href="/";
        })
        .fail(function(xhr, status, errorThrown) {
            if (status === "canceled")
                return;

            alert("통장 삭제 중 에러가 발생하였습니다.")
            console.log(xhr)
            console.log(status)
            console.log(errorThrown)
        })
        .always(function () {
            setTimeout(function () { isAjaxing = false; }, 1000);
        });
    });

    /* /admin/bankbook-detail */
    $("#btn-delete-bankbook-admin").on("click", function () {
        if (!confirm("통장을 삭제합니다\n*통장의 잔액 관계없이 삭제됩니다"))
            return false;

        $.ajax({
            url: "/api/bankbook",
            data: "bookNo=" + $("input[name='bookNo']").val(),
            type: "DELETE",
            dataType: "json"
        })
            .done(function(json) {
                alert("통장이 삭제되었습니다.");
                location.href="/admin";
            })
            .fail(function(xhr, status, errorThrown) {
                if (status === "canceled")
                    return;

                alert("통장 삭제 중 에러가 발생하였습니다.")
                console.log(xhr)
                console.log(status)
                console.log(errorThrown)
            })
            .always(function () {
                setTimeout(function () { isAjaxing = false; }, 1000);
            });
    });

    $("#btn-modify-bankbook-admin").on("click", function () {
        var bookNo = $("input[name='bookNo']").val();
        var type = $("#select-bankbook-type").val();

        if (Number($("input[name='balance']").val()) < 0 && type === "NORMAL") {
            alert("일반 통장으로 전환시, 대출금이 있으면 전환할 수 없습니다.")
            return false;
        }

        $.ajax({
            url: "/api/bankbook/change-type",
            data: "bookNo=" + bookNo + "&type=" + type,
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
});
