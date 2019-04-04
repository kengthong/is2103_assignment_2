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
import javax.ejb.EJB;

/**
 *
 * @author kengthong
 */
public class Main {

    @EJB
    private static LendingEntityControllerRemote lendingEntityController;

    @EJB
    private static MemberEntityControllerRemote memberEntityController;

    @EJB
    private static BookEntityControllerRemote bookEntityController;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(
            bookEntityController,
            memberEntityController,
            lendingEntityController
        );
        mainApp.run();
    }
    
}
