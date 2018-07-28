function buildNav() {
    var menu = "            <ul class=\"nav navbar-nav\">\n" +
    "                <li><a href=\"orgs.html\">Organization Setup</a></li>\n" +
    "                <li><a href=\"apps.html\">Application Setup</a></li>\n" +
    "                <li><a href=\"generateURL.html\">Generate Custom URL</a></li>\n" +
    "                <li><a href=\"referralList.html\">Referral List</a></li>\n" +
    "                <li><a href=\"haxors.html\">Haxors</a></li>\n" +
    "                <li><a href=\"stats.html\">Site Statistics</a></li>\n" +
    "            </ul>";
    $("#myNavbar").html(menu);
    var pageName = document.location.pathname.match(/[^\/]+$/)[0];
    $("a[href$='" + pageName + "']").parent().attr("class","active");
}