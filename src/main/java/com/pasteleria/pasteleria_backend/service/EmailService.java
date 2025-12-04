package com.pasteleria.pasteleria_backend.service;

import com.pasteleria.pasteleria_backend.model.Order;
import com.pasteleria.pasteleria_backend.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@pasteleria.com}")
    private String fromEmail;

    // Si no se inyecta JavaMailSender (por falta de config), Spring puede fallar al arrancar si no se maneja.
    // Una estrategia es usar @Autowired(required = false)
    public EmailService(@org.springframework.beans.factory.annotation.Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(Order order) {
        if (mailSender == null) {
            logEmailSimulation(order);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(order.getUserEmail());
            helper.setSubject("Confirmación de Orden #" + order.getId() + " - Pastelería 1000 Sabores");

            String htmlContent = buildHtmlContent(order);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Correo enviado a " + order.getUserEmail());

        } catch (MessagingException | RuntimeException e) {
            System.err.println("Falló el envío de correo real. Usando simulación. Error: " + e.getMessage());
            logEmailSimulation(order);
        }
    }

    private void logEmailSimulation(Order order) {
        System.out.println("==================================================");
        System.out.println("SIMULACIÓN DE ENVÍO DE CORREO");
        System.out.println("Para: " + order.getUserEmail());
        System.out.println("Asunto: Confirmación de Orden #" + order.getId());
        System.out.println("Contenido:");
        System.out.println("Hola, gracias por tu compra.");
        System.out.println("Total: $" + order.getTotal());
        System.out.println("Productos:");
        for (OrderItem item : order.getItems()) {
            System.out.println("- " + item.getProductName() + " x" + item.getQuantity() + " ($" + item.getUnitPrice() + ")");
        }
        System.out.println("==================================================");
    }

    private String buildHtmlContent(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>¡Gracias por tu compra!</h1>");
        sb.append("<p>Tu orden #").append(order.getId()).append(" ha sido confirmada.</p>");
        sb.append("<h3>Detalle de productos:</h3>");
        sb.append("<ul>");
        for (OrderItem item : order.getItems()) {
            sb.append("<li>")
              .append(item.getProductName())
              .append(" x").append(item.getQuantity())
              .append(" - $").append(item.getUnitPrice())
              .append("</li>");
        }
        sb.append("</ul>");
        sb.append("<h3>Total Pagado: $").append(order.getTotal()).append("</h3>");
        sb.append("<p>Esperamos que disfrutes tus pasteles.</p>");
        return sb.toString();
    }
}
