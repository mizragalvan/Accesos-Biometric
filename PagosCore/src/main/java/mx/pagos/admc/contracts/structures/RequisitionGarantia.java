package mx.pagos.admc.contracts.structures;


import lombok.Data;

@Data
public class RequisitionGarantia {

	private Integer idRequisitionGar;//primary key
	private Integer idGarantia;
	private Integer idSupplierRequisition;
	
	private String descripcion;
	private String clave;
	private String medNoreste;
	private String medNoroeste;
	private String medSuereste;
	private String medSuroeste;
	private String colindaNoreste;
	private String colindaNoroeste;
	private String colindaSuereste;
	private String colindaSuroeste;
	private String medMtsCuadrados;
	private String direccion;
	private String nombreNotario;
	private String numNotario;
	private String NumeroEscrituraPublica;
	private String fhInscripcionRpp;
	private String fechaEscrituraInmueble;
	
	private String inscripcionRpp;
	private String estado;
	private String estadoRegistro;
	private String folioElectronico;
	private String regMatrimonial;
	private String nomConyuge;
	private String tipo;
	
	
}