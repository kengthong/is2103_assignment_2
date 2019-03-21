/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

/**
 *
 * @author hiixdayah
 */
public class LibraryOperationModule {
    
}

doLendBook() 
memberEntityController.retrieveMemberByMemberId(member identityNumber) (check whether member exist)
bookEntityController.retrieveBookByBookId(bookId ) (check whether book exist)
lendingEntityController.checkIsBookLent(bookId) (boolean - return false if book is available) 
(SELECT DISTINCT b
FROM BookEntity b, IN (b.lending) l
WHERE l.bookId = :bookId and l.available = false)

(Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.book.bookId = :inBookId") ; 
        query.setParameter("inBookId",bookId) ; 

        if ( query.getResultList().isEmpty() ) {
            return false ;
        } else {
            return true ;
        } ) 

lendingEntityController.checkNumBooksLoaned(member identityNumber) (return number of books loaned, if = 3, cannot loan out)
fineEntityController.checkForFines(member identityNumber) (boolean - return true if member has fine, l.memberId = :memberId and f.paid = false)


viewLentBooks() 
memberEntityController.retrieveMemberByMemberId(member identityNumber) (check whether member exist)
return lendingEntityController.retrieveBookLoanedByMember(member identityNumber) (return list of lendings)

doReturnBooks()
memberEntityController.retrieveMemberByMemberId(member identityNumber) (check whether member exist)
lendingEntityController.retrieveBookLoanedByMember(member identityNumber) (return list of lendings)
lendingEntityController.setBookAvailable() (l.available = true)

doExtendBook() 
memberEntityController.retrieveMemberByMemberId(member identityNumber) (check whether member exist)
lendingEntityController.retrieveBookLoanedByMember(member identityNumber) (return list of lendings)
reservationEntityController.checkForReservation(bookId) (return true if book is reserved)
lendingEntityController.extendDueDate(lendId) 
lendingEntityController.retrieveLendingEntityByLendId(lendId) (print lendentity.duedate())

doPayFines()
memberEntityController.retrieveMemberByMemberId(member identityNumber) (check whether member exist)
fineEntityController.retrieveFinesByMember (member identityNumber) (return list of fines)
fineEntityController.payFine(fineId) (set f.paid = true)

manageReservations() 
reservationEntityController.retrieveAllReservations(bookId)
reservationEntityController.deleteReservation(bookId) 






