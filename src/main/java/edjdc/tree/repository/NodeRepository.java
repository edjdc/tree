package edjdc.tree.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edjdc.tree.model.Node;

/**
 * Respository responsável pela integração com o banco de dados
 * 
 * @author Edivilson Dalacosta
 * @since 09/09/2017
 */
public interface NodeRepository extends JpaRepository<Node, Long> {

	@Query(" from Node n left join fetch n.children where n.parent is null")
	Node getRootNode();

	@Query("from Node n where n.parent.id = :parentId")
	List<Node> getChildren(@Param("parentId") Long parentId);
	
	@Query("select count(n) from Node n where n.parent is null")
	Long countByParentIsNull();
	

	@Query("select count(n) from Node n where n.parent is null and n.id <> :id")
	Long countByParentIsNullAndIdNot(@Param("id") Long id);
	
}
