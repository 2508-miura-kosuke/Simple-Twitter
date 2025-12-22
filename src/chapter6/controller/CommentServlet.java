package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentServlet() {
	    InitApplication application = InitApplication.getInstance();
	    application.init();
	}

    //つぶやきの返信
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  //セッションを使用できるようにします。
	  HttpSession session = request.getSession();
      List<String> errorMessages = new ArrayList<String>();

	  //top.jspからパラメータを取得
	  String text = request.getParameter("text");

	  //textのバリデーションを行う
      if (!isValid(text, errorMessages)) {
          session.setAttribute("errorMessages", errorMessages);
          //セッションにmessageという名前をつけて入力した文字をセット
          session.setAttribute("message", text);
          //エラーの場合はtop.jsp画面に表示
          request.getRequestDispatcher("./").forward(request, response);
          return;
      }

      //commentという変数にtextの値をセット
      Comment comment = new Comment();
      comment.setText(text);

	  //利用者IDの値がほしい
      //ログイン情報のパラメータを取得し、userという変数に値を代入
      User user = (User) session.getAttribute("loginUser");
      //現在ログインしているuserのidの値をmessageにセット
      comment.setUserId(user.getId());

      //返信を行うつぶやきのIDの値がほしい
      //パラメータを取得する
	  String sendMessage = request.getParameter("messageId");

	  //取得したパラメータをint型に変換
	  int messageId = Integer.parseInt(sendMessage);
	  //更新するメッセージのidをセット
	  comment.setMessageId(messageId);

	  //CommentServiceのinsertメソッドを呼び出す
      new CommentService().insert(comment);
	  //
      response.sendRedirect("./");
    }

      //isValidで返信のメッセージにエラーメッセージを適用する。
      private boolean isValid(String text, List<String> errorMessages) {

  	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        if (StringUtils.isBlank(text)) {
            errorMessages.add("メッセージを入力してください");
        } else if (140 < text.length()) {
            errorMessages.add("140文字以下で入力してください");
        }
        if (errorMessages.size() != 0) {
            return false;
        }
        return true;
    }
}
