package mx.pagos.util.mail;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import mx.pagos.util.mail.structure.EmailBean;
import mx.pagos.util.mail.structure.SmtpServerConfigurations;

@Service
public class EmailThreadPool extends Object {
//    private static final String GREATER_THAN_CHARACTER = ">";
//    private static final String LESS_THAN_CHARACTER = "<";
//    private static final String CONTENT_ID = "Content-ID";
    private static final Logger LOG = Logger.getLogger(EmailThreadPool.class);
    
    public static Runnable makeRunnable(final EmailBean email,
            final SmtpServerConfigurations smtpServerConfigurations) {
        return new Runnable() {
            public void run() {
//                LOG.info("Inicia servicio de envío de correo...");
//                try {
//    		        LOG.debug("Cargando configuraciones para envío...");
//    		        final Properties smtpProperties = new Properties();
//    		        setServerConfigurations(smtpProperties, smtpServerConfigurations);
//                    final Message msg = this.configureEmailMessage(smtpProperties, smtpServerConfigurations,
//                            smtpMailConfigurations);
//    				Transport.send(msg, msg.getRecipients(Message.RecipientType.TO));
//                    LOG.info("Correo enviado exitosamente a " + email.getTo());
//    			} catch (MessagingException messagingException) {
//                    LOG.error("Error al envíar el correo a " + email.getTo(), messagingException);
//    			} catch (IOException iOException) {
//                    LOG.error("Error al procesar el o los archivo(s) adjunto(s) del correo enviado a " + email.getTo(),
//                            iOException);
//    			}
            }
            
//            private void setServerConfigurations(final Properties smtpProperties,
//                    final SmtpServerConfigurations smtpServerConfigurations) {
//                smtpProperties.put("mail.transport.protocol", smtpServerConfigurations.getProtocol());
//                smtpProperties.put("mail.smtp.host", smtpServerConfigurations.getHost());
//                smtpProperties.put("mail.smtp.port", smtpServerConfigurations.getPort());
//                smtpProperties.put("mail.smtp.user", smtpServerConfigurations.getUser());
//            }
//
//            private Message configureEmailMessage(final Properties smtpProperties,
//                    final SmtpServerConfigurations smtpServerConfigurations,
//                    final SmtpMailConfigurations smtpMailCountConfigurations)
//                    throws MessagingException, AddressException, IOException {
//                final Message msg = new MimeMessage(this.createMailSession(smtpProperties, smtpServerConfigurations,
//                        smtpMailCountConfigurations));
//                msg.setSubject(email.getSubject());
//                msg.setFrom(new InternetAddress(smtpServerConfigurations.getMailDirectory()));
//                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
//                msg.setSentDate(new Date());
//                msg.setContent(this.createMimeMultiPart(smtpMailCountConfigurations));
//                return msg;
//            }
//
//            private Session createMailSession(final Properties smtpProperties,
//                    final SmtpServerConfigurations smtpServerConfigurations,
//                    final SmtpMailConfigurations smtpMailCountConfigurations) {
//                Session mailSession;
//                smtpProperties.put("mail.smtp.starttls.enable",
//                        smtpMailCountConfigurations.getTransportLayerSecurity());
//                smtpProperties.put("mail.smtp.password", smtpMailCountConfigurations.getPassword());
//                if (smtpMailCountConfigurations.getAuthentication()) {
//                	smtpProperties.put("mail.smtp.auth", smtpMailCountConfigurations.getAuthentication());
//                    mailSession = Session.getInstance(smtpProperties, new SmtpAuthenticator(smtpServerConfigurations.
//                	        getUser(), smtpMailCountConfigurations.getPassword()));
//                } else
//                	mailSession = Session.getInstance(smtpProperties, null);
//                mailSession.setDebug(false);
//                return mailSession;
//            }
//	                	
//            private MimeMultipart createMimeMultiPart(final SmtpMailConfigurations smtpMailCountConfigurations)
//                    throws MessagingException, IOException {
//                final MimeMultipart mimeMultipart = new MimeMultipart();
//                mimeMultipart.addBodyPart(this.processEmailContent(email));
//                this.processEmailImagesAndAttachements(email, mimeMultipart);
//                return mimeMultipart;
//            }
//
//            private MimeBodyPart processEmailContent(final EmailBean mail)
//                    throws MessagingException {
//                final MimeBodyPart bodyPart = new MimeBodyPart();
//                bodyPart.setContent(mail.getEmailTextContent(), "text/html;charset=\"iso-8859-1\"");
//                return bodyPart;
//            }
//
//            private void processEmailImagesAndAttachements(final EmailBean mail, final MimeMultipart mimeMultipart)
//                    throws MessagingException, IOException {
//                if (mail.getImages() != null)
//                    processEmailImages(mail, mimeMultipart);
//                if (mail.getAttachements() != null)
//                    this.processEmailAttachments(mail, mimeMultipart);
//            }
//
//            private void processEmailAttachments(final EmailBean mail, final MimeMultipart mimeMultipart)
//                    throws IOException, MessagingException {
//                for (ArchivoAdjuntoBean archivo: mail.getAttachements()) {
//                    final MimeBodyPart mimeBodyPart = new MimeBodyPart();
//                    mimeBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(archivo.getContent(),
//                            archivo.getMimeType())));
//                    mimeBodyPart.setFileName(archivo.getNombre());
//                    mimeBodyPart.setHeader(CONTENT_ID, LESS_THAN_CHARACTER + archivo.getNombre()
//                            + GREATER_THAN_CHARACTER);
//                    mimeMultipart.addBodyPart(mimeBodyPart);
//                }
//            }
//
//            private void processEmailImages(final EmailBean mail, final MimeMultipart mimeMultipart)
//                    throws MessagingException {
//                for (ImagesBean image : mail.getImages()) {
//                    final MimeBodyPart mimeBodyPart = new MimeBodyPart();
//                    mimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(image.getPath().
//                            replace("file:/", ""))));
//                    mimeBodyPart.setHeader(CONTENT_ID, LESS_THAN_CHARACTER + image.getName()
//                            + GREATER_THAN_CHARACTER);
//                    mimeMultipart.addBodyPart(mimeBodyPart);
//                }
//            }
//            
//            class SmtpAuthenticator extends javax.mail.Authenticator {
//    			private String username;
//    			private String password;
//    			
//    			public SmtpAuthenticator(final String usernameParameter, final String passwordParameter) {
//    				this.username = usernameParameter;
//    				this.password = passwordParameter;
//    			}
//    			
//    			public PasswordAuthentication getPasswordAuthentication() {
//    				return new PasswordAuthentication(this.username, this.password);
//    			}
//    		}	
        };
    }
}
