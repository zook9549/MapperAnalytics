function buildNav() {
    var menu = "    <nav class=\"bd-navbar navbar navbar-inverse navbar-fixed-top\">\n" +
        "        <a class=\"navbar-brand\" href=\"#\"><img src=\"/images/logo.gif\" id=\"logo\" height=\"40\" alt=\"Team Referral\"><span id=\"brand-label\">Team Referral</span></a>\n" +
        "        <div class=\"navbar-header\">\n" +
        "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#myNavbar\">\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "            </button>\n" +
        "        </div>\n" +
        "        <div class=\"collapse navbar-collapse\" id=\"myNavbar\">\n" +
        "            <ul class=\"nav navbar-nav\">\n" +
        "                <li><a href=\"orgs.html\">Organization Setup</a></li>\n" +
        "                <li><a href=\"apps.html\">Application Setup</a></li>\n" +
        "                <li><a href=\"generateURL.html\">Generate Custom URL</a></li>\n" +
        "                <li><a href=\"referralList.html\">Referral List</a></li>\n" +
        "                <li><a href=\"haxors.html\">Haxors</a></li>\n" +
        "                <li><a href=\"stats.html\">Site Statistics</a></li>\n" +
        "            </ul>"
    "        </div>\n" +
    "    </nav>";
    $("#navContainer").html(menu);
    var pageName = document.location.pathname.match(/[^\/]+$/)[0];
    $("a[href$='" + pageName + "']").parent().attr("class", "active");
}