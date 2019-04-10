/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Scanner;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author hiixdayah
 */
public class AdministrationOperationModule {

    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private LibraryOperationControllerRemote libraryOperationControllerRemote ; 
    private StaffEntity currentStaffEntity;

    public AdministrationOperationModule() {
    }

    public AdministrationOperationModule(LibraryOperationControllerRemote libraryOperationControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, MemberEntityControllerRemote memberEntityControllerRemote, StaffEntity currentStaffEntity) {
        this();
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.libraryOperationControllerRemote = libraryOperationControllerRemote ; 
        this.currentStaffEntity = currentStaffEntity;
    }

    public void menuAdministrationOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Administration Operation ***\n");
            System.out.println("1: Member Management");
            System.out.println("2: Book Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doManageMember();
                } else if (response == 2) {
                    doManageBook();
                } else if (response == 3) {
                    doManageStaff();
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

    private void doManageMember() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Administration Operation :: Member Management ***\n");
            System.out.println("1: Add Member");
            System.out.println("2: View Member Details");
            System.out.println("3: Update Member");
            System.out.println("4: Delete Member");
            System.out.println("5: View All Members");
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewMember();
                } else if (response == 2) {
                    doViewMemberDetails();
                } else if (response == 3) {
                    System.out.print("Enter Member Identity Number> ");
                    scanner.nextLine() ;
                    String identityNumber = scanner.nextLine().trim();
                    try {
                    MemberEntity thisMemberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
                    doUpdateMember(thisMemberEntity);
                    } catch (MemberNotFoundException ex) {
                        System.out.println("Member cannot be found!") ; 
                    }
                    
                } else if (response == 4) {
                    System.out.print("Enter Member Identity Number> ");
                    scanner.nextLine() ;
                    String identityNumber = scanner.nextLine().trim();
                    try { 
                        MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
                        doDeleteMember(memberEntity);
                    } catch (MemberNotFoundException ex) {
                        System.out.println("Member cannot be found!") ; 
                    }
                } else if (response == 5) {
                    doViewAllMembers();
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

    private void doCreateNewMember() {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();

        System.out.println("*** ILS :: Administration Operation :: Member Management :: Add Member ***\n");
        System.out.print("Enter Identity Number> ");
        newMemberEntity.setIdentityNumber(scanner.nextLine().trim());
        System.out.print("Enter Security Code> ");
        newMemberEntity.setSecurityCode(scanner.nextLine().trim());
        System.out.println("security code ==" + newMemberEntity.getSecurityCode());
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

    private void doViewMemberDetails() {
        System.out.println("*** ILS :: Administration Operation :: Member Management :: View Member Details ***\n");
        System.out.print("Enter Member Identity Number> ");
        Scanner scanner = new Scanner(System.in);
        String identityNumber =  scanner.nextLine().trim() ; 
 

        try {
            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber);
            System.out.printf("%-20s%-20s%-35s%-20s%-20s%-20s%-20s%-20s%-20s\n", "Member Id", "Identity Number", "Security Code", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
            System.out.printf("%-20s%-20s%-35s%-20s%-20s%-20s%-20s%-20s%-20s\n", memberEntity.getMemberId().toString(), memberEntity.getIdentityNumber(), memberEntity.getSecurityCode(), memberEntity.getFirstName(), memberEntity.getLastName(), memberEntity.getGender().toString(), memberEntity.getAge().toString(), memberEntity.getPhone(), memberEntity.getAddress());

        } catch (MemberNotFoundException ex) {
            System.out.println("Member cannot be found!\n");
        }

    }

    private void doUpdateMember(MemberEntity memberEntity) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Member Management :: Update Member ***\n");

        System.out.print("Enter Identity Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setIdentityNumber(input);
        }

        System.out.print("Enter Security Code (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setSecurityCode(input);
        }

        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setFirstName(input);
        }

        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setLastName(input);
        }

        System.out.print("Enter Gender (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setGender(input);
        }

        System.out.print("Enter Age (blank if no change)> ");
        input = scanner.nextLine().trim() ; 
           if (input.isEmpty()) {
           } else {
        int age = Integer.parseInt(input) ; 
            memberEntity.setAge(age);
           }
        
        

        System.out.print("Enter Phone (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setPhone(input);
        }

        System.out.print("Enter Address (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            memberEntity.setAddress(input);
        }

        memberEntityControllerRemote.updateMember(memberEntity);
        System.out.println("Member updated successfully!\n");
    }

    private void doDeleteMember(MemberEntity memberEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Member Management :: Delete Member ***\n");
        System.out.printf("Confirm Delete Member %s %s (Member ID: %d) (Enter 'Y' to Delete)> ", memberEntity.getFirstName(), memberEntity.getLastName(), memberEntity.getMemberId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                memberEntityControllerRemote.deleteMember(memberEntity.getMemberId());
                System.out.println("Member deleted successfully!\n");
            } catch (MemberNotFoundException ex) {
                System.out.println("An error has occurred while deleting member: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Member NOT deleted!\n");
        }
    }

    private void doViewAllMembers() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** ILS :: Administration Operation :: Member Management :: View All Members ***\n");

        List<MemberEntity> memberEntities = memberEntityControllerRemote.retrieveAllMembers();
        System.out.printf("%-20s%-20s%-35s%-20s%-20s%-20s%-20s%-20s%-20s\n", "Member Id", "Identity Number", "Security Code", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");

        for (MemberEntity memberEntity : memberEntities) {
            System.out.printf("%-20s%-20s%-35s%-20s%-20s%-20s%-20s%-20s%-20s\n", memberEntity.getMemberId().toString(), memberEntity.getIdentityNumber(), memberEntity.getSecurityCode(), memberEntity.getFirstName(), memberEntity.getLastName(), memberEntity.getGender().toString(), memberEntity.getAge().toString(), memberEntity.getPhone(), memberEntity.getAddress());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doManageBook() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Administration Operation :: Book Management ***\n");
            System.out.println("1: Add Book");
            System.out.println("2: View Book Details");
            System.out.println("3: Update Book");
            System.out.println("4: Delete Book");
            System.out.println("5: View All Books");
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewBook();
                } else if (response == 2) {
                    doViewBookDetails();
                } else if (response == 3) {
                    System.out.print("Enter Book Isbn> ");
                    scanner.nextLine() ; 
                    String isbn = scanner.nextLine().trim() ; 
                    try {
                    BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
                    doUpdateBook(bookEntity);
                    } catch (BookNotFoundException ex) {
                        System.out.println("Book cannot be found!") ; 
                    }
                } else if (response == 4) {
                    System.out.print("Enter Book Isbn> ");
                    scanner.nextLine() ;
                    String isbn = scanner.nextLine().trim() ; 
                    try {
                    BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
                    doDeleteBook(bookEntity);
                    } catch (BookNotFoundException ex) {
                        System.out.println("Book cannot be found!") ; 
                    }
                } else if (response == 5) {
                    doViewAllBooks();
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

    private void doCreateNewBook() {
        Scanner scanner = new Scanner(System.in);
        BookEntity newBookEntity = new BookEntity();

        System.out.println("*** ILS :: Administration Operation :: Book Management :: Add Book ***\n");
        System.out.print("Enter Title> ");
        newBookEntity.setTitle(scanner.nextLine().trim());
        System.out.print("Enter Published Year> ");
        newBookEntity.setPublishedYear(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Isbn> ");
        newBookEntity.setIsbn(scanner.nextLine().trim());

        try {
            bookEntityControllerRemote.retrieveBookByIsbn(newBookEntity.getIsbn());
            System.out.println("Book already exists and cannot be created again!\n");
        } catch (BookNotFoundException ex) {
            bookEntityControllerRemote.createNewBook(newBookEntity);
            System.out.println("Book has been created successfully!\n");
        }
    }

    private void doViewBookDetails() {
        System.out.println("*** ILS :: Administration Operation :: Book Management :: View Book Details ***\n");
        System.out.print("Enter Book Isbn> ");
        Scanner scanner = new Scanner(System.in);
        String isbn = scanner.nextLine().trim() ; 

        try {
            BookEntity bookEntity = bookEntityControllerRemote.retrieveBookByIsbn(isbn);
            System.out.printf("%-20s%-60s%-20s%-20s\n", "Book Id", "Title", "Published Year", "Isbn");
            System.out.printf("%-20s%-60s%-20s%-20s\n", bookEntity.getBookId().toString(), bookEntity.getTitle(), bookEntity.getPublishedYear().toString(), bookEntity.getIsbn());

        } catch (BookNotFoundException ex) {
            System.out.println("Book cannot be found!\n");
        }

    }

    private void doUpdateBook(BookEntity bookEntity) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Book Management :: Update Book ***\n");

        System.out.print("Enter Title (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            bookEntity.setTitle(input);
        }

        System.out.print("Enter Year (blank if no change)> ");
      input = scanner.nextLine().trim() ; 
           if (input.isEmpty()) {
           } else {
        int year = Integer.parseInt(input) ; 
            bookEntity.setPublishedYear(year);
           }
        

        System.out.print("Enter Isbn (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            bookEntity.setIsbn(input);
        }

        bookEntityControllerRemote.updateBook(bookEntity);
        System.out.println("Book updated successfully!\n");
    }

    private void doDeleteBook(BookEntity bookEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Book Management :: Delete Book ***\n");
        System.out.printf("Confirm Delete Book %s %s (Book ID: %d) (Enter 'Y' to Delete)> ", bookEntity.getTitle(), bookEntity.getIsbn(), bookEntity.getBookId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                bookEntityControllerRemote.deleteBook(bookEntity.getBookId());
                System.out.println("Book deleted successfully!\n");
            } catch (BookNotFoundException ex) {
                System.out.println("An error has occurred while deleting book: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Book NOT deleted!\n");
        }
    }

    private void doViewAllBooks() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** ILS :: Administration Operation :: Book Management :: View All Books ***\n");

        List<BookEntity> bookEntities = bookEntityControllerRemote.retrieveAllBooks();
        System.out.printf("%-20s%-70s%-20s%-20s\n", "Book Id", "Title", "Published Year", "Isbn");

        for (BookEntity bookEntity : bookEntities) {
            System.out.printf("%-20s%-70s%-20s%-20s\n", bookEntity.getBookId().toString(), bookEntity.getTitle(), bookEntity.getPublishedYear().toString(), bookEntity.getIsbn());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doManageStaff() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Administration Operation :: Staff Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewStaff();
                } else if (response == 2) {
                    doViewStaffDetails();
                } else if (response == 3) {
                    System.out.print("Enter Staff Username> ");
                    scanner.nextLine() ; 
                    String username = scanner.nextLine().trim();
                    try {
                    StaffEntity staffEntity = staffEntityControllerRemote.retrieveStaffByUsername(username);
                    doUpdateStaff(staffEntity);
                    } catch (StaffNotFoundException ex) {
                    System.out.println("Staff cannot be found!") ; 
                }
                } else if (response == 4) {
                    System.out.print("Enter Staff Username> ");
                    scanner.nextLine() ; 
                    String username = scanner.nextLine().trim() ; 
                    try {
                    StaffEntity staffEntity = staffEntityControllerRemote.retrieveStaffByUsername(username);
                    doDeleteStaff(staffEntity);
                    
                    } catch (StaffNotFoundException ex) {
                    System.out.println("Staff cannot be found!") ; 
                }
                } else if (response == 5) {
                    doViewAllStaff();
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

    private void doCreateNewStaff() {
        Scanner scanner = new Scanner(System.in);
        StaffEntity newStaffEntity = new StaffEntity();

        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Add Staff ***\n");
        System.out.print("Enter First Name> ");
        newStaffEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newStaffEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Username> ");
        newStaffEntity.setUserName(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newStaffEntity.setPassword(scanner.nextLine().trim());

        try {
            staffEntityControllerRemote.retrieveStaffByUsername(newStaffEntity.getUserName());
            System.out.println("Staff already exists and cannot be created again!\n");
        } catch (StaffNotFoundException ex) {
            staffEntityControllerRemote.createNewStaff(newStaffEntity);
            System.out.println("Staff has been created successfully!\n");
        }
    }

    private void doViewStaffDetails() {
        System.out.println("*** ILS :: Administration Operation :: Staff Management :: View Staff Details ***\n");
        System.out.print("Enter Staff Id> ");
        Scanner scanner = new Scanner(System.in);
        Long staffId = scanner.nextLong();

        try {
            StaffEntity staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
            System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");
            System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());

        } catch (StaffNotFoundException ex) {
            System.out.println("Staff cannot be found!\n");
        }

    }

    private void doUpdateStaff(StaffEntity staffEntity) {

        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Update Staff ***\n");

        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            staffEntity.setFirstName(input);
        }

        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            staffEntity.setLastName(input);
        }
        

        System.out.print("Enter Username (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            staffEntity.setUserName(input);
        }

        System.out.print("Enter Password (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            staffEntity.setPassword(input);
        }

        staffEntityControllerRemote.updateStaff(staffEntity);
        System.out.println("Staff updated successfully!\n");
    }

    private void doDeleteStaff(StaffEntity staffEntity) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** ILS :: Administration Operation :: Staff Management :: Staff Book ***\n");
        System.out.printf("Confirm Delete Staff %s %s (Staff ID: %d) (Enter 'Y' to Delete)> ", staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getStaffId());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                if (currentStaffEntity.getStaffId().equals(staffEntity.getStaffId())) {
                    System.out.println("Staff is currently logged in and cannot be deleted!") ; 
                } else {
                staffEntityControllerRemote.deleteStaff(staffEntity.getStaffId()) ; 
                System.out.println("Staff deleted successfully!\n");
                }
            } catch (StaffNotFoundException ex) {
                System.out.println("An error has occurred while deleting staff: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Staff NOT deleted!\n");
        }
    }

    private void doViewAllStaff() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** ILS :: Administration Operation :: Staff Management :: View All Staff ***\n");

        List<StaffEntity> staffEntities = staffEntityControllerRemote.retrieveAllStaff();
        System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", "Staff Id", "First Name", "Last Name", "Username", "Password");

        for (StaffEntity staffEntity : staffEntities) {
            System.out.printf("%-20s%-20s%-20s%-20s%-20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getUserName(), staffEntity.getPassword());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

}
