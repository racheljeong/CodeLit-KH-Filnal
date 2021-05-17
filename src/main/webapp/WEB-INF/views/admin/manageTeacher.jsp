<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%-- 로그인 검증용 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:requestEncoding value="utf-8"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="CodeLit" name="title"/>
</jsp:include>

<!-- 컨텐츠 시작 -->

<!-- 개인 CSS, JS 위치 -->

        <style>
			#alarm {
			  width: 25px; 
			  height: 25px;
			}
			.jb-larger { 
			  font-weight: 900;
			}
			.Page{
			  display: inline-block;
			}
			.pagination-span{
			  position: absolute;
			  left: 44%;
			}
			.pagination{
			  width: 100%;
			  height: 100%;
			  margin: 0 auto;
			}
			.board-write{
			  float: right;
			}
        
        
        
          #memberBoardTable button {
            height: 2rem;
            padding: auto;
          }
          #memberBoardTable td {
            padding:  auto;
          }
          

        </style>

		<script>
			window.onload = function() {
				
				let $del = $('.deleteMemberFrm');
				
				$del.each(function(i, elem) {
					
					elem.addEventListener('submit', function(e) {
						
						if(confirm("정말로 삭제하시겠습니까?")) {
							return true;
						} else {
							e.preventDefault();
						}
						
					});
					
				});
				
				
				
				
			}
		</script>


        <div class="container">

          <div class="mt-5">
            <h2 class=" jb-larger mt-3">강사 관리</h2>
            
	        <form method="GET" id="teacherSearchFrm" action="${pageContext.request.contextPath}/admin/manageTeacher.do">
            	<div class="row col-8 mt-5 ms-1">
		            
		            <table>
		            	<tr>
		            		<td class="col-2">
		            			<select class="form-select" name="category">
					                <option selected>주 강의분야</option>
					                <c:forEach items="${categoryList}" var="cat">
					                	<option value="${cat.no}">${cat.name}</option>
					                </c:forEach>
					        	</select>
		            		</td>
		            		<td class="col-3">
		            			<input type="search" id="form1" class="form-control offset-1"  placeholder="아이디"/>              
		            		</td>
		            		<td>
			               		<button type="submit" class="btn btn-primary offset-4" id="searchBtn">
			                  		<i class="fas fa-search"></i>
			               		</button>
		            		</td>
		            		<td>
		            			<span>둘 중 하나만 설정 후 검색도 가능</span>
		            		</td>
		            	</tr>
        
		            </table>
		            
            	</div>
            </form>
          </div>
          
          <div class="mt-4">
            <table class="table text-center" id="memberBoardTable">
              <thead class="table-primary">
                <tr>
                  <th scope="col">번호</th>
                  <th scope="col">아이디</th>
                  <th scope="col">수강 강좌</th>
                  <th scope="col">탈퇴 처리</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${teacherList}" var="teacher" varStatus="vs">
                <tr>
                    <td>${vs.count}</td>
                    <td><a href="#">${teacher.refMemberId}</a></td>
                    <td>대기</td>
                    <td>
                    	<form:form method="POST" class="deleteTeacherFrm"
                    				action="${pageContext.request.contextPath}/admin/deleteTeacher.do">
                    	<input type="hidden" name="memberId" value="${teacher.refMemberId}" />
                        <button type="submit" class="btn btn-danger text-light">탈퇴</button>
                    	</form:form>
                    </td>
                </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
  
              <div class="mt-5">
                <span class="pagination-span">
                  <ul class="pagination board-paging pull-right">
                    <li class="page-item"><a class="page-link" href="#">&#60</a></li>
                    <li class="page-item"><a class="page-link" href="#">1</a></li>
                    <li class="page-item"><a class="page-link" href="#">2</a></li>
                    <li class="page-item"><a class="page-link" href="#">3</a></li>
                    <li class="page-item"><a class="page-link" href="#">4</a></li>
                    <li class="page-item"><a class="page-link" href="#">5</a></li>
                    <li class="page-item"><a class="page-link" href="#">&#62</a></li>
                  </ul>
                </span>
              </div>
            </div>

        </div> <!-- 컨테이너-->

			<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
			<hr/>



<!-- 컨텐츠 끝 -->

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>