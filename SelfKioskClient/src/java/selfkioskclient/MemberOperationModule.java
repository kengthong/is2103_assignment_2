/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateful.SelfKioskOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import java.util.List;
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
    private LendingEntityControllerRemote lendingEntityController;
    
    
    
    public MemberOperationModule(
            SelfKioskOperationControllerRemote selfKioskOperationController,
            BookEntityControllerRemote bookEntityController,
            LendingEntityControllerRemote lendingEntityController
    ) {
        this.selfKioskOperationController = selfKioskOperationController;
        this.bookEntityController = bookEntityController;
        this.lendingEntityController = lendingEntityController;
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
                    viewLentBook();
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
        Integer bookIdEntered = 0;
        
        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");
        


            System.out.print("Enter Book ID: ");
            bookIdEntered = sc.nextInt();
            Long bookId = (long) bookIdEntered;
            
            if(bookId != 0)
            {
                try
                {
                    BookEntity bookToBorrow = bookEntityController.retrieveBookByBookId(bookId);
                    String dueDate = "2019-03-25";
                    System.out.println("Successfully lent book. Due Date: " + dueDate + ".");
                }
                catch(BookNotFoundException ex)
                {
                }
            }
    }

    private void viewLentBook()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");

        String memberIdentityNumber = this.currentActiveMember.getIdentityNumber();
        List<LendingEntity> lentBooks = this.lendingEntityController.retrieveBooksLoanedByMember(memberIdentityNumber);
        
        System.out.println("Currently Lent Books:");
        System.out.println("Id\t| Title\t| Due date");
        for (LendingEntity lendingEntity : lentBooks)
        {
            Long lendId = lendingEntity.getLendId();
            String title = lendingEntity.getTitle();
            // date
            String dueDate = "2019-03-14";
            System.out.println(lendId + "\t| " + title + "\t| " + dueDate);
        }
    }
}
