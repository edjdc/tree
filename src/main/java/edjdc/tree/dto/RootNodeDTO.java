package edjdc.tree.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO utilizado para armazenar dados para exibição de um nó raiz.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
public class RootNodeDTO implements Serializable {

	private static final long serialVersionUID = -8079205775903803088L;
	
	private Long id;
	private String code;
	private String description;
	private String detail;
	private Long parentId;
	private List<RootNodeDTO> children;

	public RootNodeDTO() {

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

	public List<RootNodeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<RootNodeDTO> children) {
		this.children = children;
	}

}
