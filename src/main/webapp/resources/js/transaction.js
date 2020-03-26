$(document).ready(function () {
    /* /transaction */
    if(isEmptyBankbooks) {
        alert("거래할 통장을 먼저 생성해 주세요.");
        location.href = "/new-bankbook";
    }

    $("#select-transaction-bankbook").val(transactionBookNo);
    $("#select-transaction-bankbook option[value=\"" + transactionBookNo+ "\"]").attr("selected", "true");
    renderTransactionWrapper(transactionBookNo)

    if (receiveBookId !== null && receiveBookId !== "") {
        $("#nav-transfer-tab").trigger("click");
    }

    $("#withdraw-or-deposit").on("change", function (e) {
        $("input[name='amount']").val(0);
        $("input[name='memo']").val("");
    });

    $("input[name='amount']").on("focusout", function (e) {
        var inputVal = $(this).val();
        inputVal = Number(inputVal.replace(/[^0-9]/gi,''));
        $(this).val(inputVal.format());
    });

    $("#btn-withdraw, #btn-transfer").on("click", function () {
        /* required 체크 */
        var target = $(this).attr("form");
        var $required = $(target + " [required]");
        for (var i = 0; i < $required.length; i++) {
            var $ele = $($required[i]);
            if (!$ele.val()) {
                var msg = "알 수 없는 오류";
                if ($ele.attr("name") === "amount") {
                    msg = "금액을 입력해 주십시오";
                } else if ($ele.attr("name") === "memo") {
                    msg = "메모를 입력해 주십시오";
                }
                alert(msg);
                $ele.focus();
                return false;
            }
        }

        /* amount 체크 */
        var amount = Number($(target + " input[name='amount']").val().replace(/[^0-9]/gi,''))
        if (amount === 0) {
            alert("금액을 입력해 주십시오");
            return false;
        }
        $(target + " input[name='amount']").val(amount);

        /* receiverBookId 체크 */
        if (target === "#frm-transfer" &&
            $(target + " input[name='receiverBookId']").val() === $(target + " input[name='transactionBookId']").val()) {
            alert("송금 통장과 수취 통장이 같을 수 없습니다.");
            return false;
        }

        var addtionalMsg = "";
        if (target === "#frm-transfer" && isTransferChargeOccurs)
            addtionalMsg = "\n*이체 수수료가 차감됩니다.";

        if (!confirm("거래를 실행합니다" + addtionalMsg))
            return false;

        var requestDTO = $(target).serializeObject();
        var requestURL = "/api/transaction/" + requestDTO.type.toLowerCase();

        $.ajax({
            url: requestURL,
            data: JSON.stringify(requestDTO),
            type: "POST",
            contentType: "application/json",
            dataType: "json"
        })
        .done(function(json) {
            var transactionResultString = null;
            if (json.data != null)
                transactionResultString = String.format("요청 금액 : {0}\n발생 수수료 : {1}\n실거래 금액 : {2}\n남은 잔액 : {3}",
                    json.data.requestAmount.format(), json.data.transactionFee.format(), json.data.actualAmount.format(), json.data.requestBankbookRemainBalance.format());

            if (json.message !== "OK") {
                alert("*거래 실패*\n사유 : " + json.message + "\n" + transactionResultString);
                return;
            }

            alert("거래가 성공하였습니다.\n" + transactionResultString)
            location.href="/";
        })
        .fail(function(xhr, status, errorThrown) {
            if (status === "canceled")
                return;

            var json = xhr.responseJSON;
            var transactionResultString = null;
            if (json.data != null)
                transactionResultString = String.format("요청 금액 : {0}\n발생 수수료 : {1}\n실거래 금액 : {2}\n남은 잔액 : {3}",
                    json.data.requestAmount.format(), json.data.transactionFee.format(), json.data.actualAmount.format(), json.data.requestBankbookRemainBalance.format());

            alert("*거래 실패*\n사유 : " + json.message + "\n" + transactionResultString);

            console.log(xhr)
            console.log(status)
            console.log(errorThrown)
        })
        .always(function () {
            $(target + " input[name='amount']").val(amount.format());
            setTimeout(function () { isAjaxing = false; }, 1000);
        });
    });
});

