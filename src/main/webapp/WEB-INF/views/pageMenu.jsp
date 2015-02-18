<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach var="page" items="${gallery.getChildPageList(parentPage)}">  
		<c:choose>
			<c:when test="${page.getParentID() != gallery.getID() }">
	    		<div class="pages" id="<c:out value="${page.getId()}"></c:out>" onclick="selectPage.call(this, event)" data-parentid="<c:out value="${parentPage}"></c:out>" style="zoom: <c:out value="${scale}"></c:out>">
	    		<c:choose>
					<c:when test="${scale <= 0.1}">
						<c:set var="scale" value="${scale - 0.01}" scope="request"/>
					</c:when>	
					<c:otherwise>
						<c:set var="scale" value="${scale - 0.1}" scope="request"/>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<div class="pages" id="<c:out value="${page.getId()}"></c:out>" onclick="selectPage.call(this, event)" data-parentid="<c:out value="${parentPage}"></c:out>" >
			</c:otherwise>
		</c:choose>
		<div class="delete_slide">
			<button class="delete_1">
				<img src="resources/images/delete.png" width="20px"/>
			</button>
		</div>
		<div class="move_slide">
			<button class="up_1"><img src="resources/images/up.png" width="10px" height="10"/></button><br/>
			<button class="down_1"><img src="resources/images/down.png" width="10px" height="10"/></button>
		</div>
		<div class="slide_image">
			<img src="resources/images/Placeholder.png"/>
			<div class="slide_number">
				<input type=" number" name="quantity"
				min="1" max="1000"
				value="<c:out value="${page.getOrderNumber() + 1}"></c:out>" >
			</div>
		</div>
	</div>
    
    <c:set var="parentPage" value="${page.getId()}" scope="request"/>
    <jsp:include page="pageMenu.jsp"/>
</c:forEach>