/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.LendingEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface LibraryOperationControllerLocal {

    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;

    StaffEntity getActiveStaff();

    void doLendBook();

    List<LendingEntity> viewLentBooks(String identityNumber);

    void doReturnBook();

    void doExtendBook();

    void doPayFines();

    void doManageReservations();
    
}
