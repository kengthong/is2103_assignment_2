/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface ReservationControllerLocal {

    public List<ReservationEntity> retrieveAllReservations();

    public boolean checkForReservation(Long bookId);

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;

    List<ReservationEntity> retrieveAllReservationsByBookId(Long bookId);

}
