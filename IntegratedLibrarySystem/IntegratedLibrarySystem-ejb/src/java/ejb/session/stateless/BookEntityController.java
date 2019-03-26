/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import javax.ejb.Stateless;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Stateless
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @Override
    public BookEntity getBook(Integer bookId) throws BookNotFoundException {
        
        return null;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
