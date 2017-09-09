package edjdc.tree.exception;

import java.io.Serializable;
import java.util.List;

/**
 * Classe responsável por retornar informações sobre exceptions.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = -3219910235400832037L;

	private String errorMessage;
	private List<String> errors;

	public ExceptionResponse() {

	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
