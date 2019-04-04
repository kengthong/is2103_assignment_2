/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.BookIsOnLoanException;
import util.exception.LendingNotFoundException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberNotAtTopOfReserveList;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface LendingEntityControllerRemote {
    
    public void checkIsBookLent(Long bookId) throws BookIsOnLoanException ;
    public void checkIfMemberExceedsMaxLoans(String identityNumber) throws MaxLoansExceeded;
    public void setBookAvailable(String identityNumber, Long returnBookId) ;
    public List<LendingEntity> retrieveBooksLoanedByMember(String identityNumber) ; 
    public Date generateDueDate(Date date); 
    public LendingEntity retrieveLendingByBookId(Long bookId);  

    LendingEntity createNewLending(LendingEntity newLendingEntity);

    LendingEntity retrieveLendingByLendingId(Long lendId) throws LendingNotFoundException;

    void updateLendingEntity(LendingEntity lendingEntity);

    void deleteLendingEntity(Long lendId) throws LendingNotFoundException;

    void checkIfMemberOnReserveList(String identityNumber) throws MemberNotAtTopOfReserveList;

    


    
}
