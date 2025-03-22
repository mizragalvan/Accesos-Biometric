package mx.pagos.admc.contracts.daos;
//package mx.contratos.admc.contracts.daos;
//
//import static org.springframework.ldap.query.LdapQueryBuilder.query;
//
//import javax.naming.NamingException;
//
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.ldap.core.ContextMapper;
//import org.springframework.ldap.core.DirContextAdapter;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.stereotype.Repository;
//
//import mx.contratos.admc.contracts.interfaces.Ldapable;
//import mx.contratos.admc.contracts.structures.LdapConnectionParameters;
//import mx.contratos.admc.util.shared.LdapConstants;
//import mx.contratos.general.exceptions.BusinessException;
//import mx.contratos.security.structures.User;
//
//@Repository("LdapDAO")
//public class LdapDAO implements Ldapable {
//
//	private static final String MESSAGE_ERROR_FIND_USER_AD = "No se encontró el usuario de Active Directory";
//
//	private LdapTemplate ldapTemplate;
//	
//	public void setLdapTemplate(final LdapTemplate ldapTemplateParameter) {
//		if (this.ldapTemplate == null) {
//			this.ldapTemplate = ldapTemplateParameter;
//		}
//	}
//	
//	@Override
//    public void createConectionWithLdap(final LdapConnectionParameters ldapConnectionParameters) 
//    		throws BusinessException {
//		
//		final org.springframework.ldap.core.support.LdapContextSource ctxSrc = 
//				new org.springframework.ldap.core.support.LdapContextSource();
//	    ctxSrc.setUrl(ldapConnectionParameters.getUrl());
//	    ctxSrc.setBase(ldapConnectionParameters.getBase());
//	    ctxSrc.setUserDn(ldapConnectionParameters.getUserDn());
//	    ctxSrc.setPassword(ldapConnectionParameters.getPassword());
//	    ctxSrc.afterPropertiesSet();
//	    
//	    final LdapTemplate tmpl = new LdapTemplate(ctxSrc);
//	    tmpl.setIgnorePartialResultException(true);
//	    this.setLdapTemplate(tmpl);
//	}
//	
//	@Override
//	public User findUserActiveDirectory(final String name) throws BusinessException {
//		try {
//			return this.ldapTemplate.searchForObject(query().where(LdapConstants.sAMAccountType).
//                    is(LdapConstants.sAMAccountType_Value).and(query().where(LdapConstants.sAMAccountName).
//							is(name)), new UserContext());
//		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
//			throw new BusinessException(MESSAGE_ERROR_FIND_USER_AD, emptyResultDataAccessException);
//		}
//	}
//
//	private class UserContext implements ContextMapper<User> {
//		@Override
//		public User mapFromContext(final Object ctx) throws NamingException {
//			final DirContextAdapter adapter  = (DirContextAdapter) ctx;
//			final User bean = new User();
//			bean.setName(adapter.getStringAttribute(LdapConstants.name));
//			bean.setUsername(adapter.getStringAttribute(LdapConstants.sAMAccountName));
//			return bean;
//		}
//	}
//}
