package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserMessage;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserMessageDao {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public UserMessageDao() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

    //特定のユーザーのつぶやき閲覧
    public List<UserMessage> select(Connection connection, Integer id, int num, String startDay, String endDay) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();

            //idがnullなら全件取得
	        sql.append("SELECT ");
	        sql.append("    messages.id as id, ");
	        sql.append("    messages.text as text, ");
	        sql.append("    messages.user_id as user_id, ");
	        sql.append("    users.account as account, ");
	        sql.append("    users.name as name, ");
	        sql.append("    messages.created_date as created_date, ");
	        sql.append("    messages.updated_date as updated_date ");
	        sql.append("FROM messages ");
	        sql.append("INNER JOIN users ");
	        sql.append("ON messages.user_id = users.id ");

	        //つぶやきの絞り込み(between andで範囲指定)
	        sql.append("WHERE messages.created_date BETWEEN ? AND ? ");

	        //idがnull以外だったら、その値に対応するユーザーIDの投稿を取得する
	        if(id != null) {
	        	sql.append("AND messages.user_id = ? ");
	        }

	        sql.append("ORDER BY created_date DESC limit " + num);

            ps = connection.prepareStatement(sql.toString());

            //デフォルト値の開始日時をバインド変数に入れる
            ps.setString(1, startDay);

            //デフォルト値の終了日時をバインド変数に入れる
            ps.setString(2, endDay);

            //idがnull以外なら
            //バインド変数(?)に値を入れる
            if(id != null) {
            	ps.setInt(3, id);
            }

            //データベースからデータを取得して、rsにデータを格納
            ResultSet rs = ps.executeQuery();

            //ResultSetからUserMessage
            List<UserMessage> messages = toUserMessages(rs);
            return messages;
        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }

    private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());
	  	//空のリストを作成
        List<UserMessage> messages = new ArrayList<UserMessage>();
        try {
        	//next()が次の行が存在するか確認している
            while (rs.next()) {
            	//new UserMessage()で格納用オブジェクト作成
                UserMessage message = new UserMessage();
                message.setId(rs.getInt("id"));
                message.setText(rs.getString("text"));
                message.setUserId(rs.getInt("user_id"));
                message.setAccount(rs.getString("account"));
                message.setName(rs.getString("name"));
                message.setCreatedDate(rs.getTimestamp("created_date"));
                message.setUpdatedDate(rs.getTimestamp("updated_date"));
                //データベースから取得し、セットした値をmessagesに追加
                messages.add(message);
            }
            return messages;
        } finally {
            close(rs);
        }
    }
}