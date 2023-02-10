package com.franbnd;

import com.franbnd.Controller.LibraryController;
import com.franbnd.Model.*;
import com.franbnd.Service.BookService;
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

        System.out.println("->  Welcome to the Library App  <-");

        // User Menu
        Scanner sc = new Scanner(System.in);
        BookService bookService = new BookService();
        boolean shouldLoop = true;
        String input = "";

        while (shouldLoop){
            
            System.out.println("--> Choose: add | buy | list | exit");
            input = sc.nextLine();

            if (input.equals("add")){

                System.out.print("-> Name of book to add: ");
                String name = sc.nextLine();
                System.out.print("-> Isbn of book to add: ");
                int isbn = sc.nextInt();
                System.out.print("-> Author of book to add: ");
                int author = sc.nextInt();
                System.out.print("-> Avialable copies of book to add: ");
                int copies = sc.nextInt();

                Book newBook = new Book(isbn, author, name, copies);
                bookService.addBook(newBook);

            }else if(input.equals("buy")){
                System.out.print("-> Isbn of book to purchase: ");
                int isbnBook = sc.nextInt();
                Book bookPurchased = bookService.getBookByIsbnServ(isbnBook);
                System.out.println("--> Book purchased: "+ bookPurchased);
            
            }else if(input.equals("list")){
                List<Book> allBooks = bookService.getAllBooks();
                System.out.println("--> List of books: "+ allBooks);    

            }else if(input.equals("exit")){
                shouldLoop = false;
                // will break the while loop next time the while loop condition is checked
            }

        }
        sc.close();
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
                            "(105, 3, 'mason and dixon', 0);");
            ps6.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}


