package org.sid.cinema.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Data // permet de generer les getter et setter automatiquement
// permet de generer les contructeurs avec parameter
@NoArgsConstructor //permet de genererer les contructeur sans paramètre
@AllArgsConstructor
@ToString //permet de geneerer le tostring
public class Cinema implements Serializable {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long id;
 private String name;
 private double longitude, latitude, altitude;
 private int nombreSalles;
 @OneToMany(mappedBy = "cinema")
private Collection<Salle> salles;
 @ManyToOne
 private Ville ville;




}
