<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:forEach var="page" items="${gallery.getChildPageList(parentPage)}">    
    <div class="first" id="<c:out value="${page.getId()}"></c:out>" onclick="selectPage.call(this, event)" data-parentid="<c:out value="${parentPage}"></c:out>">
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