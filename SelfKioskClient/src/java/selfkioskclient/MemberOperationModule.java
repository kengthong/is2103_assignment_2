/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateful.SelfKioskOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import entity.BookEntity;
import entity.MemberEntity;
import java.util.Scanner;
import util.exception.BookNotFoundException;

/**
 *
 * @author kengthong
 */
public class MemberOperationModule {

    private SelfKioskOperationControllerRemote selfKioskOperationController;
    private BookEntityControllerRemote bookEntityController;
    private MemberEntity currentActiveMember;
    
    
    
    public MemberOperationModule(
            SelfKioskOperationControllerRemote selfKioskOperationController,
            BookEntityControllerRemote bookEntityController) {
        this.selfKioskOperationController = selfKioskOperationController;
        this.bookEntityController = bookEntityController;
        init();
    }
    
    private void init()
    {
        this.currentActiveMember = this.selfKioskOperationController.getCurrentActiveMember();
    }
    
    public void displayMenu()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Self-Service Kiosk :: Main ***\n") ; 
        System.out.println("You are logged in as " + this.currentActiveMember + "\n") ; 
        
        System.out.println("1: Borrow Book"); 
        System.out.println("2: View Lent Books");
        System.out.println("3: Return Book");
        System.out.println("4: Extend Book");
        System.out.println("5: Pay Fines");
        System.out.println("6: Search Book");
        System.out.println("7: Reserve Book");
        System.out.println("8: Logout\n");
        
        while(response <1 || response >8)
        {
            
            System.out.print("> ");
            response = sc.nextInt();
            
            switch(response)
            {
                case 1:
                    //Borrow book
                    doBorrowBook();
                case 2:
                    //View Lent Books
                case 3:
                    //Return book
                case 4:
                    //Extend book
                case 5:
                    //Pay fines
                case 6:
                    //Search book
                case 7:
                    //Reserve book
                case 8:
                    //Log out
                default:
                    System.out.println("Invalid Option");
            }
        }
    }
    
    private void doBorrowBook()
    {
        Scanner sc = new Scanner(System.in);
        Integer bookId = 0;
        
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");
        


            System.out.print("Enter Book ID: ");
            bookId = sc.nextInt();
            
            if(bookId != 0)
            {
                try
                {
                    BookEntity bookToBorrow = bookEntityController.getBook(bookId);
                    String dueDate = "2019-03-25";
                    System.out.println("Successfully lent book. Due Date: " + dueDate + ".");
                }
                catch(BookNotFoundException ex)
                {
                    
                }
            }
    }

}
