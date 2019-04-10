/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.MemberEntity;
import javax.ejb.Remote;
import util.exception.InvalidLoginException;

/**
 *
 * @author kengthong
 */
@Remote
public interface SelfKioskOperationControllerRemote {

    void doMemberLogin(String username, String password) throws InvalidLoginException;

    MemberEntity getCurrentActiveMember();
    
}
