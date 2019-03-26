/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateful.SelfKioskOperationControllerRemote;
import java.util.Scanner;
import util.exception.InvalidLoginException;

/**
 *
 * @author kengthong
 */
public class MainApp {

    private SelfKioskOperationControllerRemote selfKioskOperationController;
    
    public MainApp(SelfKioskOperationControllerRemote selfKioskOperationController) {
        this.selfKioskOperationController = selfKioskOperationController;
    }
    
    
    public void run()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Welcome to Self-Service Kiosk *** \n") ; 
        System.out.println("1: Login") ; 
        System.out.println("2: Exit\n") ; 
        
        while( response <1 || response >2)
        {
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
}
