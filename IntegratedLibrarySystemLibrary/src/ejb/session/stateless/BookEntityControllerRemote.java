/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import javax.ejb.Remote;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface BookEntityControllerRemote {

    BookEntity getBook(Integer bookId) throws BookNotFoundException;
    
}
