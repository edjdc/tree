package edjdc.tree.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edjdc.tree.assemblers.NodeAssembler;
import edjdc.tree.dto.CreateNodeDTO;
import edjdc.tree.dto.NodeDTO;
import edjdc.tree.dto.RootNodeDTO;
import edjdc.tree.dto.UpdateNodeDTO;
import edjdc.tree.model.Node;
import edjdc.tree.service.NodeService;

/**
 * Controller que contém os endpoints de acesso ao recurso Node
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
@RestController
@RequestMapping("/node")
public class NodeController {

	@Autowired
	private NodeAssembler nodeAssembler;
	
	@Autowired
	private NodeService nodeService;
	
	@PostMapping
	public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody CreateNodeDTO createNodeDTO) {
		Node node = nodeAssembler.toNode(createNodeDTO);
		nodeService.create(node);
		
		Map<String, Long> map = new HashMap<>();
		map.put("id", node.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(map);
	}
	
	@PutMapping
	public ResponseEntity<Map<String, Long>> update(@Valid @RequestBody UpdateNodeDTO updateNodeDTO) {
		Node node = nodeAssembler.toNode(updateNodeDTO);
		nodeService.update(node);

		Map<String, Long> map = new HashMap<>();
		map.put("id", node.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		nodeService.delete(id);
	}
	
	@GetMapping("/{parentId}")
	public ResponseEntity<List<NodeDTO>> getChildren(@PathVariable Long parentId) {
		List<Node> childrens = nodeService.getChildren(parentId);
		
		List<NodeDTO> childrensDTO = childrens.stream()
			.map(n -> nodeAssembler.toNodeDTO(n))
			.collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(childrensDTO);
	}
	
	@GetMapping
	public ResponseEntity<RootNodeDTO> getRootNode() {
		Node rootNode = nodeService.getRootNode();
		RootNodeDTO rooNodeDTO = nodeAssembler.toRooNodeDTO(rootNode);
		
		return ResponseEntity.status(HttpStatus.OK).body(rooNodeDTO);
	}
}
