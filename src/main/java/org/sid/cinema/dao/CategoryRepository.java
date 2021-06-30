package org.sid.cinema.dao;

import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource //toutes  les mothodes de category repository sont acccessible via une api restful
@CrossOrigin("*")
public interface CategoryRepository extends JpaRepository<Categorie,Long> {


}
