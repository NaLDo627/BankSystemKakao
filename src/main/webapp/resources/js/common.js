var isAjaxing = false;

$.fn.serializeObject = function() {
    var result = {}
    var extend = function(i, element) {
        var node = result[element.name]
        if ("undefined" !== typeof node && node !== null) {
            if ($.isArray(node)) {
                node.push(element.value)
            } else {
                result[element.name] = [node, element.value]
            }
        } else {
            result[element.name] = element.value
        }
    }

    $.each(this.serializeArray(), extend)
    return result
}

Number.prototype.format = function(){
    if(this==0) return 0;

    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');

    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

    return n;
};

String.prototype.numberFormat = function(){
    var num = parseFloat(this);
    if( isNaN(num) ) return "0";

    return num.format();
};

$.ajaxPrefilter(function (options, originalOptions, jqXHR) {
    if (isAjaxing) {
        alert("처리중입니다. 잠시만 기다려주세요.");
        jqXHR.abort();
        return;
    }
    isAjaxing = true;

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    if (options.type === 'POST' || options.type === 'PUT' || options.type === 'DELETE') {
        options.headers = options.headers || {};
        options.headers[header] = token;
    }
});

function getUrlParams() {
    var params = {};
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
    return params;
}

String.format = function() {
    var theString = arguments[0];
    for (var i = 1; i < arguments.length; i++) {
        var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
        theString = theString.replace(regEx, arguments[i]);
    }

    return theString;
};

$(function () {
    $("#btn-logout").on("click", function () {
        $("#form-logout").submit();
    });
})

