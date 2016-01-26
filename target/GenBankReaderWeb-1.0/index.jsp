<%--
    Document   : index
    Created on : Jan 9, 2016, 2:28:01 PM
    Author     : Wout van Helvoirt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML>

<html>
    <head>
        <title>${initParam.author} | ${initParam.page_name}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <script src="<c:url value="/js/jquery-2.1.4.min.js" />" type="text/javascript"></script>
        <script src="<c:url value="/js/formValidation.js" />" type="text/javascript"></script>

        <link href='https://fonts.googleapis.com/css?family=Josefin+Sans:600,700|Merriweather:700,400italic,400,300italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css" />" />
        <link rel="shortcut icon" href="<c:url value="/img/favicon.ico" />">
    </head>
    <body>
        <div id="page">

            <%-- Header --%>
            <jsp:include page="/includes/header.jsp" />

            <%-- Main body block page --%>
            <div class="main clearfix">
                <div id="blockone" class="primary clearfix">
                    <div class="entry-content">
                        <p>Welcome to my online GenBank Reader. This Reader is able to analyse GenBank files
                            and give you information about it's genes and cds's. To use the GenBank Reader,
                            start by selecting your GenBank file and click the upload button to continue.
                            The settings that can be selected on the next page are: a summary of the GenBank file,
                            fetch gene/CDS names that match a RegEx pattern, fetch the features within a coordinate
                            region and find the sequence sites that match sequence RegEx pattern.
                        </p>

                        <%-- Error messages (if) --%>
                        <c:if test="${requestScope.error != null}">
                            <ul>
                                <li class="errorMessage">${requestScope.error}</li>
                            </ul>
                        </c:if>
                    </div>
                    <div>

                        <%-- File upload form --%>
                        <jsp:include page="/includes/file_upload_form.jsp" />
                    </div>
                </div>
            </div>
            
            <%-- Footer --%>
            <jsp:include page="/includes/footer.jsp" />
        </div>

    </body>
</html>
