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
            log.info("过期提醒邮件未启用，跳过发送");
            return;
        }

        String subject = "【药箱提醒】" + medicineName + "即将过期";
        String content = buildExpireReminderContent(medicineName, days);

        sendHtmlMail(to, subject, content);
    }

    @Async
    public void sendLowStockReminder(String to, String medicineName, int quantity) {
        if (!mailConfig.getExpireReminderEnabled()) {
            return;
        }

        String subject = "【药箱提醒】" + medicineName + "库存不足";
        String content = buildLowStockReminderContent(medicineName, quantity);

        sendHtmlMail(to, subject, content);
    }

    private String buildExpireReminderContent(String medicineName, int days) {
        if (days > 0) {
            return String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto;">
                        <h2 style="color: #409EFF;">家庭药箱提醒</h2>
                        <div style="background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p style="font-size: 16px;">您的药品 <strong>%s</strong> 将在 <strong>%d 天</strong> 后过期</p>
                        </div>
                        <p>请及时处理，避免使用过期药品！</p>
                        <hr>
                        <p style="color: #999; font-size: 12px;">此邮件由家庭常用药管理系统自动发送，请勿回复</p>
                    </div>
                </body>
                </html>
                """, medicineName, days);
        } else {
            return String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto;">
                        <h2 style="color: #F56C6C;">家庭药箱警告</h2>
                        <div style="background: #fef0f0; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <p style="font-size: 16px;">您的药品 <strong>%s</strong> <strong style="color: #F56C6C;">已过期</strong></p>
                        </div>
                        <p>请立即处理该药品！</p>
                        <hr>
                        <p style="color: #999; font-size: 12px;">此邮件由家庭常用药管理系统自动发送，请勿回复</p>
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
                    <h2 style="color: #E6A23C;">家庭药箱提醒</h2>
                    <div style="background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0;">
                        <p style="font-size: 16px;">您的药品 <strong>%s</strong> 库存不足，当前仅剩 <strong>%d 件</strong></p>
                    </div>
                    <p>请及时补充库存！</p>
                    <hr>
                    <p style="color: #999; font-size: 12px;">此邮件由家庭常用药管理系统自动发送，请勿回复</p>
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
            log.info("邮件发送成功: {}", to);
        } catch (MessagingException e) {
            log.error("邮件发送失败: {}", e.getMessage());
        }
    }
}
