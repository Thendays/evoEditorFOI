<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:forEach items="${pages}" var="page" >
    <!-- TODO: print the node here -->
    <c:set var="node" value="${node}" scope="request"/>
    <jsp:include page="page.jsp"/>
</c:forEach>