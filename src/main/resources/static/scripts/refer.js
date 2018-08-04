function buildNav() {
    var menu = "    <nav class=\"bd-navbar navbar navbar-inverse navbar-fixed-top\">\n" +
        "        <a class=\"navbar-brand\" href=\"#\"><img src=\"/images/logo.gif\" id=\"logo\" height=\"40\" alt=\"Team Referral\"><span id=\"brand-label\">Team Referral</span></a>\n" +
        "        <div class=\"navbar-header\">\n" +
        "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#myNavbar\" aria-controls=\"navbarNavDropdown\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "                <span class=\"icon-bar\"></span>\n" +
        "            </button>\n" +
        "        </div>\n" +
        "        <div class=\"collapse navbar-collapse\" id=\"myNavbar\">\n" +
        "            <ul class=\"nav navbar-nav\">\n" +
        "                <li><a class=\"nav-link\" href=\"orgs.html\">Organization Setup</a></li>\n" +
        "                <li><a class=\"nav-link\" href=\"apps.html\">Application Setup</a></li>\n" +
        "                <li><a class=\"nav-link\" href=\"generateURL.html\">Generate Custom URL</a></li>\n" +
        "                <li><a class=\"nav-link\" href=\"referralList.html\">Referral List</a></li>\n" +
        "                <li><a class=\"nav-link\" href=\"haxors.html\">Haxors</a></li>\n" +
        "                <li><a href=\"stats.html\" class=\"nav-link dropdown-toggle\" id=\"navbarDropdownMenuLink\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">Site Statistics</a>" +
        "    <div class=\"dropdown-menu\"  aria-labelledby=\"navbarDropdownMenuLink\">\n" +
        "      <a class=\"dropdown-item\" href=\"stats.html#chart1\">Top Referring Users</a>\n" +
        "      <a class=\"dropdown-item\" href=\"stats.html#chart2\">Top Referring IP Addresses</a>\n" +
        "      <a class=\"dropdown-item\" href=\"stats.html#chart3\">Referrrals By Application</a>\n" +
        "      <a class=\"dropdown-item\" href=\"stats.html#chart4\">Attack Vectors By Country</a>\n" +
        "    </div></li>\n" +
        "            </ul>"
    "        </div>\n" +
    "    </nav>";
    $("#navContainer").html(menu);
    var pageName = document.location.pathname.match(/[^\/]+$/)[0];
    $("a[href$='" + pageName + "']").parent().attr("class", "active");
}
