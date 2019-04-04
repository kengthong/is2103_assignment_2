/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import javax.ejb.Remote;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface LibraryOperationControllerRemote {

    StaffEntity staffLogin(String username, String password);
    
}
