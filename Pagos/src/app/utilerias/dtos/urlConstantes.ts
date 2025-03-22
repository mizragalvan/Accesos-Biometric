export class ConstantesURL {
    static readonly SUPPLIER_SEARCH_PROVIDERS = 'supplier/searchProviders.do';
    static readonly FINANTIAL_ENTITY_FINDACTIVE = 'financialEntity/findActive.do';
    static readonly LEGAL_REPRESENTATIVE_GDA = 'legalRepresentative/findByDgaAndFinancialEntity.do';
    static readonly LEGAL_REPRESENTATIVE_DATA = 'legalRepresentative/findDataFinantialEntity.do';
    static readonly REQUISITION_SAVE_1_2 = 'requisition/saveRequisitionInProgressPart1And2.do';
    static readonly REQUISITION_SAVE_3 = 'requisition/saveRequisitionInProgressPart3.do';
    static readonly REQUISITION_SAVE_5 = 'requisition/saveRequisitionInProgressPart5.do';
    static readonly REQUISITION_SAVE_6 = 'requisition/saveRequisitionInProgressPart6.do';
    static readonly REQUISITION_SAVE_7 = 'requisition/saveRequisitionInProgressPart7.do';
    static readonly REQUISITION_SAVE_FILE = 'document/uploadService.do';
    static readonly UPLOAD_SERVICE_ANGULAR = 'controller/document/uploadServiceAngular.do';
    static readonly UPLOAD_SERVICE_ARCHIVO_FINAL = 'controller/document/uploadServiceArchivoFinal.do';
    static readonly DOCUMENT_FIELD = 'documentPersonalityFields/finDocumentFieldsByDocument.do';
    static readonly DOCUMENT_GENERAL_DOWNLOAD = 'document/generalDownloadService.do?idDocument=&isDeleteFile=';
    static readonly DOCUMENT_FILE_DOWNLOAD = 'document/downloadFileService.do';
    static readonly FIND_REQUISITION_FINANCIAL_ENTITY = 'requisition/findRequisitionFinancialEntityByIdRequisition.do';
    static readonly FINANCIAL_FIND_DATA_FINANCIAL_ENTITY_REQUISITION = "financialEntity/findDataFinantialEntityRequisition.do";
    static readonly GENERATE_QR = 'document/generateQR.do';
    static readonly GET_QR = 'document/getQR.do';
    static readonly DOWNLOAD_QR = 'document/downloadQR.do';
    static readonly DOWNLOAD_DOC_QR = 'document/downloadDocQR.do';
    static readonly TOTAL_PAGES_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT = 'requisition/returnTotalPagesShowRequisitionsManagement.do';
    static readonly TOTAL_DATA_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT = 'requisition/returnTotalDataShowRequisitionsManagement.do';
    static readonly FIND_ALL_REQUISITIONS_GENERAL = 'requisition/findAllRequisitions.do';
    static readonly TOTAL_PAGES_APPLICANT_IN_PROGRESS_REQUISITIONS = 'requisition/countTotalPagesApplicantInProgressRequisitions.do';
    static readonly FIND_APPLICANT_INPROGRESS_REQUISITIONS = 'requisition/findApplicantInProgressRequisitions.do';
    static readonly FIND_PAGINATED_REQUISITIONS_MANAGEMENT = 'requisition/findPaginatedRequisitionsManagement.do';
    static readonly SAVE_REQUISITION_EDITED_DATA = 'requisition/saveRequisitionEditedData.do';
    static readonly DOCUMENT_FILE_DOWNLOAD_REQUIRED = 'requisition/downloadDocumentRequired.do?idDocument=';
    static readonly DS_SAVE_SIGNED_DOCUMENT = "digitalSignature/saveSignedDocument.do";
    static readonly DOCUMENT_FILE_PDF_REQUIRED = 'requisition/documentRequiredpdf.do?idDocument=';
    static readonly DOWNLOAD_DOC_REQUIRED = 'requisition/downloadDocRequired.do';
    static readonly REQUISITIN_SAVE = 'requisition/saveOrUpdateAngular.do';
    static readonly REQUISITION_SAVE_NOTIFICATION = 'requisition/saveOrUpdateAngularNotification.do';
    static readonly FIND_FLOW_BY_USER_PROFILE = 'profiling/findFlowsByUserProfiles.do';
    static readonly SEARCH_UNIT_BY_USER_PROFILE = 'profiling/findUnitsByUserProfiles.do';
    static readonly SEARCH_COMPANY_BY_USER_PROFILE = 'profiling/findCompaniesByUserProfiles.do';
    static readonly GUARDAR_IDENTIFICADOR_FLUJO = 'profile/establecerIdentificadorFlujo.do';
    static readonly FIND_FLOW_SCREEN = 'requisition/findFlowScreen.do';
    static readonly FIND_FLOW_STEP_SESSION = 'findFlowStepSession.do';
    static readonly FIND_PAGINATED_TRAY_BY_STATUS = 'requisition/findPaginatedTrayBystatus.do';
    static readonly FIND_PAGINATED_TRAY_BY_STATUS_GRAFICA = 'requisition/findPaginatedTrayBystatusGrafica.do';
    static readonly FIND_PAGINATED_TRAY_BY_DATE = 'requisition/findPaginatedTrayPorFechas.do';
    static readonly FIND_BY_IDREQUISITION_AND_FLOW_STATUS = 'comment/findByIdRequisitionAndFlowStatus.do';
    static readonly OBTENER_SOLICITUDES_PENDIENTES = 'requisition/obtenerSolicitudesPendientesPorEnviar.do';
    static readonly FIND_BY_ID = 'requisition/findById.do';
    static readonly FIND_REQUISITION_VERSION_HISTORY = 'requisition/findRequisitionVersionHistory.do';
    static readonly FIND_IDREQUISITION_FLOW_STATUS = 'comment/findByIdRequisitionAndFlowStatus.do';
    static readonly VERIFY_CONTRACT_DOCUMENT = 'requisition/verifyContractDocument.do';
    static readonly DOWNLOAD_PREVIEW = 'requisition/downloadPreview.do';
    static readonly DOWNLOAD_DRAFT_CONTRACT = 'requisition/downloadDraftContract.do';
    static readonly DOWNLOAD_DRAFT_CONTRACT_PDF = 'requisition/obtenerBorradorContratoPDF.do';
    static readonly FIND_PROFILE_SCREEN_TRAY_BY_IDPROFILE = 'profile/findProfileScreenTrayByIdProfile.do';
    static readonly REQUISITION_DETAIL = 'requisition/detailRequisition.do';
    static readonly SEARCH_EVALUATORS = 'Alerts/searchEvaluators.do';
    static readonly SEARCH_AVAILABLE_LAWYERS = "Alerts/searchAviableLawyers.do";
    static readonly FIND_LOGGED_USSER = 'user/findLoggedUser.do';
    static readonly FIND_REQUIRED_DOCUMENT_BY_SUPPLIER = 'supplier/findRequiredDocumentsByIdSupplier.do';
    static readonly FIND_HISTORY_DOC_VERSION = 'requisition/findHistoryDocumentsVersions.do';
    static readonly FIND_DOCUMENT_ATTACHMENT = 'requisition/findDocumentsAttachment.do';
    static readonly VALIDATE_PASSWORD = 'user/validatePassword.do';
    static readonly CHANGE_PASSWORD = 'user/changePassword.do';
    static readonly VERIFY_CURRENT_DOCUMENT = 'controller/document/verifydocument.do';
    static readonly FIND_REQUISITION_VERSION_BY_ID = 'requisition/findWholeRequisitionVersionById.do';
    static readonly SECCION_TO_SHOW_BY_DOCUMENT = 'documentPersonalityFields/sectionsToShowByDocument.do';
    static readonly FIND_DRAFT_BY_ID = 'requisition/findDraftById.do';
    static readonly AREA_FIND_ALL = 'area/findAll.do';

    static readonly FIND_DOCUMENT_BY_PERSONALITY = 'personality/findRequiredDocumentByPersonality.do';
    static readonly ACTIVE_NOTIFICATIONS_BY_USER = 'notification/findActiveNotificationsByUser.do';
    static readonly VERIFY_DRFAT_DOCUMENT = 'requisition/verifyDrafDocument.do';
    static readonly NOTICE_FIND_AVAILABLE = 'notice/findNoticeByAvailables.do';
    static readonly UPDATE_NOTIFICATION_BY_ID = 'notification/updateStatusNotificacionById.do';
    static readonly SEND_NOTIFICATION_BY_USER_AND_STEP = 'notification/sendNotificationByUserAndStep.do';
    static readonly SEND_REVIEW_DRAFT_ANGULAR = 'requisition/sendReviewDraftAngular.do';
    static readonly SEND_REVIEW_DRAFT_ANGULAR_NOTIFICATION = 'requisition/sendReviewDraftAngularNotification.do';
    static readonly FIND_ALL_COMMENTS_BY_ID_REQUISITION = 'comment/findAllCommentsByIdRequisition.do';
    static readonly FIND_COMMENTS_BY_IDREQUISITION_FLOWSTATUS_TYPE = 'comment/findByIdRequisitionAndFlowStatusAndCommentType.do';
    static readonly REQUEST_MODIFICATION_DRAFT = 'requisition/requestModificationDraft.do';
    static readonly REQUEST_MODIFICATION_DRAFT_NOTIFICATION = 'requisition/requestModificationDraftNotification.do';
    static readonly FIND_REQUISITION_LEGAL_REPRESENTATIVES = 'requisition/findRequisitionLegalRepresentative.do';
    static readonly FIND_REQUISITION_OBLIGATIONS = 'requisition/findObligations.do';
    static readonly SAVE_SIGN_CONTRACT = 'requisition/saveSignContract.do';
    static readonly SAVE_SIGN_CONTRACT_NOTIFICATION = 'requisition/saveSignContractNotification.do';
    static readonly SAVE_REQUISITION_DRATF_PART2 = 'requisition/saveRequisitionDraftPart2.do';
    static readonly SAVE_REQUISITION_DRATF_PROEM = 'requisition/saveRequisitionDraftProem.do';
    static readonly SAVE_REQUISITION_DRATF_CLAUSULES = 'requisition/saveRequisitionDraftClausules.do';
    static readonly SAVE_REQUISITION_DRATF_PROPERTY = 'requisition/saveRequisitionDraftProperty.do';
    static readonly FIND_DOCUMENTS_ATTACHMENTDTO = 'requisition/findDocumentsAttachmentDTO.do';
    static readonly SAVE_DOCUMENT_ATTACHMENT_DTO = 'requisition/saveDocumentAttachmentDTO.do';





    static readonly GRAFICA_TIEMPO_EN_PASOS = 'grafica/graficaDeTiempoEnBandeja.do';
    static readonly GRAFICA_TOTAL_SOLICITUDES = 'grafica/graficaTotalSolicitudesEnPasos.do';
    static readonly GRAFICA_TIEMPOS_DE_BARRAS = 'reportes/graficaBarrasTiempos.do';
    static readonly GRAFICA_POR_STATUS = 'reportes/graficaStatus.do';
    static readonly GRAFICA_POR_AREA = 'reportes/graficaPastelArea.do';
    static readonly CONTRATOS_CERRADOS_POR_MES = 'reportes/contratosCerradosPorMes.do';
    static readonly LISTA_FILTROS = 'reportes/getListaFiltro.do';
    static readonly DATOS_GRAFICA_FILTROS = 'reportes/getDatosGraficaFiltros.do';
    static readonly REPORTE_EXCEL_SOLICITUDES = 'reportes/reporteExcelSolicitudes.do';
    static readonly REPORTE_EXCEL_TIEMPO_POLITICA_FECHAS = 'reportes/reporteExcelTiempoPoliticaFechas.do';
    static readonly GENERA_EXCEL_FINALIZADOS = 'reportes/generaExcelFinalizados.do';
    static readonly GENERA_EXCEL_SOLICITUDES = 'reportes/generaExcelSolicitudes.do';
    static readonly GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES = 'reportes/generaExcelGraficaSolicitudes.do';
    static readonly GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES_CERRADAS_ANIO = 'reportes/generaExcelGraficaSolicitudesCerradas.do';
    static readonly GENERA_EXCEL_AREA_SELECCIONADA_PASTEL = 'reportes/generaExcelAreaSeleccionada.do';
    static readonly GENERA_EXCEL_TIEMPO_POLITICA = 'reportes/generaExcelTiempoPolitica.do';
    static readonly GENERAR_REPORTE_EXCEL_GRAFICA = 'reportes/generarReporteExcelGrafica.do';
    static readonly GENERAR_REPORTE_EXCEL_GRAFICA_PASTEL_AREAS = 'reportes/generarReporteExcelGraficaPastelAreas.do';
    
    static readonly UPDATE_SUPPLIER_DRAFT_FIELDS = 'supplier/updateDraftSupplierFields.do';
    static readonly SUPPLIER_UPDATE_REP_LEGAL_ANGULAR = 'supplier/updateRepLegalAngular.do';
    static readonly SEND_DRAFT_CONTRACT_NEGOTIATOR = 'requisition/sendDraftContractNegotiator.do';
    static readonly SEND_DRAFT_CONTRACT_NEGOTIATOR_NOTIFICATION = 'requisition/sendDraftContractNegotiatorNotification.do';
    static readonly SEND_TO_VO_BO = 'requisition/sendToVoBo.do';
    static readonly SEND_TO_DOCUMENT_FINAL = 'requisition/sendToDocumentFinal.do';
    static readonly ENVIAR_VOBO_JURIDICO = 'requisition/enviarVoBoJuridico.do';
    static readonly RECHAZAR_VOBO_JURIDICO = 'requisition/rechazarVoBoJuridico.do';
    static readonly FIND_SUPPLIER_LAWYERS_BY_SUPPLIER = 'supplier/findLegalRepresentativesByIdSupplier.do';
    static readonly PERSONALITY_FIND_REQUIRED_DOCUMENT = 'personality/findRequiredDocumentByPersonality.do';
    static readonly SEARCH_PROVIDERS_BY_RFC = 'supplier/searchProvidersByRfc.do';
    static readonly FIND_SUPPLIER_WITNESSES_BY_SUPPLIER = 'supplier/findWitnessesByIdSupplierAndType.do';
    static readonly SAVE_CONTRACT_CANCELLATION_COMMENT = 'requisition/saveContractCancellationComment.do';
    static readonly SAVE_CONTRACT_CANCELLATION_COMMENT_NOTIFICATION = 'requisition/saveContractCancellationCommentNotification.do';
    static readonly DELETE_SERVICE = 'controller/document/deleteFileService.do';
    static readonly DELETE_DIGITALIZATION = "controller/requisition/deleteDigitalizationByIdDocument.do";
    static readonly SAVE_CONTRACT_TO_DIGITIZE = 'requisition/saveContractToDigitize.do';
    static readonly FIND_DIGITALIZATION_DOCUMENT_VERSION = 'requisition/findDigitalizationDocumentVersion.do';
    static readonly SAVE_REQUISITION_TEMPLATE = 'requisition/saveTemplate.do';
    static readonly DELETE_REQUISITION_TEMPLATE = 'requisition/deleteTemplate.do';
    static readonly SAVE_AND_SEND_CONTRACT_TO_DIGITIZE = 'requisition/saveAndSendContractToDigitize.do';
    static readonly SEND_PRINT_CONTRACT = 'requisition/sendPrintContract.do';
    static readonly FIND_VERSIONS_CONTRACT_DTO = 'requisition/findContractVersionDTO.do';
    static readonly DOWNLOAD_REQDOC_FROM_HISTORY = 'requisition/downloadReqDocumentFromHistory.do';
    static readonly IS_COMPANY_NAME_EXIST = 'document/isCompanyNameExist.do';
    static readonly VALIDATE_DATE = 'document/validateDate.do';

    static readonly GET_LIST_DEALS_END = 'contratosCelebrados/obtenerContratosCelebrados.do';
    static readonly GET_LIST_ALL_DEAL_END = 'contratosCelebrados/obtenerTotalContratosCelebrados.do';
    static readonly CHANGE_ATTEND_STATUS = 'requisition/changeAttendStatus.do';
    static readonly DOWNLOAD_CONTRACT_ZIP = 'contratosCelebrados/downloasRar.do';
    static readonly DESCARGAR_REPORTE_EXCEL = 'reportes/descargarReporteExcel.do';
    static readonly GET_IDS_SUPPLIERPERSON_BY_IDREQUISITION = 'requisition/getIdsSupplierPersonByIdRequisition.do';
    static readonly FIND_SUPPLIER_PERSON_BY_REQUISITION = 'supplier/findLegalRepresentativesByIdRequisition.do';

    static readonly FIND_SUPPLIER_LAWYERS_BY_SUPPLIER_ACTIVE = 'supplier/findLegalRepresentativesByIdSupplieraActive.do';
    static readonly FIND_SUPPLIER_WITNESSES_BY_SUPPLIER_ACTIVE = 'supplier/findWitnessesByIdSupplierAndTypeActive.do';

    static readonly ADD_RED_FLAG = 'redFlag/addRedFlag.do';
    static readonly FIND_RED_FLAG = 'redFlag/findReflags.do';
    static readonly GET_REQUISITION_STATUS = 'requisitionStatusTurn/getRequisitionStatus.do';
    static readonly REQUEST_ONLY_UPDATE_DRAFT = 'requisition/requestOnlyUpdateDraft.do';

    static readonly SAVE_TYPE_ANEXO = 'anexo/addTypeByAnexo.do';
    static readonly FIND_TYPES_ANEXOS = 'anexo/findTypesByAnexos.do';
    static readonly DELETE_TYPE_ANEXO = 'anexo/deleteTypeByAnexo.do';
    static readonly FIND_REQUISITION_BY_IDDOCUMENTTYPE ="anexo/findRequisitionByIdDocumentType.do";
    static readonly FIND_TAGS_ANEXOS = "anexo/findTagsAnexos.do";


     // Catálogo puesto.
     static readonly POSITIONS_FIND_ALL_PAGED = 'findAllPositionCatalogPaged.do';
     static readonly POSITIONS_FIND_TOTAL_ROWS = 'returnTotalRowsOfCatalogPosition.do';
     static readonly CHANGE_POSITION_STATUS = 'position/changePositionStatus.do';
     static readonly POSITION_SAVE_OR_UPDATE = 'position/saveOrUpdate.do';
     // Admin Config
     static readonly FIND_CONFIGURATION_BY_CATEGORY = 'configuration/findConfigurationsByCategory.do';
     static readonly UPDATE_CONFIGURATION = "configuration/updateConfiguration.do";
     // Bitácora de movimientos.
     static readonly BINNACLE_FIND_BY_LOG_CATEGORY_PAGINATED = 'binnacle/findByLogCategoryTypesPaginated.do';
     static readonly BINNACLE_FIND_BY_LOG_CATEGORY_TOTAL_PAGES = 'binnacle/findByLogCategoryTypesTotalPages.do';
     static readonly GET_FOLDER_LOG_FILES = 'log/getFolderLogFiles.do';
     static readonly FIND_USERS_BY_STATUS = 'user/findByStatus.do';
     static readonly DOWNLOAD_LOG_FILE = 'log/downloadLogFile.do';
     static readonly BINNACLE_DELETE_BY_DATES_RANGE = 'binnacle/deleteByDatesRange.do';
     static readonly DELETE_OLDER_LOG_FILES_THAN_DATE = 'log/deleteOlderLogFilesThanDate.do';
     // Config alertas
     static readonly SEARCH_FLOWS = 'searchFlows.do';
     static readonly SEARCH_STATUSBYFLOW = 'findStatusByFlow.do';
     static readonly SEARCH_ALERTSBYFLOWANDSTATUS = 'findAlertbyFlowStatus.do';
     static readonly DOCTYPE_FIND_BY_STATUS = 'documentType/findByRecordStatus.do';
     static readonly FIND_ALERT_CONFLICTS = "Alerts/findAlertConflicts.do";
     static readonly ALERTS_SAVE_OR_UPDATE = 'Alerts/saveOrUpdate.do';
     static readonly FIND_ALERT_BY_ID = 'Alerts/findAlertById.do';
     static readonly ALERTS_DELETE = 'Alerts/deleteAlert.do';
     // Catálogo noticias y avisos.
     static readonly NOTICE_FIND_ALL_AVAILABLE_PAGED = 'notice/findAllAvailableNoticesPaged.do';
     static readonly NOTICE_SAVE_OR_UPDATE = 'notice/saveOrUpdate.do';
     static readonly NOTICE_DELETE_BY_ID = 'notice/deleteNoticeById.do';
     // Días Inhabiles
     static readonly FIND_ALL_HOLIDAYS = 'holiday/findAll.do';
     static readonly SAVE_HOLIDAYS = 'holiday/saveHolidays.do';
     // Gestión de perfiles
     static readonly FIND_PROFILE_CATALOG_PAGED = 'profiling/findAllProfileCatalogPaged.do';
     static readonly FIND_PROFILE_TOTAL_ROWS = 'profiling/returnTotalRowsOfProfile.do';
     static readonly VALIDATE_PROFILE_NAME = 'profiling/validateProfileName.do';
     static readonly PROFILE_SAVEORUPDATE = 'profile/Saveorupdate.do';
     static readonly PROFILE_CHANGE_STATUS = 'profile/changeStatus.do';
     static readonly PROFILE_FINDMENUBYID = 'profile/findMenuProfileById.do';
     // Menú
     static readonly MENU_FIND_ALL = 'menu/findAll.do';
     // Flow
     static readonly FIND_FLOW_BY_STATUS = 'flow/findByStatus.do';
     //FlowScreenActionService
     static readonly SEARCH_FLOWSCREENACTIONBYFLOW = 'findFlowScreenActionByFlow.do';
     //findFlowScreenActionByProfile
     static readonly FIND_FLOWSCREENACTIONBYPROFILE = 'findFlowScreenActionByProfile.do';
     // Gestión de usuario
     static readonly FIND_ALL_USERS_PAGED = 'user/findAllUsersCatalogPaged.do';
     static readonly FIND_TOTAL_PAGES_FOR_CATALOG_OF_USER = 'user/returnTotalRowsOfUser.do';
     static readonly PROFILE_FIND_BY_STATUS = 'profile/findByRecordStatus.do';
     static readonly POSITION_FIND_BY_RECORD_STATUS = 'position/findByRecordStatus.do';
     static readonly FIND_AREAS_BY_STATUS = 'area/findByStatus.do';
     static readonly SEARCH_USERSBYNAME = 'searchUsersByName.do';
     static readonly SEARCH_LAWYERS = 'searchLawyerByIdUser.do';
     static readonly SEARCH_USER_MAIL = 'searchUserMail.do';
     static readonly SEARCH_LAWYER_NAME = 'searchLawyerName.do';
     static readonly SAVE_CHANGES_LAWYER = 'saveChangesLawyer.do';
     static readonly FIND_USER_BY_ID = 'user/findUserById.do';
     static readonly PROFILE_FIND_BY_IDUSER = 'profile/findByIdUser.do';
     static readonly SAVEORUPDATEUSER = 'profile/saveOrUpdateUser.do';
     static readonly CHANGEUSERSTATUS = 'changeUserStatus.do';
     // Vacaciones.
     static readonly FIND_USERS_BY_AREA = 'user/findUserByArea.do';
     static readonly FIND_HOLIDAYS_BY_USER = 'holidayUser/findHolidaysByUser.do';
     static readonly SAVE_HOLIDAYS_USER = 'holidayUser/saveHolidaysUser.do';
     // Documentos requeridos
     static readonly REQUIRED_DOCUMENT_FIND_ALL_CATALOG_PAGED = 'requiredDocument/findAllRequiredDocumentCatalogPaged.do';
     static readonly REQUIRED_DOCUMENT_TOTAL_PAGES = 'requiredDocument/returnTotalRowsOfRequiredDocument.do';
     //PersonalitiesService
     static readonly PERSONALITY_FIND_BY_STATUS = 'personality/findByStatus.do';
     static readonly REQUIRED_DOCUMENT_SAVE_OR_UPDATE = 'requiredDocument/saveOrUpdate.do';
     static readonly REQUIRED_DOCUMENT_CHANGE_STATUS = 'requiredDocument/changeStatus.do';
     //FinancialEntityController
     static readonly FINANCIAL_FIND_ALL_CATALOG_PAGED = 'financialEntity/findAllFinancialEntityCatalogPaged.do';
     static readonly FINANCIAL_FIND_TOTAL_PAGES = 'financialEntity/returnTotalRowsOfFinancialEntity.do';
     static readonly CHANGE_FINANCIAL_STATUS = 'financialEntity/changeFinancialEntityStatus.do';
     static readonly FINANCIAL_SAVE_OR_UPDATE = 'financialEntity/saveOrUpdate.do';
     //Supplier
     static readonly SUPPLIER_FIND_ALL_CATALOG_PAGED = 'supplier/findAllSupplierCatalogPaged.do';
     static readonly SUPPLIER_FIND_TOTAL_PAGES = 'supplier/returnTotalRowsOfSupplier.do';
     static readonly SEARCH_PROVIDERS_BY_ID = 'document/searchProvidersById.do';
     static readonly SUPPLIER_CHANGE_STATUS = 'supplier/changeStatus.do';
     static readonly SUPPLIER_PERSON_CHANGE_STATUS = 'supplierPerson/changeStatusPerson.do';
     static readonly SEARCH_SUPPLIER_PERSONS_BY_IDSUPPLIER_AND_TYPE = 'supplier/findSupplierPersonsByIdSupplierAndType.do';
     static readonly SAVE_OR_UPDATE_SUPPLIER = 'supplier/saveOrUpdate.do';
     // Tipo de Documentos
     static readonly DOCTYPE_FIND_ALL_CATALOG_PAGED = 'requisition/findAllDocumentTypeCatalogPaged.do';
     static readonly DOCTYPE_FIND_TOTAL_PAGES = 'requisition/returnTotalRowsOfDocumentType.do';
     static readonly DOWNLOAD_DOCUMENTTYPE = 'requisition/downloadDocumentType.do';
     static readonly DOWNLOAD_DOCUMENTTYPE_NATURAL = 'requisition/downloadDocumentTypeNatural.do';
     static readonly DOCTYPE_CHANGE_STATUS = 'documentType/changeDocTypeStatus.do';
     static readonly DOCTYPE_SAVEORUPDATE = 'documentType/Saveorupdate.do';
     // Representante legal
     static readonly LEGAL_FIND_ALL_CATALOG_PAGED = 'legalRepresentative/findAllLegalRepresentativeCatalogPaged.do';
     static readonly LEGAL_FIND_FINANCIAL_ENTITIES_BY_ID_LEGAL = 'legalRepresentative/findFinancialEntitiesByIdLegalRepresentative.do';
     static readonly CHANGE_LEGAL_STATUS = 'legalRepresentative/changeLegalRepresentativeStatus.do';
     static readonly LEGAL_FIND_TOTAL_PAGES = 'legalRepresentative/returnTotalRowsOfLegalRepresentative.do';
     static readonly LEGAL_SAVE_OR_UPDATE = 'legalRepresentative/saveOrUpdate.do';
     // Administracion Solicitudes
     static readonly CHANGE_REQUISITION_STATUS_CANCELLED = 'requisition/changeRequisitionStatusToCancelled.do';

     static readonly FIND_ALL_AREAS_CATALOG_PAGED = 'area/findAreasCatalogPaged.do';
     static readonly CHANGE_AREA_STATUS ='area/changeAreaStatus.do';
     static readonly AREA_SAVE_OR_UPDATE = 'area/saveorupdate.do';
     static readonly TOTAL_PAGES_SHOW_AREAS = 'area/returnTotalPagesShowAreas.do';

     static readonly FIND_SCREEN_BY_STATUS = 'screen/findScreenByStatus.do';
      //Dashboard
      static readonly FINDPROFILESCREENTRAYBYIDPROFILE='profile/findProfileScreenTrayByIdProfile.do';
      static readonly FIND_PAGINATED_TRAY_REQUISITIONS_BY_STATUS='requisition/findPaginatedTrayBystatus.do';
      static readonly REQUISITION_FIND_BY_ID="requisition/findById.do"; // Me muestra el ID de la solicitud para poder mostrar el documento 
    //Firma de contrato
    static readonly SEARCH_AVAILABLE_EVALUATORS="Alerts/searchEvaluators.do";
    //Enviar contrato al evaluador
    static readonly SEND_SIGNING_CONTRACT="requisition/sendSigningContract.do";
      // Digital Signature
    static readonly DS_GET_USER_INFORMATION='digitalSignature/getUserInformation.do';
    static readonly DS_UPDATE_USER_INFORMATION='digitalSignature/updateUserInformation.do';
    static readonly DS_SEND_DOCUMENT='digitalSignature/sendDocumentToProvider.do';
    static readonly DS_POSITION_SIGNED_DOCUMENT='digitalSignature/positionSignedDocument.do';
    static readonly DS_STATUS_DOCUMENT='digitalSignature/statusDocument.do';
    static readonly DS_DELETE_SIGNED_DOCUMENT='digitalSignature/deleteSignedDocument.do';
    static readonly DS_REENVIAR_SIGNED_DOCUMENT='digitalSignature/reenvioEnvelope.do';
    static readonly DS_STATUS_DIGITAL_SIGNATURE_DOCUMENT='digitalSignature/statusDigitalSignatureDocument.do';
    static readonly DS_STATUS_RECIPIENT_SIGNED_DOCUMENT='digitalSignature/statusRecipienSignedDocument.do';
    static readonly DS_CORRECT_SIGNED_DOCUMENT='digitalSignature/correccionEnvelope.do';
    static readonly DS_VIEW_SIGNED_DOCUMENT='digitalSignature/viewEnvelopeSigned.do';
    static readonly DS_GET_CONTACTS='digitalSignature/getContacts.do';
    static readonly DS_SAVE_UPDATE_CONTACT='digitalSignature/saveUpdateContact.do';
    static readonly DS_GET_USER_DOCUMENTS='digitalSignature/getUserDocuments.do';
    static readonly DS_SET_RECIPIENT_SIGNED='digitalSignature/setRecipientSigned.do';
    static readonly DS_DELETE_CONTACT='digitalSignature/deleteContact.do';
    static readonly DS_RESEND_INVITATION_EMAIL='digitalSignature/resendInvitationEmail.do';
    static readonly DS_VALIDATE_SECRET_CODE_RECIPIENT='digitalSignature/validateSecretCodeRecipient.do';
    static readonly DS_VIEW_DOCUMENT='digitalSignature/viewDocument.do';
    static readonly DS_DOWNLOAD_SIGNED_DOCUMENT='digitalSignature/downloadSignedDocument.do';
    static readonly DS_DELETE_DOCUMENT='digitalSignature/deleteDocument.do';
     // Digital Signature Contracts
     static readonly DS_VALIDATE_DRAFT_DOCUSIGN='digitalSignature/validateDraftDocusign.do';
     static readonly DS_GET_DOCUMENT_BY_ID_REQUISITION='digitalSignature/getDocumentByIdRequisition.do';
     static readonly DS_DELETE_DOCUMENT_BY_ID = 'digitalSignature/deleteDocumentById.do';
     static readonly DS_VIEW_DOCUMENT_CONTRACT='digitalSignature/viewDocumentContract.do';
     static readonly DS_GET_DOCUMENT_INFORMATION='digitalSignature/getDocumentInformation.do';
     static readonly DS_GET_SIGNATURE_OPTION='digitalSignature/getSignatureOption.do';
     static readonly DS_GET_USER='requisition/getUser.do';
     static readonly DS_DOCUMENT_EXTENSION='digitalSignature/documentDetailExtension.do';
     //Evisign
     static readonly EVI_SEND_DOCUMENT = 'digitalSignature/sendDocumentToEviSign.do';
    
    //Fotbol Liga MX
    static readonly GET_PARTIDO_LIGA_MX='partidos/statusPartidosLigaMX.do';
    static readonly GET_PARTIDOS_INTERNATIONALS='partidos/partidosInternationals.do';
    
    
    static readonly UPLOAD_LOGO_PRODUCTO='logos/logosProductos.do';
    static readonly GET_LAST_ID_EMPLEADO='empleado/obtenerUltimoIdEmpleado.do';

    //PayMethod

    static readonly GET_PAYMENT_SERVICE='';


}
