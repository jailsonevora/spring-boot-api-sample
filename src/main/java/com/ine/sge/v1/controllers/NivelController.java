package com.ine.sge.v1.controllers;

import com.ine.sge.exception.ResourceNotFoundException;
import com.ine.sge.models.Nivel;
import com.ine.sge.dao.INivelRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController("NivelV1")
@RequestMapping("/api/v1")
@Api(value = "niveis", description = "Nivel API")
public class NivelController implements com.ine.sge.interfaces.resource.INivel {

	private final INivelRepository nivelRepository;

	@Autowired
	public NivelController(INivelRepository nivelRepository) {
		this.nivelRepository = nivelRepository;
	}


	@RequestMapping(value = "/niveis/{id}", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Retrieves given level", response= Nivel.class)
	public ResponseEntity<?> show(@Valid @PathVariable Long id){
		verify(id);
		return new ResponseEntity<> (nivelRepository.findById(id), HttpStatus.OK);
	}

	@RequestMapping(value="/niveis", method=RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "Retrieves all the levels", response=Nivel.class, responseContainer="List")
	public ResponseEntity<Page<Nivel>> showall(Pageable pageable) {
		return new ResponseEntity<>(nivelRepository.findAll(pageable), HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/niveis", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "Creates a new level", notes="The newly created level Id will be sent in the location response header")
	public ResponseEntity<Void> create(@Valid @RequestBody Nivel newLevel){

		newLevel = nivelRepository.save(newLevel);

		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newLevel.getId()).toUri());

		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);

	}

	@Transactional
	@RequestMapping(value = "/niveis/{id}", method = RequestMethod.PUT, produces = "application/json")
	@ApiOperation(value = "Updates given level")
	public ResponseEntity<Void> update(@Valid @PathVariable Long id, @Valid @RequestBody Nivel toUpdate){

		Optional<Nivel> updatedOptionalClass = nivelRepository.findById(id);
		if (updatedOptionalClass.isPresent()){

			Nivel afterIsPresent = updatedOptionalClass.get();
			afterIsPresent.setEstado(toUpdate.getEstado());
			afterIsPresent.setCategoria(toUpdate.getCategoria());
			afterIsPresent.setClasse(toUpdate.getClasse());

			afterIsPresent.setDivisao(toUpdate.getDivisao());
			afterIsPresent.setGrupo(toUpdate.getGrupo());
			afterIsPresent.setPosicao(toUpdate.getPosicao());
			afterIsPresent.setSubcategoria(toUpdate.getSubcategoria());
			afterIsPresent.setSubclasse(toUpdate.getSubclasse());
			afterIsPresent.setLastModifiedBy(toUpdate.getLastModifiedBy());

			nivelRepository.save(afterIsPresent);

			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			throw new ResourceNotFoundException("Level with id " + id + " not found");
	}

	@Transactional
	@RequestMapping(value = "/niveis/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ApiOperation(value = "Deletes given level")
	public ResponseEntity<Void> delete(@Valid @PathVariable Long id, @Valid @RequestBody String lastModifiedBy){
		//		verify(id);
		//		nivelRepository.deleteById(id);

		Optional<Nivel> softDelete = nivelRepository.findById(id);
		if (softDelete.isPresent()) {
			Nivel afterIsPresent = softDelete.get();
			afterIsPresent.setEstado(0);
			afterIsPresent.setLastModifiedBy(lastModifiedBy);

			nivelRepository.save(afterIsPresent);
		}
		else
			throw new ResourceNotFoundException("Entity with id " + id + " not found");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// region private
	private void verify(Long id) throws ResourceNotFoundException {
		nivelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Level with id " + id + " not found"));
	}
	// endregion


}
