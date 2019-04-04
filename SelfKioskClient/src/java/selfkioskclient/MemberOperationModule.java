/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import entity.BookEntity;
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
    private FineControllerRemote fineControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private ReservationControllerRemote reservationControllerRemote;
    private MemberEntity currentActiveMember;

    public MemberOperationModule(
            BookEntityControllerRemote bookEntityControllerRemote,
            FineControllerRemote fineControllerRemote,
            LendingEntityControllerRemote lendingEntityControllerRemote,
            MemberEntityControllerRemote memberEntityControllerRemote,
            ReservationControllerRemote reservationControllerRemote,
            MemberEntity currentActiveMember
    ) {
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.currentActiveMember = currentActiveMember;
    }

    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        System.out.println("*** Self-Service Kiosk :: Main ***\n");
        System.out.println("You are logged in as " + this.currentActiveMember + "\n");

        System.out.println("1: Borrow Book");
        System.out.println("2: View Lent Books");
        System.out.println("3: Return Book");
        System.out.println("4: Extend Book");
        System.out.println("5: Pay Fines");
        System.out.println("6: Search Book");
        System.out.println("7: Reserve Book");
        System.out.println("8: Logout\n");

        while (response < 1 || response > 8) {

            System.out.print("> ");
            response = sc.nextInt();

            switch (response) {
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
                    doExtendBook();
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

        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");

        System.out.print("Enter Book ID: ");
        Long bookId = sc.nextLong();
        String identityNumber = currentActiveMember.getIdentityNumber();

        LendingEntity newLendingEntity = new LendingEntity();
        Date date = new Date();

        try { //check if book and member exists 
            BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByBookId(bookId);
            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);

            lendingEntityControllerRemote.checkIsBookLent(bookId);
            fineControllerRemote.checkIfMemberHasFines(identityNumber);
            lendingEntityControllerRemote.checkIfMemberExceedsMaxLoans(identityNumber);
            List<ReservationEntity> reservations = reservationControllerRemote.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                lendingEntityControllerRemote.checkIfMemberOnReserveList(identityNumber);
            }

            newLendingEntity.setMember(memberEntity);
            newLendingEntity.setBook(bookEntity);
            Date duedate = lendingEntityControllerRemote.generateDueDate(date);
            newLendingEntity.setLendDate(date);
            newLendingEntity.setDueDate(duedate);
            newLendingEntity.setHasReturned(false);
            newLendingEntity = lendingEntityControllerRemote.createNewLending(newLendingEntity);

            Date newDueDate = newLendingEntity.getDueDate();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("Successfully lent book. Due Date: " + dt1.format(newDueDate) + ".");
        } catch (BookNotFoundException
                | MemberNotFoundException
                | BookIsOnLoanException
                | MemberHasFinesException
                | MaxLoansExceeded
                | MemberNotAtTopOfReserveList ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doExtendBook()
    {
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");
        System.out.println("Currently Lent Books:\n");
        
Id |Title                  | Due Date
4  |The Hobbit             | 2019-03-12
Enter Book to Extend> 4
Book successfully extended. New due date: 2019-03-25
    }
    
    private void viewLentBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");

        String memberIdentityNumber = this.currentActiveMember.getIdentityNumber();
        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(memberIdentityNumber);

        System.out.println("Currently Lent Books:");
        System.out.println("Id\t| Title\t| Due date");
        printLending(lentBooks);
    }
    
    private void printLending(List<LendingEntity> lentBooks)
    {
        for (LendingEntity lendingEntity : lentBooks) {
            Long lendId = lendingEntity.getLendId();
            //            String title = lendingEntity.getTitle
            String title = "test";
            // date
            String dueDate = "2019-03-14";
            System.out.println(lendId + "\t| " + title + "\t| " + dueDate);
        }
    }
}
