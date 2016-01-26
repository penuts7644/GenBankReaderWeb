<%-- 
    Document   : index
    Created on : Jan 9, 2016, 2:28:01 PM
    Author     : Wout van Helvoirt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="post" enctype="multipart/form-data" action="<c:url value="/readgbk.do"/>">
    
    <%-- Upload file button and submit button --%>
    <input id="button1" type="file" name="inputFile" accept=".gbk,.gb" required />
    <input id="button4" class="buttonInactive" type="submit" value="Upload" />
</form>
