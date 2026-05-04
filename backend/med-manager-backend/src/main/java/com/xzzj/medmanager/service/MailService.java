package com.xzzj.medmanager.service;

import com.xzzj.medmanager.config.MailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailConfig mailConfig;

    @Async
    public void sendExpireReminder(String to, String medicineName, int days) {
        if (!mailConfig.getExpireReminderEnabled()) {
            log.info("过期提醒功能已禁用");
            return;
        }

        String subject = "Expiration Reminder: " + medicineName + " is about to expire";
        String content = buildExpireReminderContent(medicineName, days);

        sendHtmlMail(to, subject, content);
    }

    @Async
    public void sendLowStockReminder(String to, String medicineName, int quantity) {
        if (!mailConfig.getExpireReminderEnabled()) {
            log.info("库存提醒功能已禁用"); 
            return;
        }

        String subject = "Low Stock Reminder: " + medicineName + " is low in stock";        
        String content = buildLowStockReminderContent(medicineName, quantity);

        sendHtmlMail(to, subject, content);
    }

    private String buildExpireReminderContent(String medicineName, int days) {
        if (days > 0) {
            return String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto;">
                        <h2 style="color: #409EFF;">Expiration Reminder</h2>
                        <div style="background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p style="font-size: 16px;">Product <strong>%s</strong> is expiring in <strong>%d</strong> days</p>
                        </div>
                        <p>Expiration date: %s</p>
                        <hr>
                        <p style="color: #999; font-size: 12px;">Expiration date: %s</p>
                    </div>
                </body>
                </html>
                """, medicineName, days);
        } else {
            return String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto;">
                            <h2 style="color: #F56C6C;">Expiration Reminder</h2>
                        <div style="background: #fef0f0; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p style="font-size: 16px;">Product <strong>%s</strong> <strong style="color: #F56C6C;">�ѹ���</strong></p>
                        </div>
                        <p>Expiration date: %s</p>
                        <hr>
                        <p style="color: #999; font-size: 12px;">Expiration date: %s</p>
                    </div>
                </body>
                </html>
                """, medicineName);
        }
    }

    private String buildLowStockReminderContent(String medicineName, int quantity) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #E6A23C;">Low Stock Reminder</h2>
                    <div style="background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="font-size: 16px;">Product <strong>%s</strong> is low in stock, <strong>%d ��</strong></p>
                    </div>
                    <p>Low stock reminder: %s is low in stock, %d left</p>
                    <hr>
                    <p style="color: #999; font-size: 12px;">Low stock reminder: %s is low in stock, %d left</p>
                </div>
            </body>
            </html>
            """, medicineName, quantity);
    }

    private void sendHtmlMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(mailConfig.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
