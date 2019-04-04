/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.MemberEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local(LibraryOperationControllerLocal.class)
@Remote(LibraryOperationControllerRemote.class)
@Stateful
public class LibraryOperationController implements LibraryOperationControllerRemote, LibraryOperationControllerLocal {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal ; 
    
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal ; 
    
    
    private StaffEntity currentActiveStaff ; 
    
    public LibraryOperationController() {
       
    }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException {
     try {
             return this.currentActiveStaff = staffEntityControllerLocal.staffLogin(username, password);
        } catch (InvalidLoginException ex) {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

    @Override
    public StaffEntity getActiveStaff() {
        return this.currentActiveStaff ; 
    }

    @Override
    public void doLendBook() {
        
    }

    @Override
    public List<LendingEntity> viewLentBooks(String identityNumber)  {
        System.out.print("Currently Lent Books:") ; 
     
        
        try { 
        MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber);
        return memberEntity.getLendings() ; 
        //System.out.printf("Id", "|", "Title", "Due Date") ; 
        //System.out.printf("%-5s%",  ) ; 
        } catch (MemberNotFoundException ex) {
            System.out.println("Member Identity Number " + identityNumber + "does not exist!") ; 
            return null ; 
            
        }
            
              
                
    }

    @Override
    public void doReturnBook() {
    }

    @Override
    public void doExtendBook() {
    }

    @Override
    public void doPayFines() {
    }

    @Override
    public void doManageReservations() {
    }
    

  
    
}
