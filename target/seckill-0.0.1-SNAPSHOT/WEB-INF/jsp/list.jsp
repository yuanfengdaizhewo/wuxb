<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
   <head>
      <title>秒杀列表</title>
	<jsp:include page="common/head.jsp"></jsp:include>
   </head>
   <body>
  			<div class="container">
  				<div class="panel panel-default">
  					<div class="panel-heading text-center">
  						<h2>秒杀列表</h2>
  					</div>
  					
  					<div class="panel-body">
  						<table>
	  						<thead>
	  							<tr>
	  								<th>名称</th>
	  								<th>库存</th>
	  								<th>开始时间</th>
	  								<th>结束时间</th>
	  								<th>创建时间</th>
	  								<th>详情页</th>
	  							</tr>
	  						</thead>
	  						<tbody>
	  							<c:forEach items="${list }" var="sk"></c:forEach>
	  							<tr>
	  								<td>${sk.name }</td>
	  								<td>${sk.number }</td>
	  								<td><fmt:formatDate value="${sk.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  								<td><fmt:formatDate value="${sk.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  								<td><fmt:formatDate value="${sk.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  								<td><a class="btn btn-info" href="/seckill/${sk.seckillId }/detail" target="_blank">link</a></td>
	  							</tr>
	  						</tbody>
  						</table>	
  					</div>
  				</div>
  			</div>
   </body>

</html>