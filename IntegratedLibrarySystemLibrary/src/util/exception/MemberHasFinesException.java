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
public class MemberHasFinesException extends Exception {

    public MemberHasFinesException() {
    }
    
    public MemberHasFinesException(String msg) {
        super(msg);
    }
}
