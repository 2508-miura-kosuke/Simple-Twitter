<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>簡易Twitter</title>
        <link href="./css/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
    	<!-- divで囲っておくと、スタイルシートを適用するときに適用がしやすい。-->
        <div class="main-contents">
            <div class="header">
                <c:if test="${ empty loginUser }">
        			<a href="login">ログイン</a>
        			<!-- a要素で囲い、今いるリソースから相対位置で「/signup」というURLにアクセス-->
        			<a href="signup">登録する</a>
   				</c:if>
   				<c:if test="${ not empty loginUser }">
        			<a href="./">ホーム</a>
        			<a href="setting">設定</a>
        			<a href="logout">ログアウト</a>
    			</c:if>
			</div>
    			<c:if test="${ not empty loginUser }">
   					 <div class="profile">
   					 	<!-- c:outタグが画面出力、出力値をvalueに設定-->
   					 	<!-- セッション領域にあるloginUserのnameに対応するvalueが出力-->
				        <div class="name"><h2><c:out value="${loginUser.name}" /></h2></div>
				        <div class="account">@<c:out value="${loginUser.account}" /></div>
				        <div class="description"><c:out value="${loginUser.description}" /></div>
    				</div>
				</c:if>
				<!-- メッセージつぶやき機能-->
				<c:if test="${ not empty errorMessages }">
    				<div class="errorMessages">
        				<ul>
            				<c:forEach items="${errorMessages}" var="errorMessage">
                				<li><c:out value="${errorMessage}" />
            				</c:forEach>
        				</ul>
    				</div>
   					<c:remove var="errorMessages" scope="session" />
				</c:if>

				<div class="form-area">
					<!-- TopServletにあるisShowMessageFormがtrueであればログイン状態でテキストエリアを表示-->
    				<c:if test="${ isShowMessageForm }">
        				<form action="message" method="post">
            				いま、どうしてる？<br />
            				<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
            				<br />
            				<input type="submit" value="つぶやく">（140文字まで）
        				</form>
   					</c:if>
				</div>
				<!--メッセージを表示-->
				<div class="messages">
    				<c:forEach items="${messages}" var="message">
        				<div class="message">
            				<div class="account-name">
				                <span class="account">
				                	<!-- http://localhost:8080/Chapter6/?user_id=ユーザーIDで各ユーザー毎のつぶやきが表示 -->
									<a href="./?user_id=<c:out value="${message.userId}"/> ">
										<c:out value="${message.account}" />
									 </a>
				                </span>
				                <span class="name">
				                	<c:out value="${message.name}" />
				                </span>
           					</div>
       						<div class="text">
       							<!--pre要素を使用して、ソースコード上に入力されている半角スペースやタブ、改行などがそのまま反映された状態で表示-->
       							<pre><c:out value="${message.text}" /></pre>
       						</div>
       						<!--メッセージの投稿した日付を表示-->
           					<div class="date">
           						<c:if test="${ message.createdDate == message.updatedDate}">
           						<fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
           						</c:if>
           						<!--メッセージを編集し更新したら更新した日付を表示-->
           						<c:if test="${ message.createdDate != message.updatedDate}">
           						<fmt:formatDate value="${message.updatedDate}" pattern="yyyy/MM/dd HH:mm:ss" />
           						</c:if>
   							</div>
   							<!-- トップ画面に編集ボタンを追加(ログインしたユーザーのみ) -->
   							<c:if test="${loginUser.id == message.userId}">
   								<form action="edit" method="get">
   									<input type="hidden" name= "messageId" value="${message.id}">
   									<input type="submit" value="編集">
   								</form>
   								<!-- トップ画面につぶやき削除ボタンを追加(ログインしたユーザーのみ) -->
        						<form action="deleteMessage" method="post">
        							<input type="hidden" name= "messageId" value="${message.id}">
   									<input type="submit" value="削除">
   								</form>
   							</c:if>
   							<!--つぶやきの返信内容をServletに送信-->
   							<c:if test="${not empty loginUser}">
   								<form action="comment" method="post">
   									<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
            						<br />
            						<input type="hidden" name= "messageId" value="${message.id}">
            						<input type="submit" value="返信">（140文字まで）
								</form>
							</c:if>
							<!--つぶやきの返信を表示-->
							<!--その投稿に関する返信だけを表示-->

      					</div>
    				</c:forEach>
				</div>
            <div class="copyright"> Copyright(c)KOSUKE MIURA</div>
        </div>
    </body>
</html>