/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import javax.ejb.Local;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface MemberEntityControllerLocal {

    MemberEntity doMemberLogin(String username, String password) throws InvalidLoginException;

    MemberEntity retrieveMemberByUsername(String username) throws MemberNotFoundException;
    
}
