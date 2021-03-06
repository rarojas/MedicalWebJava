package com.pagp.medicalweb.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagp.medicalweb.db.entity.DoctorEntity;
import com.pagp.medicalweb.db.entity.EnfermeroEntity;
import com.pagp.medicalweb.db.entity.receta.ConsultaEntity;
import com.pagp.medicalweb.db.entity.receta.DiagnosticoEntity;
import com.pagp.medicalweb.services.impl.DoctoresServices;
import com.pagp.medicalweb.web.core.AuthenticationFacade;
import com.pagp.medicalweb.web.core.dto.AuthenticatedUser;

@RestController
@RequestMapping("/api/doctores")
public class DoctoresController {

	@Autowired
	private DoctoresServices doctoresServices;

	@Autowired
	private AuthenticationFacade authenticationFacade;

	@PreAuthorize("hasRole('ADMINISTRADOR_CE')")
	@RequestMapping
	List<DoctorEntity> obtenerDoctores() {
		return doctoresServices.obtenerDoctores(authenticationFacade.getAuthentication().getIdEntidad());
	}

	@PreAuthorize("hasRole('ADMINISTRADOR_CE')")
	@RequestMapping("/enfermeros")
	List<EnfermeroEntity> obtenerEnfermeros() {
		return doctoresServices.getEnfermerosByEntidad(authenticationFacade.getAuthentication().getIdEntidad());
	}

	@PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping("/obtenerConsultas/{idEntidad}")
	List<ConsultaEntity> obtenerConsultas(@PathVariable int idEntidad) {
		AuthenticatedUser user = authenticationFacade.getAuthentication();
		return doctoresServices.obtenerConsultas(user.getIdEntidad());
	}

	// @PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping("/consulta/{idSolicitud}")
	ConsultaEntity obtenerConsulta(@PathVariable int idSolicitud) {
		return doctoresServices.obtenerConsulta(idSolicitud);
	}

	// @PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping("/diagnosticos")
	List<DiagnosticoEntity> diagnosticos() {
		AuthenticatedUser user = authenticationFacade.getAuthentication();
		return doctoresServices.obtenerDiagnosticos(user.getId());
	}

	// @PreAuthorize("hasRole('DOCTOR')")
	@RequestMapping("/diagnosticos/{idDiagnostico}")
	DiagnosticoEntity diagnosticos(@PathVariable int idDiagnostico) {
		return doctoresServices.obtenerDiagnostico(idDiagnostico);
	}

}
