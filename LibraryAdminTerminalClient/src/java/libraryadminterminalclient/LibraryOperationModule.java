/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateful.LibraryOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
public class LibraryOperationModule {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote ; 
    private FineControllerRemote fineControllerRemote;
    private ReservationControllerRemote reservationControllerRemote;
    private StaffEntity currentStaffEntity;

    public LibraryOperationModule() {
    }

    public LibraryOperationModule(LibraryOperationControllerRemote libraryOperationControllerRemote , StaffEntityControllerRemote staffEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, FineControllerRemote fineControllerRemote, ReservationControllerRemote reservationControllerRemote, StaffEntity currentStaffEntity) {
        this();
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.fineControllerRemote = fineControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;
        this.libraryOperationControllerRemote = libraryOperationControllerRemote ; 
        this.currentStaffEntity = currentStaffEntity;
    }

    public void menuLibraryOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Library Operation ***\n");
            System.out.println("1: Lend Book"); 
            System.out.println("2: View Lent Books"); 
            System.out.println("3: Return Book");
            System.out.println("4: Extend Book");
            System.out.println("5: Pay Fines"); 
            System.out.println("6: Manage Reservations"); 
            System.out.println("7: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doLendBook();
                } else if (response == 2) {
                    viewLentBooks();
                } else if (response == 3) {
                    doReturnBook();
                } else if (response == 4) {
                    doExtendBook();
                } else if (response == 5) {
                    doPayFines();
                } else if (response == 6) {
                    doManageReservations();
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 7) {
                break;
            }
        }

    }

    private void doLendBook() {
        
        libraryOperationControllerRemote.doLendBook() ; 
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: Lend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Book ID> ");
        Long bookId = scanner.nextLong();
        LendingEntity newLendingEntity = new LendingEntity();
        Date date = new Date();

        try { //check if book and member exists 
            BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByBookId(bookId);
            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
          
            lendingEntityControllerRemote.checkIsBookLent(bookId);
            fineControllerRemote.checkIfMemberHasFines(identityNumber);
            lendingEntityControllerRemote.checkIfMemberExceedsMaxLoans(identityNumber);
            List<ReservationEntity> reservations = reservationControllerRemote.retrieveAllReservationsByBookId(bookId);
            if(!reservations.isEmpty()){
                lendingEntityControllerRemote.checkIfMemberOnReserveList(identityNumber);
            }
          
            newLendingEntity.setMember(memberEntity);
            newLendingEntity.setBook(bookEntity);
            Date duedate = lendingEntityControllerRemote.generateDueDate(date);
            newLendingEntity.setLendDate(date);
            newLendingEntity.setDueDate(duedate);
            newLendingEntity.setHasReturned(false);
            newLendingEntity = lendingEntityControllerRemote.createNewLending(newLendingEntity);
            System.out.println("Successfully lent book to member. Due Date: " + newLendingEntity.getDueDate());
        } catch (
                BookNotFoundException | 
                MemberNotFoundException | 
                BookIsOnLoanException | 
                MemberHasFinesException |
                MaxLoansExceeded |
                MemberNotAtTopOfReserveList ex
            ) {
            System.out.println(ex.getMessage());
        }

    }

    private void viewLentBooks() {
      

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: View Lent Books ***\n");
        System.out.println("Enter Member Identity Number>");
        String identityNumber = scanner.nextLine().trim();
        System.out.println(libraryOperationControllerRemote.viewLentBooks(identityNumber)) ;
//        
//          try { 
//        MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
//        List<LendingEntity> lendingEntities = memberEntity.getLendings() ; 
//        System.out.println(memberEntity.getLendings()) ; 
//        System.out.printf("%-5s%-50s%-20s\n", "Id","Title", "Due Date") ; 
//        for (LendingEntity lendingEntity : lendingEntities) { 
//        System.out.printf("%-5s%-50s%-20s\n", lendingEntity.getLendId() , lendingEntity.getBook().getTitle() , lendingEntity.getDueDate()) ; 
//        }
//          } catch (MemberNotFoundException ex) {
//                System.out.println("Member Cannot Be Found!") ; 
//                }

    }

    private void doReturnBook() {
        libraryOperationControllerRemote.doReturnBook() ; 
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("*** ILS :: Library Operation :: Return Book ***\n");
//        System.out.println("Enter Member Identity Number> ");
//        String identityNumber = scanner.nextLine().trim();
//        LendingEntity lendingEntity = new LendingEntity();
//
//        try {
//            memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
//            System.out.println("Currently Lent Books:\n");
//            List<LendingEntity> listOfBooks = lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
//
//            if (!listOfBooks.isEmpty()) {
//                System.out.println("Enter Book to Return>");
//                Long returnBookId = scanner.nextLong();
//                lendingEntityControllerRemote.setBookAvailable(identityNumber, returnBookId);
//                System.out.println("Book successfully returned.");
//            } else {
//                System.out.println("There are no books to return!");
//            }
//        } catch (MemberNotFoundException ex) {
//            System.out.println("Member Identity Number cannot be found!");
//        }
    }

    private void doExtendBook() {
        libraryOperationControllerRemote.doExtendBook() ; 
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("*** ILS :: Library Operation :: Extend Book ***\n");
//        System.out.println("Enter Member Identity Number> ");
//        String identityNumber = scanner.nextLine().trim();
//        LendingEntity lendingEntity = new LendingEntity();
//
//        try {
//            memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
//            System.out.println("Currently Lent Books:\n");
//            List<LendingEntity> listOfBooks = lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
//
//            if (!listOfBooks.isEmpty()) {
//                System.out.println("Enter Book to Extend>");
//                Long extendBookId = scanner.nextLong();
//                lendingEntity = lendingEntityControllerRemote.retrieveLendingByBookId(extendBookId);
//                if (reservationControllerRemote.checkForReservation(extendBookId)) {
//                    System.out.println("Book has been reserved and cannot be extended!");
//                } else {
//                    String duedate = lendingEntityControllerRemote.generateDueDate(lendingEntity.getDueDate());
//                    lendingEntity.setDueDate(duedate);
//                    System.out.println("Book successfully extended. New due date: " + lendingEntity.getDueDate());
//                }
//            }
//
//        } catch (MemberNotFoundException ex) {
//            System.out.println("Member Identity Number cannot be found!");
//        }
    }

    private void doPayFines() {
        libraryOperationControllerRemote.doPayFines() ; 
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("*** ILS :: Library Operation :: Pay Fines ***\n");
//        System.out.println("Enter Member Identity Number>\n");
//        String identityNumber = scanner.nextLine().trim();
//
//        try {
//            memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
//            System.out.println("Unpaid Fines for Member:\n");
//            fineControllerRemote.retrieveFinesByMember(identityNumber);
//            //if list is empty System.out.println("There are no outstanding fines!") ; 
//            System.out.println("Enter Fine ID to Settle>\n");
//            Long fineId = scanner.nextLong();
//            fineControllerRemote.payFine(fineId);
//            System.out.println("Select Payment Method (1: Cash, 2: Card)>");
//            int method = scanner.nextInt();
//            if (method == 1) {
//                System.out.println("Fine successfully paid.");
//            } else if (method == 2) {
//                System.out.println("Enter Name of Card>");
//                scanner.nextLine().trim();
//                System.out.println("Enter Card Number>");
//                scanner.nextLine().trim();
//                System.out.println("Enter Card Expiry>");
//                scanner.nextLine().trim();
//                System.out.println("Enter Pin>");
//                scanner.nextLine().trim();
//                System.out.println("Fine successfully paid.");
//
//            }
//
//        } catch (MemberNotFoundException ex) {
//            System.out.println("Member Identity Number cannot be found!");
//        }

    }

    private void doManageReservations() {
        
 //       libraryOperationControllerRemote.doManageReservations() ; 
//        Scanner scanner = new Scanner(System.in);
//        Integer response = 0;
//
//        while (true) {
//            System.out.println("*** ILS :: Library Operation :: Manage Reservations ***\n");
//            System.out.println("1: View Reservations for Books");
//            System.out.println("2: Delete Reservations");
//            System.out.println("3: Back");
//            response = 0;
//
//            while (response < 1 || response > 3) {
//                System.out.print("> ");
//
//                response = scanner.nextInt();
//
//                if (response == 1) {
//                    retrieveAllReservations();
//                } else if (response == 2) {
//                    System.out.println("Enter Reservation Id>");
//                    Long reservationId = scanner.nextLong();
//                    reservationControllerRemote.deleteReservation(reservationId);
//                } else if (response == 3) {
//                    break;
//                } else {
//                    System.out.println("Invalid option, please try again!\n");
//                }
//            }
//            if (response == 3) {
//                break;
//            }
//        }

    }
    
    private void retrieveAllReservations() {
//        System.out.print("Enter Book Isbn>\n");
//        Scanner scanner = new Scanner(System.in);
//        String isbn = scanner.nextLine().trim();
//        try {
//        BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByIsbn(isbn) ;
//        List <ReservationEntity> reservationEntities = bookEntity.getReservations() ; 
//        System.out.println("List of Reservations for Book: " + bookEntity.getTitle() ) ; 
//        System.out.printf("%-20s%-20s\n", "Reservation Id", "Member") ; 
//        
//        for (ReservationEntity reservationEntity : reservationEntities) {
//            System.out.printf("%-20s%-20s\n", reservationEntity.getReservationId(), reservationEntity.getMembers()) ; 
//        }
//        
// 
//        } catch(BookNotFoundException ex) {
//            System.out.println("Book cannot be found!") ; 
//        }
//
//}
//    
}
}


    /**
     *
     * doLendBook() memberEntityController.retrieveMemberByMemberId(member
     * identityNumber) (check whether member exist)
     * bookEntityController.retrieveBookByBookId(bookId ) (check whether book
     * exist) lendingEntityController.checkIsBookLent(bookId) (boolean - return
     * false if book is available
     *
     * )
     * lendingEntityController.checkNumBooksLoaned (member identityNumber)
     * (return number of books loaned, if = 3, cannot loan out)
     * fineEntityController.checkForFines(member identityNumber) (boolean -
     * return true if member has fine , l .memberId = :memberId and f.paid =
     * false
     *
     * )
     *
     *
     * viewLentBooks()
     *
     *
     * memberEntityController.retrieveMemberByMemberId(member identityNumber)
     * (check whether member exist) return
     * lendingEntityController.retrieveBookLoanedByMember(member identityNumber)
     * (return list of lendings)
     *
     * doReturnBooks() memberEntityController.retrieveMemberByMemberId(member
     * identityNumber) (check whether member exist)
     * lendingEntityController.retrieveBookLoanedByMember(member identityNumber)
     * (return list of lendings) lendingEntityController.setBookAvailable()
     * (l.available = true)
     *
     * doExtendBook() memberEntityController.retrieveMemberByMemberId(member
     * identityNumber) (check whether member exist)
     * lendingEntityController.retrieveBookLoanedByMember(member identityNumber)
     * (return list of lendings)
     * reservationEntityController.checkForReservation(bookId) (return true if
     * book is reserved) lendingEntityController.extendDueDate(lendId)
     * lendingEntityController.retrieveLendingEntityByLendId(lendId) (print
     * lendentity.duedate())
     *
     * doPayFines() memberEntityController.retrieveMemberByMemberId(member
     * identityNumber) (check whether member exist)
     * fineEntityController.retrieveFinesByMember (member identityNumber)
     * (return list of fines) fineEntityController.payFine(fineId) (set f.paid =
     * true)
     *
     * manageReservations()
     * reservationEntityController.retrieveAllReservations(bookId)
     * reservationEntityController.deleteReservation(bookId)
     *
     */
