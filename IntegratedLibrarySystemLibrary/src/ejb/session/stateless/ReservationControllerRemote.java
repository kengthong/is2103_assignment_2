/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface ReservationControllerRemote {

    public List<ReservationEntity> retrieveAllReservations();

    public boolean checkForReservation(Long bookId);

    public void deleteReservation(Long reservationId) throws ReservationNotFoundException;

    List<ReservationEntity> retrieveAllReservationsByBookId(Long bookId);

}
