package chapter6.beans;

import java.io.Serializable;
import java.util.Date;
//ユーザー登録=usersテーブルに登録する=Userクラスで扱う
public class User implements Serializable {

	//フィールド変数を定義している。private データ型 フィールド名
	//フィールド変数はクラス内の変数のこと
	private int id;
    private String account;
    private String name;
    private String email;
    private String password;
    private String description;
    private Date createdDate;
    private Date updatedDate;

    //フィールドは外部からアクセスできないので、メソッドを介してアクセスしている。
    //getterはフィールドの値を取り出すメソッド
    //getter,setterは外部で扱えないといけないため、publicで記述している。
    public int getId() {
        return id;
    }

    //setterはフィールドに値を代入するためのメソッド
    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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