(function () {
    "use strict";

    function showStatic() {
        $(".dyn").hide();
        $(".stat").show();
    }

    function showDynamic() {
        $(".stat").hide();
        $(".dyn").show();
    }

    $(document).on("dialog-ready", function () {
        if ($(".static").attr("checked")) {
            showStatic();
        } else {
            showDynamic();
        }

        $(".static").click(function () {
            showStatic();
        });

        $(".dynamic").click(function () {
            showDynamic();
        });
    });
})();