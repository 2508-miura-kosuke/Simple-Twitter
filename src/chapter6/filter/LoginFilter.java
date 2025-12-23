package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//ユーザー編集画面とつぶやき編集画面にフィルターをかける
@WebFilter(urlPatterns = {"/edit", "/setting"})
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//ServletRequest/ResponseをHttpServletに型変換
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

		//セッションを取得
		HttpSession session = httpRequest.getSession(false);

		//セッション領域にログインユーザー情報があるかないか
		//ログインしていれば、リクエストのあった画面に遷移
		if(session != null && session.getAttribute("loginUser") != null) {
			chain.doFilter(httpRequest, httpResponse); // サーブレットを実行
		}else {
			session = httpRequest.getSession(true);
            //エラーメッセージをセット
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add("ログインしてください");
            session.setAttribute("errorMessages", errorMessages);
			//ログイン画面に遷移
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
		}
	}

	//アプリケーション起動時にコールされる。パラメータの初期化を行う。
	@Override
	public void init(FilterConfig config) {
	}

	//アプリケーション停止時にコールされる。保持しているインスタンスの破棄などを行う。
	@Override
	public void destroy() {
	}
}