/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(ReservationControllerLocal.class)
@Remote(ReservationControllerRemote.class)
public class ReservationController implements ReservationControllerRemote, ReservationControllerLocal {
    
    
    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    public ReservationController()
    {
    }

}
