(function(window, document, $) {
    "use strict";

    $(document).on("foundation-contentloaded", function(e) {

        var $container = $(e.target).hasClass(".richtext-container") ? $(e.target) : $(e.target).find(".richtext-container");

        $container.each(function () {
            $(this).closest(".coral-FixedColumn-column").addClass("coral-RichText-FixedColumn-column");
        });

        // Copy hidden text field to RTE
        $container.each(function() {
            var $richTextDiv = $(this).find(".coral-RichText-editable");
            if (!$richTextDiv.data("rteinstance")) {
                var html = $(this).find("input[type=hidden].coral-Textfield").val();
                $richTextDiv.empty().append(html);
            }
        });

        // Copy RTE text to hidden field
        $container.on("editing-finished", ".coral-RichText-editable", function(e, editedContent) {
            var el = $(this).closest(".richtext-container");
            el.find("input[type=hidden].coral-Textfield").val(editedContent);
        });

        // Register ...
        CUI.util.plugClass(CUI.RichText, "richEdit", function(rte) {
            CUI.rte.Utils.setI18nProvider(new CUI.rte.GraniteI18nProvider());
            CUI.rte.ConfigUtils.loadConfigAndStartEditing(rte, $(this));
            $(this).data("rteinstance", rte);
        });

        var $richTextDiv = $(e.target).find(".richtext-container>.coral-RichText");
        $richTextDiv.each(function() {
            var $this = $(this);
            if ($this.data("useFixedInlineToolbar") && !$this.data("rteinstance")) {
                $this.richEdit();
            }
        });
    });

    var rteFinish = function() {
        var rteInstance = $(this).data("rteinstance");
        if (rteInstance) {
            //    rteInstance.finish(false);
        }
    };

    $(document).on("dialog-beforeclose", "form.cq-dialog", function(e) {
        $(this).find(".richtext-container>.coral-RichText").each(rteFinish);
    });

    $(document).on("click", ".coral-Wizard-nextButton", function(e) {
        $(this).closest(".foundation-form").find(".richtext-container>.coral-RichText").each(rteFinish);
    });

    CUI.rte.Theme.BLANK_IMAGE = Granite.HTTP.externalize("/etc/clientlibs/granite/coralui2/optional/rte/resources/images/blank.png");

})(window, document, Granite.$);
