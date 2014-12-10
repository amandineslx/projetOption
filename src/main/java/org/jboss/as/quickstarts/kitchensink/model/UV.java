package org.jboss.as.quickstarts.kitchensink.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class UV extends Responsabilite {
	
	@NotNull
	@NotEmpty
	private String intitule;
	
	@NotNull
	@NotEmpty
	@Pattern(regexp="[2-9][0-9][0-9][0-9]")
	private int annee;
	
	@NotNull
	@NotEmpty
	@ManyToOne(fetch = FetchType.EAGER)
	private Intervenant responsable;
	
	@OneToMany(fetch=FetchType.EAGER)
	private Collection<Module> modules;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Collection<Promotion> promos;
	
	public String getIntitule() {
		return this.intitule;
	}
	
	public void setIntitule(String intitule) {
		this.intitule=intitule;
	}
	
	public int getAnnee() {
		return this.annee;
	}
	
	public Intervenant getResponsable() {
		return this.responsable;
	}
	
	public void setResponsable(Intervenant respo) {
		this.responsable=respo;
	}
	
	public Collection<Module> getModules() {
		return this.modules;
	}
	
	public Collection<Promotion> getPromotions() {
		return this.promos;
	}
	
}
