<%-- 
    Document   : header
    Created on : Jan 11, 2016, 9:13:16 PM
    Author     : Wout van Helvoirt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Header with image --%>
<div class="site-header">
    <div class="site-header-inside">
        <div class="headband">
            <div class="hero-header">

                <%-- Title webpage, contains link to home page --%>
                <a href="<c:url value="/index.jsp" />">
                    <h1 class="hero-title">${initParam.app_name}</h1>
                    <p class="hero-subtitle">Upload GenBank - Click Here</p>
                </a>
            </div>
        </div>
    </div>
    <div class="header-image"></div>
</div>
