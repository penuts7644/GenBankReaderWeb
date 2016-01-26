<%-- 
    Document   : index
    Created on : Jan 9, 2016, 2:28:01 PM
    Author     : Wout van Helvoirt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form method="post" action="<c:url value="/searchgbk.do"/>">

    <%-- Select option button, pattern button and submit button --%>
    <select id="button2" name="selectedOption" required>
        <option value="">-- Please select one of the following options.</option>
        <option value="Summary">Give me a summary of my GenBank file.</option>
        <option value="Fetch Gene(s)">Fetch the gene(s) that match my name pattern.</option>
        <option value="Fetch CDS(s)">Fetch the CDS(s) that match my name pattern.</option>
        <option value="Fetch Features">Fetch features within my coordinates region.</option>
        <option value="Find Site(s)">Find sequence site(s) that match my sequence pattern.</option>
    </select>
    <input id="button3" type="text" name="optionInputValue" placeholder="Please enter a pattern to search for." />
    <input id="button4" class="buttonInactive" type="submit" value="Search" />
</form>
