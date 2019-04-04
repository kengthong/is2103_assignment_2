/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */

@Stateless
@Local(BookEntityControllerLocal.class)
@Remote(BookEntityControllerRemote.class)
public class BookEntityController implements BookEntityControllerRemote, BookEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2New-ejbPU")
    private EntityManager entityManager;
    
    public BookEntityController()
    {
    }
    
    @Override
    public BookEntity createNewBook(BookEntity newBookEntity)  {
        entityManager.persist(newBookEntity);
        entityManager.flush();
        
        return newBookEntity;
    }
    
    @Override 
    public BookEntity retrieveBookByIsbn(String isbn) throws BookNotFoundException {
       Query query = entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.isbn = :inIsbn");
        query.setParameter("inIsbn", isbn);
        
        try
        {
            return (BookEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
             throw new BookNotFoundException("Book Isbn " + isbn + " does not exist!");

        }
    }
    
    @Override
    public BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntity = entityManager.find(BookEntity.class, bookId);
        
        if(bookEntity != null)
        {
            return bookEntity;
        }
        else
        {
            throw new BookNotFoundException("Book ID " + bookId + " does not exist!");
        }
    }
    
    @Override
    public void updateBook(BookEntity bookEntity) {
        entityManager.merge(bookEntity);
    }
    
    @Override
    public void deleteBook(Long bookId) throws BookNotFoundException
    {
        BookEntity bookEntityToRemove = retrieveBookByBookId(bookId);
        entityManager.remove(bookEntityToRemove);
    }
    
    @Override
    public List<BookEntity> retrieveAllBooks()
    {
        Query query = entityManager.createQuery("SELECT b FROM BookEntity b");
        
        return query.getResultList();
    }    

    public void persist(Object object) {
        entityManager.persist(object);
    }
}
