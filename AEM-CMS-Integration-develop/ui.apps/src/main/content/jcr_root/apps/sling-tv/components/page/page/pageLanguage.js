use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function (AuthoringUtils) {

    var language = currentPage.getLanguage(false).getLanguage();

    return {
        language: language
    };
});