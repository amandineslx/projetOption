package org.jboss.as.quickstarts.kitchensink.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.kitchensink.data.ResponsabiliteRepository;
import org.jboss.as.quickstarts.kitchensink.model.*;
import org.jboss.as.quickstarts.kitchensink.service.ResponsabiliteRegistration;

@Path("/responsabilites")
@RequestScoped
public class ResponsabiliteResourceRESTService {
	
	@Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private ResponsabiliteRepository repository;

    @Inject
    ResponsabiliteRegistration registration;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Responsabilite lookupResponsabiliteById(@PathParam("id") long id) {
        Responsabilite responsabilite = repository.findById(id);
        if (responsabilite == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return responsabilite;
    }

    @GET
    @Path("/formations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Formation> listAllFormations() {
        return  repository.findAllOrderedByIntitule(Formation.class);
    }
    
    @GET
    @Path("/uvs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UV> listAllUVs() {
        return  repository.findAllOrderedByIntitule(UV.class);
    }
    
    @GET
    @Path("/modules")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Module> listAllModules() {
        return  repository.findAllOrderedByIntitule(Module.class);
    }

    @GET
    @Path("/interventions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Intervention> listAllInterventions() {
        return  repository.findAllOrderedByIntitule(Intervention.class);
    }
    
    @POST
    @Path("/uvs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUV(UV uv) {
        return createResponsabilite(uv);
    }
    
    @POST
    @Path("/modules")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createModule(Module module) {
        return createResponsabilite(module);
    }

    @POST
    @Path("/addmoduletouv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModuleToUV(@QueryParam("moduleId") int moduleId, @QueryParam("UVId") int UVId) {
        Module module = (Module) repository.findById(moduleId);
        UV uv = (UV) repository.findById(UVId);
        if (module == null || uv == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            uv.getModules().add(module);
            module.getUVs().add(uv);
            createResponsabilite(uv);
            return createResponsabilite(module);
        }
    }
    
    private Response createResponsabilite(Responsabilite responsabilite) {
    	
    	Response.ResponseBuilder builder = null;
        try {
        	
        	if ( idAlreadyExists(responsabilite.getId()) )
            	registration.update(responsabilite);
            else {
            	// Validates responsabilite using bean validation
            	validateResponsabilite(responsabilite);
            	registration.register(responsabilite);
            }
        	
            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = RESTServicesResources.createViolationResponse(ce.getConstraintViolations(), log);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }
    
    private void validateResponsabilite(Responsabilite responsabilite) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
    	// AJout du try catch sinon on ne récupère pas certaines érreures de validation
    	try {
	        Set<ConstraintViolation<Responsabilite>> violations = validator.validate(responsabilite);
	        if (!violations.isEmpty()) {
	            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
	        }
	    	} catch ( Exception e ) {
    		System.out.println(e.getStackTrace());
    	}
    }
    
    public boolean idAlreadyExists(long id) {
        Responsabilite responsabilite = null;
        try {
            responsabilite = repository.findById(id);
        } catch (NoResultException e) {}
        return responsabilite != null;
    }
}
