/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfkioskclient;

import ejb.session.stateful.SelfKioskOperationControllerRemote;
import ejb.session.stateless.BookEntityControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author kengthong
 */
public class Main {

    @EJB
    private static BookEntityControllerRemote bookEntityController;

    @EJB
    private static SelfKioskOperationControllerRemote selfKioskOperationController;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(
            selfKioskOperationController,
            bookEntityController
        );
        mainApp.run();
    }
    
}
