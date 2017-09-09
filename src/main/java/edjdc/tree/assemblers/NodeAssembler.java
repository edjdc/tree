package edjdc.tree.assemblers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edjdc.tree.dto.CreateNodeDTO;
import edjdc.tree.dto.NodeDTO;
import edjdc.tree.dto.RootNodeDTO;
import edjdc.tree.dto.UpdateNodeDTO;
import edjdc.tree.exception.NodeNotFoundException;
import edjdc.tree.model.Node;
import edjdc.tree.service.NodeService;

/**
 * Assembler utilizado para converter os dados da entidade Node para DTOs.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
@Component
public class NodeAssembler {

	@Autowired
	private NodeService nodeService;

	public Node toNode(CreateNodeDTO createNodeDTO) {
		Node node = new Node();
		node.setCode(createNodeDTO.getCode());
		node.setDescription(createNodeDTO.getDescription());
		node.setDetail(createNodeDTO.getDetail());

		if (createNodeDTO.getParentId() != null) {
			Node parent = nodeService.getNode(createNodeDTO.getParentId());
			if (parent == null) {
				throw new NodeNotFoundException();
			}
			node.setParent(parent);
		}

		return node;
	}

	public Node toNode(UpdateNodeDTO updateNodeDTO) {
		Node node = new Node();
		node.setId(updateNodeDTO.getId());
		node.setCode(updateNodeDTO.getCode());
		node.setDescription(updateNodeDTO.getDescription());
		node.setDetail(updateNodeDTO.getDetail());

		if (updateNodeDTO.getParentId() != null) {
			Node parent = nodeService.getNode(updateNodeDTO.getParentId());
			if (parent == null) {
				throw new NodeNotFoundException();
			}
			node.setParent(parent);
		}

		return node;
	}

	public UpdateNodeDTO toUpdateNodeDTO(Node node) {
		UpdateNodeDTO updateNodeDTO = new UpdateNodeDTO();
		updateNodeDTO.setId(node.getId());
		updateNodeDTO.setDescription(node.getDescription());
		updateNodeDTO.setDetail(node.getDetail());
		updateNodeDTO.setCode(node.getCode());

		if (node.getParent() != null) {
			updateNodeDTO.setParentId(node.getParent().getId());
		}

		return updateNodeDTO;
	}

	public NodeDTO toNodeDTO(Node node) {
		NodeDTO nodeDTO = new NodeDTO();
		nodeDTO.setId(node.getId());
		nodeDTO.setCode(node.getCode());
		nodeDTO.setDescription(node.getDescription());
		nodeDTO.setDetail(node.getDetail());

		boolean hasChildren = !node.getChildren().isEmpty();
		nodeDTO.setHasChildren(hasChildren);

		if (node.getParent() != null) {
			nodeDTO.setParentId(node.getParent().getId());
		}

		return nodeDTO;
	}

	public RootNodeDTO toRooNodeDTO(Node node) {
		RootNodeDTO rootNodeDTO = new RootNodeDTO();
		rootNodeDTO.setId(node.getId());
		rootNodeDTO.setCode(node.getCode());
		rootNodeDTO.setDescription(node.getDescription());
		rootNodeDTO.setDetail(node.getDetail());

		if (node.getParent() != null) {
			rootNodeDTO.setParentId(node.getParent().getId());
		}

		if (!node.getChildren().isEmpty()) {
			List<RootNodeDTO> children = new ArrayList<>();
			node.getChildren().forEach(n -> children.add(toRooNodeDTO(n)));
			rootNodeDTO.setChildren(children);
		}

		return rootNodeDTO;
	}

}
