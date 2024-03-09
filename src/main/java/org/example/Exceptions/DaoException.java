package org.example.Exceptions;

import java.sql.SQLException;

/**
 *  Main author: Caitlin Maguire
 */
public class DaoException extends SQLException {

    public DaoException(){

    }

    public DaoException(String aMessage){
        super(aMessage);
    }
}
