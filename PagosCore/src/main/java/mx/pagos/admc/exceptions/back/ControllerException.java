package mx.pagos.admc.exceptions.back;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

public class ControllerException {

	  @ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
	  @ExceptionHandler(DataIntegrityViolationException.class)
	  public void conflict() {
	  }
	  @ExceptionHandler({SQLException.class,DataAccessException.class})
	  public String databaseError() {
	    return "databaseError";
	  }
	  @ExceptionHandler(Exception.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception exception) {

	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", exception);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName("error");
	    return mav;
	  }
	  
}