/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kengthong
 */
public class BookIsAlreadyOverdueException extends Exception {
    
        public BookIsAlreadyOverdueException() {
    }

    
    public BookIsAlreadyOverdueException(String msg) {
        super(msg);
    }
}
