/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateful.SelfKioskOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import java.util.Scanner;
import util.exception.InvalidLoginException;

/**
 *
 * @author kengthong
 */
public class MainApp {

    private SelfKioskOperationControllerRemote selfKioskOperationController;
    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private MemberOperationModule memberOperationModule;
    private LendingEntityControllerRemote lendingEntityControllerRemote;
    
    public MainApp(
            SelfKioskOperationControllerRemote selfKioskOperationController,
            BookEntityControllerRemote bookEntityControllerRemote,
            MemberEntityControllerRemote memberEntityControllerRemote,
            LendingEntityControllerRemote lendingEntityControllerRemote
    ) {
        this.selfKioskOperationController = selfKioskOperationController;
        this.bookEntityControllerRemote = bookEntityControllerRemote;
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.memberOperationModule = new MemberOperationModule(
                this.selfKioskOperationController,
                this.bookEntityControllerRemote,
                this.lendingEntityControllerRemote
                
        );
    }
    
    
    public void run()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Welcome to Self-Service Kiosk *** \n") ; 
        
        
        while( response <1 || response >2)
        {
            System.out.println("1: Login") ; 
            System.out.println("2: Exit\n") ; 
            System.out.print("> ");
            response = sc.nextInt();
            
            if(response == 1)
                {
                    //register
                }
            else if(response == 2)
                {
                    // do login
                    try 
                    {
                        doLogin();
                        memberOperationModule.displayMenu();
                    } 
                    catch (InvalidLoginException ex)
                    {
                    }
                }
            else if(response == 3)
                {
                    break;
                }
        }
    }
    
    private void doLogin() throws InvalidLoginException
    {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("*** Self-Service Kiosk :: Login ***\n");
        
    
        while(true)
        {
            System.out.print("Enter username> ");
            username = sc.nextLine().trim();
            System.out.print("Enter password> ");
            password = sc.nextLine().trim();
            
            if(username.length() > 0 && password.length() > 0)
            {
                try
                {
                    this.selfKioskOperationController.doMemberLogin(username, password);
                    System.out.println("Login successful!");
                }
                catch (InvalidLoginException ex)
                {
                    System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    throw new InvalidLoginException();
                }
            }
        }
    }
    
    private void doRegisterMember()
    {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Staff ***\n");
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
        System.out.print("Enter Address> ");
        newMemberEntity.setAddress(scanner.nextLine().trim());
        
        
        newMemberEntity = memberEntityControllerRemote.createNewMember(newMemberEntity);
        System.out.println("You have been registered successfully!\n");
    }
}
