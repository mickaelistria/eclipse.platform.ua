<%--
 (c) Copyright IBM Corp. 2000, 2002.
 All Rights Reserved.
--%>
<%@ include file="header.jsp"%>

<% 
	SearchData data = new SearchData(application, request);
	WebappPreferences prefs = data.getPrefs();
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><%=ServletResources.getString("Search", request)%></title>
     
<style type="text/css">
/* need this one for Mozilla */
HTML { 
	width:100%;
	height:100%;
	margin:0px;
	padding:0px;
	border:0px;
 }

BODY {
	background:<%=prefs.getToolbarBackground()%>;
	border:0px;
	text:white;
	height:100%;
}

TABLE {
	font: icon;
	background:<%=prefs.getToolbarBackground()%>;
	margin:0;
	padding:0;
	height:100%;
}

FORM {
	background:<%=prefs.getToolbarBackground()%>;
	height:100%;
	margin:0;
}

INPUT {
	font: icon;
	margin:0px;
	padding:0px;
}


#searchTable {
	padding-left:5;
}

#searchWord {	
	padding-left:4px;
	padding-right:4px;
	border:1px solid ThreeDShadow;
}

#go {
	background:WindowText;
	color:Window;
	font-weight:bold;
	border:1px solid ThreeDShadow;
}


#advanced {
	text-decoration:underline; 
	text-align:right;
	color:#0066FF; 
	cursor:hand;
	margin-left:4px;
	border:0px;
}

<%
	if (data.isIE()) {
%>
#go {
	padding-left:1px;
}
<%
	}
%>
</style>

<script language="JavaScript">
var isIE = navigator.userAgent.indexOf('MSIE') != -1;
var isMozilla = navigator.userAgent.toLowerCase().indexOf('mozilla') != -1 && parseInt(navigator.appVersion.substring(0,1)) >= 5;

<%
	String[] selectedBooks = data.getSelectedTocs();
%>
// create list of books initilize selectedBooks variable used by advances search
var selectedBooks = new Array(<%=selectedBooks.length%>);
<%
for (int i=0; i<selectedBooks.length; i++) 
{
%>
	selectedBooks[<%=i%>] = "<%=UrlUtil.JavaScriptEncode(selectedBooks[i])%>";
<%
}
%>

var advancedDialog;
var w = 400;
var h = 300;

function saveSelectedBooks(books)
{
	selectedBooks = new Array(books.length);
	for (var i=0; i<selectedBooks.length; i++){
		selectedBooks[i] = new String(books[i]);
	}
}

function openAdvanced()
{
	advancedDialog = window.open("advanced.jsp?searchWord="+escape(document.getElementById("searchWord").value), "advancedDialog", "resizeable=no,height="+h+",width="+w );
	advancedDialog.focus(); 
}

function closeAdvanced()
{
	try {
		if (advancedDialog)
			advancedDialog.close();
	}
	catch(e) {}
}

/**
 * This function can be called from this page or from
 * the advanced search page. When called from the advanced
 * search page, a query is passed.
 */
function doSearch(query)
{
	if (!query || query == "")
	{
		var form = document.forms["searchForm"];
		var searchWord = form.searchWord.value;
		var maxHits = form.maxHits.value;
		if (!searchWord || searchWord == "")
			return;
		query ="searchWord="+escape(searchWord)+"&maxHits="+maxHits;
	}
	query=query+"&encoding=js";
		
	/******** HARD CODED VIEW NAME *********/
	// do some tests to ensure the results are available
	if (parent.HelpFrame && 
		parent.HelpFrame.NavFrame && 
		parent.HelpFrame.NavFrame.showView &&
		parent.HelpFrame.NavFrame.ViewsFrame && 
		parent.HelpFrame.NavFrame.ViewsFrame.search && 
		parent.HelpFrame.NavFrame.ViewsFrame.search.ViewFrame) 
	{
		parent.HelpFrame.NavFrame.showView("search");
		var searchView = parent.HelpFrame.NavFrame.ViewsFrame.search.ViewFrame;
		searchView.location.replace("searchView.jsp?"+query);
	}
}

function fixHeights()
{
	if (!isIE) return;
	
	var h = document.getElementById("searchWord").offsetHeight;
	document.getElementById("go").style.height = h;
}

function onloadHandler(e)
{
	var form = document.forms["searchForm"];
	form.searchWord.value = '<%=UrlUtil.JavaScriptEncode(data.getSearchWord())%>';
	fixHeights();
}

</script>

</head>

<body onload="onloadHandler()"  onunload="closeAdvanced()">

	<form  name="searchForm"   onsubmit="doSearch()">
		<table id="searchTable" align="left" valign="middle" cellspacing="0" cellpadding="0" border="0">
			<tr nowrap  valign="middle">
				<td>
					&nbsp;<%=ServletResources.getString("Search", request)%>:
				</td>
				<td>
					<input type="text" id="searchWord" name="searchWord" value='' size="20" maxlength="256" alt='<%=ServletResources.getString("SearchExpression", request)%>'>
				</td>
				<td >
					&nbsp;<input type="button" onclick="this.blur();doSearch()" value='<%=ServletResources.getString("GO", request)%>' id="go" alt='<%=ServletResources.getString("GO", request)%>'>
					<input type="hidden" name="maxHits" value="500" >
				</td>
				<td nowrap>
					<a id="advanced" href="javascript:openAdvanced();" alt='<%=ServletResources.getString("Advanced", request)%>' onmouseover="window.status='<%=ServletResources.getString("Advanced", request)%>'; return true;" onmouseout="window.status='';"><%=ServletResources.getString("Advanced", request)%></a>&nbsp;
				</td>
			</tr>

		</table>
	</form>		

</body>
</html>

