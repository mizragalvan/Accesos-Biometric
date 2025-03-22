package mx.pagos.admc.core.business.digitalsignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.business.digitalsignature.ApiRestUtils;
import mx.pagos.admc.contracts.business.digitalsignature.pscworldapi.PscWorld;
import mx.pagos.admc.contracts.business.digitalsignature.pscworldapi.PscWorldApiClient;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.digitalsignature.DocumentPscWorld;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.util.shared.Constants;


@Service
public class PscWorldBusiness {
	
	@Autowired
	private Configurable configurable;
	
	private static final Logger LOG = Logger.getLogger(PscWorldBusiness.class);
	
	public File generateConservationCertificate(File fileToGenerate, Integer idRequisition, String fileCertificatePath)
			throws Exception {
		try {
			
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_URL_API);
			String bearerToken = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_BEARER_TOKEN);
			
			String username = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_PASSWORD);
			
			PscWorldApiClient apiClient = new PscWorldApiClient(bearerToken);
			apiClient.setUrl(apiUrl);
			PscWorld pscWorld = new PscWorld(apiClient);
			
			String base64Hash = calculateFileHash(fileToGenerate.getPath());
	            
            DocumentPscWorld documentPscWorld = new DocumentPscWorld();
			documentPscWorld.setUsuario(username);
			documentPscWorld.setPassword(password);
			documentPscWorld.setHash(base64Hash);
			documentPscWorld.setIdentificador(idRequisition.toString());
			
			String responseApi = pscWorld.save(documentPscWorld);
			
			byte[] decodedBytes = Base64.getDecoder().decode(responseApi);
			try (OutputStream stream = new FileOutputStream(fileCertificatePath)) {
			    stream.write(decodedBytes);
			}
			
			return new File(fileCertificatePath);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	private String calculateFileHash(String filePath) throws IOException, NoSuchAlgorithmException {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;
			MessageDigest md = MessageDigest.getInstance(Constants.ALGORITHM_SHA_256);

			try (FileInputStream fis = new FileInputStream(filePath)) {
				while ((bytesRead = fis.read(buffer)) != -1) {
					md.update(buffer, 0, bytesRead);
				}
			}

			byte[] fileHash = md.digest();
			return Base64.getEncoder().encodeToString(fileHash);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	
	public File getConservationCertificate(DocumentDS documentDS) throws Exception {
		try {
			
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_URL_API);
			String bearerToken = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_BEARER_TOKEN);

			String username = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_PSC_WORLD_PASSWORD);

			PscWorldApiClient apiClient = new PscWorldApiClient(bearerToken);
			apiClient.setUrl(apiUrl);
			PscWorld pscWorld = new PscWorld(apiClient);
			
			DocumentPscWorld documentPscWorld = new DocumentPscWorld();
			documentPscWorld.setUsuario(username);
			documentPscWorld.setPassword(password);
			documentPscWorld.setIdentificador(documentDS.getIdRequisition().toString());
			
			String responseApi = pscWorld.find(documentPscWorld);
			
			return ApiRestUtils.createAsn1FileFromBase64(documentDS.getDocumentName(), responseApi); 
			
		} catch (Exception exception) {
			
			if (exception.getMessage().contains(Constants.COD_ERROR_NOT_FOUND_PSC)) {
				LOG.info("No existe un certificado de conservación con la clave proporcionada: " + documentDS.getIdRequisition());
				return null;
			}
			
			exception.printStackTrace();
			throw exception;
		}
	}

	
}
