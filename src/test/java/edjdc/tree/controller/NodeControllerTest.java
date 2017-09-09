package edjdc.tree.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import edjdc.tree.TreeApplication;
import edjdc.tree.assemblers.NodeAssembler;
import edjdc.tree.dto.CreateNodeDTO;
import edjdc.tree.dto.UpdateNodeDTO;
import edjdc.tree.model.Node;
import edjdc.tree.repository.NodeRepository;

/**
 * Classe responsável pelos testes dos endpoints rest.
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TreeApplication.class)
@WebAppConfiguration
public class NodeControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private NodeAssembler nodeAssembler;
	
	@Autowired
	private NodeRepository nodeRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).build();
		nodeRepository.deleteAllInBatch();
	}

	@Test
	public void createNode() throws Exception {
		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setCode("123");
		createNodeDTO.setDescription("Test");
		createNodeDTO.setDetail("Test");

		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createNodeDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void createNodeWithParent() throws Exception {
		Node rootNode = new Node();
		rootNode.setCode("1");
		nodeRepository.save(rootNode);
		
		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setCode("2");
		createNodeDTO.setParentId(rootNode.getId());

		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createNodeDTO)))
				.andExpect(status().isCreated());
	}

	@Test
	public void createNodeWithParentNotSaved() throws Exception {
		
		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setCode("2");
		createNodeDTO.setParentId(1L);

		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createNodeDTO)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void createNodeWithManyRootNodes() throws Exception {
		Node rootNode = new Node();
		rootNode.setCode("123");
		nodeRepository.save(rootNode);

		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		createNodeDTO.setCode("321");
		
		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createNodeDTO)))
				.andExpect(status().isConflict());
	}

	@Test
	public void createNodeWithConstraintViolation() throws Exception {
		CreateNodeDTO createNodeDTO = new CreateNodeDTO();
		
		mockMvc.perform(post("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createNodeDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", is("Validation Error")));
	}

	@Test
	public void updateNode() throws Exception {
		Node node = new Node();
		node.setCode("123");
		node.setDescription("Test");
		node.setDetail("Test");
		nodeRepository.save(node);

		UpdateNodeDTO updateNodeDTO = nodeAssembler.toUpdateNodeDTO(node);
		updateNodeDTO.setDescription("Test changed!");

		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(node.getId().intValue())));
		
		Node findOne = nodeRepository.findOne(node.getId());
		assertThat(findOne.getDescription(), equalTo("Test changed!"));
	}

	@Test
	public void updateNodeWithConstraintViolation() throws Exception {
		UpdateNodeDTO updateNodeDTO = new UpdateNodeDTO();
		
		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", is("Validation Error")));
	}

	@Test
	public void updateNodeWithoutId() throws Exception {
		UpdateNodeDTO updateNodeDTO = new UpdateNodeDTO();
		updateNodeDTO.setCode("123");
		updateNodeDTO.setDescription("Test");
		updateNodeDTO.setDetail("Test");
		
		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", is("Validation Error")));
	}

	@Test
	public void updateNodeNotFound() throws Exception {
		UpdateNodeDTO updateNodeDTO = new UpdateNodeDTO();
		updateNodeDTO.setId(1L);
		updateNodeDTO.setCode("123");
		updateNodeDTO.setDescription("Test");
		updateNodeDTO.setDetail("Test");

		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateNodeWithParentNotSave() throws Exception {
		Node node = new Node();
		node.setCode("123");
		node.setDescription("Test");
		node.setDetail("Test");
		nodeRepository.save(node);

		UpdateNodeDTO updateNodeDTO = nodeAssembler.toUpdateNodeDTO(node);
		updateNodeDTO.setDescription("Test changed!");
		updateNodeDTO.setParentId(1L);

		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isNotFound());
		
	}

	@Test
	public void updateNodeWithManyRootNodes() throws Exception {
		Node rootNode = new Node();
		rootNode.setCode("1");
		nodeRepository.save(rootNode);
		
		Node node = new Node();
		node.setCode("2");
		node.setParent(rootNode);
		nodeRepository.save(node);

		UpdateNodeDTO updateNodeDTO = nodeAssembler.toUpdateNodeDTO(node);
		updateNodeDTO.setParentId(null);

		mockMvc.perform(put("/node")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateNodeDTO)))
				.andExpect(status().isConflict());
	}
	
	
	@Test
	public void deleteNode() throws Exception {
		Node node = new Node();
		node.setCode("123");
		node.setDescription("Test");
		node.setDetail("Test");
		nodeRepository.save(node);
		
		mockMvc.perform(delete("/node/" + node.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(node)))
				.andExpect(status().isNoContent());
		
		boolean exists = nodeRepository.exists(node.getId());
		assertFalse(exists);
	}
	
	@Test
	public void getChildren() throws Exception {
		Node rootNode = new Node();
		rootNode.setCode("1");
		rootNode.setDescription("Root node");
		rootNode.setDetail("Root node");
		nodeRepository.save(rootNode);
		
		Node node1 = new Node();
		node1.setCode("12");
		node1.setDescription("Node 1");
		node1.setDetail("Node 1");
		node1.setParent(rootNode);
		nodeRepository.save(node1);
		
		Node node2 = new Node();
		node2.setCode("123");
		node2.setDescription("Node 2");
		node2.setDetail("Node 2");
		node2.setParent(rootNode);
		nodeRepository.save(node2);

		mockMvc.perform(get("/node/" + rootNode.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(node1.getId().intValue())))
				.andExpect(jsonPath("$[0].code", is(node1.getCode())))
				.andExpect(jsonPath("$[0].description", is(node1.getDescription())))
				.andExpect(jsonPath("$[0].detail", is(node1.getDetail())))
				.andExpect(jsonPath("$[0].parentId", is(rootNode.getId().intValue())))
				.andExpect(jsonPath("$[0].hasChildren", is(false)))
				.andExpect(jsonPath("$[1].id", is(node2.getId().intValue())))
				.andExpect(jsonPath("$[1].code", is(node2.getCode())))
				.andExpect(jsonPath("$[1].description", is(node2.getDescription())))
				.andExpect(jsonPath("$[1].detail", is(node2.getDetail())))
				.andExpect(jsonPath("$[1].parentId", is(rootNode.getId().intValue())))
				.andExpect(jsonPath("$[1].hasChildren", is(false)));
	}
	
	@Test
	public void getRootNode() throws Exception {
		Node rootNode = new Node();
		rootNode.setCode("1");
		rootNode.setDescription("Root node");
		rootNode.setDetail("Root node");
		nodeRepository.save(rootNode);
		
		Node node1 = new Node();
		node1.setCode("12");
		node1.setDescription("Node 1");
		node1.setDetail("Node 1");
		node1.setParent(rootNode);
		nodeRepository.save(node1);
		
		Node node2 = new Node();
		node2.setCode("123");
		node2.setDescription("Node 2");
		node2.setDetail("Node 2");
		node2.setParent(rootNode);
		nodeRepository.save(node2);

		mockMvc.perform(get("/node")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(rootNode.getId().intValue())))
				.andExpect(jsonPath("$.code", is(rootNode.getCode())))
				.andExpect(jsonPath("$.description", is(rootNode.getDescription())))
				.andExpect(jsonPath("$.detail", is(rootNode.getDetail())))
				.andExpect(jsonPath("$.parentId", is(nullValue())))
				.andExpect(jsonPath("$.children", hasSize(2)))
				.andExpect(jsonPath("$.children[0].id", is(node1.getId().intValue())))
				.andExpect(jsonPath("$.children[0].code", is(node1.getCode())))
				.andExpect(jsonPath("$.children[0].description", is(node1.getDescription())))
				.andExpect(jsonPath("$.children[0].detail", is(node1.getDetail())))
				.andExpect(jsonPath("$.children[0].parentId", is(rootNode.getId().intValue())))
				.andExpect(jsonPath("$.children[1].id", is(node2.getId().intValue())))
				.andExpect(jsonPath("$.children[1].code", is(node2.getCode())))
				.andExpect(jsonPath("$.children[1].description", is(node2.getDescription())))
				.andExpect(jsonPath("$.children[1].detail", is(node2.getDetail())))
				.andExpect(jsonPath("$.children[1].parentId", is(rootNode.getId().intValue())));
	}
	
}
