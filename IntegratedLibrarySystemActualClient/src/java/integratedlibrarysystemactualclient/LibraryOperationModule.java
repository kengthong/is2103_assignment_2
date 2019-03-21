/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedlibrarysystemactualclient;

import ejb.session.stateful.LibraryOperationControllerRemote;
import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.Date;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Hidayah
 */
public class LibraryOperationModule {

    private LibraryOperationControllerRemote libraryOperationControllerRemote;    
    private StaffEntity currentStaffEntity;
    
    
    public LibraryOperationModule() {
    }

    public LibraryOperationModule(LibraryOperationControllerRemote libraryOperationControllerRemote, StaffEntity currentStaffEntity) {
        this();    
        this.libraryOperationControllerRemote = libraryOperationControllerRemote;
        this.currentStaffEntity = currentStaffEntity;
    }
    
    public void menuLibraryOperation() throws MemberNotFoundException, BookNotFoundException {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
    while(true) {
            System.out.println("*** ILS :: Library Operation ***\n");
            System.out.println("1: Register Member");
            System.out.println("2: Lend Book");
            System.out.println("3: Back\n");
            response = 0;  
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doRegisterMember() ; 
                } else if (response == 2) {
                    doLendBook() ; 
                } else if (response == 3) {
                    break ; 
                } else {
                    System.out.println("Invalid option, please try again!\n") ; 
                }
            }
               if(response == 3)
            {
                break;
            }
        }
    }
    
    
    private void doRegisterMember() throws MemberNotFoundException {
     Scanner scanner = new Scanner (System.in) ; 
    MemberEntity newMemberEntity = new MemberEntity() ; 
    
    System.out.println("*** ILS :: Library Operation :: Register Member ***\n") ; 
    System.out.print("Enter First Name> ");
    newMemberEntity.setFirstName(scanner.nextLine().trim());
    System.out.print("Enter Last Name> ");
    newMemberEntity.setLastName(scanner.nextLine().trim());
    System.out.print("Enter Gender> ");
    newMemberEntity.setGender(scanner.nextLine().trim());
    System.out.print("Enter Age> ");
    newMemberEntity.setAge(scanner.nextInt()); 
    scanner.nextLine() ; 
    System.out.print("Enter Identity Number> ");
    newMemberEntity.setIdentityNumber(scanner.nextLine().trim());
    System.out.print("Enter Phone> ");
    newMemberEntity.setPhone(scanner.nextLine().trim());
    System.out.print("Enter Address>") ; 
    newMemberEntity.setAddress(scanner.nextLine());
    

   try { 
        libraryOperationControllerRemote.retrieveMemberByIdentityNumber(newMemberEntity.getIdentityNumber()) ;
        System.out.println("Member has already been registered previously and cannot register again!\n") ; 
    } catch (MemberNotFoundException ex) { 
        libraryOperationControllerRemote.createNewMember(newMemberEntity); 
    System.out.println("Member has been registered successfully!\n") ; 
    }   
    }
    
    
    private void doLendBook() throws MemberNotFoundException, BookNotFoundException {
        
    Scanner scanner = new Scanner(System.in) ; 
    LendingEntity newLendingEntity = new LendingEntity() ; 
    MemberEntity newMemberEntity = new MemberEntity() ; 
    BookEntity newBookEntity = new BookEntity() ; 

    
    System.out.println("*** ILS :: Library Operation :: Lend Book ***\n") ; 
    System.out.print("Enter Book Id> ");
    Long bookId = scanner.nextLong() ; 
    scanner.nextLine() ; 
    System.out.print("Enter Member Identity Number>");
    String memberId = scanner.nextLine().trim() ; 
    Date date = new Date() ; 
    newLendingEntity.setLendDate(date) ;
      
  
     
     try {
          libraryOperationControllerRemote.retrieveBookByBookId(bookId) ;
          libraryOperationControllerRemote.retrieveMemberByIdentityNumber(memberId) ; 

           
               if ( libraryOperationControllerRemote.checkLentBook(bookId) ) {
               System.out.println("Book has been lent out and cannot be borrowed!") ; 
           } else {
              newMemberEntity = libraryOperationControllerRemote.retrieveMemberByIdentityNumber(memberId) ;
              newLendingEntity.setMember(newMemberEntity) ;
              newBookEntity = libraryOperationControllerRemote.retrieveBookByBookId(bookId) ; 
              newLendingEntity.setBook(newBookEntity) ; 
              libraryOperationControllerRemote.createNewLending(newLendingEntity); 
      System.out.println("Book: " + newLendingEntity.getBook().getTitle() + " lent to " + newLendingEntity.getMember().getFirstName() + " " + newLendingEntity.getMember().getLastName() + ".\n") ; 
     }
     }
        
     catch (BookNotFoundException | MemberNotFoundException ex) {
         System.out.println("Book ID or Member ID cannot be found!\n") ; 
     }
             
          }
            
      }
     
 
      
   
