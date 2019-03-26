/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.BookNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface BookEntityControllerLocal {

    BookEntity retrieveBookByBookId(Integer bookId) throws BookNotFoundException;
    
    public BookEntity createNewBook(BookEntity newBookEntity) ; 
    public void updateBook(BookEntity bookEntity) ; 
    public BookEntity retrieveBookByBookId(Long bookId) throws BookNotFoundException ; 
    public BookEntity retrieveBookByIsbn(String isbn) throws BookNotFoundException ; 
    public void deleteBook(Long bookId) throws BookNotFoundException ; 
    public List<BookEntity> retrieveAllBooks() ; 




    
}
