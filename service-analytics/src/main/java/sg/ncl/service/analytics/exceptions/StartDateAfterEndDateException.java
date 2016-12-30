package sg.ncl.service.analytics.exceptions;


import sg.ncl.common.exception.base.BadRequestException;

/**
 * @author: Tran Ly Vu
 * @version: 1.0
 */
public class StartDateAfterEndDateException extends BadRequestException{

    public StartDateAfterEndDateException() {super("Start date is after end date");}
}
