<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <div class="rentalList">
            <table class="table table-bordered mx-auto" style="width: 650px;">
                <thead>
                    <tr class="table-primary">
                        <th>書籍名</th>
                        <th>貸出日</th>
                        <th>返却日</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lendingHistoryInfo" items="${rentalList}">
                        <tr>
                            <td>
                                <form method="post" class="title" action="<%=request.getContextPath()%>/details">
                                    <a href="javascript:void(0)" onclick="this.parentNode.submit();"> ${lendingHistoryInfo.title} </a> <input type="hidden" name="bookId" value="${lendingHistoryInfo.bookId}">
                                </form>
                            </td>
                            <td>${lendingHistoryInfo.rentDate}</td>
                            <td>${lendingHistoryInfo.returnDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>