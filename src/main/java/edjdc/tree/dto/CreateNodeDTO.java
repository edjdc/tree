package edjdc.tree.dto;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * DTO utilizado para armazenar dados para criação de um Node.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
public class CreateNodeDTO implements Serializable {

	private static final long serialVersionUID = -379434860455840705L;

	@NotBlank
	@Size(max = 50)
	private String code;
	
	@Size(max = 255)
	private String description;
	
	@Size(max = 255)
	private String detail;
	
	private Long parentId;

	public CreateNodeDTO() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}