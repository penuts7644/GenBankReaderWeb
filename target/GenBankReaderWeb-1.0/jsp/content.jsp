<%-- 
    Document   : index
    Created on : Jan 9, 2016, 2:28:01 PM
    Author     : Wout van Helvoirt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <c:if test = "${sessionScope.gbkObj == null}">
            <c:redirect url="/index.jsp"/>
        </c:if>
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

           <%-- Page only when there is a GenBank object --%>
            <c:choose>
                <c:when test = "${sessionScope.gbkObj != null}">

                    <%-- Header --%>
                    <jsp:include page="/includes/header.jsp" />

                    <div class="main clearfix">
                        <div id="blockone" class="primary clearfix">
                            <div class="entry-content">
                                <p>Great, you have uploaded your GenBank file! Now, select one of the five
                                    available options below and enter a search pattern (name, sequence or coordinates)
                                    if necessary. Once done, hit the search button and behold the results.
                                </p>
                                <ul>
                                    <li class="textMessage">RegEx patterns can be used when searching by name or sequence.</li>
                                    <li class="textMessage">When using coordinates; please separate the two values by a comma.</li>
                                </ul>

                                <%-- Form for selecting options --%>
                                <jsp:include page="/includes/option_select_form.jsp" />
                            </div>

                            <%-- Show output if available --%>
                            <c:if test="${sessionScope.message != null}">
                                <c:forEach var="i" items="${sessionScope.message}">
                                    <p class="outputMessageBlock"><c:out value="${i}"/></p>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    
                    <%-- Redirect if there is no GenBank object --%>
                    <c:redirect url="/index.jsp"></c:redirect>
                </c:otherwise>
            </c:choose>

            <%-- Footer --%>
            <jsp:include page="/includes/footer.jsp" />
        </div>
    </body>
</html>
