/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.XMLGregorianCalendar;
import net.java.dev.jaxb.array.AnyTypeArray;

/**
 *
 * @author Sing Jie
 */
public class MainApp {

    private MemberEntity currentMemberEntity;

    public MainApp() {

    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to BDM Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    try {
                        doLogin();
                        menuMain();
                    } catch (InvalidLoginException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginException_Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** BDM Client :: Login ***\n");
        System.out.print("Enter Identity Number> ");
        String identityNumber = scanner.nextLine().trim();
        System.out.print("Enter Security Code> ");
        String securityCode = scanner.nextLine().trim();

        if (identityNumber.length() > 0 && securityCode.length() > 0) {
            try {
                currentMemberEntity = memberLogin(identityNumber, securityCode);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginException_Exception ex) {
                throw ex;

            }
        } else {
            System.out.println("Invalid login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** BDM Client :: Main ***\n");
            System.out.println("You are login as " + currentMemberEntity.getFirstName() + " " + currentMemberEntity.getLastName() + "\n");
            System.out.println("1: View Lent Books");
            System.out.println("2: Return Book");
            System.out.println("3: Extend Book");
            System.out.println("4: Pay Fines");
            System.out.println("5: Reserve Book");
            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    viewLentBook();
                } else if (response == 2) {
                    doReturnBook();
                } else if (response == 3) {
                    doExtendBook();
                } else if (response == 4) {
                    doPayFines();
                } else if (response == 5) {
                    doReserveBook();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    private void viewLentBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** BDM Client :: View Lent Books ***\n");

        String identityNumber = currentMemberEntity.getIdentityNumber();

        List<LendingEntity> lentBooks = retrieveBooksLoanedByMember(identityNumber);
        printLending(lentBooks);

    }

    private void printLending(List<LendingEntity> lentBooks) {
        System.out.println("Currently Lent Books:");
        System.out.format("%-5s %-1s %-60s %-1s %-10s %n", "Id", "|", "Title", "|", "Due date");
        if (!lentBooks.isEmpty()) {
            for (LendingEntity lendingEntity : lentBooks) {
                Long bookId = lendingEntity.getBook().getBookId();
                String title = lendingEntity.getBook().getTitle();

                XMLGregorianCalendar dueDate = lendingEntity.getDueDate();
                Date due = dueDate.toGregorianCalendar().getTime();
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String dd = dt1.format(due);
                System.out.format("%-5d %-1s %-60s %-1s %-10s %n", bookId, "|", title, "|", dd);
            }
        }

        System.out.println();
    }

    private void doReturnBook() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** BDM Client :: Return Book ***\n");

        String identityNumber = currentMemberEntity.getIdentityNumber();
        Long memberId = currentMemberEntity.getMemberId();

        try {
            List<LendingEntity> lentBooks = retrieveBooksLoanedByMember(identityNumber);
            printLending(lentBooks);
            System.out.print("Enter Book to Return> ");
            Long bookId = sc.nextLong();
            doReturnBook(bookId, memberId);
            System.out.println("Please drop to machine complete return.");
            System.out.println("Book successfully returned.");
        } catch (LendingNotFoundException_Exception
                | MemberNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doExtendBook() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** BDM Client :: Extend Book ***\n");
        String identityNumber = currentMemberEntity.getIdentityNumber();

        try {
            List<LendingEntity> lentBooks = retrieveBooksLoanedByMember(identityNumber);
            printLending(lentBooks);

            System.out.print("Enter Book to Extend> ");
            Long bookId = sc.nextLong();
            LendingEntity updatedLendingEntity = doExtendBook(identityNumber, bookId);
            XMLGregorianCalendar newDueDate = updatedLendingEntity.getDueDate();
            Date newDue = newDueDate.toGregorianCalendar().getTime();
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Book successfully extended. New due date: " + dt1.format(newDue));
        } catch (LendingNotFoundException_Exception | MemberHasFinesException_Exception | BookIsAlreadyOverdueException_Exception | BookHasBeenReservedException_Exception ex) {
            System.out.print("Extend book failed. ");
            System.out.println(ex.getMessage());
        }

    }

    private void doReserveBook() {
        List<AnyTypeArray> results;
//        String identityNumber = currentActiveMember.getIdentityNumber();

        Scanner sc = new Scanner(System.in);
        System.out.println("*** BDM Client :: Reserve Book ***\n");
        System.out.print("Enter Title to Search> ");
        String titleToSearch = sc.nextLine().trim();

        results = searchBookToReserve(titleToSearch);
        printReserveResults(results);

        if (results.isEmpty()) {
            return;
        }

        System.out.print("Enter Book ID to Reserve> ");
        Long bookId = sc.nextLong();

        try {
            doReserveBook(currentMemberEntity, bookId);
            System.out.println("Book successfully reserved.");
        } catch (BookIsAlreadyLoanedByMemberException_Exception | BookIsAvailableForLoanException_Exception | BookNotFoundException_Exception | MemberHasFinesException_Exception | MultipleReservationException_Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        }
    }

    private void printReserveResults(List<AnyTypeArray> results) {
        System.out.println("Search Results:");
        System.out.format("%-5s %-1s %-60s %-1s %-10s %n", "Id", "|", "Title", "|", "Availability");
        if (!results.isEmpty()) {
            for (AnyTypeArray result : results) {
                List<Object> items = result.getItem();
                Long bookId = (Long) items.get(0);
                String title = (String) items.get(1);
//                Boolean hasReturned = (Boolean) items.get(2);
//                Date dueDate = (Date) items.get(2);
//
//                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
//                String dd = "Due on " + dt1.format(dueDate);

                /*Added this part*/
                String status = (String) items.get(2);
                System.out.format("%-5d %-1s %-60s %-1s %-10s %n", bookId, "|", title, "|", status);
            }
        } else {
            System.out.println("No such book to reserve.");
        }
    }

    private void doPayFines() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*** BDM Client :: Pay Fines ***\n");
//        System.out.print("Enter Member Identity Number>");
//        String identityNumber = scanner.nextLine().trim();

        try {
//            MemberEntity memberEntity = retrieveMemberByIdentityNumber(identityNumber);
            String identityNumber = currentMemberEntity.getIdentityNumber();
            List<FineEntity> fineEntities = retrieveFinesByMember(identityNumber);

            if (!fineEntities.isEmpty()) {
                System.out.println("Unpaid Fines For Member:");
                System.out.println("Fine ID\t| Amount ");
                for (FineEntity fineEntity : fineEntities) {
                    Long fineId = fineEntity.getFineId();
                    Double amount = fineEntity.getAmount();
                    System.out.println(fineId + "\t|" + amount);
                }
                System.out.println("Enter Fine ID to Settle>\n");
                Long fineIdToPay = scanner.nextLong();
                FineEntity fineEntity = retrieveFineByFineId(fineIdToPay);
                fineEntity.setHasPaid(true);
                System.out.println("Select Payment Method (1: Cash, 2: Card)>");
                int method = scanner.nextInt();

                System.out.println("Enter Name of Card>");
                scanner.nextLine().trim();
                System.out.println("Enter Card Number>");
                scanner.nextLine().trim();
                System.out.println("Enter Card Expiry>");
                scanner.nextLine().trim();
                System.out.println("Enter Pin>");
                scanner.nextLine().trim();
                setHasPaidTrue(fineIdToPay);
                System.out.println("Fine successfully paid.");

            } else {
                System.out.println("There are no outstanding fines for member!");
            }
        } catch (FineNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private static ejb.session.ws.MemberEntity memberLogin(java.lang.String identityNumber, java.lang.String securityCode) throws InvalidLoginException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.memberLogin(identityNumber, securityCode);
    }

    private static java.util.List<ejb.session.ws.LendingEntity> retrieveBooksLoanedByMember(java.lang.String identityNumber) {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.retrieveBooksLoanedByMember(identityNumber);
    }

    private static void doReturnBook(java.lang.Long bookId, java.lang.Long memberId) throws LendingNotFoundException_Exception, MemberNotFoundException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        port.doReturnBook(bookId, memberId);
    }

    private static LendingEntity doExtendBook(java.lang.String identityNumber, java.lang.Long bookId) throws LendingNotFoundException_Exception, MemberHasFinesException_Exception, BookIsAlreadyOverdueException_Exception, BookHasBeenReservedException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.doExtendBook(identityNumber, bookId);
    }

    private static void doReserveBook(ejb.session.ws.MemberEntity currentMember, java.lang.Long bookId) throws BookIsAlreadyLoanedByMemberException_Exception, BookIsAvailableForLoanException_Exception, BookNotFoundException_Exception, MemberHasFinesException_Exception, MultipleReservationException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        port.doReserveBook(currentMember, bookId);
    }

    private static FineEntity retrieveFineByFineId(java.lang.Long fineIdToPay) throws FineNotFoundException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.retrieveFineByFineId(fineIdToPay);
    }

    private static java.util.List<ejb.session.ws.FineEntity> retrieveFinesByMember(java.lang.String identityNumber) {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.retrieveFinesByMember(identityNumber);
    }

    private static MemberEntity retrieveMemberByIdentityNumber(java.lang.String identityNumber) throws MemberNotFoundException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.retrieveMemberByIdentityNumber(identityNumber);
    }

    private static void setHasPaidTrue(java.lang.Long fineIdToPay) throws FineNotFoundException_Exception {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        port.setHasPaidTrue(fineIdToPay);
    }

    private static java.util.List<net.java.dev.jaxb.array.AnyTypeArray> searchBookToReserve(java.lang.String titleToSearch) {
        ejb.session.ws.BookDropWebService_Service service = new ejb.session.ws.BookDropWebService_Service();
        ejb.session.ws.BookDropWebService port = service.getBookDropWebServicePort();
        return port.searchBookToReserve(titleToSearch);
    }

}
