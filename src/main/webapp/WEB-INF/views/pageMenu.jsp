<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach var="page" items="${gallery.getChildPageList(parentPage)}">
		<br/>
		<c:choose>
			<c:when test="${page.getId() == selected_item.getId()}"> 
<!-- 			selected page definirano u index.jsp linija: 62 -->
				<c:set var="isSelected" scope="request" value=" selected"/>
			</c:when>
			<c:otherwise>
				<c:set var="isSelected" scope="request" value=""/>
			</c:otherwise>
		</c:choose>	
		<c:choose>			
			<c:when test="${page.getParentID() != gallery.getID() }">							
	    		<div class="pages<c:out value="${isSelected}"></c:out>" id="<c:out value="${page.getId()}"></c:out>" onclick="selectPage.call(this, event)" data-parentid="<c:out value="${page.getParentID()}"></c:out>" >
			</c:when>
			<c:otherwise>
				<div class="pages<c:out value="${isSelected}"></c:out>" id="<c:out value="${page.getId()}"></c:out>" onclick="selectPage.call(this, event)" data-parentid="<c:out value="${gallery.getID()}"></c:out>" style="zoom: 1;">
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
			<img src="resources/images/<c:out value="${page.getUsedResource().getName()}"></c:out>.png"/>
			<div class="slide_number">
				<input type="number" name="quantity"
				min="1" max="1000"
				value="<c:out value="${page.getOrderNumber() + 1}"></c:out>" disabled="disabled">
			</div>
		</div>
	</div>
    
    <c:set var="parentPage" value="${page.getId()}" scope="request"/>
    <jsp:include page="pageMenu.jsp"/>
</c:forEach>