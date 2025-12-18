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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {

	/**
	   * ロガーインスタンスの生成
	   */
	    Logger log = Logger.getLogger("twitter");

	    /**
	    * デフォルトコンストラクタ
	    * アプリケーションの初期化を実施する。
	    */
	    public SignUpServlet() {
	        InitApplication application = InitApplication.getInstance();
	        application.init();

	    }

	    //doGetメソッドを使用。jspでget呼び出しを行い、signup.jspを表示
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {

		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());
		  	//request.getRequestDispatcherで引数に遷移する画面を指定
		  	//forwardで遷移が行われる。
	        request.getRequestDispatcher("signup.jsp").forward(request, response);
	    }

	    //①doPostメソッドを使用。jspでpost呼び出しされると実行される。
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        List<String> errorMessages = new ArrayList<String>();

	        //③リクエストパラメータをUserオブジェクトにセットする。
	        User user = getUser(request);
	        //④リクエストパラメータに対するバリデーションを行う。
	        if (!isValid(user, errorMessages)) {
	            request.setAttribute("errorMessages", errorMessages);
	            request.getRequestDispatcher("signup.jsp").forward(request, response);
	            return;
	        }
	        //⑥UserServiceのinsertメソッドを呼び出して、DBへユーザーの登録を行う。
	        new UserService().insert(user);
	        //TopServletのdoGetが呼び出される
	        response.sendRedirect("./");
	    }

	    //②UserクラスのgetUserメソッドを使用。ユーザー登録画面からの入力値(リクエストパラメータ)を取得
	    private User getUser(HttpServletRequest request) throws IOException, ServletException {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		  	//signup.jspでname属性で設定したパラメータを取得
		  	//取得したパラメータをUser.javaで設定したsetterメソッドを使用して値を代入する。
	        User user = new User();
	        user.setName(request.getParameter("name"));
	        user.setAccount(request.getParameter("account"));
	        user.setPassword(request.getParameter("password"));
	        user.setEmail(request.getParameter("email"));
	        user.setDescription(request.getParameter("description"));
	        return user;
	    }

	    //⑤isValidメソッドを使用。入力値に対するバリデーションを行います。
	    //バリデーションとは妥当性確認。
	    //入力値が不正な場合は再度signupを表示するようになっている。
	    private boolean isValid(User user, List<String> errorMessages) {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        String name = user.getName();
	        String account = user.getAccount();
	        String password = user.getPassword();
	        String email = user.getEmail();

	        //String型のaccountを引数とするselectメソッドを呼び出す
	        User existingAccount = new UserService().select(account);

	        if (!StringUtils.isEmpty(name) && (20 < name.length())) {
	            errorMessages.add("名前は20文字以下で入力してください");
	        }

	        if (StringUtils.isEmpty(account)) {
	            errorMessages.add("アカウント名を入力してください");
	        } else if (20 < account.length()) {
	            errorMessages.add("アカウント名は20文字以下で入力してください");
	        }

	        //すでにアカウントが存在するならエラーメッセージが出る
	        if (existingAccount != null) {
	        	errorMessages.add("すでに存在するアカウントです");
	        }

	        if (StringUtils.isEmpty(password)) {
	            errorMessages.add("パスワードを入力してください");
	        }

	        if (!StringUtils.isEmpty(email) && (50 < email.length())) {
	            errorMessages.add("メールアドレスは50文字以下で入力してください");
	        }

	        if (errorMessages.size() != 0) {
	            return false;
	        }
	        return true;
	    }
	}