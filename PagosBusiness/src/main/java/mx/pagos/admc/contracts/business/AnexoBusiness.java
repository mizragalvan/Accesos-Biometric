package mx.pagos.admc.contracts.business;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ICell;
import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.IRunElement;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFieldRun;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFNum;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFSDTCell;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Anexable;
import mx.pagos.admc.contracts.structures.ConfigAnexoImg;
import mx.pagos.admc.contracts.structures.FolioByPlantilla;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.admc.contracts.structures.TypeByAnexo;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.document.version.business.DocumentVersionBusiness;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;


@Service
public class AnexoBusiness {
	private static final Logger LOG = Logger.getLogger(AnexoBusiness.class);
	private static final int CODE_200 = 200;
	private static final int CODE_405 = 405;
	
	private Map<BigInteger, BigInteger> numIDs = new HashMap<BigInteger, BigInteger>();

	@Autowired
	private Anexable anexable;
	/**
	 * Autowired de la clase.
	 */
	@Autowired
	private DocumentVersionBusiness documentVersionBusiness;

	public final TypeByAnexo save(TypeByAnexo anexo) {
		try {
			if (valid(anexo)) {
				if (anexo.getIdTypeByAnexo() <= 0) {
					LOG.debug("Se guardará: Anexo");
					Integer id = this.anexable.save(anexo);
					LOG.debug("Guardado exitoso. Se regreso el id " + id);
					return new TypeByAnexo(CODE_200, "", id);
				} else {
					LOG.debug("Se actualizá: Anexo");
					Integer id = this.anexable.UpdateTypesByAnexos(anexo);
					LOG.debug("Update exitoso. Se regreso el id " + id);
					return new TypeByAnexo(CODE_200, "", id);
				}
			}
			return new TypeByAnexo(CODE_405, "", 0);
		} catch (BusinessException e) {
			LOG.error("AnexoBusiness - save: " + e.getMessage());
			return new TypeByAnexo(CODE_405, e.getMessage(), 0);
		} catch (Exception e) {
			LOG.error("AnexoBusiness - save: " + e.getMessage());
			return new TypeByAnexo(CODE_405, "Ha ocurrido un error en el servidor", 0);
		}
	}

	public final TypeByAnexo delete(TypeByAnexo anexo) {
		try {
			if (anexo != null && anexo.getIdTypeByAnexo() != null && anexo.getIdTypeByAnexo() > 0) {
				LOG.debug("Se elimina el anexo con id: " + anexo.getIdTypeByAnexo());
				this.anexable.deleteTypeByAnexo(anexo.getIdTypeByAnexo());
				return new TypeByAnexo(CODE_200, "", anexo.getIdTypeByAnexo());
			}
			return new TypeByAnexo(CODE_405, "Dato requerido", 0);
		} catch (Exception e) {
			LOG.error("AnexoBusiness - delete: " + e.getMessage());
			return new TypeByAnexo(CODE_405, "Ha ocurrido un error en el servidor", 0);
		}
	}

	public final List<TypeByAnexo> findTypesByAnexosAndPerson(final TypeByAnexo anexo) {
		try {
			LOG.debug("Se obtendrá la lista: Anexos por contrato" + anexo.getIdDocumentType());
			return this.anexable.findTypesByAnexosAndPerson(anexo.getIdDocumentType(), anexo.getMoral(), anexo.getFolio());
		} catch (Exception e) {
			LOG.error("AnexoBusiness - findTypesByAnexosAndPerson: " + e.getMessage());
			return null;
		}
	}
	
	public final List<TagField> findTagsAnexos() {
		try {
			LOG.debug("Se obtendrá la lista: Tags de Anexos.");
			return this.anexable.findTagsAnexos();
		} catch (Exception e) {
		  LOG.error("AnexoBusiness - findTagsAnexos: " + e.getMessage());
		  return null;
		}
	}
	
	
	public final List<FolioByPlantilla> findFolioByTypeDocument(final TypeByAnexo anexo) {
		try {
			LOG.debug("Se obtendrá la lista: Folios por tipo de documento" + anexo.getIdDocumentType());
			List<FolioByPlantilla> res = this.anexable.findFolioByDocumentType(anexo.getIdDocumentType());
			LOG.info("Resultado de consulta: " + res);
			return res;
		} catch (Exception e) {
			LOG.error("AnexoBusiness - findFolioByTypeDocument: " + e.getMessage());
			return null;
		}
	}

	public final List<TypeByAnexo> findTypesByAnexosAndPersonOrderPage(final TypeByAnexo anexo) {
		try {
			LOG.debug("Se obtendrá la lista: Anexos por contrato" + anexo.getIdDocumentType());
			return this.anexable.findTypesByAnexosAndPersonOrderPage(anexo);
		} catch (Exception e) {
			LOG.error("AnexoBusiness - findTypesByAnexosAndPersonOrderPage: " + e.getMessage());
			return null;
		}
	}

	private boolean valid(TypeByAnexo anexo) throws BusinessException {
		if (anexo == null) {
			throw new BusinessException("Datos requeridos");
		}

		anexo.setIdTypeByAnexo(anexo.getIdTypeByAnexo() == null ? 0 : anexo.getIdTypeByAnexo());

		if (anexo.getIdDocumentType() == null || anexo.getIdDocumentType() <= 0) {
			throw new BusinessException("Datos requeridos");
		}

		if (anexo.getName() == null || anexo.getName().trim().isEmpty()) {
			throw new BusinessException("Datos requeridos");
		}
		if (anexo.getOrder() <= 0 ) {
			throw new BusinessException("Datos requeridos");
		}

		// Validar si el orden o la pagina ya estan utilizados
		List<TypeByAnexo> lista = findTypesByAnexosAndPersonOrderPage(anexo);
		if (lista != null && lista.size() > 0) {
			throw new BusinessException("Datos duplicados");
		}
		return true;
	}
	
	public final List<TypeByAnexo> findTypesByFolio(final Integer folio) {
		try {
			LOG.debug("Se obtendrá la lista: Anexos por folio" + folio);
			return this.anexable.findTypesByFolio(folio);
		} catch (Exception e) {
			LOG.error("AnexoBusiness - findTypesByAnexosAndPerson: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Metodo encargado de anexar archivos a contrato.
	 * @param idRequisition
	 * @param finalPath
	 * @param filePath
	 * @throws BusinessException
	 * @throws IOException
	 */
	public void addAnexoToContract(final Integer idRequisition, String finalPath, File filePath)
			throws BusinessException, IOException {
		
//		finalPath=finalPath.replace("R:\\Contratos\\","/Volumes/Contratos/Contratos/");
//		finalPath=finalPath.replace("\\","/");
		
		
		File draftRequisition = new File(finalPath);
		List<VersionDTO> dtos = this.documentVersionBusiness.findContractVersionDTO(idRequisition);
		List<TypeByAnexo> listAnex = findTypesByFolio(idRequisition);
		int conEnca = 0;
	    if (draftRequisition.getName().toLowerCase().endsWith(".pdf")) {
	        System.out.println("El archivo en " + filePath.getAbsolutePath() + " es un PDF.");
	    } else {
	        System.out.println("El archivo en " + filePath.getAbsolutePath() + " no es un PDF.");
//	        FileInputStream ficheroStream = new FileInputStream(draftRequisition);     
//	        docRespaldo(filePath, draftRequisition);
//			XWPFDocument docx = new XWPFDocument(ficheroStream);
//			if(!listAnex.isEmpty()) {
//				try {
//					LOG.info("TOTAL DE ANEXOS: " + listAnex.size());
//					if(listAnex.size() > 0) {
//						TypeByAnexo orden = listAnex.get(conEnca);
//						for(int i = 0; i < listAnex.size(); i++) {
//							if(orden.getName().equals(listAnex.get(i).getName())) {
//								String extension = FilenameUtils.getExtension(listAnex.get(i).getFileName()).toUpperCase();
//								XWPFRun run = getInfoDoc(docx, listAnex.get(i).getName());
//								if(run == null) {
//									docx.createParagraph();
//									XWPFParagraph parag = docx.getParagraphs().get(docx.getParagraphs().size());
//									List<XWPFRun> listRuns = parag.getRuns();
//									run =  listRuns.get(listRuns.size());
//								}
//								agregaPorExtension(extension, listAnex.get(i).getFileName(), docx, run,
//										filePath, listAnex.get(i).getName());
//								conEnca++;
//							} else {
//								orden = listAnex.get(conEnca);
//								i = (conEnca-1);
//							}
//						}
//						cleanTagDoc(docx);
//					} else {
//						
//					}
//					FileOutputStream newDoc = new FileOutputStream(filePath+"\\"+draftRequisition.getName());
//					docx.write(newDoc);
//					newDoc.close();
//					LOG.info("FINALIZA ANEXO DE ARCHIVOS");				
//				} catch (Exception e) {
//					// TODO: handle exception
//					 e.getStackTrace();
//				}
//			} else {
//				LOG.info("El contrato no tiene anexos enlazados");
//			}
	    }
	}
	/**
	 * Metodo encargado de crear respaldo de documento.
	 * @param filePath
	 * @param draftRequisition
	 */
	public void docRespaldo(File filePath, File draftRequisition) {
		try {
			FileInputStream ficheroStream = new FileInputStream(draftRequisition);
			String nameFile = draftRequisition.getName().split(".docx")[0];
			XWPFDocument docRes = new XWPFDocument(ficheroStream);
			FileOutputStream docResp = new FileOutputStream(filePath+"\\"+nameFile+"_RESPALDO.docx");
			docRes.write(docResp);
	        docResp.close();
	        LOG.info("RESPALDO CREADO");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Obtiene XWPFRun correspondiente a la etiqueta.
	 * @param docx
	 * @param page
	 * @param nameAnex
	 * @return XWPFRun
	 */
	private XWPFRun getInfoDoc(XWPFDocument docx, String nameAnex) {
	    XWPFRun runParameter = null;
	    for (XWPFParagraph totParDoc : docx.getParagraphs()) {
	    	if (totParDoc.getParagraphText().contains(nameAnex)) {
	            if (totParDoc.getText().contains(nameAnex)) {
	                List<XWPFRun> listRuns = totParDoc.getRuns();
	                for (int index = 0; index < listRuns.size(); index++) {	                	
	                	if (index > 1) {
	                        final String runs = listRuns.get(index - 2).toString() + listRuns.get(index - 1).toString()
	                                + listRuns.get(index).toString();
	                        boolean findIt = 
	                        		runs.contains("[&" + nameAnex + "&]") ? true : false;
	                        if(findIt) {
	                        	runParameter = listRuns.get(index);
	                        }
	                    } else if(listRuns.size() == 1) {
	                    	final String runs = listRuns.get(index).toString();
	                        boolean findIt = 
	                        		runs.contains("[&" + nameAnex + "&]") ? true : false;
	                        if(findIt) {
	                        	runParameter = listRuns.get(index);
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return runParameter;
	}
	/**
	 * Método encargado de agregar anexo por tipo de extensión.
	 * @param extension
	 * @param anexName
	 * @param docx
	 * @param run
	 * @param filePath
	 */
	private void agregaPorExtension(String extension, String anexName, XWPFDocument docx, 
			XWPFRun run, File filePath, String tagName) {
		switch(extension) {
			case "JPG":
				LOG.info("SE AGREGA ANEXO JPG");
				agregaImagenPagina(docx, anexName, run, filePath, extension);
			break;
			case "JPEG":
				LOG.info("SE AGREGA ANEXO JPG");
				agregaImagenPagina(docx, anexName, run, filePath, extension);
			break;
			case "PNG":
				LOG.info("SE AGREGA ANEXO PNG");
				agregaImagenPagina(docx, anexName, run, filePath, extension);
			break;
			case "DOC":
				LOG.info("SE AGREGA ANEXO DOCX");
				mergueDocWord(docx, anexName, run, filePath, extension, tagName);
			break;
			case "DOCX":
				LOG.info("SE AGREGA ANEXO DOCX");
				mergueDocWord(docx, anexName, run, filePath, extension, tagName);
			break;
			case "PDF":
				LOG.info("SE AGREGA ANEXO PDF");
				agregarContenidoPdf(docx, anexName, run, filePath);
			break;
			default:
				break;
		}
	}
	/**
	 * Método encargado de agregar imagen al documento.
	 * @param docx
	 * @param nameAnex
	 * @param running
	 * @param filePath
	 * @param extension
	 */
	public void agregaImagenPagina(XWPFDocument docx, String nameAnex, 
			XWPFRun running, File filePath, String extension) {
        try {
        	XWPFRun run = running;
            final XWPFParagraph xwpfParagraph = (XWPFParagraph) run.getParent();
            int conPic = 0;
            List<CTDrawing> imgArray = run.getCTR().getDrawingList();
            for(CTDrawing neIm: imgArray) {
                conPic++;
            }
            int format = (extension.equals("PNG")) ? XWPFDocument.PICTURE_TYPE_PNG:XWPFDocument.PICTURE_TYPE_JPEG;
            File img = new File(filePath+"\\"+nameAnex);
            FileInputStream imgFile = new FileInputStream(img);
            if(conPic > 0) {
            	XWPFPicture lastImg = run.getEmbeddedPictures().get(conPic-1);
            	String nameImg = lastImg.getDescription();
            	boolean canSplit = nameImg.contains("-") ? true:false;
            	if(canSplit) {
	            	String isPdf = nameImg.split("-")[0];
	            	if(isPdf.equals("pdf") && isPdf.length() != 3) {
	            		run.addCarriageReturn();
		            	run.addBreak(BreakType.PAGE);
		                xwpfParagraph.addRun(run);
	            	}
            	} else {
            		run.addCarriageReturn();
	            	run.addBreak(BreakType.PAGE);
	                xwpfParagraph.addRun(run);
            	}
            }
            run.addBreak();
            run.addPicture(imgFile, format, nameAnex, Units.toEMU(500), Units.toEMU(500));
            imgFile.close();
            CTDrawing drawing = run.getCTR().getDrawingArray(conPic);
            CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
            CTAnchor anchor = getAnchorImg(graphicalobject, Units.toEMU(500), Units.toEMU(500), conPic, img.getName());
            drawing.setAnchorArray(new CTAnchor[]{anchor});
            drawing.removeInline(0);
        } catch (IOException ioEx) {
            LOG.info("Error: " +  ioEx.getMessage());
        } catch (InvalidFormatException ifEx) {
        	LOG.info("Error: " +  ifEx.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * Método encargado de agregar archivo PDF al documento.
	 * @param docBase
	 * @param nameAnex
	 * @param running
	 * @param filePath
	 */
	public void agregarContenidoPdf(XWPFDocument docBase, String nameAnex, 
			XWPFRun running, File filePath) {
        try {
            List<ConfigAnexoImg> anexos = obtenerAnexos(nameAnex, filePath);
            agregarImagenes(docBase, anexos, running);
        } catch (IOException ioEx) {
        	LOG.info("Error: " + ioEx.getMessage());
        } catch (InvalidFormatException ifEx) {
        	LOG.info("Error: " + ifEx.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * Método encargado de combinar archivos docs.
	 * @param doc
	 * @param nameAnex
	 * @param running
	 * @param filePath
	 */
	private void mergueDocWord(XWPFDocument doc, String nameAnex, 
			XWPFRun running, File filePath, String extension, String tagName) {
		File ficheroAnexo = new File(filePath + "\\" + nameAnex);
        XWPFDocument docAnex = convertirAWord(ficheroAnexo, filePath, nameAnex, extension);
        XWPFParagraph xwpfParagraph = (XWPFParagraph) running.getParent();
        int inicioMerge = 0;   
        for(int index = 0; index < doc.getParagraphs().size(); index++) {
            if (xwpfParagraph == doc.getParagraphs().get(index)) {
            	inicioMerge = (index + 1);
            }
        }
        if(inicioMerge == 0) { 
        	inicioMerge = doc.getParagraphs().size(); 
        	doc.createParagraph();
        }
        
        String etiqueta = cleanSpecificTag(running, tagName);
        XmlCursor cursor = doc.getParagraphs().get(inicioMerge - 1).getCTP().newCursor();
        cursor.toNextSibling();
        XWPFParagraph lastParDocAnex = doc.insertNewParagraph(cursor);
        XWPFRun lstRun = lastParDocAnex.createRun();
        lstRun.setText(etiqueta);
        lstRun.addCarriageReturn();
        lastParDocAnex.addRun(lstRun);
        try {
        	//XWPFParagraph paraPos = doc.getParagraphs().get(inicioMerge);
        	XWPFParagraph paraPos = lastParDocAnex;
            XmlCursor cursor1 = paraPos.getCTP().newCursor();
            XWPFParagraph parNewAdd = doc.insertNewParagraph(cursor1);
            getBodyElements(docAnex.getBodyElements(), doc, parNewAdd);
            
           /*String etiqueta = cleanSpecificTag(running, tagName);
            XmlCursor cursor = doc.getParagraphs().get(inicioMerge).getCTP().newCursor();
            cursor.toNextSibling();
            XWPFParagraph lastParDocAnex = doc.insertNewParagraph(cursor);
            //XWPFParagraph lastParDocAnex = doc.getParagraphs().get(inicioMerge);
            XWPFRun lstRun = lastParDocAnex.createRun();
            lstRun.setText(etiqueta);
            lstRun.addCarriageReturn();
            lstRun.addBreak(BreakType.PAGE);
            lastParDocAnex.addRun(lstRun);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
     * Metodo encargado de obtener los elementos del cuerpo.
     * @param bodyElements
     * @param resultBody
     * @param parNewAdd
     * @throws Exception
     */
    private void getBodyElements(List<IBodyElement> bodyElements, IBody resultBody, XWPFParagraph parNewAdd) throws Exception {
        for (IBodyElement bodyElement : bodyElements) {
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph)bodyElement;
                XWPFParagraph newParPPr = createParaPPr(paragraph, resultBody, parNewAdd);
                getRunElements(paragraph.getIRuns(), newParPPr);
            } else if (bodyElement instanceof XWPFSDT) {
                XWPFSDT sDT = (XWPFSDT)bodyElement;
                XWPFSDT resSDT = createSDT(sDT, resultBody);
            } else if (bodyElement instanceof XWPFTable) {
                XWPFTable table = (XWPFTable)bodyElement;
                XWPFTable resultTable = createTable(table, resultBody, parNewAdd);
                getTableRows(table.getRows(), resultTable);
            }
        }
    }

    /**
     * Obtiene filas de tablas.
     * @param tableRows
     * @param resultTable
     * @throws Exception
     */
    private void getTableRows(List<XWPFTableRow> tableRows, XWPFTable resultTable) throws Exception {
        for (XWPFTableRow tableRow : tableRows) {
            XWPFTableRow resultTableRow = createRowOnTable(tableRow, resultTable);
            getTableCells(tableRow.getTableICells(), resultTableRow);
        }
    }

    /**
     * Crea fila y valida cantidad de celdas.
     * @param tableRow
     * @param resultTable
     * @return
     */
    private XWPFTableRow createRowOnTable(XWPFTableRow tableRow, XWPFTable resultTable) {
        XWPFTableRow resultTableRow = resultTable.createRow();
        for (int i = resultTableRow.getTableCells().size(); i > 0; i--) {
            resultTableRow.removeCell(i-1);
        }
        resultTableRow.getCtRow().setTrPr(tableRow.getCtRow().getTrPr());
        return resultTableRow;
    }

    /**
     * Valida celdas dentro de una fila para su posterior creación.
     * @param tableICells
     * @param resultTableRow
     * @throws Exception
     */
    private void getTableCells(List<ICell> tableICells, XWPFTableRow resultTableRow) throws Exception {
        for (ICell tableICell : tableICells) {
            if (tableICell instanceof XWPFSDTCell) {
                XWPFSDTCell sDTCell = (XWPFSDTCell)tableICell;
                XWPFSDTCell resultSdtTableCell = createSdtCell(sDTCell, resultTableRow);
            } else if (tableICell instanceof XWPFTableCell) {
                XWPFTableCell tableCell = (XWPFTableCell)tableICell;
                XWPFTableCell resultTableCell = createCellTcPr(tableCell, resultTableRow);
                getBodyElements(tableCell.getBodyElements(), resultTableCell, null);
            }
        }
    }

    /**
     * Crea una celda en la fila.
     * @param sDTCell
     * @param resultTableRow
     * @return
     */
    private XWPFSDTCell createSdtCell(XWPFSDTCell sDTCell, XWPFTableRow resultTableRow) {
        XWPFTableCell resultTableCell = resultTableRow.createCell();
        return null;
    }

    /**
     * Cre una celda en fila con TcPr.
     * @param tableCell
     * @param resultTableRow
     * @return
     */
    private XWPFTableCell createCellTcPr(XWPFTableCell tableCell, XWPFTableRow resultTableRow) {
        XWPFTableCell resultTableCell = resultTableRow.createCell();
        resultTableCell.removeParagraph(0);
        resultTableCell.getCTTc().setTcPr(tableCell.getCTTc().getTcPr());
        return resultTableCell;
    }

    /**
     * Metodo encargado de crear tabla en caso de encontrarse una dentro
     * del documento.
     * @param table
     * @param resultBody
     * @param parNewAdd
     * @return
     */
    private XWPFTable createTable(XWPFTable table, IBody resultBody, XWPFParagraph parNewAdd) {
        if (resultBody instanceof XWPFDocument) {
            XWPFDocument resultDocument = (XWPFDocument)resultBody;
            XmlCursor cursor = parNewAdd.getCTP().newCursor();
            XWPFTable resultTable = resultDocument.insertNewTbl(cursor);
            resultTable.removeRow(0);
            resultTable.getCTTbl().setTblPr(table.getCTTbl().getTblPr());
            resultTable.getCTTbl().setTblGrid(table.getCTTbl().getTblGrid());
            handleStyles(resultDocument, table);
            return resultTable;
        }
        return null;
    }

    /**
     * Metodo encargado de gregar o crear un parrafo para
     * evitar que el documento se cree corrupto.
     * @param sDT
     * @param resultBody
     * @return
     */
    private XWPFSDT createSDT(XWPFSDT sDT, IBody resultBody) {
        if (resultBody instanceof XWPFDocument) {
            XWPFDocument resultDocument = (XWPFDocument)resultBody;
            XWPFParagraph resultParagraph = resultDocument.createParagraph();
        } else if (resultBody instanceof XWPFTableCell) {
            XWPFTableCell resultTableCell = (XWPFTableCell)resultBody;
            XWPFParagraph resultParagraph = resultTableCell.addParagraph();
        }
        return null;
    }

    /**
     * Crea un parrafo nuevo dentro del documento
     * con la ayuda del componente PPr.
     * @param paragraph
     * @param resultBody
     * @param parNewAdd
     * @return
     */
    private XWPFParagraph createParaPPr(XWPFParagraph paragraph, IBody resultBody, XWPFParagraph parNewAdd) {
        if (resultBody instanceof XWPFDocument) {
            XWPFDocument resultDocument = (XWPFDocument)resultBody;
            XWPFParagraph paraPos = parNewAdd;
            XmlCursor cursor1 = paraPos.getCTP().newCursor();
            XWPFParagraph subParAdd = resultDocument.insertNewParagraph(cursor1);
            XWPFParagraph resultParagraph = subParAdd;
            resultParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
            resultParagraph.getCTP().getPPr().setSpacing(paragraph.getCTP().getPPr().getSpacing());
            handleStyles(resultDocument, paragraph);
            handleNumberings(paragraph, resultParagraph);
            return resultParagraph;
        } else if (resultBody instanceof XWPFTableCell) {
            XWPFTableCell resultTableCell = (XWPFTableCell)resultBody;
            XWPFParagraph resultParagraph = resultTableCell.addParagraph();
            resultParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
            handleStyles(resultTableCell, paragraph);
            return resultParagraph;
        }
        return null;
    }

    /**
     * Obtiene la numeración de los elementos en
     * caso de traer para poder establecerla.
     * @param paragraph
     * @param resultParagraph
     */
    private void handleNumberings(XWPFParagraph paragraph, XWPFParagraph resultParagraph) {
    	BigInteger numID = paragraph.getNumID();
    	if(paragraph.getNumID() != null) {
    		numID = BigInteger.valueOf(paragraph.getNumID().intValue());
        }
        if (numID == null) return;
        BigInteger resultNumID = this.numIDs.get(numID);
        if (resultNumID == null) {
            XWPFDocument document = paragraph.getDocument();
            XWPFNumbering numbering = document.createNumbering();
            XWPFNum num = numbering.getNum(numID);
            if(num != null) {
	            BigInteger abstractNumID = numbering.getAbstractNumID(numID);
	            XWPFAbstractNum abstractNum = numbering.getAbstractNum(abstractNumID);
	            XWPFAbstractNum resultAbstractNum = new XWPFAbstractNum(
	                    (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum)
	                            abstractNum.getCTAbstractNum().copy());
	            XWPFDocument resultDocument = resultParagraph.getDocument();
	            XWPFNumbering resultNumbering = resultDocument.createNumbering();
	            BigInteger pos = resultNumbering.getAbstractNumID(abstractNumID);
	            resultAbstractNum.getCTAbstractNum().setAbstractNumId(pos);
	            BigInteger resultAbstractNumID = resultNumbering.addAbstractNum(resultAbstractNum);
	            resultNumID = resultNumbering.addNum(resultAbstractNumID);
	            XWPFNum resultNum = resultNumbering.getNum(resultNumID);
	            resultNum.getCTNum().setLvlOverrideArray(num.getCTNum().getLvlOverrideArray());
	            this.numIDs.put(numID, resultNumID);
            }
            resultParagraph.setNumID(resultNumID);
        }
    }

    /**
     * Obtiene los estilos de los elementos para poder alamacenarlos por ID
     * y posteriormente almacenarlos.
     * @param resultBody
     * @param bodyElement
     */
    private void handleStyles(IBody resultBody, IBodyElement bodyElement) {
        XWPFDocument document = null;
        String styleID = null;
        if (bodyElement instanceof XWPFParagraph) {
            XWPFParagraph paragraph = (XWPFParagraph)bodyElement;
            document = paragraph.getDocument();
            styleID = paragraph.getStyleID();
        } else if (bodyElement instanceof XWPFTable) {
            XWPFTable table = (XWPFTable)bodyElement;
            if (table.getPart() instanceof XWPFDocument) {
                document = (XWPFDocument)table.getPart();
                styleID = table.getStyleID();
            }
        }
        if (document == null || styleID == null || "".equals(styleID)) return;
        XWPFDocument resultDocument = null;
        if (resultBody instanceof XWPFDocument) {
            resultDocument = (XWPFDocument)resultBody;
        } else if (resultBody instanceof XWPFTableCell) {
            XWPFTableCell resultTableCell = (XWPFTableCell)resultBody;
            resultDocument = resultTableCell.getXWPFDocument();
        }
        if (resultDocument != null) {
            XWPFStyles styles = document.getStyles();
            XWPFStyles resultStyles = resultDocument.getStyles();
            XWPFStyle style = styles.getStyle(styleID);
            for (XWPFStyle relatedStyle : styles.getUsedStyleList(style)) {
                if (resultStyles.getStyle(relatedStyle.getStyleId()) == null) {
                    resultStyles.addStyle(relatedStyle);
                }
            }
        }
    }

    /**
     * Obtiene elementos de tipo Run.
     * @param runElements
     * @param resultRunBody
     * @throws Exception
     */
    private void getRunElements(List<IRunElement> runElements, IRunBody resultRunBody) throws Exception {
        for (IRunElement runElement : runElements) {
            if (runElement instanceof XWPFFieldRun) {
                XWPFFieldRun fieldRun = (XWPFFieldRun)runElement;
                XWPFFieldRun resultFieldRun = createFieldRunRPr(fieldRun, resultRunBody);
                getPictures(fieldRun, resultFieldRun);
            } else if (runElement instanceof XWPFHyperlinkRun) {
                XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun)runElement;
                XWPFHyperlinkRun resultHyperlinkRun = createHyperlinkRPr(hyperlinkRun, resultRunBody);
                getPictures(hyperlinkRun, resultHyperlinkRun);
            } else if (runElement instanceof XWPFRun) {
                XWPFRun run = (XWPFRun)runElement;
                XWPFRun resultRun = createRunRPr(run, resultRunBody);
                getPictures(run, resultRun);
            } else if (runElement instanceof XWPFSDT) {
                XWPFSDT sDT = (XWPFSDT)runElement;
            }
        }
    }

    /**
     * Crea campo con propiedad RPr.
     * @param fieldRun
     * @param resultRunBody
     * @return
     */
    private XWPFFieldRun createFieldRunRPr(XWPFFieldRun fieldRun, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph) {
            XWPFParagraph resultParagraph = (XWPFParagraph)resultRunBody;
            XWPFFieldRun resultFieldRun = (XWPFFieldRun)resultParagraph.createRun();
            resultFieldRun.getCTR().setRPr(fieldRun.getCTR().getRPr());
            handleRunStyles(resultParagraph.getDocument(), fieldRun);
            return resultFieldRun;
        }
        return null;
    }

    /**
     * Metodo que obtiene los estilos por ID y de igual forma los va estableciendo.
     * @param resultBody
     * @param runElement
     */
    private void handleRunStyles(IBody resultBody, IRunElement runElement) {
        XWPFDocument document = null;
        String styleID = null;
        if (runElement instanceof XWPFRun) {
            XWPFRun run = (XWPFRun)runElement;
            document = run.getDocument();
            styleID = run.getStyle();
        } else if (runElement instanceof XWPFHyperlinkRun) {
            XWPFHyperlinkRun run = (XWPFHyperlinkRun)runElement;
            document = run.getDocument();
            styleID = run.getStyle();
        } else if (runElement instanceof XWPFFieldRun) {
            XWPFFieldRun run = (XWPFFieldRun)runElement;
            document = run.getDocument();
            styleID = run.getStyle();
        }
        if (document == null || styleID == null || "".equals(styleID)) return;
        XWPFDocument resultDocument = null;
        if (resultBody instanceof XWPFDocument) {
            resultDocument = (XWPFDocument)resultBody;
        } else if (resultBody instanceof XWPFTableCell) {
            XWPFTableCell resultTableCell = (XWPFTableCell)resultBody;
            resultDocument = resultTableCell.getXWPFDocument();
        }
        if (resultDocument != null) {
            XWPFStyles styles = document.getStyles();
            XWPFStyles resultStyles = resultDocument.getStyles();
            XWPFStyle style = styles.getStyle(styleID);
            for (XWPFStyle relatedStyle : styles.getUsedStyleList(style)) {
                if (resultStyles.getStyle(relatedStyle.getStyleId()) == null) {
                    resultStyles.addStyle(relatedStyle);
                }
            }
        }
    }

    /**
     * Obtiene imagenes embebidas en el documento.
     * @param runElement
     * @param resultRunElement
     * @throws Exception
     */
    private void getPictures(IRunElement runElement, IRunElement resultRunElement) throws Exception {
        List<XWPFPicture> pictures = null;
        if (runElement instanceof XWPFFieldRun) {
            XWPFFieldRun fieldRun = (XWPFFieldRun)runElement;
            pictures = fieldRun.getEmbeddedPictures();
        } else if (runElement instanceof XWPFHyperlinkRun) {
            XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun)resultRunElement;
            pictures = hyperlinkRun.getEmbeddedPictures();
        } else if (runElement instanceof XWPFRun) {
            XWPFRun run = (XWPFRun)runElement;
            pictures = run.getEmbeddedPictures();
        } else if (runElement instanceof XWPFSDT) {
            XWPFSDT sDT = (XWPFSDT)runElement;
        }
        if (pictures != null) {
            for (XWPFPicture picture : pictures) {
                XWPFPictureData pictureData = picture.getPictureData();
                XWPFPicture resultPicture = checkPictureInstance(runElement, picture, pictureData, resultRunElement);
            }
        }
    }

    /**
     * Valida el tipo de imagen.
     * @param runElement
     * @param picture
     * @param pictureData
     * @param resultRunElement
     * @return
     */
    private XWPFPicture checkPictureInstance(IRunElement runElement, XWPFPicture picture, XWPFPictureData pictureData, IRunElement resultRunElement) {
        if (resultRunElement instanceof XWPFFieldRun) {
            XWPFFieldRun fieldRun = (XWPFFieldRun)runElement;
            XWPFFieldRun resultFieldRun = (XWPFFieldRun)resultRunElement;
            XWPFPicture resultPicture = createPictureDrawing(fieldRun, resultFieldRun, picture, pictureData);
            return resultPicture;
        } else if (resultRunElement instanceof XWPFHyperlinkRun) {
            XWPFHyperlinkRun hyperlinkRun = (XWPFHyperlinkRun)runElement;
            XWPFHyperlinkRun resultHyperlinkRun = (XWPFHyperlinkRun)resultRunElement;
            XWPFPicture resultPicture = createPictureDrawing(hyperlinkRun, resultHyperlinkRun, picture, pictureData);
            return resultPicture;
        } else if (resultRunElement instanceof XWPFRun) {
            XWPFRun run = (XWPFRun)runElement;
            XWPFRun resultRun = (XWPFRun)resultRunElement;
            XWPFPicture resultPicture = createPictureDrawing(run, resultRun, picture, pictureData);
            return resultPicture;
        } else if (resultRunElement instanceof XWPFSDT) {
            XWPFSDT sDT = (XWPFSDT)resultRunElement;
        }
        return null;
    }

    /**
     * Crea imagen con objeto drawing.
     * @param run
     * @param resultRun
     * @param picture
     * @param pictureData
     * @return
     */
    private XWPFPicture createPictureDrawing(XWPFRun run, XWPFRun resultRun, XWPFPicture picture, XWPFPictureData pictureData) {
        try {
            XWPFPicture resultPicture = resultRun.addPicture(
                    pictureData.getPackagePart().getInputStream(),
                    pictureData.getPictureType(),
                    pictureData.getFileName(),
                    Units.pixelToEMU((int)picture.getWidth()),
                    Units.pixelToEMU((int)picture.getDepth()));
            String rId = resultPicture.getCTPicture().getBlipFill().getBlip().getEmbed();
            resultRun.getCTR().setDrawingArray(0, run.getCTR().getDrawingArray(0));//simply copy the underlying XML bean to avoid more code
            String declareNameSpaces = "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main'; ";
            org.apache.xmlbeans.XmlObject[] selectedObjects = resultRun.getCTR().getDrawingArray(0).selectPath(
                    declareNameSpaces
                            + "$this//a:blip");
            for (org.apache.xmlbeans.XmlObject blipObject : selectedObjects) {
                if (blipObject instanceof org.openxmlformats.schemas.drawingml.x2006.main.CTBlip) {
                    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlip)blipObject;
                    if (blip.isSetEmbed()) blip.setEmbed(rId);
                }
            }
            selectedObjects = resultRun.getCTR().getDrawingArray(0).selectPath(
                    declareNameSpaces
                            + "$this//a:hlinkClick");
            for (org.apache.xmlbeans.XmlObject hlinkClickObject : selectedObjects) {
                if (hlinkClickObject instanceof org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink) {
                    org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink hlinkClick =
                            (org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink)hlinkClickObject;
                    if (hlinkClick.isSetId()) hlinkClick.setId("");
                }
            }
            return resultPicture;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Crea hipervinculo con ayuda de RPr.
     * @param hyperlinkRun
     * @param resultRunBody
     * @return
     */
    private XWPFHyperlinkRun createHyperlinkRPr(XWPFHyperlinkRun hyperlinkRun, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph) {
            XWPFParagraph resultParagraph = (XWPFParagraph)resultRunBody;
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink resultCTHyperLink = resultParagraph.getCTP().addNewHyperlink();
            resultCTHyperLink.addNewR();
            XWPFHyperlinkRun resultHyperlinkRun =  new XWPFHyperlinkRun(resultCTHyperLink, resultCTHyperLink.getRArray(0), resultParagraph);
            if (hyperlinkRun.getAnchor() != null) {
                resultHyperlinkRun = resultParagraph.createHyperlinkRun(hyperlinkRun.getAnchor());
            }
            resultHyperlinkRun.getCTR().setRPr(hyperlinkRun.getCTR().getRPr());//
            copyText(hyperlinkRun, resultHyperlinkRun);
            handleRunStyles(resultParagraph.getDocument(), hyperlinkRun);
            return resultHyperlinkRun;
        } else if (resultRunBody instanceof XWPFSDT) {

        }
        return null;
    }

    /**
     * Metodo encargado de hacer copia de texto dentro de runs.
     * @param run
     * @param resultRun
     */
    private void copyText(XWPFRun run, XWPFRun resultRun) {
        for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
            resultRun.setText(run.getText(i), i);           
            if(run.getFontFamily() == null){
                String styleID = run.getParagraph().getStyleID();
                XWPFDocument doc = run.getParagraph().getDocument();
                XWPFStyles styles = doc.getStyles();
                XWPFStyle style = styles.getStyle(styleID);
                if (style != null) {                	
                	CTStyle ctStyle = style.getCTStyle();
                	CTRPr ctrPr = ctStyle.getRPr();
                	CTFonts ctFonts = ctrPr.getRFontsArray(i);
                	if(ctFonts!= null){
                		String fontFamily = ctFonts.getAscii();
                		resultRun.setFontFamily(fontFamily);
                		resultRun.getCTR().getRPr().getRFontsArray(i).setHAnsi(fontFamily);
                	}
                }
            }
        }
    }

    /**
     * Crea un nuevo run con la propiedad Rpr.
     * @param run
     * @param resultRunBody
     * @return
     */
    private XWPFRun createRunRPr(XWPFRun run, IRunBody resultRunBody) {
        if (resultRunBody instanceof XWPFParagraph) {
            XWPFParagraph resultParagraph = (XWPFParagraph)resultRunBody;
            XWPFRun resultRun = resultParagraph.createRun();
            resultRun.getCTR().setRPr(run.getCTR().getRPr());
            copyText(run, resultRun);
            handleRunStyles(resultParagraph.getDocument(), run);
            return resultRun;
        }
        return null;
    }
	 
	
	/**
	 * Convierte archivo anexado a XWPFDocument.
	 * @param archivoWord
	 * @return XWPFDocument
	 */
	public XWPFDocument convertirAWord(File archivoWord, File filePath, String name, String extension) {
        XWPFDocument documentoWord = null;
        final LocalOfficeManager officeManager = LocalOfficeManager.install();
        try {
        	if(extension.equals("DOC")) {
        		String nombreDoc = name.substring(0, name.indexOf("."));
                officeManager.start();
                JodConverter
                         .convert(archivoWord)
                         .to(new File(filePath  + "\\" + nombreDoc + ".docx"))
                         .execute();
                InputStream texto = new FileInputStream(filePath  + "\\" + nombreDoc +".docx");
                documentoWord = new XWPFDocument(texto);
        	} else {
        		InputStream texto = new FileInputStream(archivoWord);
                documentoWord = new XWPFDocument(texto);
        	}
            
        } catch (IOException | OfficeException e) {
        	LOG.info("Error al convertir a Docx");
            e.printStackTrace();
        } finally {
            OfficeUtils.stopQuietly(officeManager);
        }
        return documentoWord;
    }
	/**
	 * Método encargado de obtener el ancho y posicion de la imagen.
	 * @param graphicalobject
	 * @param width
	 * @param height
	 * @param conPic
	 * @param name
	 * @return CTAnchor
	 * @throws Exception
	 */
	private CTAnchor getAnchorImg(CTGraphicalObject graphicalobject, int width, int height,
            int conPic, String name) throws Exception {
		String anchorXML =
		"<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
		     +"distT=\"0\" distB=\"0\" distL=\"114300\" distR=\"114300\" simplePos=\"0\" relativeHeight=\"251658240\" behindDoc=\"0\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
		     +"<wp:simplePos x=\"1990725\" y=\"895350\"/>"
		     +"<wp:positionH relativeFrom=\"page\"><wp:align>center</wp:align></wp:positionH>"
		     +"<wp:positionV relativeFrom=\"page\"><wp:align>center</wp:align></wp:positionV>"
		     + "<wp:extent cx=\""+width+"\" cy=\""+height+"\"/>"
		     +"<wp:effectExtent l=\"0\" t=\"0\" r=\"6350\" b=\"6350\"/><wp:wrapSquare wrapText=\"bothSides\"/>"
		     +"<wp:docPr id=\""+conPic+"\" name=\"Drawing "+conPic+"\" descr=\""+name+"\"/><wp:cNvGraphicFramePr/>"
		     +"</wp:anchor>";
		
		CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
		CTAnchor anchor = drawing.getAnchorArray(0);
		anchor.setGraphic(graphicalobject);
		return anchor;
	}
	/**
	 * Método de obtine el pdf convertido a imagen.
	 * @param docName
	 * @param filePath
	 * @return
	 */
	public List<ConfigAnexoImg> obtenerAnexos(String docName, File filePath) {
		LocalDate hoy = LocalDate.now();
        String carpetaImg = hoy.toString() + "_" + docName;
        String temImagen = filePath + "\\" + carpetaImg + "\\";
        crearDirectorio(temImagen);
        String ruta = filePath + "\\" + docName;
        List<ConfigAnexoImg> imagenes = new ArrayList<ConfigAnexoImg>();
        try {
            imagenes = generateImageFromPDF(ruta, "jpg", temImagen);
        } catch (Exception e) {
        	LOG.info("Error: " + e.getMessage());
        }
        return imagenes;
    }
	/**
	 * Método encargado de crear directorio para imagenes del archivo pdf.
	 * @param ruta
	 */
	private void crearDirectorio (String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
            	LOG.info("Directorio creado");
            } else {
            	LOG.info("Error al crear directorio");
            }
        }
    }
	/**
	 * Método que regresa lista de imagenes creadas.
	 * @param filename
	 * @param extension
	 * @param rutaImagen
	 * @return List<ConfigAnexoImg>
	 * @throws IOException
	 */
	private List<ConfigAnexoImg> generateImageFromPDF(String filename, String extension, String rutaImagen) throws IOException {
        List<ConfigAnexoImg> imagenes = new ArrayList<ConfigAnexoImg>();
        PDDocument document = PDDocument.load(new File(filename));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            ImageIOUtil.writeImage(bim, String.format(rutaImagen + "pdf-%d.%s", page + 1, extension), 300);
            imagenes.add(new ConfigAnexoImg(page, String.format(rutaImagen + "pdf-%d.%s", page + 1, extension), ("pdf-%d.%s"+(page + 1))));
        }
        document.close();
        return imagenes;
    }
	/**
	 * Método que agrega imagenes del pdf al documento.
	 * @param docBase
	 * @param anexos
	 * @param running
	 * @throws Exception
	 */
	private void agregarImagenes (XWPFDocument docBase, List<ConfigAnexoImg> anexos, XWPFRun running) throws Exception {
		XWPFRun run = running;
        final XWPFParagraph xwpfParagraph = (XWPFParagraph) run.getParent();
        //xwpfParagraph.removeRun(1);
        int conPic = 0;
        List<CTDrawing> imgArray = run.getCTR().getDrawingList();
        for(CTDrawing neIm: imgArray) {
            conPic++;
        }
        for (ConfigAnexoImg img : anexos) {
            InputStream pic1 = new FileInputStream(img.getRutaArchivo());
            if (anexos.size() != 1) {  
             run.addBreak();
            }
            //run.addPicture(pic1, Document.PICTURE_TYPE_JPEG, img.getName(), 610,660);
            if (conPic < anexos.size() -1){
            	run.addPicture(pic1, Document.PICTURE_TYPE_JPEG, img.getName(), 290,310);            	
            } else {
            	run.addPicture(pic1, Document.PICTURE_TYPE_JPEG, img.getName(), Units.toEMU(610), Units.toEMU(632));   
            }
            pic1.close();
            CTDrawing drawing = run.getCTR().getDrawingArray(conPic);
            CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
            
            if (conPic < anexos.size() -1){
                CTAnchor anchor = getAnchor(graphicalobject, conPic, img.getName());
                drawing.setAnchorArray(new CTAnchor[]{anchor}); 
            }  else {
                CTAnchor anchor = getAnchor1(graphicalobject, conPic, img.getName());
                drawing.setAnchorArray(new CTAnchor[]{anchor}); 
            }  
            
            drawing.removeInline(0);
            conPic++;
        }

    }

	/**
	 * Método que obtiene tamaño y posición para imagenes de pdf.
	 * @param graphicalobject
	 * @param conPic
	 * @param name
	 * @return CTAnchor
	 * @throws Exception
	 */
	private CTAnchor getAnchor(CTGraphicalObject graphicalobject, int conPic, String name) throws Exception {
        String anchorXML =
                "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                        +"distT=\"0\" distB=\"0\" distL=\"114300\" distR=\"114300\" simplePos=\"0\" relativeHeight=\"251658240\" behindDoc=\"0\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                        +"<wp:simplePos x=\"1990725\" y=\"895350\"/>"
                        +"<wp:positionH relativeFrom=\"page\"><wp:posOffset>0</wp:posOffset></wp:positionH>"
                        +"<wp:positionV relativeFrom=\"page\"><wp:posOffset>0</wp:posOffset></wp:positionV>"
                        + "<wp:extent cx=\"7747000\" cy=\"9017000\"/>"
                        +"<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/><wp:wrapSquare wrapText=\"bothSides\"/>"
                        +"<wp:docPr id=\""+conPic+"\" name=\"Drawing "+(conPic)+"\" descr=\""+name+"\"/><wp:cNvGraphicFramePr/>"
                        +"</wp:anchor>";

        CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(graphicalobject);
        return anchor;
    }
	private CTAnchor getAnchor1(CTGraphicalObject graphicalobject, int conPic, String name) throws Exception {
        String anchorXML =
                "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                        +"distT=\"0\" distB=\"0\" distL=\"114300\" distR=\"114300\" simplePos=\"0\" relativeHeight=\"251658240\" behindDoc=\"0\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                        +"<wp:simplePos x=\"1990725\" y=\"895350\"/>"
                        +"<wp:positionH relativeFrom=\"page\"><wp:posOffset>0</wp:posOffset></wp:positionH>"
                        +"<wp:positionV relativeFrom=\"page\"><wp:posOffset>0</wp:posOffset></wp:positionV>"
                        + "<wp:extent cx=\"7747000\" cy=\"8026400\"/>"
                        +"<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/><wp:wrapSquare wrapText=\"bothSides\"/>"
                        +"<wp:docPr id=\""+conPic+"\" name=\"Drawing "+(conPic)+"\" descr=\""+name+"\"/><wp:cNvGraphicFramePr/>"
                        +"</wp:anchor>";

        CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(graphicalobject);
        return anchor;
    }
	/**
	 * Método encargado de quitar etiquetas en el documento.
	 * @param docBase
	 */
	public void cleanTagDoc(XWPFDocument docBase) {
		try {
			List<TagField> res = this.anexable.findTagsAnexos();
			for(TagField tag: res) {
				XWPFRun run = getInfoDoc(docBase, tag.getField());
				if(run != null) {
					XWPFParagraph xwpfParagraph = (XWPFParagraph) run.getParent();
					List<XWPFRun> xwpfRunList = xwpfParagraph.getRuns();
					for (int index = 0; index < xwpfRunList.size(); index++) {
						if(index > 1) {
							String textRun = xwpfRunList.get(index - 2).toString() + xwpfRunList.get(index - 1).toString()
				                    + xwpfRunList.get(index).toString();
							if(textRun.contains(tag.getTag())) {
								xwpfRunList.get(index - 2).setText(xwpfRunList.get(index - 2)
										.toString().replace("[&", ""), 0);
								xwpfRunList.get(index-1).setText(xwpfRunList.get(index - 1)
										.toString().replace(tag.getField(), ""), 0);
								xwpfRunList.get(index).setText(xwpfRunList.get(index)
										.toString().replace("&]", ""), 0);
								xwpfParagraph.removeRun(1);
							}
						}  else if(xwpfRunList.size() == 1) {
	                    	final String textRun = xwpfRunList.get(index).toString();
	                    	if(textRun.contains(tag.getTag())) {
								xwpfRunList.get(index).setText(xwpfRunList.get(index)
										.toString().replace("[&"+tag.getField()+"&]", ""), 0);;
								xwpfParagraph.removeRun(1);
							}
	                    }
			        }
				}
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Método encargado de quitar etiquetas especifica en el documento.
	 * @param docBase
	 */
	public String cleanSpecificTag(XWPFRun run, String tagName) {
		String etiqueta = "[&"+tagName+"&]";
		if(run != null) {
			XWPFParagraph xwpfParagraph = (XWPFParagraph) run.getParent();
			List<XWPFRun> xwpfRunList = xwpfParagraph.getRuns();
			for (int index = 0; index < xwpfRunList.size(); index++) {
				if(index > 1) {
					String textRun = xwpfRunList.get(index - 2).toString() + xwpfRunList.get(index - 1).toString()
		                    + xwpfRunList.get(index).toString();
					if(textRun.contains(("[&" + tagName + "&]"))) {
						xwpfRunList.get(index - 2).setText(xwpfRunList.get(index - 2)
								.toString().replace("[&", ""), 0);
						xwpfRunList.get(index-1).setText(xwpfRunList.get(index - 1)
								.toString().replace(tagName, ""), 0);
						xwpfRunList.get(index).setText(xwpfRunList.get(index)
								.toString().replace("&]", ""), 0);
						xwpfParagraph.removeRun(1);
					}
				}  else if(xwpfRunList.size() == 1) {
		        	final String textRun = xwpfRunList.get(index).toString();
		        	if(textRun.contains(("[&" + tagName + "&]"))) {
						xwpfRunList.get(index).setText(xwpfRunList.get(index)
								.toString().replace("[&"+tagName+"&]", ""), 0);;
						xwpfParagraph.removeRun(1);
					}
		        }
		    }
		}
		return etiqueta;
	}
	
}
