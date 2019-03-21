/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integratedlibrarysystemactualclient;

import ejb.session.stateful.LibraryOperationControllerRemote;
import javax.ejb.EJB;
import util.exception.BookNotFoundException;
import util.exception.MemberNotFoundException;


/**
 *
 * @author Hidayah
 */
public class Main {

    @EJB
    private static LibraryOperationControllerRemote libraryOperationControllerRemote;
    
    
    public static void main(String[] args) throws MemberNotFoundException, BookNotFoundException {
        MainApp mainApp = new MainApp(libraryOperationControllerRemote);
        mainApp.runApp();
    }    
}
