/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedlibrarysystemactualclient;

import ejb.session.stateful.LibraryOperationControllerRemote;
import entity.StaffEntity;
import java.sql.SQLException;
import java.util.Scanner;
import javax.naming.NamingException;
import util.exception.BookNotFoundException;
import util.exception.EntityManagerException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author Hidayah
 */

public class MainApp {

    private LibraryOperationControllerRemote libraryOperationControllerRemote;    
    private LibraryOperationModule libraryOperationModule;    
    private StaffEntity currentStaffEntity;    
    
    
    public MainApp() {        
    }
    
    public MainApp(LibraryOperationControllerRemote libraryOperationControllerRemote) {
        this.libraryOperationControllerRemote = libraryOperationControllerRemote;
    }
    
    public void runApp() throws MemberNotFoundException, BookNotFoundException {
        Scanner scanner = new Scanner (System.in) ; 
        Integer response = 0 ; 
      
        while (true) {
    System.out.println("*** Welcome to Integrated Library System (ILS) ***\n") ; 
    System.out.println("1: Login") ; 
    System.out.println("2: Exit\n") ; 
    response = 0 ; 
    
   while (response < 1 || response > 2) {
        System.out.print("> ") ; 
        response = scanner.nextInt() ; 
        if (response == 1) {
            
            try {
                
                doLogin() ; 
                libraryOperationModule = new LibraryOperationModule(libraryOperationControllerRemote, currentStaffEntity) ;
                menuMain() ; 
            } catch (InvalidLoginException ex) {
            }
            
        }
        else if (response == 2){
            break ; 
        }
        else {
            System.out.println("Invalid option, please try again!\n") ; 
        }
    }
    if (response == 2) {
        break ; 
    }
}
}
    
    
    private void doLogin() throws InvalidLoginException {
     Scanner scanner = new Scanner (System.in) ; 
    String username = "" ; 
    String password = "" ; 
    
    System.out.println("*** ILS :: Login ***\n") ; 
    System.out.print("Enter username> ");
    username = scanner.nextLine().trim();
    System.out.print("Enter password> ");
    password = scanner.nextLine().trim();
    
     if(username.length() > 0 && password.length() > 0)
        {
            try
            {
                currentStaffEntity = libraryOperationControllerRemote.staffLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n") ; 
                throw new InvalidLoginException() ; 
            }
        } else { 
         System.out.println("Invalid login credential!") ; 
     }
}    
    

private void menuMain() throws MemberNotFoundException, BookNotFoundException  {
     Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
    while (true) {
        System.out.println("*** Integrated Library System (ILS) ***\n");
        System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
        System.out.println("1: Library Operation");
        System.out.println("2: Logout\n");
        response = 0;
        
        while (response < 1 || response > 2) {
            System.out.print("> ") ; 
            response = scanner.nextInt() ; 
            if (response == 1) {
                libraryOperationModule.menuLibraryOperation() ; 
            } else if (response == 2) { 
                break ; 
            } else {
                System.out.println("Invalid option, please try again!\n") ; 
            }
        }
        
        if (response == 2) {
            break ; 
        }
    }
}
}
