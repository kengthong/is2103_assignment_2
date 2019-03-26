/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Stateless
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private EntityManager em;
    
    @Override
    public BookEntity retrieveBookByBookId(Integer bookId) throws BookNotFoundException {
        
        BookEntity bookEntity = em.find(BookEntity.class, bookId);
        
        if(bookEntity != null)
        {
            return bookEntity;
        }
        else
        {
            throw new BookNotFoundException("Product ID " + bookId + " does not exist!");
        }          
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }
    
}
