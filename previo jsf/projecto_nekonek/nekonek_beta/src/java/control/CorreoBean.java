package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import modelo.Usuario;

@ManagedBean
@ViewScoped
public class CorreoBean {
    private String asunto;
    private String contmensaje;
    private List<Usuario> listaUsr;   // todos los usuarios
    private List<Usuario> seleccionados; // usuarios seleccionados

    // 🔹 Se ejecuta al iniciar el bean
    @PostConstruct
    public void init() {
        listarUsuarios(); // carga usuarios automáticamente
    }

    // 🔹 Cargar usuarios desde BD
    public void listarUsuarios() {
        listaUsr = new ArrayList<>();
        String sql = "SELECT u_nombre, u_email FROM usuarios";

        try (Connection conn = CoonBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usr = new Usuario();
                usr.setUNombre(rs.getString("u_nombre"));
                usr.setUEmail(rs.getString("u_email"));
                listaUsr.add(usr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Enviar correo a seleccionados
    public void enviarCorreo() {
        if (seleccionados == null || seleccionados.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "No seleccionaste ningún usuario"));
            return;
        }

        
        final String user = "jyusena@gmail.com";
        final String pass = "aqrd rmqu rzes qflw";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(user));

            // destinatarios = solo seleccionados
            InternetAddress[] dests = new InternetAddress[seleccionados.size()];
            int i = 0;
            for (Usuario u : seleccionados) {
                dests[i] = new InternetAddress(u.getUEmail());
                i++;
            }

            mensaje.setRecipients(Message.RecipientType.TO, dests);
            mensaje.setSubject(asunto);
            mensaje.setText(contmensaje);

            Transport.send(mensaje);

            // limpiar campos tras enviar
            asunto = "";
            contmensaje = "";
            seleccionados.clear();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Correos enviados exitosamente"));

        } catch (MessagingException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error enviando correos"));
            e.printStackTrace();
        }
    }

    // Getters y Setters
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getContmensaje() { return contmensaje; }
    public void setContmensaje(String contmensaje) { this.contmensaje = contmensaje; }

    public List<Usuario> getListaUsr() { return listaUsr; }
    public void setListaUsr(List<Usuario> listaUsr) { this.listaUsr = listaUsr; }

    public List<Usuario> getSeleccionados() { return seleccionados; }
    public void setSeleccionados(List<Usuario> seleccionados) { this.seleccionados = seleccionados; }
}
