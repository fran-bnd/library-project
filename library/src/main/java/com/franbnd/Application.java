package com.franbnd;

import com.franbnd.Controller.LibraryController;
import com.franbnd.DAO.*;
import com.franbnd.Model.*;
import com.franbnd.Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
/**
 * The main method will start a new Javalin API on the console at localhost:8080.
 * LibraryController class for API documentation as well as instructions for how to access the API endpoints.
 */
public class Application {
    public static void main(String[] args) {
        databaseSetup();
        LibraryController libraryController = new LibraryController();
        libraryController.startAPI();
    }
    /**
     * This method will destroy and set up new book and author tables.
     * This is not a normal way to set up your tables, in real projects you should set up your database schema in a SQL editor such as DBeaver or DataGrip. 
     */
    public static void databaseSetup(){
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps1 = conn.prepareStatement("drop table if exists book");
            ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("drop table if exists author");
            ps2.executeUpdate();
            PreparedStatement ps3 = conn.prepareStatement("create table author(" +
                    "id int primary key auto_increment, " +
                    "name varchar(255)); ");
            ps3.executeUpdate();
            PreparedStatement ps4 = conn.prepareStatement("create table book(" +
                    "isbn int primary key, " +
                    "author_id int, "+
                    "title varchar(255), " +
                    "copies_available varchar(255), " +
                    "foreign key (author_id) references author(id));");
            ps4.executeUpdate();
            PreparedStatement ps5 = conn.prepareStatement(
                    "insert into author (name) values " +
                            "('jorge luis borges')," +
                            "('italo calvino')," +
                            "('thomas pynchon')," +
                            "('marshall mcluhan')," +
                            "('immanuel kant')");
            ps5.executeUpdate();
            PreparedStatement ps6 = conn.prepareStatement(
                    "insert into book (isbn, author_id, title, copies_available) values " +
                            "(100, 1, 'ficciones', 2)," +
                            "(101, 1, 'book of sand', 0)," +
                            "(102, 2, 'mr palomar', 1)," +
                            "(103, 2, 'invisible cities', 3)," +
                            "(104, 3, 'crying of lot 49', 0)," +
                            "(105, 3, 'mason and dixon', 0)," +
                            "(106, 4, 'understanding media', 1)," +
                            "(107, 5, 'critique of pure reason', 7);");
            ps6.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("Running App");

        // User Menu
        Scanner sc = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        boolean shouldLoop = true;
        while (shouldLoop){
            System.out.println("--> Choose: add | purchase | exit");
            List<Book> allBooks = bookDAO.getAllBooks();
            System.out.println("--> Current books: "+ allBooks);
            String input = sc.nextLine();
            if (input.equals("add")){
                System.out.print("-> Name of book to add: ");
                String name = sc.nextLine();
                System.out.print("-> Author of book to add: ");
                String author = sc.nextLine();
                Book newBook = new Book();
                bookDAO.addBook(newBook);

            }else if(input.equals("purchase")){
                System.out.print("-> Isbn of book to purchase: ");
                int isbnBook = sc.nextInt();
                bookDAO.getBookByIsbn(isbnBook);
                
            }else if(input.equals("exit")){
                shouldLoop = false;
                // will break the while loop next time the while loop condition is checked
            }

        }
        sc.close();
    }
}


