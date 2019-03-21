package dao;

import entity.BookEntity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import util.exception.BookNotFoundException;


/**
 *
 * @author Hidayah
 */
public class BookEntityManager {

    
    public BookEntityManager() {  
    }
    
    public BookEntity createBook(BookEntity newBookEntity, EntityManager entityManager) {
        entityManager.persist(newBookEntity) ;
        entityManager.flush() ; 
        
        return newBookEntity ; 
        
    }
    
    public BookEntity returnBook(String id, EntityManager entityManager) {
         Query query = entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.bookId = :inbookId");
        query.setParameter("inbookId", id);
        return (BookEntity) query.getSingleResult() ;
    }
    
     public BookEntity retrieveBook(Long id, EntityManager entityManager) throws BookNotFoundException {
     
         BookEntity bookEntity = entityManager.find(BookEntity.class, id);
        
        if(bookEntity != null)
        {
            return bookEntity;
        }
        else
        {
            throw new BookNotFoundException("Book ID " + id + " does not exist!");
        }               
    }
    }



