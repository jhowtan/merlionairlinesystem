package MAS.Bean;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;

@Stateless(name = "MailEJB")
@LocalBean
public class MailBean {

    @Resource(name = "mail/MAS")
    private Session session;

    public MailBean() {
    }

    public boolean send(String recipientEmail, String recipientName, String subject, String body) {
        if (recipientEmail.contains("@example.com")) {
            // Ignore emails to this domain
            return true;
        }
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(session.getProperty("mail.from"), "Merlion Airlines"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail, recipientName));
            msg.setSubject(subject);
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(body);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (MessagingException|UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
