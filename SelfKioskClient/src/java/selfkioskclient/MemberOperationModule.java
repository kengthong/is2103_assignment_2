package selfkioskclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.BookHasBeenReservedException;
import util.exception.BookIsAlreadyLoanedByMemberException;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsAvailableForLoanException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.FineNotFoundException;
import util.exception.LendingNotFoundException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.MultipleReservationException;

/**
 *
 * @author kengthong
 */
public class MemberOperationModule {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote;
    private FineControllerRemote fineControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
//    private ReservationControllerRemote reservationControllerRemote;
    private MemberEntity currentActiveMember;

    public MemberOperationModule(
            LibraryOperationControllerRemote libraryOperationControllerRemote,
            BookEntityControllerRemote bookEntityControllerRemote,
            FineControllerRemote fineControllerRemote,
            LendingEntityControllerRemote lendingEntityControllerRemote,
            MemberEntityControllerRemote memberEntityControllerRemote,
            //            ReservationControllerRemote reservationControllerRemote,
            MemberEntity currentActiveMember
    ) {
        this.fineControllerRemote = fineControllerRemote;
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
                    doReturnBook();
                } else if (response == 4) {
                    //Extend book
                    doExtendBook();

                } else if (response == 5) {
                    //Pay fines
                    doPayFines();
                } else if (response == 6) {
                    //Search book
                    doSearchBook();
                } else if (response == 7) {
                    //Reserve book
                    doReserveBook();
                } else if (response == 8) {
                    //Logout
//                    doLogOut();
                } else if (response == 9) {
                    libraryOperationControllerRemote.setFines(369, currentActiveMember);
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 8) {
                break;
            }
        }
    }

    private void doBorrowBook() {
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Self-Service Kiosk :: Borrow Book ***\n");

        System.out.print("Enter Book ID: ");
        Long bookId = sc.nextLong();
        System.out.println() ; 
        String identityNumber = currentActiveMember.getIdentityNumber();

        try {
//            System.out.println("number =" + identityNumber + " , bookId =" + bookId);
            LendingEntity newLendingEntity = libraryOperationControllerRemote.doLendBook(identityNumber, bookId);
            Date newDueDate = newLendingEntity.getDueDate();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Successfully lent book. Due Date: " + dt1.format(newDueDate) + ".\n");
        } catch (BookNotFoundException
                | MemberNotFoundException
                | BookIsOnLoanException
                | MemberHasFinesException
                | MaxLoansExceeded
                | MemberNotAtTopOfReserveList ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doExtendBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Extend Book ***\n");
        String identityNumber = this.currentActiveMember.getIdentityNumber();
        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);

        System.out.print("Enter Book to Extend> ");
        Long bookId = sc.nextLong();

        try {
            LendingEntity updatedLendingEntity = libraryOperationControllerRemote.doExtendBook(identityNumber, bookId);
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
        System.out.println("*** Self-Service Kiosk :: Pay Fines ***\n");
//        System.out.print("Enter Member Identity Number>");
        String identityNumber = currentActiveMember.getIdentityNumber();

        try {
//            memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
            List<FineEntity> fineEntities = fineControllerRemote.retrieveFinesByMember(identityNumber);

            if (!fineEntities.isEmpty()) {
                System.out.println("Unpaid Fines For Member:");
                System.out.println("Fine ID\t| Amount ");
                for (FineEntity fineEntity : fineEntities) {
                    Long fineId = fineEntity.getFineId();
                    Double amount = fineEntity.getAmount();
                    System.out.println(fineId + "\t|" + amount);
                }
                System.out.print("Enter Fine ID to Settle> ");
                Long fineIdToPay = scanner.nextLong();
                scanner.nextLine();
//                FineEntity fineEntity = fineControllerRemote.retrieveFineByFineId(fineIdToPay);
//                fineEntity.setHasPaid(true);
                
                System.out.print("Enter Name of Card> ");
                scanner.nextLine().trim();
                System.out.print("Enter Card Number> ");
                scanner.nextLine().trim();
                System.out.print("Enter Card Expiry> ");
                scanner.nextLine().trim();
                System.out.print("Enter Pin> ");
                scanner.nextLine().trim();
                fineControllerRemote.setHasPaidTrue(fineIdToPay);
                System.out.println("Fine successfully paid.");

            } else {
                System.out.println("There are no outstanding fines for member!\n");
            }

        } catch (FineNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doReserveBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n");

        System.out.print("Enter Title to Search> ");
        String titleToSearch = sc.nextLine().trim();
        System.out.println();

        List<Object[]> results = libraryOperationControllerRemote.searchBook(titleToSearch);
        printSearchResults(results);

        if (results.isEmpty()) {
            return;
        }

        System.out.print("Enter Book ID to Reserve> ");
        Long bookId = sc.nextLong();

        try {
            libraryOperationControllerRemote.doReserveBook(currentActiveMember, bookId);
            System.out.println("Book successfully reserved.");
        } catch (BookIsAvailableForLoanException
                | MultipleReservationException
                | MemberHasFinesException
                | BookNotFoundException
                | BookIsAlreadyLoanedByMemberException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        }
//
//Id |Title | Availability
//6 | Dream of the Red Chamber | Due on 2019-03-22
//Enter Book ID to Reserve: 6
//Book successfully reserved.
    }

    private void doReturnBook() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Return Book ***\n");

        String identityNumber = this.currentActiveMember.getIdentityNumber();
        Long memberId = this.currentActiveMember.getMemberId();
        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);

        System.out.print("Enter Book to Return> ");
        Long bookId = sc.nextLong();
        try {
            libraryOperationControllerRemote.doReturnBook(bookId, memberId);
            System.out.println("Book successfully returned.");
        } catch (LendingNotFoundException
                | MemberNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doSearchBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: Search Book ***\n");

        System.out.print("Enter Title to Search> ");
        String titleToSearch = sc.nextLine().trim();
        System.out.println();

        List<Object[]> results = libraryOperationControllerRemote.searchBook(titleToSearch);
        printSearchResults(results);
        System.out.print("Enter any button to go back to the menu> ");
        sc.nextLine().trim();
        System.out.println();
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

//    private void printReserveResults(List<Object[]> results) {
//        System.out.println("Search Results:");
//
//        System.out.format("%-5s %-1s %-60s %-1s %-10s %n", "Id", "|", "Title", "|", "Availability");
//        if (!results.isEmpty()) {
//            for (Object[] result : results) {
//                Long bookId = (Long) result[0];
//                String title = (String) result[1];
//                Boolean hasReturned = (Boolean) result[2];
//                Date dueDate = (Date) result[3];
//
//                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//                String dd = "Due on " + dt1.format(dueDate);
//
//                System.out.format("%-5d %-1s %-60s %-1s %-10s %n", bookId, "|", title, "|", dd);
//            }
//        } else {
//            System.out.println("No such book to reserve.");
//        }
//        System.out.println();
//    }
    private void printSearchResults(List<Object[]> results) {
        System.out.println("Search Results:");

        System.out.format("%-5s %-1s %-60s %-1s %-10s %n", "Id", "|", "Title", "|", "Availability");
        if (!results.isEmpty()) {
            for (Object[] result : results) {
                Long bookId = (Long) result[0];
                String title = (String) result[1];
                String status = (String) result[2];

                System.out.format("%-5d %-1s %-60s %-1s %-10s %n", bookId, "|", title, "|", status);
            }
        } else {
            System.out.println("No such book found.\n");
        }

    }

    private void viewLentBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Self-Service Kiosk :: View Lent Books ***\n");

        String identityNumber = this.currentActiveMember.getIdentityNumber();

        List<LendingEntity> lentBooks = this.lendingEntityControllerRemote.retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);
    }

    //S7483027A
    //S8381028X
}