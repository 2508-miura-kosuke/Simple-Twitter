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

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public EditServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

    //つぶやきの編集画面表示
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  //パラメータを取得する
	  String id = request.getParameter("messageId");

	  //int型に変換
	  int messageId = Integer.parseInt(id);

      //MessageServiceのselectメソッドを呼び出す。戻り値あり
      Message message = new MessageService().select(messageId);
      //messageという名前でmessageに入ったデータをjspで使えるように渡す
      request.setAttribute("message", message);
      request.getRequestDispatcher("edit.jsp").forward(request, response);
    }

    //つぶやきの更新、編集されたつぶやきの情報をログインユーザ情報と合わせてDBで更新
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  //セッションを使用できるようにします。
	  HttpSession session = request.getSession();
      List<String> errorMessages = new ArrayList<String>();
      //edit.jspからtextパラメータを取得して、バリデーションを行う
      String text = request.getParameter("text");
      if (!isValid(text, errorMessages)) {
          session.setAttribute("errorMessages", errorMessages);
          //エラーの場合はedit.jsp画面に表示
          response.sendRedirect("edit.jsp");
          return;
      }
      //messageという変数にtextという値をセット
      Message message = new Message();
      message.setText(text);
      //ログイン情報のパラメータを取得し、userという変数に値を代入
      User user = (User) session.getAttribute("loginUser");
      //現在ログインしているuserのidの値をmessageにセット
      message.setUserId(user.getId());

      //パラメータを取得する
	  String updateMessage = request.getParameter("messageId");

	  //取得したパラメータをint型に変換
	  int messageId = Integer.parseInt(updateMessage);
	  //更新するメッセージのidをセット
	  message.setId(messageId);

      //MessageServiceのupdateメソッドを呼び出す
      new MessageService().update(message);
      //最終的にはtop.jspの画面につぶやきを更新した状態を表示する
      response.sendRedirect("./");
  }
    //isValidで編集するつぶやきにもエラーメッセージを適用する。
    private boolean isValid(String text, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
      " : " + new Object(){}.getClass().getEnclosingMethod().getName());

      if (StringUtils.isBlank(text)) {
          errorMessages.add("メッセージを入力してください");
      } else if (140 < text.length()) {
          errorMessages.add("140文字以下で入力してください");
      }
      //URLのつぶやきのIDが数字以外ならエラーメッセージ
      if () {
    	  errorMessages.add("不正なパラメータが入力されました");
      }
      //URLのつぶやきのIDが存在しないIDならエラーメッセージ
      if (request.getParameter("messageId")) {
    	  errorMessages.add("不正なパラメータが入力されました");
      }
      //URLのつぶやきのIDが空ならエラーメッセージ
      if (request.getParameter("messageId") = null) {
    	  errorMessages.add("不正なパラメータが入力されました");
      }

      if (errorMessages.size() != 0) {
          return false;
      }
      return true;
  }

}