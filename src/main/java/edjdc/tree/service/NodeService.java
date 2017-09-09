package edjdc.tree.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edjdc.tree.exception.MultipleRootNodeException;
import edjdc.tree.exception.NodeNotFoundException;
import edjdc.tree.model.Node;
import edjdc.tree.repository.NodeRepository;

/**
 * Service responsável pela camada de negócios
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
@Service
public class NodeService {

	@Autowired
	private NodeRepository nodeRepository;

	@Transactional
	public void create(Node node) {
		boolean isRootNode = node.getParent() == null;
		if (isRootNode) {
			// only one root node
			Long count = nodeRepository.countByParentIsNull();
			if (count > 0) {
				throw new MultipleRootNodeException();
			}
		}
		nodeRepository.save(node);
	}

	@Transactional
	public void update(Node node) {
		boolean exists = nodeRepository.exists(node.getId());
		if (!exists) {
			throw new NodeNotFoundException();
		}
		boolean isRootNode = node.getParent() == null;
		if (isRootNode) {
			// only one root node
			Long count = nodeRepository.countByParentIsNullAndIdNot(node.getId());
			if (count > 0) {
				throw new MultipleRootNodeException();
			}
		}
		nodeRepository.save(node);
	}
	
	public void validateRootNode(Node node) {
		
	}

	@Transactional
	public void delete(Long id) {
		nodeRepository.delete(id);
	}
	
	public Node getNode(Long id) {
		return nodeRepository.findOne(id);
	}

	public List<Node> getChildren(Long parentId) {
		return nodeRepository.getChildren(parentId);
	}

	public Node getRootNode() {
		return nodeRepository.getRootNode();
	}

}
