package mx.pagos.admc.contracts.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.structures.InitialSystemConfiguration;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.User;

@Service
public class InitialSystemConfigurationBusiness {
    @Autowired
    private ConfigurationsBusiness configurationBusiness;
    
    @Autowired
    private UsersBusiness usersBusiness;
    
    public Boolean isFirstApplicationRun() throws BusinessException {
        return Boolean.valueOf(this.configurationBusiness.findByName(
                ConfigurationEnum.IS_FISRT_APPLICATION_RUN.toString()));
    }
    
    public void saveApplicationConfigured() throws BusinessException {
        this.configurationBusiness.update(ConfigurationEnum.IS_FISRT_APPLICATION_RUN.toString(), 
        		new Boolean(false).toString());
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
    public void saveInitialSystemConfiguration(final InitialSystemConfiguration initialSystemConfiguration)
            throws BusinessException {
        this.saveMailConfiguration(initialSystemConfiguration);
        this.saveLdapConfiguration(initialSystemConfiguration);
        this.saveLdapAdministratorUser(initialSystemConfiguration);
        this.saveLdapSecurityAdministratorUser(initialSystemConfiguration);
        this.saveApplicationConfigured();
    }

    private void saveMailConfiguration(final InitialSystemConfiguration initialSystemConfiguration)
                    throws BusinessException {
        this.configurationBusiness.update(ConfigurationEnum.SMTP_AUTHENTICATION.toString(),
                initialSystemConfiguration.getSmtpAuthentication());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_EMAIL.toString(),
                initialSystemConfiguration.getSmtpEmail());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_HOST.toString(),
                initialSystemConfiguration.getSmtpHost());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_PASSWORD.toString(),
                initialSystemConfiguration.getSmtpPassword());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_PORT.toString(),
                initialSystemConfiguration.getSmtpPort());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_PROTOCOL.toString(),
                initialSystemConfiguration.getSmtpProtocol());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_SSL.toString(),
                initialSystemConfiguration.getSmtpSsl());
        this.configurationBusiness.update(ConfigurationEnum.SMTP_TLS.toString(),
                initialSystemConfiguration.getSmtpTls());
    }
    
    private void saveLdapConfiguration(final InitialSystemConfiguration initialSystemConfiguration)
            throws BusinessException {
        if (initialSystemConfiguration.getBaseLdap() != null) {
            this.configurationBusiness.update(ConfigurationEnum.BASE_LDAP.toString(),
                    initialSystemConfiguration.getBaseLdap());
            this.configurationBusiness.update(ConfigurationEnum.PASSWORD_LDAP.toString(),
                    initialSystemConfiguration.getPasswordLdap());
            this.configurationBusiness.update(ConfigurationEnum.URL_LDAP.toString(),
                    initialSystemConfiguration.getUrlLdap());
            this.configurationBusiness.update(ConfigurationEnum.USER_LDAP.toString(),
                    initialSystemConfiguration.getUserLdap());
        }
    }
    
    private void saveLdapAdministratorUser(final InitialSystemConfiguration initialSystemConfiguration)
            throws BusinessException {
        this.saveLdapUser(initialSystemConfiguration.getAdministratorUserName(), 1);
    }
    
    private void saveLdapSecurityAdministratorUser(final InitialSystemConfiguration initialSystemConfiguration)
            throws BusinessException {
        this.saveLdapUser(initialSystemConfiguration.getSecurityAdministratorUserName(), 2);
    }
    
    private void saveLdapUser(final String username, final Integer idProfile) throws BusinessException {
        if (username != null) {
            final User user = new User();
            user.setIsActiveDirectoryUser(true);
            user.setUsername(username);
            user.setName(username);
            user.setFirstLastName("");
            user.setEmail("configurarEmail" + idProfile + "@Configurable.com");
            user.setIsLawyer(false);
            user.setIsEvaluator(false);
            user.setIsDecider(false);
            final List<Profile> profilesList = new ArrayList<>();
            profilesList.add(new Profile(idProfile));
            user.setProfileList(profilesList);
            this.usersBusiness.saveOrUpdate(user);
        }
    }
}
