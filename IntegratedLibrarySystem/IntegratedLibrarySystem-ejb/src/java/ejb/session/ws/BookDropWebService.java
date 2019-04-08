/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.FineControllerLocal;
import ejb.session.stateless.LendingEntityControllerLocal;
import ejb.session.stateless.LibraryOperationControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.BookHasBeenReservedException;
import util.exception.BookIsAlreadyLoanedByMemberException;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsAvailableForLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.LendingNotFoundException;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotFoundException;
import util.exception.MultipleReservationException;

/**
 *
 * @author Sing Jie
 */
@WebService(serviceName = "BookDropWebService")
@Stateless()
public class BookDropWebService {

    @EJB
    private FineControllerLocal fineControllerLocal;
    @EJB
    private LibraryOperationControllerLocal libraryOperationControllerLocal;
    @EJB
    private LendingEntityControllerLocal lendingEntityControllerLocal;
    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    
    
    
    
    @WebMethod(operationName = "memberLogin")
    public MemberEntity memberLogin(@WebParam(name = "identityNumber") String identityNumber, 
                                     @WebParam(name = "securityCode") String securityCode)
                                     throws InvalidLoginException { 
        MemberEntity memberEntity = memberEntityControllerLocal.doMemberLogin(identityNumber, securityCode);
        return memberEntity ; 
    }
    
    @WebMethod(operationName = "retrieveBooksLoanedByMember")
    public List<LendingEntity> retrieveBooksLoanedByMember(@WebParam(name = "identityNumber") String identityNumber)
                                     throws LendingNotFoundException { 
        List<LendingEntity> lendingEntity = lendingEntityControllerLocal.retrieveBooksLoanedByMember(identityNumber);
        return lendingEntity ; 
    }
    
    @WebMethod(operationName = "doReturnBook")
    public void doReturnBook(@WebParam(name = "bookId") Long bookId, 
                                     @WebParam(name = "memberId") Long memberId)
                                     throws LendingNotFoundException, MemberNotFoundException { 
        
        libraryOperationControllerLocal.doReturnBook(bookId, memberId);
    }
    
    @WebMethod(operationName = "doExtendBook")
    public LendingEntity doExtendBook(@WebParam(name = "identityNumber") String identityNumber, 
                                     @WebParam(name = "bookId") Long bookId)
                                     throws LendingNotFoundException, BookHasBeenReservedException, MemberHasFinesException, BookIsAlreadyOverdueException { 
        LendingEntity lendingEntity = libraryOperationControllerLocal.doExtendBook(identityNumber, bookId);
        return lendingEntity ; 
    }
    
    @WebMethod(operationName = "searchBookToReserve")
    public List<Object[]> searchBookToReserve(@WebParam(name = "titleToSearch") String titleToSearch)
    { 
        List<Object[]> books = libraryOperationControllerLocal.searchBookToReserve(titleToSearch);
        return books ; 
    }
    
    @WebMethod(operationName = "doReserveBook")
    public void doReserveBook(@WebParam(name = "currentMember") MemberEntity currentMember, 
                                     @WebParam(name = "bookId") Long bookId)
                                     throws BookIsAlreadyLoanedByMemberException, BookIsAvailableForLoanException, MultipleReservationException, MemberHasFinesException, BookNotFoundException { 
        libraryOperationControllerLocal.doReserveBook(currentMember, bookId); 
    }
    
    @WebMethod(operationName = "retrieveFineByFineId")
    public FineEntity retrieveFineByFineId(@WebParam(name = "fineIdToPay") Long fineIdToPay) {
        FineEntity fineEntity = fineControllerLocal.retrieveFineByFineId(fineIdToPay) ; 
        return fineEntity ; 
    }
    
    @WebMethod(operationName = "retrieveFinesByMember") 
    public List<FineEntity> retrieveFinesByMember(@WebParam(name = "identityNumber") String identityNumber) {
        List<FineEntity> fineEntities = fineControllerLocal.retrieveFinesByMember(identityNumber) ; 
        return fineEntities ; 
    }
    
    @WebMethod(operationName = "retrieveMemberByIdentityNumber")
    public MemberEntity retrieveMemberByIdentityNumber(@WebParam(name = "identityNumber") String identityNumber) 
                                                       throws MemberNotFoundException {
            MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber) ;
            return memberEntity ; 
    }
    
}
