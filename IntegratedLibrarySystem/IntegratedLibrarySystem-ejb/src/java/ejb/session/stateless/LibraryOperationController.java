/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local(LibraryOperationControllerLocal.class)
@Remote(LibraryOperationControllerRemote.class)
@Stateless
public class LibraryOperationController implements LibraryOperationControllerRemote, LibraryOperationControllerLocal {

    @EJB
    private LendingEntityControllerLocal lendingEntityControllerLocal;

    @EJB
    private ReservationControllerLocal reservationControllerLocal;

    @EJB
    private FineControllerLocal fineControllerLocal;

    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;

    public LibraryOperationController() {

    }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException{
        try{
            StaffEntity retrievedStaffEntity = staffEntityControllerLocal.staffLogin(username, password);
            return retrievedStaffEntity;
        } catch (InvalidLoginException ex){
            throw ex;
        }
    }

    @Override
    public LendingEntity doLendBook(String identityNumber, Long bookId) throws BookNotFoundException, MemberNotFoundException, BookIsOnLoanException, MemberHasFinesException, MaxLoansExceeded, MemberNotAtTopOfReserveList {

        Date date = new Date();

        try {
            BookEntity bookEntity = bookEntityControllerLocal.retrieveBookByBookId(bookId);
            MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber);
            LendingEntity newLendingEntity = new LendingEntity();

            lendingEntityControllerLocal.checkIsBookLent(bookId);
            fineControllerLocal.checkIfMemberHasFines(identityNumber);
            lendingEntityControllerLocal.checkIfMemberExceedsMaxLoans(identityNumber);
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                lendingEntityControllerLocal.checkIfMemberOnReserveList(identityNumber);
            }

            newLendingEntity.setMember(memberEntity);
            newLendingEntity.setBook(bookEntity);
            Date duedate = lendingEntityControllerLocal.generateDueDate(date);
            newLendingEntity.setLendDate(date);
            newLendingEntity.setDueDate(duedate);
            newLendingEntity.setHasReturned(false);
            newLendingEntity = lendingEntityControllerLocal.createNewLending(newLendingEntity);
            
            return newLendingEntity;
            
        } catch(
                BookNotFoundException | 
                MemberNotFoundException | 
                BookIsOnLoanException | 
                MemberHasFinesException |
                MaxLoansExceeded |
                MemberNotAtTopOfReserveList ex
            ) {
            throw ex;
        }
    }

    @Override
    public LendingEntity doExtendBook(String identityNumber, Long bookId) throws MemberNotAtTopOfReserveList, MemberHasFinesException, BookIsAlreadyOverdueException {

//        If the book is already overdue
//        o Member has unpaid fines
//        o The book is reserved by another member    

        try {
            //retrieve LEnding Entity
            LendingEntity currentLendingEntity = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);
            
            //Compare due date with current date
            Date dueDate = currentLendingEntity.getDueDate();
            lendingEntityControllerLocal.checkIsBookOverdue(dueDate);
            
            //check for fines
            fineControllerLocal.checkIfMemberHasFines(identityNumber);
            //check for reserve
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                lendingEntityControllerLocal.checkIfMemberOnReserveList(identityNumber);
            }
            //extend book
            Long lendId = currentLendingEntity.getLendId();
            LendingEntity updatedLendingEntity = lendingEntityControllerLocal.extendBook(lendId);
            //return lending entity
            return updatedLendingEntity;
            
        } catch (BookIsAlreadyOverdueException 
                | MemberHasFinesException 
                | MemberNotAtTopOfReserveList ex){
            throw ex;
        }
    }
    


    @Override
    public void viewReservations() {
            System.out.print("Enter Book ID>\n");
        Scanner scanner = new Scanner(System.in);
        Long bookId = scanner.nextLong() ; 
    
        try {
        BookEntity bookEntity = bookEntityControllerLocal.retrieveBookByBookId(bookId) ;        
        
        List <ReservationEntity> reservationEntities = reservationControllerLocal.retrieveAllReservationsByBookId(bookId) ;
        if(!reservationEntities.isEmpty()) {
            for (ReservationEntity reservationEntity : reservationEntities) {
            Long reservationId = reservationEntity.getReservationId() ; 
            Long memberId = reservationEntity.getMember().getMemberId() ; 
            System.out.println(reservationId + "\t|" + memberId) ;    
            }
        }
        System.out.println() ;
        } catch (BookNotFoundException ex){
                System.out.println("Book cannot be found!") ; 
        }
        
        System.out.println() ; 
        

        } 

    @Override
    public void deleteReservation(Long bookId, String identityNumber) throws MemberNotFoundException, ReservationNotFoundException {
        try {
        MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber); 
        ReservationEntity reservationToDelete = reservationControllerLocal.retrieveReservationOfMember(bookId,memberEntity.getMemberId()) ;
        reservationControllerLocal.deleteReservation(reservationToDelete); 
        
        } catch (MemberNotFoundException | ReservationNotFoundException ex) {
           throw ex ;
        }
    }
    
    
    
   
    
    @Override
    public LendingEntity doExtendBook(String identityNumber, Long bookId) throws MemberNotAtTopOfReserveList, BookIsAlreadyOverdueException, MemberHasFinesException {
//        If the book is already overdue
//        o Member has unpaid fines
//        o The book is reserved by another member    

        try {
            //retrieve LEnding Entity
            LendingEntity currentLendingEntity = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);
            
            //Compare due date with current date
            Date dueDate = currentLendingEntity.getDueDate();
            lendingEntityControllerLocal.checkIsBookOverdue(dueDate);
            
            //check for fines
            fineControllerLocal.checkIfMemberHasFines(identityNumber);
            //check for reserve
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                lendingEntityControllerLocal.checkIfMemberOnReserveList(identityNumber);
            }
            //extend book
            Long lendId = currentLendingEntity.getLendId();
            LendingEntity updatedLendingEntity = lendingEntityControllerLocal.extendBook(lendId);
            //return lending entity
            return updatedLendingEntity;
            
        } catch (BookIsAlreadyOverdueException 
                | MemberHasFinesException 
                | MemberNotAtTopOfReserveList ex){
            throw ex;
        }
    }
    

}
