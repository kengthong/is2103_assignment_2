/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.StaffEntity;
import java.util.Scanner;


/**
 *
 * @author hiixdayah
 */
public class AdministrationOperationModule {
    
    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private StaffEntity currentStaffEntity;
        

  public AdministrationOperationModule() {  
  }
  
  public AdministrationOperationModule(MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, StaffEntity currentStaffEntity) {  
      this() ;
      this.memberEntityControllerRemote = memberEntityControllerRemote;
      this.bookEntityControllerRemote = bookEntityControllerRemote;
      this.staffEntityControllerRemote = staffEntityControllerRemote;
      this.currentStaffEntity = currentStaffEntity;
  }
  
  public void menuAdministrationOperation() {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
    
      
}
