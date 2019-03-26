/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.MemberEntityControllerLocal;
import entity.MemberEntity;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Local;
import javax.ejb.Remote;
import util.exception.InvalidLoginException;


/**
 *
 * @author kengthong
 */
@Local (SelfKioskOperationControllerLocal.class)
@Remote (SelfKioskOperationControllerRemote.class)
@Stateful
public class SelfKioskOperationController implements SelfKioskOperationControllerRemote, SelfKioskOperationControllerLocal {

    @EJB
    private MemberEntityControllerLocal memberEntityController;

    private MemberEntity currentActiveMember;
    @Override
    public void doMemberLogin(String username, String password) throws InvalidLoginException {
        try
        {
            this.currentActiveMember = memberEntityController.doMemberLogin(username, password);
        }
        catch (InvalidLoginException ex)
        {
            throw ex;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public MemberEntity getCurrentActiveMember() {
        return this.currentActiveMember;
    }
    
    
}
