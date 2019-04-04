/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 *
 * @author hiixdayah
 */
@Local(LibraryOperationControllerLocal.class)
@Remote(LibraryOperationControllerRemote.class)
@Stateless
public class LibraryOperationController implements LibraryOperationControllerRemote, LibraryOperationControllerLocal {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal ; 
    
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal ; 
    
    
        public LibraryOperationController() {
       
    }

    @Override
    public StaffEntity staffLogin(String username, String password) {
        return null;
    }
        
        
        
        
}
