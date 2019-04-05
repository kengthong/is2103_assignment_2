/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import entity.StaffEntity;
import javax.ejb.Local;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface LibraryOperationControllerLocal {

    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;

    LendingEntity doLendBook(String identityNumber, Long bookId) throws BookNotFoundException, MemberNotFoundException, BookIsOnLoanException, MemberHasFinesException, MaxLoansExceeded, MemberNotAtTopOfReserveList;

    LendingEntity doExtendBook(String identityNumber, Long bookId) throws MemberNotAtTopOfReserveList, BookIsAlreadyOverdueException, MemberHasFinesException;
    
}
