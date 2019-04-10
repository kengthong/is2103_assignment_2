/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.FineControllerRemote;
import ejb.session.stateless.LendingEntityControllerRemote;
import ejb.session.stateless.LibraryOperationControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.ReservationControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import javax.ejb.EJB;
import util.exception.MemberNotFoundException;
/**
 *
 * @author hiixdayah
 */
public class Main {

    @EJB
    private static LibraryOperationControllerRemote libraryOperationControllerRemote;

    @EJB
    private static BookEntityControllerRemote bookEntityControllerRemote;
    
    @EJB 
    private static LendingEntityControllerRemote lendingEntityControllerRemote;
    
    @EJB
    private static MemberEntityControllerRemote memberEntityControllerRemote;
    
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    
    @EJB
    private static FineControllerRemote fineControllerRemote;
    
    @EJB 
    private static ReservationControllerRemote reservationControllerRemote;
    
    
  

    
    public static void main(String[] args) throws MemberNotFoundException  {
         MainApp mainApp = new MainApp(libraryOperationControllerRemote, bookEntityControllerRemote, lendingEntityControllerRemote, memberEntityControllerRemote, staffEntityControllerRemote, fineControllerRemote, reservationControllerRemote);
         mainApp.runApp();
    }    
}
