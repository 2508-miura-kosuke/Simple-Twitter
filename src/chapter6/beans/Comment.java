package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

//コメント登録=Commentsテーブルに登録する=CommentJavaで扱う
public class Comment implements Serializable {

	//フィールド変数を定義している。private データ型 フィールド名
	//フィールド変数はクラス内の変数のこと
	private int id;
    private String text;
    private int userId;
    private int messageId;
    private Date createdDate;
    private Date updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
