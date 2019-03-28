/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import entity.LendingEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface LendingEntityControllerRemote {
    
    public boolean checkIsBookLent(Long bookId) ;
    public int checkNumBooksLoaned(String identityNumber) ; 
    public void setBookAvailable(String identityNumber, Long returnBookId) ;
    public List<LendingEntity> retrieveBooksLoanedByMember(String identityNumber) ; 
    public String generateDueDate(Date date) ; 
    public void extendDueDate(String identityNumber, Long extendBookId) ; 
    public BookEntity retrieveBookByBookId(String bookId);  


    
}
