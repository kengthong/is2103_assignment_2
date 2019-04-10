package selfkioskclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author kengthong
 */
public class MainApp {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote;
    private FineControllerRemote fineControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private MemberOperationModule memberOperationModule;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
//    private ReservationControllerRemote reservationControllerRemote;

    private MemberEntity currentActiveMember;

    public MainApp(
            LibraryOperationControllerRemote libraryOperationControllerRemote,
            BookEntityControllerRemote bookEntityControllerRemote,
            FineControllerRemote fineControllerRemote,
            MemberEntityControllerRemote memberEntityControllerRemote,
            LendingEntityControllerRemote lendingEntityControllerRemote
    //            ReservationControllerRemote reservationControllerRemote
    ) {
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.libraryOperationControllerRemote = libraryOperationControllerRemote;
        this.lendingEntityControllerRemote = lendingEntityControllerRemote;
        this.fineControllerRemote = fineControllerRemote;
//        this.reservationControllerRemote = reservationControllerRemote;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;


        while (response < 1 || response > 3) {
            System.out.println("*** Welcome to Self-Service Kiosk *** \n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            System.out.print("> ");
            response = sc.nextInt();

            if (response == 1) {
                doRegisterMember() ; 
            } else if (response == 2) {
                // do login
                try {
                    doLogin();
                    this.memberOperationModule = new MemberOperationModule(
                            this.libraryOperationControllerRemote,
                            this.bookEntityControllerRemote,
                            this.fineControllerRemote,
                            this.lendingEntityControllerRemote,
                            this.memberEntityControllerRemote,
                            //                            this.reservationControllerRemote,
                            this.currentActiveMember
                    );
                    memberOperationModule.displayMenu();
                    this.currentActiveMember = null;
                } catch (InvalidLoginException ex) {
                    System.out.println(ex.getMessage() + "\n");
                }
            } else if (response == 3) {
                break;
            }
            
            response = 0;
        }
    }
    
     private void doRegisterMember() {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();

        System.out.println("*** Self-Service Kiosk :: Register ***\n");
        System.out.print("Enter Identity Number> ");
        newMemberEntity.setIdentityNumber(scanner.nextLine().trim());
        System.out.print("Enter Security Code> ");
        newMemberEntity.setSecurityCode(scanner.nextLine().trim());
        System.out.print("Enter First Name> ");
        newMemberEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newMemberEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Gender> ");
        newMemberEntity.setGender(scanner.nextLine().trim());
        System.out.print("Enter Age> ");
        newMemberEntity.setAge(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Phone> ");
        newMemberEntity.setPhone(scanner.nextLine().trim());
        System.out.print("Enter Address>");
        newMemberEntity.setAddress(scanner.nextLine());

        try {
            memberEntityControllerRemote.retrieveMemberByIdentityNumber(newMemberEntity.getIdentityNumber());
            System.out.println("Member has already been registered previously and cannot register again!\n");
        } catch (MemberNotFoundException ex) {
            memberEntityControllerRemote.createNewMember(newMemberEntity);
            System.out.println("Member has been registered successfully!\n");
        }
    }

    private void doLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String identityNumber = "";
        String securityCode = "";
        System.out.println("*** Self-Service Kiosk :: Login ***\n");

        System.out.print("Enter Identity Number> ");
        identityNumber = sc.nextLine().trim();
        System.out.print("Enter Security Code> ");
        securityCode = sc.nextLine().trim();

        if (identityNumber.length() > 0 && securityCode.length() > 0) {
            try {
                this.currentActiveMember = this.memberEntityControllerRemote.doMemberLogin(identityNumber, securityCode);
                System.out.println("Login successful!\n");
            } catch (InvalidLoginException ex) {
                throw ex;
            }
        }

    }

    
}