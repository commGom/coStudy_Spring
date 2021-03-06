<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="../common/head.jsp"></jsp:include>
<jsp:include page="../common/header.jsp"></jsp:include>
<meta charset="UTF-8">
<link rel='stylesheet'
	href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>
<link type="text/css" rel="stylesheet" href="/resources/css/timer.css">
<link type="text/css" rel="stylesheet"
	href="/resources/css/groupSidebar_list.css">
<link rel="stylesheet" type="text/css"
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css">
<link type="text/css" rel="stylesheet"
	href="/resources/css/groupSetting.css">

<script type="text/javascript">

(function($){
  $('#ajaxConGetButton').click(function(){
        AjaxConGet();
    })
    
    /* $('#yesBtn').click(function(){
        AjaxConPost();
    }) */
   
});

 
function applyBtn(acceptYn, userNo){
    var url = "/groupPage/acceptMember";
    var param = {
    		  studygroup_no : $('#user_studygroup_no'+userNo).val(),
              user_no : $('#user_no'+userNo).val(),
              isAccept : acceptYn
    }
    $.ajax({
        type:"POST",
        url:url,
        dataType:"json",
        contentType:"application/json; charset=UTF-8",
        data:JSON.stringify(param),
        success : function(data){
        	if(acceptYn ==='Y'){
        		  alert('수락하셨습니다.');
        		  location.href= "/groupPage/groupSetting?studygroup_no=" + $('#user_studygroup_no').val();
        	}else{
        		  alert('거절되었습니다.');
        	}
          
         
        },
        error : function(request,status,error){
           // alert('code:'+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error); //에러 상태에 대한 세부사항 출력
        	  alert('수락하셨습니다.');  
        }
    })
    
}

</script>
</head>

<body>

	<div id="wrapper">
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">

				<li style="height: 75px;">
					<img src="${user.user_thumbImg}" style="width: 50px; height: 50px; float: left; border-radius: 50%; border:1px solid #f1f3f5;">
					<b>${sessionScope.user.user_lastName}${sessionScope.user.user_firstName}</b>
				</li>
				<li>
					<nav id="side-timer">
						<section id="timer">
							<span id="time_output">00:00:00</span><br>
							<div class="content_center">
								<input type="button" id="timer_bnt_save" value="저장"> <input
									type="button" id="timer_bnt_start" value="시작">
							</div>
						</section>
					</nav>
				</li>
				<li>
					<nav id="class_timer">시간표</nav>
				</li>

			</ul>
		</div>
		<div id="page-content-wrapper">
			<div class="container-fluid">
				<div class="row" id="topbar">
					<div class="col-lg-12">
						<a href="#" class="btn" id="menu-toggle"><span
							class="glyphicon glyphicon-menu-hamburger"></span></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="backback">
		<div class="container" class="row" id="group-Main-body">
			<input type="hidden" id="studygroup_no" name="studygroup_no"
				value="${studygroup_no}">
			<div id="background-body">

				<div class="col-lg-9" id="setting">
					<div id="settingBoard">
						<div id="group-accept" class="table-wrap">
							<div id="acceptTable" class="panel panel-white post panel-shadow">
								<h2>그룹 신청 목록</h2>
								<%-- <input type="hidden" name="studygroup_no"
                        value="${studyGroup.studygroup_no }"> --%>
								<table class="table table--vertical" id="accept-list">
									<tbody class="panel panel-white post panel-shadow"
										id="accept-list-board">
										<c:forEach var="applyGroupMember" items="${applyUserList}">
											<tr>
												<td></td>
												<td>${applyGroupMember.studygroup_no }
												${applyGroupMember.user_no }</td>
												<td><input type="hidden"
													id="user_studygroup_no${applyGroupMember.user_no }"
													value="${applyGroupMember.studygroup_no }"> <input
													type="hidden" id="user_no${applyGroupMember.user_no }"
													value="${applyGroupMember.user_no}">
													<button class="btn btn--primary btn--inside uppercase"
														id="yesBtn"
														onclick="applyBtn('Y',${applyGroupMember.user_no })">수락</button>
												</td>
												<td>
													<button class="btn btn--primary btn--inside uppercase"
														onclick="applyBtn('N')">거절</button>
												</td>
											</tr>
										</c:forEach>
									</tbody>


								</table>
							</div>

							<br>


						</div>


					</div>

				</div>

			</div>
		</div>
	</div>
	<jsp:include page="../common/scri.jsp"></jsp:include>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	<jsp:include page="../common/scri.jsp"></jsp:include>
	<jsp:include page="../common/footer.jsp"></jsp:include>
</body>
<!-- partial -->
<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script
	src='https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.2/jquery.min.js'></script>
<script src="/resources/js/side.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/prefixfree/1.0.7/prefixfree.min.js"></script>

<script type="text/javascript" src="/resources/js/jquery.js"></script>
<script type="module" src="/resources/js/timer.js"></script>

</html>