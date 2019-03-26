/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import java.util.List;
import javax.ejb.Local;

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
    public String generateDueDate() ; 
    public void extendDueDate(String identityNumber, Long extendBookId) ; 
    
    
    
}
