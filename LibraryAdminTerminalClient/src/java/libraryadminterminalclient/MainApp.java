/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.util.Scanner;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author hiixdayah
 */
public class MainApp {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private FineControllerRemote fineControllerRemote;
    private ReservationControllerRemote reservationControllerRemote;
    private StaffEntity currentStaffEntity;
    private LibraryOperationModule libraryOperationModule;
    private AdministrationOperationModule administrationOperationModule;
    private RegistrationOperationModule registrationOperationModule;

    public MainApp() {
    }

    public MainApp(BookEntityControllerRemote bookEntityControllerRemote, LendingEntityControllerRemote lendingEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, FineControllerRemote fineControllerRemote, ReservationControllerRemote reservationControllerRemote) {

        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.fineControllerRemote = fineControllerRemote;
        this.reservationControllerRemote = reservationControllerRemote;

    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome to Library Admin Terminal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {

                    try {

                        doLogin();
                        libraryOperationModule = new LibraryOperationModule(staffEntityControllerRemote, lendingEntityControllerRemote, memberEntityControllerRemote, bookEntityControllerRemote, fineControllerRemote, reservationControllerRemote, currentStaffEntity);
                        administrationOperationModule = new AdministrationOperationModule(staffEntityControllerRemote, bookEntityControllerRemote, memberEntityControllerRemote, currentStaffEntity);
                        registrationOperationModule = new RegistrationOperationModule(memberEntityControllerRemote, currentStaffEntity);
                        menuMain();
                    } catch (InvalidLoginException ex) {
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



    private void doLogin() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** ILS :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            try {
                currentStaffEntity = staffEntityControllerRemote.staffLogin(username, password);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                throw new InvalidLoginException();
            }
        } else {
            System.out.println("Invalid login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Main ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + "\n");
            System.out.println("1: Registration Operation");
            System.out.println("2: Library Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    registrationOperationModule.menuRegistrationOperation();
                } else if (response == 2) {
                    libraryOperationModule.menuLibraryOperation();
                } else if (response == 3) {
                    administrationOperationModule.menuAdministrationOperation();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

}
