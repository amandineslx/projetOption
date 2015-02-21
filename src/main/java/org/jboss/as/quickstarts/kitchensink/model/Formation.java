package org.jboss.as.quickstarts.kitchensink.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author Nicolas, Téo, Amandine
 * Une option est le regroupement d'eleves ayant choisi la meme specialite a l'ecole
 * Le departement informatique gere actuellement 8 options :
 * La promotion tronc commun de 1ere annee FING
 * La formation ingenierie logicielle 1ere annee
 * La promotion GSI de 2eme annee FING
 * La promotion GIPAD de 2eme annee FING
 * La formation ingenierie logicielle 2eme annee
 * La promotion GSI de 3eme annee FING
 * La promotion GIPAD de 3eme annee FING
 * La formation ingenierie logicielle 3eme annee
 *
 */
@SuppressWarnings("serial")
@Entity
public class Formation extends Responsabilite {
	
	public Formation() {}
	
	/**
	 * Liste des UVs de l'option
	 */
	@ManyToMany(fetch=FetchType.EAGER, mappedBy="options")
	private Collection<UV> uvs;
	
	public Collection<UV> getUVs() {
		return this.uvs;
	}
	
}
