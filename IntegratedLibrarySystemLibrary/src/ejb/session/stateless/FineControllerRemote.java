/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.MemberHasFinesException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface FineControllerRemote {
    
       public void checkIfMemberHasFines(String identityNumber) throws MemberHasFinesException ;
       public List<FineEntity> retrieveFinesByMember(String identityNumber) ; 
       public void payFine(Long fineId) ; 

}
