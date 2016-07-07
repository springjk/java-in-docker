package com.bingo.helloworld;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorld extends HttpServlet{

    // 创建静态全局变量
    static Connection conn;

    /* 获取数据库连接的函数*/
    public static Connection getConnection() throws Exception {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://mysql/docker", "root", "docker");

        } catch (Exception e) {
            throw e;
        }

        return con;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        StringBuilder out = new StringBuilder("");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter pw = resp.getWriter();

        try {
            conn = getConnection();
            pw.println("<html>"
                    + "<head>"
                    + "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
                    + "<title>Hello World</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Weclcome JAVA in docker</h1>"
                    + "<p>数据库连接成功，连接代码：</p>"
                    + "<p><pre>DriverManager.getConnection(\"jdbc:mysql://mysql/docker\", \"root\", \"docker\")</pre></p>"
                    + "</body></html>");
        } catch (Exception e) {
            System.out.println(e);
            pw.println("数据库连接错误，是否更改了 docker-compose 默认的数据库配置？");
        }

        pw.close();
    }

    public static void main(String[] args) {

    }

}
