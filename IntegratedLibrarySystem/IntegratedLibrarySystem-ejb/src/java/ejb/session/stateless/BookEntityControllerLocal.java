/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import javax.ejb.Local;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface BookEntityControllerLocal {

    BookEntity retrieveBookByBookId(Integer bookId) throws BookNotFoundException;
    
}
