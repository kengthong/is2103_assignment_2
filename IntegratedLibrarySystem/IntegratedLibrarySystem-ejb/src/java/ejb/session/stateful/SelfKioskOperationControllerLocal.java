/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import util.exception.InvalidLoginException;

public interface SelfKioskOperationControllerLocal {

    void doMemberLogin(String username, String password) throws InvalidLoginException;
    
}
