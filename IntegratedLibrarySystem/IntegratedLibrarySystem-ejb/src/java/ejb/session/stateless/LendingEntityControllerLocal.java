/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.LendingNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface LendingEntityControllerLocal {
    
    public boolean checkIsBookLent(Long bookId) ;
    public int checkNumBooksLoaned(String identityNumber) ; 
    public void setBookAvailable(String identityNumber, Long returnBookId) ;
    public List<LendingEntity> retrieveBooksLoanedByMember(String identityNumber) ; 
    public Date generateDueDate(Date date); 
    public LendingEntity retrieveLendingByBookId(Long bookId);      

    LendingEntity createNewLending(LendingEntity newLendingEntity);

    LendingEntity retrieveLendingByLendingId(Long lendId) throws LendingNotFoundException;

    void updateLendingEntity(LendingEntity lendingEntity);

    void deleteLendingEntity(Long lendId) throws LendingNotFoundException;

    boolean checkForReservations(Long bookId);

    boolean checkIfMemberOnReserveList(String identityNumber);
    
    
}
