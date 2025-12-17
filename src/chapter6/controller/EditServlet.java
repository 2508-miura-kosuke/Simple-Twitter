package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.Message;
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

      request.setAttribute("message", message);
      request.getRequestDispatcher("edit.jsp").forward(request, response);
    }

    //つぶやきの更新
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  //パラメータを取得する
	  String id = request.getParameter("messageId");

	  //int型に変換
	  int messageId = Integer.parseInt(id);

	  ////MessageServiceのselectメソッドを呼び出す
	  new MessageService().select(messageId);

    }
}