/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.BookEntity;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.BookHasBeenReservedException;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.FineIsAlreadyPaidException;
import util.exception.FineNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
public class LibraryOperationModule {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote;
    private FineControllerRemote fineControllerRemote;
    private ReservationControllerRemote reservationControllerRemote;
    private StaffEntity currentStaffEntity;

    public LibraryOperationModule() {
    }

    public LibraryOperationModule(LibraryOperationControllerRemote libraryOperationControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, FineControllerRemote fineControllerRemote, ReservationControllerRemote reservationControllerRemote, StaffEntity currentStaffEntity) {
        this();
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.fineControllerRemote = fineControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;
        this.libraryOperationControllerRemote = libraryOperationControllerRemote;
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
                    viewLentBook();
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: Lend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Book ID> ");
        Long bookId = scanner.nextLong();
//        LendingEntity newLendingEntity = new LendingEntity();
//        Date date = new Date();

        try { //check if book and member exists 

            LendingEntity newLendingEntity = this.libraryOperationControllerRemote.doLendBook(identityNumber, bookId);

            Date newDueDate = newLendingEntity.getDueDate();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("Successfully lent book to member. Due Date: " + dt1.format(newDueDate) + ".");
        } catch (BookNotFoundException
                | MemberNotFoundException
                | BookIsOnLoanException
                | MemberHasFinesException
                | MaxLoansExceeded
                | MemberNotAtTopOfReserveList ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void viewLentBook() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: View Lent Books ***\n");
        System.out.print("Enter Member Identity Number>");
        String identityNumber = scanner.nextLine().trim();

        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);
    }

    private void printLending(List<LendingEntity> lentBooks) {
        System.out.println("Currently Lent Books:");
//        System.out.println("Id\t| Title\t| Due date");

        System.out.format("%-5s %-1s %-60s %-1s %-10s %n", "Id", "|", "Title", "|", "Due date");

        if (!lentBooks.isEmpty()) {
            for (LendingEntity lendingEntity : lentBooks) {
                Long bookId = lendingEntity.getBook().getBookId();
                String title = lendingEntity.getBook().getTitle();

                Date dueDate = lendingEntity.getDueDate();
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String dd = dt1.format(dueDate);
                System.out.format("%-5d %-1s %-60s %-1s %-10s %n", bookId, "|", title, "|", dd);
            }
        }

        System.out.println();
    }

    private void doReturnBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: Return Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();

        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);
        System.out.print("Enter Book to Return> ");
        Long bookId = scanner.nextLong();

        try {
            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
            libraryOperationControllerRemote.doReturnBook(bookId, memberEntity.getMemberId());
            System.out.println("Book successfully returned.");
        } catch (LendingNotFoundException
                | MemberNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doExtendBook() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: Extend Book ***\n");
        System.out.print("Enter Member Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);

        System.out.print("Enter Book to Extend> ");
        Long bookId = scanner.nextLong();

        try {
            LendingEntity updatedLendingEntity = this.libraryOperationControllerRemote.doExtendBook(identityNumber, bookId);
            Date newDueDate = updatedLendingEntity.getDueDate();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Book successfully extended. New due date: " + dt1.format(newDueDate));
        } catch (BookIsAlreadyOverdueException
                | LendingNotFoundException
                | MemberHasFinesException
                | BookHasBeenReservedException ex) {
            System.out.print("Extend book failed, ");
            System.out.println(ex.getMessage());
        }

    }

    private void doPayFines() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** ILS :: Library Operation :: Pay Fines ***\n");
        System.out.print("Enter Member Identity Number>");
        String identityNumber = scanner.nextLine().trim();
        System.out.println();

        try {
            memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
            List<FineEntity> fineEntities = fineControllerRemote.retrieveFinesByMember(identityNumber);

            if (!fineEntities.isEmpty()) {
                System.out.println("Unpaid Fines For Member:");
                System.out.println("ID\t| Amount ");
                for (FineEntity fineEntity : fineEntities) {
                    Long fineId = fineEntity.getFineId();
                    Double amount = fineEntity.getAmount();
                    System.out.println(fineId + "\t|" + amount);
                }
                System.out.println("Enter Fine ID to Settle>\n");
                Long fineIdToPay = scanner.nextLong();
                FineEntity fineEntity = fineControllerRemote.retrieveFineByFineId(fineIdToPay);
                fineEntity.setHasPaid(true);
                System.out.println("Select Payment Method (1: Cash, 2: Card)>");
                int method = scanner.nextInt();
                scanner.nextLine();
                if (method == 2) {
                    System.out.print("Enter Name of Card> ");
                    scanner.nextLine().trim();
                    System.out.print("Enter Card Number> ");
                    scanner.nextLine().trim();
                    System.out.print("Enter Card Expiry> ");
                    scanner.nextLine().trim();
                    System.out.print("Enter Pin> ");
                    scanner.nextLine().trim();
                }
                
                fineControllerRemote.setHasPaidTrue(fineIdToPay);
                System.out.println("Fine successfully paid.\n");
                

            } else {
                System.out.println("There are no outstanding fines for member!\n");
            }

        } catch (MemberNotFoundException 
                | FineNotFoundException 
                | FineIsAlreadyPaidException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doManageReservations() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Library Operation :: Manage Reservations ***\n");
            System.out.println("1: View Reservations for Books");
            System.out.println("2: Delete Reservations");
            System.out.println("3: Back");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    viewReservations();
                } else if (response == 2) {
                    System.out.print("Enter Member Identity Number>");
                    scanner.nextLine();
                    String identityNumber = scanner.nextLine().trim();
                    printReservations(identityNumber);

                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }

    }

    private void viewReservations() {
        System.out.print("Enter Book ID>");
        Scanner scanner = new Scanner(System.in);
        Long bookId = scanner.nextLong();

        try {
            BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByBookId(bookId);

            List<ReservationEntity> reservationEntities = reservationControllerRemote.retrieveAllReservationsByBookId(bookId);
            if (!reservationEntities.isEmpty()) {
                System.out.println("Reservations under this book:");

                System.out.format("%-20s %-1s %-10s %-1s %-50s %n", "Reservation ID", "|", "Member ID", "|", "Member Identity Number");
                for (ReservationEntity reservationEntity : reservationEntities) {
                    Long reservationId = reservationEntity.getReservationId();
                    Long memberId = reservationEntity.getMember().getMemberId();
                    String memberIdentity = reservationEntity.getMember().getIdentityNumber().trim();
                    System.out.format("%-20d %-1s %-10d %-1s %-50s %n", reservationId, "|", memberId, "|", memberIdentity);
                }
            } else {
                System.out.println("There are no reservations made for this book!");
            }
            System.out.println();
        } catch (BookNotFoundException ex) {
            System.out.println("Book cannot be found!");
        }

        System.out.println();

    }

    private void printReservations(String identityNumber) {
        Scanner scanner = new Scanner(System.in);

        try {
            MemberEntity memberEntity = this.memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);

            List<ReservationEntity> reservedBooks = this.reservationControllerRemote.retrieveReservationsByMember(memberEntity.getMemberId());
            if (!reservedBooks.isEmpty()) {
                System.out.println("Books reserved by member:");

                System.out.format("%-10s %-1s %-60s %n", "Book ID", "|", "Title");
                for (ReservationEntity reservationEntity : reservedBooks) {
                    Long bookId = reservationEntity.getBook().getBookId();
                    String title = reservationEntity.getBook().getTitle();

                    System.out.format("%-10d %-1s %-60s %n", bookId, "|", title);
                }

                System.out.print("Book ID of Reservation to be deleted> ");
                Long bookId = scanner.nextLong();
                try {
                    this.libraryOperationControllerRemote.deleteReservation(bookId, identityNumber);
                    System.out.println("Reservation has been successfully deleted!");
                } catch (MemberNotFoundException | ReservationNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }

            } else {
                System.out.println("No books have been reserved by member!");
            }

        } catch (MemberNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

}

/**
 *
 * doLendBook() memberEntityController.retrieveMemberByMemberId(member
 * identityNumber) (check whether member exist)
 * bookEntityController.retrieveBookByBookId(bookId ) (check whether book exist)
 * lendingEntityController.checkIsBookLent(bookId) (boolean - return false if
 * book is available
 *
 * )
 * lendingEntityController.checkNumBooksLoaned (member identityNumber) (return
 * number of books loaned, if = 3, cannot loan out)
 * fineEntityController.checkForFines(member identityNumber) (boolean - return
 * true if member has fine , l .memberId = :memberId and f.paid = false
 *
 * )
 *
 *
 * viewLentBooks()
 *
 *
 * memberEntityController.retrieveMemberByMemberId(member identityNumber) (check
 * whether member exist) return
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
 * reservationEntityController.checkForReservation(bookId) (return true if book
 * is reserved) lendingEntityController.extendDueDate(lendId)
 * lendingEntityController.retrieveLendingEntityByLendId(lendId) (print
 * lendentity.duedate())
 *
 * doPayFines() memberEntityController.retrieveMemberByMemberId(member
 * identityNumber) (check whether member exist)
 * fineEntityController.retrieveFinesByMember (member identityNumber) (return
 * list of fines) fineEntityController.payFine(fineId) (set f.paid = true)
 *
 * manageReservations()
 * reservationEntityController.retrieveAllReservations(bookId)
 * reservationEntityController.deleteReservation(bookId)
 *
 */
