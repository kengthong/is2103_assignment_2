/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import java.text.SimpleDateFormat;
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
 * @author kengthong
 */
public class MemberOperationModule {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote;
//    private FineControllerRemote fineControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
//    private ReservationControllerRemote reservationControllerRemote;
    private MemberEntity currentActiveMember;

    public MemberOperationModule(
            LibraryOperationControllerRemote libraryOperationControllerRemote,
            BookEntityControllerRemote bookEntityControllerRemote,
            //            FineControllerRemote fineControllerRemote,
            LendingEntityControllerRemote lendingEntityControllerRemote,
            MemberEntityControllerRemote memberEntityControllerRemote,
            //            ReservationControllerRemote reservationControllerRemote,
            MemberEntity currentActiveMember
    ) {
        this.libraryOperationControllerRemote = libraryOperationControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.currentActiveMember = currentActiveMember;
    }

    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Self-Service Kiosk :: Main ***\n");
            System.out.println("You are login as " + currentActiveMember.getFirstName() + " " + currentActiveMember.getLastName() + "\n");

            System.out.println("1: Borrow Book");
            System.out.println("2: View Lent Books");
            System.out.println("3: Return Book");
            System.out.println("4: Extend Book");
            System.out.println("5: Pay Fines");
            System.out.println("6: Search Book");
            System.out.println("7: Reserve Book");
            System.out.println("8: Logout\n");
            response = 0;

            while (response < 1 || response > 8) {

                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {
                    //Borrow book
                    doBorrowBook();
                } else if (response == 2) {
                    //View lent book
                    viewLentBook();
                } else if (response == 3) {
                    //Return book

                } else if (response == 4) {
                    //Extend book
                    doExtendBook();

                } else if (response == 5) {
                    //Pay fines

                } else if (response == 6) {
                    //Search book

                } else if (response == 7) {
                    //Reserve book
                } else if (response == 8) {
                    //Logout
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 7){
                break;
            }
        }
    }

    private void doBorrowBook() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");

        System.out.print("Enter Book ID: ");
        Long bookId = sc.nextLong();
        String identityNumber = currentActiveMember.getIdentityNumber();
        

        try {
            System.out.println("number ="+ identityNumber + " , bookId ="+ bookId);
            LendingEntity newLendingEntity = libraryOperationControllerRemote.doLendBook(identityNumber, bookId);
            Date newDueDate = newLendingEntity.getDueDate();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Successfully lent book. Due Date: " + dt1.format(newDueDate) + ".");
        } catch (
                BookNotFoundException
                | MemberNotFoundException
                | BookIsOnLoanException
                | MemberHasFinesException
                | MaxLoansExceeded
                | MemberNotAtTopOfReserveList ex) {
            System.out.println(ex.getMessage());
        }
//        LendingEntity newLendingEntity = new LendingEntity();
//        Date date = new Date();
//
//        try { //check if book and member exists 
//            BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByBookId(bookId);
//            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
//
//            lendingEntityControllerRemote.checkIsBookLent(bookId);
//            fineControllerRemote.checkIfMemberHasFines(identityNumber);
//            lendingEntityControllerRemote.checkIfMemberExceedsMaxLoans(identityNumber);
//            List<ReservationEntity> reservations = reservationControllerRemote.retrieveAllReservationsByBookId(bookId);
//            if (!reservations.isEmpty()) {
//                lendingEntityControllerRemote.checkIfMemberOnReserveList(identityNumber);
//            }
//
//            newLendingEntity.setMember(memberEntity);
//            newLendingEntity.setBook(bookEntity);
//            Date duedate = lendingEntityControllerRemote.generateDueDate(date);
//            newLendingEntity.setLendDate(date);
//            newLendingEntity.setDueDate(duedate);
//            newLendingEntity.setHasReturned(false);
//            newLendingEntity = lendingEntityControllerRemote.createNewLending(newLendingEntity);
//
//            Date newDueDate = newLendingEntity.getDueDate();
//            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//
//            System.out.println("Successfully lent book. Due Date: " + dt1.format(newDueDate) + ".");
//        } catch (BookNotFoundException
//                | MemberNotFoundException
//                | BookIsOnLoanException
//                | MemberHasFinesException
//                | MaxLoansExceeded
//                | MemberNotAtTopOfReserveList ex) {
//            System.out.println(ex.getMessage());
//        }
    }

    private void doExtendBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");
        String memberIdentityNumber = this.currentActiveMember.getIdentityNumber();
        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(memberIdentityNumber);
        printLending(lentBooks);

        System.out.print("Enter Book to Extend> ");
        Long bookId = sc.nextLong();

//        List<ReservationEntity> reservations = reservationControllerRemote.retrieveAllReservationsByBookId(bookId);
        //if //check for reservation
        //retrieve member
        //view lent books
        // After selecting lendId, check for reservations
        //extend due dates
        // retrieve lending by lendID
        System.out.println("Book successfully extended. New due date: 2019-03-25");

    }

    private void viewLentBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");

        String identityNumber = this.currentActiveMember.getIdentityNumber();

        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);
    }

    private void printLending(List<LendingEntity> lentBooks) {
        System.out.println("Currently Lent Books:");
        System.out.println("Id\t| Title\t| Due date");

        if (!lentBooks.isEmpty()) {
            for (LendingEntity lendingEntity : lentBooks) {
                Long lendId = lendingEntity.getLendId();
                //            String title = lendingEntity.getTitle
                String title = "test";
                // date
                String dueDate = "2019-03-14";
                System.out.println(lendId + "\t| " + title + "\t| " + dueDate);
            }
        }
        
        System.out.println();
    }
}
