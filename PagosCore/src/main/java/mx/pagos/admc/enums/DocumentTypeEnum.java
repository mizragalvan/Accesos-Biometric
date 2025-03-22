package mx.pagos.admc.enums;

import java.util.LinkedHashMap;

public enum DocumentTypeEnum {
    SERVICES_PROVISION_ENTITY("Prestación de Servicios Entidades"),
    SERVICES_PROVISION_COMPANY("Prestación de Servicios Empresas (Secosa e Inmobiliaria)"),
    SUPPLY("Suministro"),
    APPRAISERS_PF("Peritos Valuadores PF"),
    APPRAISERS_PM("Peritos Valuadores PM"),
    VERIFIERS_PF("Verificadores PF"),
    VERIFIERS_PM("Verificadores PM"),
    VALUATION_UNITS("Unidades de Valuación"),
    MORTGAGE_BROKERS("Brokers Hipotecarios"),
    LAW_FIRMS("Despachos (Jurídico)"),
    EXTRAJUDICIAL_COLLECTION_OFFICES("Despachos Cobranza Extrajudicial"),
    MEDICS("Médicos"),
    PHARMACIES("Farmacias"),
    LABORATORIES("Laboratorios"),
    HOSPITALS("Hospitales"),
    RHH_TRAINING("Capacitación RRH"),
    SPONSORSHIP("Patrocinio"),
    EVENTS("Eventos"),
    MORTGAGE_DEVELOPMENTS("Desarrollos Hipotecarios"),
    MIP_CONTRACTS("Contratos MIP"),
    RIGHTS_ASSIGNMENT("Cesión de Derechos"),
    OUTSOURCING_LETTER("Carta de Subcontratación"),
    OUTSOURCING_APPROVAL_LETTER("Carta de Autorización de Subcontratación"),
    SPONSORSHIP_LETTER("Carta de patrocinio"),
    CONFIDENTIALITY_AGREEMENT("Convenio de Confidencialidad"),
    AMENDMENT_AGREEMENT("Convenio Modificatorio"),
    TERMINATION_AND_SETTLEMENT_AGREEMENT("Convenio de Terminación y Finiquito"),
    TERMINATION_NOTICE("Aviso de Terminación"),
    REAL_ESTATE_EXTENSION_NOTICE("Inmobiliaria - Aviso de Prórroga"),
    REAL_ESTATE_TERMINATION_NOTICE("Inmobiliaria - Aviso de Terminación"),
    REAL_ESTATE_TERMINATION_AND_SETTLEMENT_AGREEMENT("Inmobiliaria - Convenio de Terminación y Finiquito"),
    REAL_ESTATE_ATM_LEASE("Inmobiliaria - Arrendamiento de Cajero Automático"),
    REAL_ESTATE_EXPATRIATES_LEASE("Inmobiliaria - Arrendamiento Expatriados"),
    REAL_ESTATE_THIRD_PARTY_BUILDINGS_RENTAL("Inmobiliaria - Arrendamiento de Inmuebles de Terceros"),
    REAL_ESTATE_OWN_BUILDINGS_RENTAL("Inmobiliaria - Arrendamiento de Inmuebles Propios"),
    REAL_ESTATE_SPACE_LEASE("Inmobiliaria - Arrendamiento de Espacio"),
    REAL_ESTATE_SPACE_COMODATO("Inmobiliaria - Comodato Espacio"),
    REAL_ESTATE_ATM_COMODATO("Inmobiliaria - Comodato de Cajero Automático"),
    REAL_ESTATE_AMENDMENT_AGREEMENT("Inmobiliaria - Convenio Modificatorio"),
    OTHERS("Otros"),
    
    
    ADM_ADJUSTMENTS_BUILDINGS("(Ampliacion) Edificios Adecuaciones ADM")
    ;
    
    private String name;
    
    DocumentTypeEnum(final String nameParam) {
        this.name = nameParam;
    }
    
    public String getName() {
        return this.name;
    }
    
    private static LinkedHashMap<String, String> docType;
	
  	public final static LinkedHashMap<String, String> getDocType() {
  		if (docType == null) {
  			docType = new LinkedHashMap<String, String>();
  			for (DocumentTypeEnum aux:DocumentTypeEnum.values())
  				docType.put(aux.name(), aux.getName());
  		}
  		return docType;
  	}
}

