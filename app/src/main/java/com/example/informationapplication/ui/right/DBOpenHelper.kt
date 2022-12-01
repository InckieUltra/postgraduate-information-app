package com.example.informationapplication.ui.right

import java.sql.*


object DBOpenHelper {
    private const val driver = "com.mysql.jdbc.Driver" //MySQL 驱动
    private const val url = "jdbc:mysql://124.220.207.224:3306/postgraduateinfosys?useSSL=false" //MYSQL数据库连接Url
    private const val user = "root" //用户名
    private const val password = "123456" //密码//获取MYSQL驱动
    //获取连接
    /**
     * 连接数据库
     */
    val conn: Connection?
        get() {
            var conn: Connection? = null
            try {
                Class.forName(driver) //获取MYSQL驱动
                conn = DriverManager.getConnection(url, user, password) as Connection //获取连接
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            return conn
        }

    /**
     * 关闭数据库
     */
    fun closeAll(conn: Connection?, ps: PreparedStatement?) {
        if (conn != null) {
            try {
                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        if (ps != null) {
            try {
                ps.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 关闭数据库
     */
    fun closeAll(conn: Connection?, ps: PreparedStatement?, rs: ResultSet?) {
        if (conn != null) {
            try {
                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        if (ps != null) {
            try {
                ps.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        if (rs != null) {
            try {
                rs.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}