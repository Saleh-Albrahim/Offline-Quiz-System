/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import Models.QuizSets;
import Models.Reports;
import Models.Users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SAlbr
 */
public class DatabaseManager {

    public Connection connection;
    Statement s;

    public DatabaseManager() {
        openConnection();
        createTables();

    }

    private void openConnection() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/test", "root", "7540");
            s = connection.createStatement();

        } catch (Exception ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createTables() {
        try {
            String sql = "create table Users ("
                    + "UserName varchar(255),"
                    + "Password varchar(255),"
                    + "Job varchar(255))";
            s.execute(sql);
            sql = "insert into Users (UserName,Password,Job) Values ("
                    + "'1','a4ayc/80/OGda4BO/1o/V0etpOqiLx1JwB5S3beHW0s=','Teacher')";
            s.execute(sql);
            sql = "create table reports ("
                    + "UserName varchar(255),"
                    + "Quizname varchar(255),"
                    + "Marks varchar(255))";
            s.execute(sql);

        } catch (SQLException ex) {

        }
    }

    public String SearchLogin(String text, String encoded) throws Exception {

        String sql = "select * from Users where UserName = '" + text + "' And Password = '" + encoded + "' ";
        ResultSet rs = s.executeQuery(sql);
        if (rs.next()) {
            String job = rs.getString("Job");
            return job;
        } else {
            throw new Exception();
        }

    }

    public void createQuiz(String text) {
        try {
            String sql = "create table " + text + " ("
                    + "Q varchar(255),"
                    + "A varchar(255),"
                    + "B varchar(255),"
                    + "C varchar(255),"
                    + "D varchar(255),"
                    + "correct varchar(255))";
            s.execute(sql);
        } catch (SQLException ex) {

        }
    }

    public void InsertQuiz(String t, String q, String a, String b, String c, String d, String correct) throws SQLException {
        String sql = "insert into `" + t + "` (Q,A,B,C,D,correct) values ('" + q + "' , '" + a + "' , '" + b + "' , '" + c + "' , '" + d + "' , '" + correct + "' )";
        s.execute(sql);
    }

    public ArrayList<Users> getAllUsers() {
        ArrayList<Users> Sets = new ArrayList<>();

        try {
            String sql = "SELECT * FROM  Users ";

            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                Users newCont = new Users();
                newCont.setUserName(rs.getString("UserName"));
                newCont.setJop(rs.getString("Job"));

                Sets.add(newCont);
            }
            return Sets;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Sets;
    }

    public void addUser(Users newUser) throws SQLException {
        String sql = "INSERT INTO Users (username,password,job)VALUES"
                + "('" + newUser.getUserName() + "','" + newUser.getPassword() + "','" + newUser.getJop() + "')";
        s.execute(sql);
    }

    public void removeUser(String keyword) throws SQLException {

        String sql = "DELETE FROM Users  where username =  '" + keyword + "'";
        s.execute(sql);

    }

    public ArrayList<String> getQuizNames() {
        try {
            String sql = "show tables";
            ResultSet rs = s.executeQuery(sql);
            ArrayList<String> sets = new ArrayList<>();
            while (rs.next()) {
                if (rs.getString("TABLE_NAME").equals("USERS") || rs.getString("TABLE_NAME").equals("REPORTS")) {
                    continue;
                } else {
                    sets.add(rs.getString("TABLE_NAME"));
                }
            }

            return sets;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<QuizSets> getQuizByName(String name) {
        try {
            String sql = "select * from `" + name + "`";
            ResultSet rs = s.executeQuery(sql);
            ArrayList<QuizSets> sets = new ArrayList<>();
            while (rs.next()) {
                QuizSets quiz = new QuizSets();
                quiz.setQ(rs.getString("Q"));
                quiz.setA(rs.getString("A"));
                quiz.setB(rs.getString("B"));
                quiz.setC(rs.getString("C"));
                quiz.setD(rs.getString("D"));
                quiz.setCorrect(rs.getString("CORRECT"));
                sets.add(quiz);
            }
            return sets;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void reportMarks(String userName, String q, int marks) {
        try {
            String sql = "insert into reports(username,quizname,marks) values ('" + userName + "' , '" + q + "' , '" + marks + "')";
            s.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Reports> getReport(String userName) {
        try {
            String sql = "select * from reports where username = '" + userName + "'";
            ArrayList<Reports> sets = new ArrayList<>();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Reports r = new Reports();
                r.setName(rs.getString("username"));
                r.setQuiz(rs.getString("quizname"));
                r.setMarks(rs.getString("marks"));
                sets.add(r);
            }
            return sets;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Reports> getAllReports() {
        try {
            String sql = "select * from reports";
            ArrayList<Reports> sets = new ArrayList<>();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Reports r = new Reports();
                r.setName(rs.getString("username"));
                r.setQuiz(rs.getString("quizname"));
                r.setMarks(rs.getString("marks"));
                sets.add(r);
            }
            return sets;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
