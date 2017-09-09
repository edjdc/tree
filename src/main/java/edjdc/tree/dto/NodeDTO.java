package edjdc.tree.dto;

import java.io.Serializable;

/**
 * DTO utilizado para armazenar dados para a exibição de um Node.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
public class NodeDTO implements Serializable {

	private static final long serialVersionUID = -3519352959074018688L;
	
	private Long id;
	private String code;
	private String description;
	private String detail;
	private Long parentId;
	private Boolean hasChildren;

	public NodeDTO() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

}
