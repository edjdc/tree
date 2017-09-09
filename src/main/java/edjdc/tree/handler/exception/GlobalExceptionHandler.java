package edjdc.tree.handler.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edjdc.tree.exception.ExceptionResponse;
import edjdc.tree.exception.MultipleRootNodeException;
import edjdc.tree.exception.NodeNotFoundException;

/**
 * Classe responsável por capturar exceptions e retornar o erro de forma amigável.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {


	@ExceptionHandler(value = MultipleRootNodeException.class)
	public ResponseEntity<ExceptionResponse> handlerMultipleRootNodeException(MultipleRootNodeException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage("Multiple root nodes");
        
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = NodeNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handlerNodeNotFoundException(NodeNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage("Node not found");

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
        
        List<String> validErrors = new ArrayList<String>();
        for (FieldError fieldError : result.getFieldErrors()) {
            validErrors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        
		ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage("Validation Error");
        response.setErrors(validErrors);
		
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = Exception.class)
	public void handleException(Exception ex) {
		
	}
}
